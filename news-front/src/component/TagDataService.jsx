import React from 'react';

import axios from 'axios'
// import $ from "jquery"

const TAGS_URL = 'tags'
const ROOT_API_URL = 'http://localhost:8080'
const TAGS_API_URL = `${ROOT_API_URL}/news/${TAGS_URL}`

class TagDataService extends React.Component{

    getAllTags(fromPage, howManyTags){
        return axios.get(`${TAGS_API_URL}`,{
            params: {
                pageNumber: fromPage,
                howMany: howManyTags,
            }
        });
    }

    getTagById(tagId){
        return axios.get(`${TAGS_API_URL}/` + tagId);
    }

    deleteTag(tagId){
        return axios.delete(`${TAGS_API_URL}/` + tagId);
    }

    createTag(tag){
        return axios.post(`${TAGS_API_URL}`, tag);
    }

    editTag(tag){
        return axios.put(`${TAGS_API_URL}`, tag);
    }
}

export default new TagDataService()
