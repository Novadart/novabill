'use strict';

/*
 * See http://engineering.talis.com/articles/client-side-error-logging/
 */

angular.module('novabill.logging', [])

.constant('CSRF_HEADER', angular.element("meta[name='_csrf_header']").attr("content"))

.constant('CSRF_VALUE', angular.element("meta[name='_csrf']").attr("content"))

/**
 * Service that gives us a nice Angular-esque wrapper around the
 * stackTrace.js pintStackTrace() method. 
 */
.factory("nTrace", [function(){
	return {
		print: printStackTrace
	};
}])

/**
 * Override Angular's built in exception handler, and tell it to 
 * use our new exceptionLoggingService which is defined below
 */
.provider("$exceptionHandler", {
	$get: ['nExceptionLogger', function(nExceptionLogger){
		return nExceptionLogger;
	}]
})

/**
 * Exception Logging Service, currently only used by the $exceptionHandler
 * it preserves the default behaviour ( logging to the console) but 
 * also posts the error server side after generating a stacktrace.
 */
.factory("nExceptionLogger", ["$log","$window", "nTrace", 'nConstants', 'CSRF_HEADER', 'CSRF_VALUE',
                              function($log, $window, nTrace, nConstants, CSRF_HEADER, CSRF_VALUE){

	function error(exception, cause){

		// preserve the default behaviour which will log the error
		// to the console, and allow the application to continue running.
		$log.error.apply($log, arguments);

		// now try to log the error to the server side.
		try{
			var errorMessage = exception.toString();

			// use our nTrace to generate a stack trace
			var stackTrace = nTrace.print({e: exception});

			// use AJAX (in this example jQuery) and NOT 
			// an angular service such as $http
			var heads = {};
			heads[CSRF_HEADER] = CSRF_VALUE;
			
			$.ajax({
				type: "POST",
				headers : heads,
				url: nConstants.conf.clientUiErrorUrl, 
				contentType: "application/json",
				data: angular.toJson({
					source : 'nExceptionLogger',
					url: $window.location.href,
					message: errorMessage,
					type: "exception",
					stackTrace: stackTrace,
					cause: ( cause || "")
				})
			});

		} catch (loggingError){
			$log.warn("Error server-side logging failed");
			$log.log(loggingError);
		}
	};

	return error;
}])


.factory("nApplicationLogger", ["$log", "$window", 'CSRF_HEADER', 'CSRF_VALUE',
                                function($log, $window, CSRF_HEADER, CSRF_VALUE){
	var heads = {};
	heads[CSRF_HEADER] = CSRF_VALUE;
	
	return{

		error: function(message){
			// preserve default behaviour
			$log.error.apply($log, arguments);
			// send server side
			$.ajax({
				type: "POST",
				headers : heads,
				url: nConstants.conf.clientUiErrorUrl,
				contentType: "application/json",
				data: angular.toJson({
					source : 'nApplicationLogger',
					url: $window.location.href,
					message: message,
					type: "error"
				})
			});
		},

		debug: function(message){
			$log.log.apply($log, arguments);
			$.ajax({
				type: "POST",
				headers : heads,
				url: nConstants.conf.clientUiErrorUrl,
				contentType: "application/json",
				data: angular.toJson({
					source : 'nApplicationLogger',
					url: $window.location.href,
					message: message,
					type: "debug"
				})
			});
		}
	};
}]);