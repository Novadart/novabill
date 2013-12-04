'use strict';

angular.module('novabill.constants', [])

.factory('NConstants', function() {
	return {
		
		exception : {
			VALIDATION : 'com.novadart.novabill.shared.client.exception.ValidationException'
		},
		
		validation : {
			NOT_UNIQUE : 'NOT_UNIQUE'
		}
		
	};
});