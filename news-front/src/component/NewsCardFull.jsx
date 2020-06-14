import React from 'react';
import TagContainerSimple from './TagContainerSimple'
import NewsDataService from './NewsDataService'

import { translate } from 'react-i18next';

class NewsCardFull extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            news:{},
            author: {},
            tags:[],
		}
    }

    componentDidMount() {
        this.refreshNews(this.props.match.params.id);
    }

    refreshNews(id) {
        NewsDataService.getNewsById(id)
        .then(
            response => {
                this.setState({news: response.data});
                this.setState({author: response.data.author});
                this.setState({tags: response.data.tags});
            }
        )
    }

	render(){
		return(

            <div className="container">
                <div className="row">
                    <div className="col-1"></div>
                    <div className="col-10">
                        <div className="my-3">
                            <div className="card w-100">
                                <h4 className="card-header alert alert-primary">{this.state.news.title}</h4>
                                <div className="card-body">
                                    <div className="container">
                                        <div className="row">
                                            <div className="col-6">
                                                <p className="card-title">{this.state.author.name + ' ' + this.state.author.surname}</p>
                                            </div>
                                            <div className="col-4"></div>
                                            <div className="col-2">
                                                <p className="card-text" style={{fontSize: 14 + 'px'}}>{this.state.news.creationDate}</p>
                                            </div>
                                        </div>
                                        <div className="row">
                                            <div className="col">
                                                <h6 className="card-title">{this.state.news.shortText}</h6>
                                                <p className="card-text">{this.state.news.fullText}</p>
                                            </div>
                                        </div>
                                        <div className="row">
                                            <div className="col my-3">
                                                <TagContainerSimple tags={this.state.tags}/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="card-footer text-muted text-center">
                                    <a href="/" class="btn btn-primary" role="button" aria-pressed="true">{this.props.t("to_news_list")}</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="col-1"></div>
                </div>
            </div>

            
		);
	}
}

export default translate()(NewsCardFull)