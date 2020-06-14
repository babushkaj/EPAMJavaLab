import React from 'react'

import AuthorDataService from './AuthorDataService'

import { translate } from 'react-i18next';

class AuthorsList extends React.Component{

    constructor(props){
        super(props);
        this.state = {
            authors:[],
            howMany: 10,
            authorsAmount: 1,
            pageNumber: 0,
        }
    }

    componentDidMount() {
        this.refreshAuthors(this.state.pageNumber);
    }

    refreshAuthors(from){
        AuthorDataService.getAllAuthors(from, this.state.howMany)
        .then(
            response => {
                this.setState({authors: response.data, authorsAmount: parseInt(response.headers['authors-amount']),
                pageNumber: parseInt(response.headers['page-number'])});
            }
        )
    }

    createAuthor(){

        let errorsExist = this.nameSurnameErrorRender();
        if(errorsExist > 0){
            return;
        }

        let newAuthorName = document.getElementById("newAuthorNameField").value;
        let newAuthorSurname = document.getElementById("newAuthorSurnameField").value;

        let newAuthor ={id: null, name: newAuthorName, surname: newAuthorSurname}; 

        AuthorDataService.createAuthor(newAuthor)
        .then(
            response => {
                document.getElementById("newAuthorNameField").value = "";
                document.getElementById("newAuthorSurnameField").value = "";
                this.refreshAuthors(this.state.pageNumber);
            }
        )
    }

    saveChangedAuthor(authorId){
        let authorName = document.getElementById("authorNameField" + authorId).value;
        let authorSurname = document.getElementById("authorSurnameField" + authorId).value;
        let author ={id: authorId, name: authorName, surname: authorSurname}; 
        AuthorDataService.editAuthor(author)
        .then(
            response => {
                this.refreshAuthors(this.state.pageNumber);
            }
        )
    }

    deleteAuthor(id){
        AuthorDataService.deleteAuthor(id)
        .then(
            response => {
                this.refreshAuthors(this.state.pageNumber);
            }
        )
    }

    cleanErrorMessage(errorFieldId){
        let previousErrorMessage = document.getElementById(errorFieldId);
        if (previousErrorMessage) {
            previousErrorMessage.remove();
        }
    }

    validateAuthorName(value) {
        let errorMessage = "";
        if (!value) {
            errorMessage = this.props.t("author_name_surname_mustnt_be_empty");
        } else if (value.length > 30) {
            errorMessage = this.props.t("author_name_surname_mustnt_be_larger_than_30_characters");
        } 
        return errorMessage;
    }

    createErrorField(fieldId, errorMessage, afterElementId) {
        let small = document.createElement('small');
        small.id = fieldId;
        small.className = "text-danger";
        small.innerText = errorMessage;
        let fieldSet = document.getElementById(afterElementId);
        fieldSet.after(small);
    }

    nameSurnameErrorRender(){
        let errorsExist = 0;

        this.cleanErrorMessage("authorNameErrorMessage");
        this.cleanErrorMessage("authorSurnameErrorMessage");
        let newAuthorName = document.getElementById("newAuthorNameField").value;
        let newAuthorSurname = document.getElementById("newAuthorSurnameField").value;
        
        let errorMessage1 = this.validateAuthorName(newAuthorName);
        let errorMessage2 = this.validateAuthorName(newAuthorSurname);

        if (errorMessage1) {
            this.createErrorField("authorNameErrorMessage", errorMessage1, "newAuthorInputGroup");
            errorsExist++;
        }

        if (errorMessage2) {
            this.createErrorField("authorSurnameErrorMessage", errorMessage2, "newAuthorInputGroup");
            errorsExist++;
        }

        return errorsExist;
    } 

    handleEditingName(index, inputValue){
        const authorsArrToChage = this.state.authors.slice();
        authorsArrToChage[index].name = inputValue;
        this.setState({authors: authorsArrToChage});
    }

    handleEditingSurname(index, inputValue){
        const authorsArrToChage = this.state.authors.slice();
        authorsArrToChage[index].surname = inputValue;
        this.setState({authors: authorsArrToChage});
    }

    render(){

        const renderPagination = () => {
            let pageButtons = [];

            for (let i = 0; i < Math.ceil(this.state.authorsAmount/this.state.howMany); i++) {
                if(i == this.state.pageNumber){
                    pageButtons.push(<li className="page-item"><a className="page-link text-danger" href="#" onClick={() => this.refreshAuthors(i)}>{i+1}</a></li>);  
                } else {
                    pageButtons.push(<li className="page-item"><a className="page-link" href="#" onClick={() => this.refreshAuthors(i)}>{i+1}</a></li>);  
                }
                      
            }
            return pageButtons;
        };

        const renderAuthorsList = this.state.authors.map((author, index) => {
            return(
                <div className="row my-1">
                    <div className="col-1"></div>
                    <div className="col-3">
                        <input id={"authorNameField" + author.id} className="form-control"  type="text" value={author.name} onChange={(e) => this.handleEditingName(index, e.target.value)}/>
                    </div>
                    <div className="col-3">
                        <input id={"authorSurnameField" + author.id} className="form-control"  type="text" value={author.surname} onChange={(e) => this.handleEditingName(index, e.target.value)}/>
                    </div>
                    <div className="col-2">
                        <button type="button" className="btn btn-outline-primary btn-block mx-1" onClick={() => this.saveChangedAuthor(author.id)}>{this.props.t("save")}</button>
                    </div>
                    <div className="col-2">
                        <button type="button" className="btn btn-outline-danger btn-block mx-1" onClick={() => this.deleteAuthor(author.id)}>{this.props.t("delete")}</button>
                    </div>
                    <div className="col-1"></div>
                </div>
            )
        }); 

        return(

            <div className="container">
            <div className="row">
                <div className="col-1"></div>
                <div className="col-10">
                <div id="newAuthorInputGroup" className="input-group mb-3 w-100 my-3">
                    <input id="newAuthorNameField" type="text" className="form-control" placeholder={this.props.t("new_author_name")} aria-describedby="button-addon2" onFocus={() => this.cleanErrorMessage("authorNameErrorMessage")}/>
                    <input id="newAuthorSurnameField" type="text" className="form-control" placeholder={this.props.t("new_author_surname")} aria-describedby="button-addon2" onFocus={() => this.cleanErrorMessage("authorSurnameErrorMessage")}/>
                    <div className="input-group-append">
                        <button className="btn btn-outline-success btn-sm" type="button" id="add-tags-button" onClick={() => this.createAuthor()}>{this.props.t("add")}</button>
                    </div>
                </div>

                {renderAuthorsList}

                <nav aria-label="Pagination">
                    <ul className="pagination justify-content-center py-4">
                        <li className="page-item">
                            {this.state.pageNumber == 0 ?
                                <a className="page-link text-muted" href="#" aria-label="Previous">
                                    <span aria-hidden="true">&laquo;</span>        
                                </a>
                            :
                                <a className="page-link" href="#" aria-label="Previous">
                                    <span aria-hidden="true" onClick = {() => this.refreshAuthors(this.state.pageNumber - 1)}>&laquo;</span>        
                                </a>
                            }
                        </li>

                        {renderPagination()}

                        <li className="page-item">
                        
                            {this.state.pageNumber == Math.floor(this.state.authorsAmount/this.state.howMany) ?
                                <a className="page-link text-muted" href="#" aria-label="Next">
                                    <span aria-hidden="true">&raquo;</span>
                                </a>
                            :
                                <a className="page-link" href="#" aria-label="Next">
                                    <span aria-hidden="true" onClick = {() => this.refreshAuthors(this.state.pageNumber + 1)}>&raquo;</span>
                                </a>
                            }

                        </li>
                    </ul>
                </nav>

                </div>
                <div className="col-1"></div>
            </div>
            
   
            
        </div>

        );
    }
}

export default translate()(AuthorsList)