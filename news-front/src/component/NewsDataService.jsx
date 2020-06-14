import React from 'react';

import axios from 'axios'
// import $ from 'jquery'

const NEWS_URL = 'news'
const ROOT_API_URL = 'http://localhost:8080'
const NEWS_API_URL = `${ROOT_API_URL}/news/${NEWS_URL}`

class NewsDataService extends React.Component{

    // getAllNews(fromPage, howManyTags){
    //     return axios.get(`${NEWS_API_URL}`, {
    //         params: {
    //             pageNumber: fromPage,
    //             howMany: howManyTags
    //         }
    //     });
    // }

    getNewsById(newsId){
        return axios.get(`${NEWS_API_URL}/` + newsId);
    }

    findNews(searchString, fromPage, howManyNews){
        return axios.get(`${NEWS_API_URL}`, {
            params: {
                search: searchString,
                pageNumber: fromPage,
                howMany: howManyNews
            }
        });
    }

    deleteNews(newsId){
        return axios.delete(`${NEWS_API_URL}/` + newsId);
    }

    createNews(news){
        return axios.post(`${NEWS_API_URL}`, news);
    }

    editNews(news){
        return axios.put(`${NEWS_API_URL}`, news);
    }

}

export default new NewsDataService()