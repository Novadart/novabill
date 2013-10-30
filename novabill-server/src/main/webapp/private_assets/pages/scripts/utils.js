angular.module('utils', []).

factory('Nsorting', function() {
	return {
		
		/**
		 * Compare two clients
		 * @return -1 if minor, 0 if equal, 1 if major
		 */
		clientsComparator : function(c1, c2) {
			var s1 = c1.name.toLowerCase();
			var s2 = c2.name.toLowerCase();
			return s1<s2 ? -1 : (s1>s2 ? 1 : 0);
		}
		
	};
});