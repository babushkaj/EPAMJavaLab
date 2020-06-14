import React from 'react'

import TagDataService from './TagDataService'

import { translate } from 'react-i18next';

class TagsList extends React.Component{

    constructor(props){
        super(props);
        this.state = {
            tags:[],
            howMany: 10,
            tagsAmount: 1,
            pageNumber: 0,
        }
    }

    componentDidMount() {
        this.refreshTags(this.state.pageNumber);
    }

    refreshTags(from){
        TagDataService.getAllTags(from, this.state.howMany)
        .then(
            (response) => {
                this.setState({tags: response.data, tagsAmount: parseInt(response.headers['tags-amount']),
                pageNumber: parseInt(response.headers['page-number'])});                     
            }
        )
    }

    createTag(){
        let errorsExist = this.tagErrorRender();
        if(errorsExist != 0){
            return;
        }

        let newTagName = document.getElementById("newTagField").value;
        let newTag ={id: null, name: newTagName}; 
        TagDataService.createTag(newTag)
        .then(
            response => {
                document.getElementById("newTagField").value = "";
                this.refreshTags(this.state.pageNumber);
            }
        )
    }

    saveChangedTag(tagId){
        let tagName = document.getElementById("tagNameField" + tagId).value;
        let tag = {id: tagId, name: tagName};
        TagDataService.editTag(tag)
        .then(
            response => {
                this.refreshTags(this.state.pageNumber);
            }
        )
    }

    deleteTag(id){
        TagDataService.deleteTag(id)
        .then(
            response => {
                this.refreshTags(this.state.pageNumber);
            }
        )
    }

    cleanErrorMessage(errorFieldId){
        let previousErrorMessage = document.getElementById(errorFieldId);
        if (previousErrorMessage) {
            previousErrorMessage.remove();
        }
    }

    validateNewsTagValue(value) {
        let errorMessage = "";
        if (!value) {
            errorMessage = this.props.t("tag_name_mustnt_be_empty");
        } else if (value.length > 30) {
            errorMessage = this.props.t("tag_name_mustnt_be_larger_than_30_characters");
        } else {
            const allTags = this.state.tags;
            for (let tag of allTags) {
                if (tag.name === value) {
                    errorMessage = this.props.t("tag_already_exists");
                    break;
                }
            }
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

    tagErrorRender(){
        let errorsExist = 0;

        this.cleanErrorMessage("tagNameErrorMessage");
        let newTag = document.getElementById("newTagField").value;
        let errorMessage = this.validateNewsTagValue(newTag);

        if(errorMessage){
            this.createErrorField("tagNameErrorMessage", errorMessage, "newTagInputGroup");
            errorsExist++;
        }

        return errorsExist;
    } 

    handleEditing(index, inputValue){
        const tagsArrToChage = this.state.tags.slice();
        tagsArrToChage[index].name = inputValue;
        this.setState({tags: tagsArrToChage});
    }

    render(){

        const renderPagination = () => {
            let pageButtons = [];

            for (let i = 0; i < Math.ceil(this.state.tagsAmount/this.state.howMany); i++) {
                if(i == this.state.pageNumber){
                    pageButtons.push(<li className="page-item"><a className="page-link text-danger" href="#" onClick={() => {this.refreshTags(i)}}>{i+1}</a></li>);  
                } else {
                    pageButtons.push(<li className="page-item"><a className="page-link" href="#" onClick={() => {this.refreshTags(i)}}>{i+1}</a></li>);  
                }
                      
            }
            return pageButtons;
        };

        const renderTagsList = this.state.tags.map((tag, index) => {
            return(
                <div className="row my-1">
                    <div className="col-1"></div>
                    <div className="col-6">
                        <input id={"tagNameField" + tag.id} className="form-control"  type="text" value={tag.name} onChange={(e) => this.handleEditing(index, e.target.value)}/>
                    </div>
                    <div className="col-2">
                        <button type="button" className="btn btn-outline-primary btn-block mx-1" onClick={() => this.saveChangedTag(tag.id)}>{this.props.t("save")}</button>
                    </div>
                    <div className="col-2">
                        <button type="button" className="btn btn-outline-danger btn-block mx-1" onClick={() => this.deleteTag(tag.id)}>{this.props.t("delete")}</button>
                    </div>
                    <div className="col-1"></div>
                </div>
            )
        })  

        return(
            <div className="container">
                <div className="row">
                    <div className="col-1"></div>
                    <div className="col-10">
                        <div id="newTagInputGroup" className="input-group mb-3 w-100 my-3">
                            <input id="newTagField" type="text" className="form-control" placeholder={this.props.t("new_tag_name")} aria-describedby="button-addon2" onFocus={() => this.cleanErrorMessage("tagNameErrorMessage")}/>
                            <div className="input-group-append">
                                <button className="btn btn-outline-success btn-sm" type="button" id="add-tags-button" onClick={() => this.createTag()}>{this.props.t("add")}</button>
                            </div>
                        </div>
    
                        {renderTagsList}

                        <nav aria-label="Pagination">
                            <ul className="pagination justify-content-center py-4">
                                <li className="page-item">
                                    {this.state.pageNumber == 0 ?
                                        <a className="page-link text-muted" href="#" aria-label="Previous">
                                           <span aria-hidden="true">&laquo;</span>        
                                        </a>
                                    :
                                        <a className="page-link" href="#" aria-label="Previous">
                                            <span aria-hidden="true" onClick = {() => this.refreshTags(this.state.pageNumber - 1)}>&laquo;</span>        
                                        </a>
                                    }
                                </li>

                                {renderPagination()}

                                <li className="page-item">

                                    {this.state.pageNumber == Math.floor(this.state.tagsAmount/this.state.howMany) ?
                                        <a className="page-link text-muted" href="#" aria-label="Next">
                                            <span aria-hidden="true">&raquo;</span>
                                        </a>
                                    :
                                        <a className="page-link" href="#" aria-label="Next">
                                            <span aria-hidden="true" onClick = {() => this.refreshTags(this.state.pageNumber + 1)}>&raquo;</span>
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

export default translate()(TagsList)
