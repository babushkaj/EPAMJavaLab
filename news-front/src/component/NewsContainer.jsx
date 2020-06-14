import React from 'react';
import NewsCardShort from './NewsCardShort'
import NewsDataService from './NewsDataService'
import TagDataService from './TagDataService'

import { translate } from 'react-i18next';
 
class NewsContainer extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            news: [],
            allTags: [],
            howMany: 3,
            newsAmount: 1,
            pageNumber: 0,
            searchString: '',
		}
    }

    componentDidMount() {
        this.refreshNews(this.state.pageNumber);
        this.refreshTags();
    }

    refreshNews(from) {
        NewsDataService.findNews(this.state.searchString, from, this.state.howMany)
        .then(
            response => {
                this.setState({news: response.data,  newsAmount: parseInt(response.headers['news-amount']),
                                pageNumber: parseInt(response.headers['page-number']), searchString: response.headers['search-string']});
            }
           
        )
    }

    refreshTags(){
        TagDataService.getAllTags(0, 2147483647)
        .then(
            response => {
                this.setState({allTags: response.data});
            }
        )
    }

    deleteNews(id) {
        NewsDataService.deleteNews(id)
        .then(
            response => {
                this.refreshNews(this.state.pageNumber);
            }
        )
    }

    showOneNews(id) {
        // window.location.href = "/news/" + id;
        this.props.history.push(`/news/` + id);
    }  

    findNews(){
        let searchString = this.collectSearchDataToSearchString();
        if(searchString){
            NewsDataService.findNews(searchString, 0, this.state.howMany)
            .then(
                response => {
                    this.setState({news: response.data,  newsAmount: parseInt(response.headers['news-amount']),
                    pageNumber: parseInt(response.headers['page-number']), searchString: response.headers['search-string']});
                }
            )
        }
    }

    collectSearchDataToSearchString(){
        const authorNameCriteriaName = "author_name";
        const authorSurnameCriteriaName = "author_surname";
        const tagCriteriaName = "tags_name";

        let searchAuthorName = document.getElementById("searchAuthorNameField").value;

        let authorNameSearchCriteria = "";
        if(searchAuthorName){
            authorNameSearchCriteria = authorNameCriteriaName + ":" + searchAuthorName;
        }

        let searchAuthorSurname = document.getElementById("searchAuthorSurnameField").value;

        let authorSurnameSearchCriteria = "";
        if(searchAuthorSurname){
            authorSurnameSearchCriteria = authorSurnameCriteriaName + ":" + searchAuthorSurname;
        }

        let selectTagFieldValue = document.getElementById("tagsSelectField").childNodes; 

        let selectedTags = [];
        for(var i=0; i<selectTagFieldValue.length; i++){
            if(selectTagFieldValue[i].selected){
                selectedTags.push(selectTagFieldValue[i].textContent);
            }
        }

        let tagsSearchCriteria = "";
        if(selectedTags.length > 0){
            tagsSearchCriteria = tagCriteriaName + ":" + selectedTags[0];
        }
        for(var j=1; j<selectedTags.length; j++){
            tagsSearchCriteria = tagsSearchCriteria + "," + tagCriteriaName + ":" + selectedTags[j];
        }

        let searchString = "";

        if(authorNameSearchCriteria){
            searchString = searchString + authorNameSearchCriteria;
        }

        if(authorSurnameSearchCriteria){
            if(searchString){
                searchString = searchString + "," + authorSurnameSearchCriteria;
            } else {
                searchString = searchString + authorSurnameSearchCriteria;
            }
        }

        if(tagsSearchCriteria){
            if(searchString){
                searchString = searchString + "," + tagsSearchCriteria;
            } else {
                searchString = searchString + tagsSearchCriteria;
            }
        }

        return searchString;

        // if(searchString){
        //     return "?search=" + searchString;
        // } else {
        //     return;
        // }
    }

    resetSearchFields(){
        window.location.href = "/";
    }

    render() {
        const renderPagination = () => {
            let pageButtons = [];

            for (let i = 0; i < Math.ceil(this.state.newsAmount/this.state.howMany); i++) {
                if(i == this.state.pageNumber){
                    pageButtons.push(<li className="page-item"><a className="page-link text-danger" href="#" onClick={() => this.refreshNews(i)}>{i+1}</a></li>);  
                } else {
                    pageButtons.push(<li className="page-item"><a className="page-link" href="#" onClick={() => this.refreshNews(i)}>{i+1}</a></li>);  
                }
                      
            }
            return pageButtons;
        };

        const renderAllTagsList = this.state.allTags.map(t => {
            return(
                <option value={t.id}>{t.name}</option>
            )
        })

        const renderNewsCards = this.state.news.map(n => {
            return(
                <NewsCardShort
                newsId={n.id}
                newsTitle={n.title} 
                newsST={n.shortText} 
                newsCrDate={n.creationDate} 
                newsAuthor={n.author}                
                newsTags={n.tags}
                deleteClick={() => this.deleteNews(n.id)}
                readClick={() => this.showOneNews(n.id)}
                toNewsListClick={() => this.showNewsList()}
                deleteClick={() => this.deleteNews(n.id)}
                t = {this.props.t}
                />
            )
        })  
     
		return(
            <div>
                <div className="container">
                    <div className="row">
                        <div className="col-1"></div>
                        <div className="col-10">
                            <div className="container">

                                <div className="row align-items-center">
                                    <div className="col-10">
                                        <div className="input-group mb-3 w-100 my-3">
                                            <input id="searchAuthorNameField" type="text" className="form-control" placeholder={this.props.t("authors_name")} aria-describedby="button-addon2"/>
                                            <input id="searchAuthorSurnameField" type="text" className="form-control" placeholder={this.props.t("authors_surname")} aria-describedby="button-addon2"/>
                                        </div>
                                        <select multiple id="tagsSelectField" className="form-control" size="2">
                                            {renderAllTagsList}
                                        </select>
                                    </div>
                                    <div className="col-2 align-self-center">
                                        <button className="btn btn-outline-primary btn-block" type="button" id="buttonSearch" onClick={() => this.findNews()}>{this.props.t("search")}</button>
                                        <button className="btn btn-outline-danger btn-block" type="button" id="buttonSearch" onClick={() => this.resetSearchFields()}>{this.props.t("clean")}</button>
                                    </div>
                                </div>

                            </div>
                            {renderNewsCards}

                            <nav aria-label="Pagination">
                                <ul className="pagination justify-content-center">
                                    <li className="page-item">
                                    {this.state.pageNumber == 0 ?
                                        <a className="page-link text-muted" href="#" aria-label="Previous">
                                            <span aria-hidden="true">&laquo;</span>        
                                        </a>
                                    :
                                        <a className="page-link" href="#" aria-label="Previous">
                                            <span aria-hidden="true" onClick = {() => this.refreshNews(this.state.pageNumber - 1)}>&laquo;</span>        
                                        </a>
                                    }
                                    </li>
                                    {renderPagination()}
                                    <li className="page-item">
                                    {this.state.pageNumber == Math.floor(this.state.newsAmount/this.state.howMany) ?
                                        <a className="page-link text-muted" href="#" aria-label="Next">
                                            <span aria-hidden="true">&raquo;</span>
                                        </a>
                                    :
                                        <a className="page-link" href="#" aria-label="Next">
                                            <span aria-hidden="true" onClick = {() => this.refreshNews(this.state.pageNumber + 1)}>&raquo;</span>
                                        </a>
                                    }
                                    </li>
                                </ul>
                            </nav>

                        </div>
                        <div className="col-1"></div>
                    </div>
                </div>
              
            </div>
		);
	}
}

export default translate()(NewsContainer)