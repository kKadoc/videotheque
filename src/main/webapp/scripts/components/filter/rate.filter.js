/* globals $ */
'use strict';

/**
 * Filtre retournant la classe CSS associée à une notation
 */
angular.module('videothequeApp').filter('rate', function() {
	return function(input) {
		input = input/2;
    	var res = "rate";
    	var int = Math.floor(input);
    	res += int;
    	var dec = input - int;
    	if (dec >= 0.5) {
    		res+="_5";
    	}
    	return res;
	};
});