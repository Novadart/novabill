'use strict';

angular.module('novabill-frontend.constants', [])

.provider('nConstantsFrontend', function() {

	// these variables are set in the environment with values coming from the server.
	// setting default values useful for testing
	var basePath = '/novabill-server/';
	var version = '0';

	//updating the values in case they are set in the environment
	if(typeof NovabillFrontendConf != 'undefined') {
		basePath = NovabillFrontendConf.basePath;
		version = NovabillFrontendConf.version;
	}

	// this set of properties is available even at module configuration time
	// these properties define the most basic configuration of the UI
	this.conf = {
			baseUrl : basePath,
			version : version,
			
			partialsBaseUrl : basePath + 'novabill/frontend/partials/',
	};
	
	var baseConf = this.conf;

	this.url = {
			htmlFragmentUrl : function(path){ return baseConf.partialsBaseUrl + (path.charAt(0)=='/' ? path.substring(1) : path) + '?v=' + baseConf.version; }
	};

	// this is the full set of properties, available only after the module is loaded
	this.$get = function(){

		var baseConf = this.conf;

		return {

			conf : baseConf,
			
			url : {
				
				htmlFragmentUrl : this.url.htmlFragmentUrl
				
			}
			
		};

	};

});