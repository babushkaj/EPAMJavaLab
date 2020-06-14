import React from 'react';

class TagButton extends React.Component {
	render(){
		return(

			<div className="btn btn-outline-primary btn-sm disabled mx-1 my-1">
				<div>
				{this.props.tagName} 
			 		<button type="button" className="close px-1" aria-label="Close" onClick={this.props.deleteClick}>
					<span aria-hidden="true">&times;</span>
					</button>
			 	</div>
			</div>

		);
	}
}

export default TagButton
