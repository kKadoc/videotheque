'use strict';

angular.module('videothequeApp')
    .factory('VideoService', function ($http) {
    	var service = {};
        
    	service.play = function(id){
    		console.log(id);
    		$http({
    		    url: 'api/play/' + id, 
    		    method: "GET"
    		 });
    	};
    	return service;
    });