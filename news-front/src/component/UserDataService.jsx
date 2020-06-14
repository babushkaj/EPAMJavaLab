import React from 'react';

import axios from 'axios'

const USERS_URL = 'users'
const ROOT_API_URL = 'http://localhost:8080/news/api'
const USERS_API_URL = `${ROOT_API_URL}/${USERS_URL}`

class UserDataService extends React.Component{

    // login(login, password){
    //     return axios.post(USERS_API_URL, login, password);
    // }
   
    login(user){
        return  axios.post(USERS_API_URL, user);
    }
 
}

export default new UserDataService()