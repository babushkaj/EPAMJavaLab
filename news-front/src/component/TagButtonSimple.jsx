import React from 'react';

class TagButtonSimple extends React.Component {
	render(){
		return(

			<div className="btn btn-outline-primary btn-sm disabled mx-1 my-1">
				<div>
				    {this.props.tagName} 
			 	</div>
			</div>

		);
	}
}

export default TagButtonSimple