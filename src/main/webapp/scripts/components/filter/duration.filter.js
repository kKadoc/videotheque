/* globals $ */
'use strict';

/**
 * Filtre de mise en forme de la durée
 */
angular.module('videothequeApp').filter('duration', function() {
	return function(input) {
		var res = "";
    	var hours = Math.floor(input / 60);
    	var min = input % 60;
    	if (hours > 0) {
    		res += hours + "h"
    	}
    	if (min > 0){
    		res += min + "min"
    	}
    	return res;
	};
});