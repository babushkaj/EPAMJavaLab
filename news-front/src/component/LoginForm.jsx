import React from 'react'

import UserDataService from './UserDataService'

import { translate } from 'react-i18next';

class LoginForm extends React.Component{

    login(){
        let userLogin = document.getElementById("loginField").value;
        let userPassword = document.getElementById("passwordField").value;
        let user = {login: userLogin, password: userPassword};
        UserDataService.login(user)
        .then(
            response => {
                user = response.data;
                if(user){
                    localStorage.setItem("user", JSON.stringify(user));
                    
                    window.location.href = "/news";
                } else {
                    //incorrect login or password
                }

            }
        )
    }

    render(){
        return(
            <div className="container">
                <div className="row ">
                    <div className="col-3"></div>
                    <div className="col-6">
                        <form>
                            <div className="form-group">
                                <label for="loginField">{this.props.t("login_field")}</label>
                                <input id="loginField" className="form-control"  type="text" placeholder={this.props.t("login_field")}/>
                            </div>
                            <div className="form-group">
                                <label for="passwordField">{this.props.t("password")}</label>
                                <input id="passwordField" className="form-control" type="password" placeholder={this.props.t("password")}/>
                            </div>
                            <button type="button" className="btn btn-primary" onClick = {() => this.login()}>{this.props.t("login")}</button>
                        </form>
                    </div>
                    <div className="col-3"></div>
                </div>
            </div>
        );
    }
}

export default translate()(LoginForm)