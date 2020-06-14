import React from 'react';
import TagContainerSimple from './TagContainerSimple'

class NewsCardShort extends React.Component {
    
	render(){
        const user = JSON.parse(localStorage.getItem("user"));
		return(
            <div className="my-3">
                <div className="card w-100">
                    <h4 className="card-header alert alert-primary">{this.props.newsTitle}</h4>
                    <div className="card-body">
                        <div className="container">
                            <div className="row">
                                <div className="col-6">
                                    <p className="card-title">{this.props.newsAuthor.name + ' ' + this.props.newsAuthor.surname}</p>
                                </div>
                                <div className="col-4"></div>
                                <div className="col-2">
                                    <p className="card-text" style={{fontSize: 14 + 'px'}}>{this.props.newsCrDate}</p>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col">
                                    <h6 className="card-title">{this.props.newsST}</h6>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-10 my-3">
                                    <TagContainerSimple tags={this.props.newsTags}/>
                                </div>
                                <div className="col-2">
                                    {/* <button type="button" class="btn btn-primary" onClick={this.props.readClick}>{this.props.t("read")}</button> */}
                                    <a href={"/news/" + this.props.newsId} className="btn btn-primary" role="button" aria-pressed="true">{this.props.t("read")}</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    {user && user.name != null?
                    <div className="card-footer text-muted text-center">
                        <div className="container">
                            <div className="row">
                                <div className="col">
                                    <a href={"/news/edit/" + this.props.newsId} className="btn btn-outline-warning btn-block mx-1" role="button" aria-pressed="true">{this.props.t("edit")}</a>
                                </div>
                                <div className="col">
                                    <button type="button" class="btn btn-outline-danger btn-block mx-1" onClick={this.props.deleteClick}>{this.props.t("delete")}</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    :null}
                </div>
            </div>
        
		);
	}
}

export default NewsCardShort
