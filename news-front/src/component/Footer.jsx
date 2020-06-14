import React from 'react';

class Footer extends React.Component {
	render(){
		return(
			<footer className="footer font-small bg-primary fixed-bottom">
				<div className="footer-copyright text-center py-3 text-light">
					© Copyright EPAM 2020: All right reserved
				</div>
			</footer>
		);
	}
}

export default Footer