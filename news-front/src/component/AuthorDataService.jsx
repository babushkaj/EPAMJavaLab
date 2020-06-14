import React from 'react';

import axios from 'axios'

const AUTHORS_URL = 'authors'
const ROOT_API_URL = 'http://localhost:8080'
const AUTHORS_API_URL = `${ROOT_API_URL}/news/${AUTHORS_URL}`

class AuthorDataService extends React.Component{

    getAllAuthors(fromPage, howManyAuthors){
        return axios.get(`${AUTHORS_API_URL}`, {
            params: {
                pageNumber: fromPage,
                howMany: howManyAuthors,
            }
        });
    }

    getAuthorById(authorId){
        return axios.get(`${AUTHORS_API_URL}/` + authorId);
    }

    deleteAuthor(authorId){
        return axios.delete(`${AUTHORS_API_URL}/` + authorId);
    }

    createAuthor(author){
        return axios.post(`${AUTHORS_API_URL}`, author);
    }

    editAuthor(author){
        return axios.put(`${AUTHORS_API_URL}`, author);
    }

}

export default new AuthorDataService()