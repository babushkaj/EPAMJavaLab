import React from 'react';
import TagButtonSimple from './TagButtonSimple'

class TagContainerSimple extends React.Component {

	render(){
        const func = this.props.tags.map(tag => {
            return(
                <TagButtonSimple tagName={tag.name}/>
            )
        })  
        
		return(
            <div>
                {func}
            </div>
		);
	}
}

export default TagContainerSimple