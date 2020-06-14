import React from 'react';
import TagButton from './TagButton'
import TagDataService from './TagDataService'

class TagContainer extends React.Component {
    constructor(props){
        super(props);
        this.getAllTagsCallback = this.getAllTagsCallback.bind(this);
        this.deleteTagCallback = this.deleteTagCallback.bind(this);
        this.state = {
            tags:[],
            message: "",
		}
    }

    componentDidMount() {
        this.refreshTags();
    }

    refreshTags(){
        TagDataService.getAllTags(this.getAllTagsCallback);
    }

    deleteTag(id){
        TagDataService.deleteTag(this.deleteTagCallback, id);
    }

    getAllTagsCallback(result){
        this.setState({tags: result});
    }

    deleteTagCallback(){
        this.setState({message: 'Tag was deleted'});
        this.refreshTags();
    }

	render(){
        const func = this.state.tags.map(tag => {
            return(
                <TagButton 
                tagName={tag.name}
                deleteClick={() => this.deleteTag(tag.id)}/>
            )
        })  
        
		return(
            <div>
                {func}
            </div>
		);
	}
}

export default TagContainer