import 'bootstrap/dist/css/bootstrap.min.css'
import 'jquery/dist/jquery.min.js'
import 'bootstrap/dist/js/bootstrap.min.js'

import './index.css'

import Header from './component/Header'
import Footer from './component/Footer'
import NewsContainer from './component/NewsContainer'
import NewsCardFull from './component/NewsCardFull'
import NewsEditForm from './component/NewsEditForm'
import LoginForm from './component/LoginForm'
import TagsList from './component/TagsList'
import AuthorsList from './component/AuthorsList'

import {
BrowserRouter as Router,
Switch,
Route
  } from"react-router-dom";

import React from 'react';

// import { translate, Trans } from 'react-i18next';
import { translate } from 'react-i18next';

class App extends React.Component {
	
	componentDidMount() {
		document.body.style.paddingTop = '60px';
		document.body.style.paddingBottom = '65px';
		document.body.style.marginLeft = 0;
		document.body.style.marginRight = 0;
	}

	render(){
		const user = JSON.parse(localStorage.getItem("user"));

		const commonPart = <Router>
								<Switch>
									<Route path="/" exact component={NewsContainer} />
									<Route path="/news" exact component={NewsContainer}  />
									<Route path="/news/:id" exact component={NewsCardFull} />
									<Route path="/news/edit/:id" exact component={NewsEditForm} />
									<Route path="/login" exact component={LoginForm} />
									<Route path="/tags/edit" exact component={TagsList} />
									<Route path="/authors/edit" exact component={AuthorsList}/> />
								</Switch>
							</Router>

		return(
			<div>
				<Header i18n={this.props.i18n}/> 
				<div className="container-fluid" role="main">
					
						{user && user.name != null?

						<div className="row">
							<div className="col-3" id="left"> 
								<div className="mx-1 my-3 position-fixed">
									<div className="list-group">
										<a href="/authors/edit" className="list-group-item">{this.props.t("add_edit_author")}</a>
										<a href="/news/edit/-1" className="list-group-item">{this.props.t("add_news")}</a>
										<a href="/tags/edit" className="list-group-item">{this.props.t("add_edit_tag")}</a>
									</div>
								</div> 
							</div>

							<div className="col-9">
								{commonPart}
							</div>
						</div>

						:

						<div className="row">
							<div className="col-12">
								{commonPart}
							</div>
						</div>
						}

					<Footer/> 
					
				</div>	
			</div>
						
		);
	}
}

export default translate()(App)