import React from 'react'
import NewsDataService from './NewsDataService'
import TagDataService from './TagDataService'
import AuthorDataService from './AuthorDataService'
import TagButton from './TagButton'

import { translate } from 'react-i18next';

class NewsEditForm extends React.Component {

    constructor(props){
        super(props);
        this.state = {
            newsId: {},
            newsTitle: '',
            newsShortText: '',
            newsFullText: '',
            creationDate: '',
            author: {},
            tags: [],
            allTags: [],
            allAuthors: [],
        }
    }

    componentDidMount() {
        if(this.props.match.params.id > 0){
            this.refreshNews(this.props.match.params.id);
        }
        this.refreshAuthorsList();
        this.refreshTagsList(); 
    }

    refreshNews(id) {
        NewsDataService.getNewsById(id)
        .then(
            response => {
                this.setState({newsId: response.data.id});
                this.setState({newsTitle: response.data.title});
                this.setState({newsShortText: response.data.shortText});
                this.setState({newsFullText: response.data.fullText});
                this.setState({creationDate: response.data.creationDate});
                this.setState({author: response.data.author});
                this.setState({tags: response.data.tags});
            }
        )
    }

    refreshTagsList(){
        TagDataService.getAllTags(0, 2147483647)
        .then(
            response => {
                this.setState({allTags: response.data});
            }
        )
    }

    refreshAuthorsList(){
        AuthorDataService.getAllAuthors(0, 2147483647)
        .then(
            response => {
                this.setState({allAuthors: response.data});
            }
        )
    }

    editNews(){
        let news = this.buildNewsObjectFromFormFieldsAndState();
        if(this.state.newsId > 0){
            NewsDataService.editNews(news)
            .then(
                response => {
                    window.location.href = "/news/edit/" + response.data.id;
                }
            )
        } else {
            NewsDataService.createNews(news)
            .then(
                response => {
                    window.location.href = "/news/edit/" + response.data.id;
                }
            )
        }
        
    }

    deleteTagFromState(tagName){
        let stateTagsArray = [];
        stateTagsArray = this.state.tags;

        for(var k=0; k<stateTagsArray.length; k++){
            if(stateTagsArray[k].name === tagName){
                stateTagsArray.splice(k, 1);
                break;
            }
        }

        this.setState({tags: stateTagsArray});
    }

    setNewTagsToState(){
        let stateTagsArray = [];
        stateTagsArray = this.state.tags;
        let newTagFieldValue = document.getElementById("newTagField").value;
        let selectTagFieldValue = document.getElementById("tag-select-field").childNodes; 

        for(var i=0; i<selectTagFieldValue.length; i++){
            if(selectTagFieldValue[i].selected){

                var containsInState = false;

                for(var j=0; j<stateTagsArray.length; j++){
                    if(stateTagsArray[j].name === selectTagFieldValue[i].textContent){
                        containsInState = true;
                        break;
                    }
                }
                if(!containsInState){
                    stateTagsArray.push({id: selectTagFieldValue[i].value, name: selectTagFieldValue[i].textContent});
                }

                containsInState = false;
            }
        }

        let errorsExist = this.tagErrorRender();
        if(errorsExist == 0){
            stateTagsArray.push({id: null, name: newTagFieldValue});
        }
        
        for(var k=0; k<selectTagFieldValue.length; k++){
            if(selectTagFieldValue[k].selected){
                selectTagFieldValue[k].selected = false;
            }
        }

        this.setState({tags: stateTagsArray});
        
    }

    buildNewsObjectFromFormFieldsAndState(){
        let errorsExist = this.newsErrorsRender();
        if(errorsExist > 0){
            return;
        }
        let formTitle = document.getElementById("newsTitleField").value;
        let formShortText = document.getElementById("newsShortTextField").value;
        let formFullText = document.getElementById("newsFullTextField").value;

        let selectedAuthor = {};
        let selectAuthorFieldValue = document.getElementById("authorsSelectField").childNodes; 

        for(var i=0; i<selectAuthorFieldValue.length; i++){
            if(selectAuthorFieldValue[i].selected){
                selectedAuthor = {id: selectAuthorFieldValue[i].value, name: null, surname: null};
            }
        }

        var newsObject = {};
        if(this.props.match.params.id < 0){
            newsObject = {
                title: formTitle,
                shortText: formShortText,
                fullText: formFullText,
                author: selectedAuthor,
                tags: this.state.tags};
        } else {
            newsObject = {
                id: this.state.newsId,
                title: formTitle,
                shortText: formShortText,
                fullText: formFullText,
                creationDate: this.state.creationDate,
                author: selectedAuthor,
                tags: this.state.tags};
        }

        return newsObject;

    }

    cleanErrorMessage(errorFieldId){
        let previousErrorMessage = document.getElementById(errorFieldId);
        if (previousErrorMessage) {
            previousErrorMessage.remove();
        }
    }

    createErrorField(fieldId, errorMessage, afterElementId) {
        let small = document.createElement('small');
        small.id = fieldId;
        small.className = "text-danger";
        small.innerText = errorMessage;
        let fieldSet = document.getElementById(afterElementId);
        fieldSet.after(small);
    }

    validateNewsTitle(value) {
        let errorMessage = "";
        if (!value) {
            errorMessage = this.props.t("title_mustnt_be_empty");
        } else if (value.length > 30) {
            errorMessage = this.props.t("title_mustnt_be_larger_than_30_characters");
        } 
        return errorMessage;
    }

    validateNewsShortText(value) {
        let errorMessage = "";
        if (!value) {
            errorMessage = this.props.t("short_text_mustnt_be_empty");
        } else if (value.length > 200) {
            errorMessage = this.props.t("short_text_mustnt_be_larger_than_200_characters");
        } 
        return errorMessage;
    }

    validateNewsFullText(value) {
        let errorMessage = "";
        if (!value) {
            errorMessage = this.props.t("full_text_mustnt_be_empty");
        } else if (value.length > 2000) {
            errorMessage = this.props.t("full_text_mustnt_be_larger_than_2000_characters");
        } 
        return errorMessage;
    }

    validateNewsTagValue(value) {
        let errorMessage = "";
        if (!value) {
            errorMessage = this.props.t(" ");
        } else if (value.length > 30) {
            errorMessage = this.props.t("tag_name_mustnt_be_larger_than_30_characters");
        } else {
            const allTags = this.state.allTags;
            for (let tag of allTags) {
                if (tag.name === value) {
                    errorMessage = this.props.t("tag_already_exists");
                    break;
                }
            }
        }
        return errorMessage;
    }

    newsErrorsRender(){
        let errorsExist = 0;

        this.cleanErrorMessage("titleErrorMessage");
        this.cleanErrorMessage("shortTextErrorMessage");
        this.cleanErrorMessage("fullTextErrorMessage");

        let newsTitle = document.getElementById("newsTitleField").value;
        let newsShortText = document.getElementById("newsShortTextField").value;
        let newsFullText = document.getElementById("newsFullTextField").value;

        let errorMessage1 = this.validateNewsTitle(newsTitle);
        let errorMessage2 = this.validateNewsShortText(newsShortText);
        let errorMessage3 = this.validateNewsFullText(newsFullText);

        if(errorMessage1){
            this.createErrorField("titleErrorMessage", errorMessage1, "titleInputGroup");
            errorsExist++;
        }

        if(errorMessage2){
            this.createErrorField("shortTextErrorMessage", errorMessage2, "shortTextInputGroup");
            errorsExist++;
        }
        
        if(errorMessage3){
            this.createErrorField("fullTextErrorMessage", errorMessage3, "fullTextInputGroup");
            errorsExist++;
        }

        return errorsExist;

    }

    tagErrorRender(){
        let errorsExist = 0;

        this.cleanErrorMessage("newTagErrorMessage");
        let newTag = document.getElementById("newTagField").value;
        let errorMessage = this.validateNewsTagValue(newTag);

        if(errorMessage){
            this.createErrorField("newTagErrorMessage", errorMessage, "newTagInputGroup");
            errorsExist++;
        }

        return errorsExist;
    }

    render(){

        const renderAllAuthorsList = this.state.allAuthors.map(a => {
            if(a.id == this.state.author.id){
                return(
                    <option selected value={a.id}>{a.name} {a.surname}</option>
                )
            } else {
                return(
                    <option value={a.id}>{a.name} {a.surname}</option>
                )
            }
        })

        const renderAllTagsList = this.state.allTags.map(t => {
            return(
                <option value={t.id}>{t.name}</option>
            )
        })

        const renderThisNewsTags = this.state.tags.map(tag => {
            return(
                <TagButton 
                tagName={tag.name}
                deleteClick={() => this.deleteTagFromState(tag.name)}
                />
            )
        }) 

        return(

            <div className="container">
            <div className="row">
                <div className="col-1"></div>
                <div className="col-10">
                    <div>
                        <form>
                            <div id="titleInputGroup" className="form-group">
                                <label for="newsTitleField">{this.props.t("title")}</label>
                                <input id="newsTitleField" className="form-control" type="text" maxLength="30" defaultValue={this.state.newsTitle} onFocus={() => this.cleanErrorMessage("titleErrorMessage")}/>
                            </div>
                            <div id="shortTextInputGroup" className="form-group">
                                <label for="newsShortTextField">{this.props.t("short_text")}</label>
                                <textarea id="newsShortTextField" className="form-control" maxLength="100" rows="2" defaultValue={this.state.newsShortText} onFocus={() => this.cleanErrorMessage("shortTextErrorMessage")}></textarea>
                            </div>
                            <div id="fullTextInputGroup" className="form-group">
                                <label for="newsFullTextField">{this.props.t("full_text")}</label>
                                <textarea className="form-control" id="newsFullTextField"  maxLength="2000" rows="8" defaultValue={this.state.newsFullText} onFocus={() => this.cleanErrorMessage("fullTextErrorMessage")}></textarea>
                            </div>
                            <div className="form-group">
                                <label for="authorsSelectField">{this.props.t("author")}</label>
                                <select id="authorsSelectField" class="form-control">
                                    {renderAllAuthorsList}
                                </select>
                            </div>
                            <div className="form-group">
                                <div className="container">
                                    <div className="row">
                                        <div className="col-6">
                                            <label>{this.props.t("selected_tags")}</label>
                                            <div>
                                                {renderThisNewsTags}
                                            </div>
                                        </div>
                                        <div className="col-6">
                                            <label for="tagsMultiselectField">{this.props.t("existing_tags")}</label>
                                            <select multiple  id="tag-select-field" className="form-control">
                                                {renderAllTagsList}
                                            </select>
                                            <div>
                                                <div id="newTagInputGroup" className="input-group mb-3 w-100 my-3">
                                                    <input id="newTagField" type="text" className="form-control" placeholder={this.props.t("new_tag_name")} aria-describedby="button-addon2" onFocus={() => this.cleanErrorMessage("newTagErrorMessage")}/>
                                                    <div className="input-group-append">
                                                        <button className="btn btn-outline-success btn-sm" type="button" id="add-tags-button" onClick={() => this.setNewTagsToState()}>{this.props.t("add")}</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="container">
                                <div className="row">
                                    <div className="col">
                                        <button type="button" className="btn btn-primary btn-block mx-1" onClick={() => this.editNews()}>{this.props.t("save")}</button>
                                    </div>
                                    <div className="col">
                                        <a href="/" className="btn btn-outline-danger btn-block mx-1" role="button" aria-pressed="true">{this.props.t("to_news_list")}</a>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
                <div className="col-1"></div>
            </div>
        </div>
           
        );
    }
}

export default translate()(NewsEditForm)