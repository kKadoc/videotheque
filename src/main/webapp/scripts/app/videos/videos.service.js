'use strict';

angular.module('videothequeApp')
    .factory('VideoService', function ($http) {
    	var service = {};
        
    	service.play = function(id){
    		$http({
    		    url: 'api/play/' + id, 
    		    method: "GET"
    		 });
    	};
    	
    	service.guess = function(filename, callback){
    		console.log(filename);
    		$http({
    		    url: 'api/guess', 
    		    method: "POST",
    		    data: { f: 'filename' }
    		 }).success(callback);
    	};
    	return service;
    });