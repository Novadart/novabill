'use strict';

angular.module('novabill.utils', ['novabill.translations', 'novabill.constants'])

/*
 * FACTORIES
 */
.factory('nSorting', ['nConstants', function(nConstants) {
	return {

		/**
		 * Compare two client addresses
		 * @return -1 if minor, 0 if equal, 1 if major
		 */
		clientAddressesComparator : function(c1, c2) {
			var s1 = c1.name.toLowerCase();
			var s2 = c2.name.toLowerCase();
			return s1<s2 ? -1 : (s1>s2 ? 1 : 0);
		},

		/**
		 * Compare two clients
		 * @return -1 if minor, 0 if equal, 1 if major
		 */
		clientsComparator : function(c1, c2) {
			var s1 = c1.name.toLowerCase();
			var s2 = c2.name.toLowerCase();
			return s1<s2 ? -1 : (s1>s2 ? 1 : 0);
		},

		/**
		 * Compare two price lists
		 * @return -1 if minor, 0 if equal, 1 if major
		 */
		priceListsComparator : function(p1, p2){
			if(p1.name === nConstants.conf.defaultPriceListName){ return -1; }
			if(p2.name === nConstants.conf.defaultPriceListName){ return 1; }

			var n1 = p1.name.toLowerCase();
			var n2 = p2.name.toLowerCase();
			return n1<n2 ? -1 : (n1>n2 ? 1 : 0);
		},

		/**
		 * Compare two objects that have a 'description' property
		 */
		descriptionComparator : function(i1, i2){
			var n1 = i1.description.toLowerCase();
			var n2 = i2.description.toLowerCase();
			return n1<n2 ? -1 : (n1>n2 ? 1 : 0);
		}

	};
}])

.factory('nRegExp', function() {
	return {

		twoDecimalsFloatNumber : /^(\+|\-)?\d+((\.|\,)\d{1,2})?$/,
		positiveTwoDecimalsFloatNumber : /^\d+((\.|\,)\d{1,2})?$/,

		positiveFloatNumber : /^\d+((\.|\,)\d+)?$/,
		floatNumber : /^\-?\d+((\.|\,)\d+)?$/,

		reserved_word : /^\::.*$/,
		
		ssn : /^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$/,
		vatId : /^([A-Z]{2})?[0-9]{11}$/

	};
})


.factory('nDownload', ['$window', '$document', 'nConstants', function($window, $document, nConstants) {

	var HIDDEN_IFRAME = angular.element('<iframe style="display: none;"></iframe>');
	angular.element($document[0].body).append(HIDDEN_IFRAME);

	return {

		_downloadPdf : function(documentClass, documentId){
			var pdfUrl = nConstants.conf.pdfDownloadUrl
			.replace('{document}', documentClass)
			.replace('{id}', documentId);
			
			HIDDEN_IFRAME.attr('src', pdfUrl);
		},
		
		_printPdf : function(documentClass, documentId){
			var pdfUrl = nConstants.conf.pdfPrintUrl
			.replace('{document}', documentClass)
			.replace('{id}', documentId);
			
			var openPrintPage = nConstants.conf.pdfPrintPageUrl
			.replace('{pdfUrl}', encodeURIComponent(pdfUrl));
			
			$window.open(openPrintPage, '_blank');
		},
		
		_formatDate : function(date){
			var formatedDate = date.getFullYear() + '-' + ('0'+(date.getMonth()+1)).slice(-2) + '-' + ('0'+date.getDate()).slice(-2);
			return formatedDate;
		},

		downloadInvoicePdf : function(documentId){
			this._downloadPdf('invoices', documentId);
		},

		downloadEstimationPdf : function(documentId){
			this._downloadPdf('estimations', documentId);
		},

		downloadCreditNotePdf : function(documentId){
			this._downloadPdf('creditnotes', documentId);
		},

		downloadTransportDocumentPdf : function(documentId){
			this._downloadPdf('transportdocs', documentId);
		},
		
		downloadPaymentsProspect : function(filteringDateType, startDate, endDate){
			var prospectUrl = nConstants.conf.pdfPaymentsProspectUrl
			.replace('{filteringDateType}', filteringDateType)
			.replace('{startDate}', startDate ? this._formatDate(startDate) : '')
			.replace('{endDate}', endDate ? this._formatDate(endDate) : '');
			
			HIDDEN_IFRAME.attr('src', prospectUrl);
			
		},
		
		downloadExportZip : function(clients, invoices, estimations, creditNotes, transportDocs){
			var exportZipUrl = nConstants.conf.exportDwonloadUrl
			.replace('{c}', clients)
			.replace('{i}', invoices)
			.replace('{e}', estimations)
			.replace('{cn}', creditNotes)
			.replace('{t}', transportDocs);
			
			HIDDEN_IFRAME.attr('src', exportZipUrl);
		},
		
		printInvoicePdf : function(documentId){
			this._printPdf('invoices', documentId);
		},

		printEstimationPdf : function(documentId){
			this._printPdf('estimations', documentId);
		},

		printCreditNotePdf : function(documentId){
			this._printPdf('creditnotes', documentId);
		},

		printTransportDocumentPdf : function(documentId){
			this._printPdf('transportdocs', documentId);
		}
		
	};
}])


/*
 * FILTERS
 */

.filter('nFriendlyDate',['$filter', function($filter){

	return function(dateToFormat, format){
		var target = new Date( parseInt(dateToFormat) );
		target.setHours(0, 0, 0, 0);

		var today = new Date();
		today.setHours(0, 0, 0, 0);

		if(target.getTime() === today.getTime()){

			return $filter('translate')('TODAY');

		} else {

			var yesterday = new Date();
			yesterday.setHours(0, 0, 0, 0);
			yesterday.setDate(yesterday.getDate() - 1);

			if(target.getTime() === yesterday.getTime()){

				return $filter('translate')('YESTERDAY');

			} else {

				return $filter('date')(dateToFormat, format);
			}

		}
	};
}])


/**
 * Replace the reserver word with some custom string. If no string is supplied, return the empty string
 */
.filter('nFilterDefault', ['$filter', 'nRegExp', function($filter, nRegExp) {
	return function(input, replacement) {
		return nRegExp.reserved_word.test(input) ? (replacement ? $filter('translate')(replacement) : '') : input;
	};
}])


/**
 * Analytics Service
 */
.provider('nAnalytics', [ function() {

	var urlPath = "";

	this.urlPath = function(value){
		urlPath = value;
	};

	this.$get = ['$rootScope', '$window', '$location', '$log', function($rootScope, $window, $location, $log){

		// setup Google Analytics if present, or mock it
		var GA = $window.ga ? $window.ga : function(action, event, url){
			$log.debug('Analytics::_trackPageview[ '+ (url ? url : $window.location.href) +' ]');
		};

		//service instance
		var nAnalyticsInstance = {

				// track page view
				trackPageview : function(url){
					if(url) {
						GA('send', 'pageview', url);
					} else {
						GA('send', 'pageview');
					}
				}

		};

		// automatically track page views
		$rootScope.$on('$viewContentLoaded', function(){
			nAnalyticsInstance.trackPageview(urlPath + '#' + $location.path());
		});

		return nAnalyticsInstance;
	}];

}]);
