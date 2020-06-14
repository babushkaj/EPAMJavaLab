import React from 'react';

import { translate } from 'react-i18next';

class Header extends React.Component {

    changeLanguage(lng){
        document.cookie = "i18next=" + lng + "; path=/";
        this.props.i18n.changeLanguage(lng);
    }

    render() {

        const user = JSON.parse(localStorage.getItem("user"));

        return (
        <nav className="navbar navbar-expand-md navbar-dark bg-primary justify-content-center fixed-top" data-spy="affix">

            <a className="navbar-brand d-flex w-50 mr-auto" href="/">News Portal</a>

            <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsingNavbar" aria-controls="collapsingNavbar" aria-expanded="false" aria-label="Toggle navigation">
                <span className="navbar-toggler-icon"></span>
            </button>

            <div className="navbar-collapse collapse w-100" id="collapsingNavbar">
                <ul className="navbar-nav w-100 justify-content-center">
                    <li className="nav-item active">
                        <a className="nav-link" href="#" onClick={() => this.changeLanguage('en')}>{this.props.t("EN")}</a>
                    </li>
                    <li className="nav-item active">
                        <a className="nav-link" href="#" onClick={() => this.changeLanguage('ru')}>{this.props.t("RU")}</a>
                    </li>
                </ul>
                

                {(user !== null && user.name !== null) ?

                    <ul className="nav navbar-nav ml-auto w-100 justify-content-end">
                        <li className="nav-item active">
                            <span className="nav-link" href="/login">{this.props.t("welcome_back") + ", " + user.name}</span>
                        </li>
                        <li className="nav-item active">
                            <a className="nav-link" href="/" onClick = {() => {localStorage.removeItem("user")}}>{this.props.t("logout")}</a>
                        </li>
                    </ul>

                :
                    
                    <ul className="nav navbar-nav ml-auto w-100 justify-content-end">
                        <li className="nav-item active">
                            <a className="nav-link" href="#">{this.props.t("register")}</a>
                        </li>
                        <li className="nav-item active">
                            <a className="nav-link" href="/login">{this.props.t("login")}</a>
                        </li>
                    </ul>

                }


               
            </div>
        </nav>);
    }
}

export default translate()(Header)