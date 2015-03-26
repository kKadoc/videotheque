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
    		$http({
    		    url: 'api/guess', 
    		    method: "POST",
    		    data: { f: filename },
    		    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
    		    transformRequest: function(obj) {
    		      var str = [];
    		      for(var p in obj)
    		      str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
    		      return str.join("&");
    		    }
    		 }).success(callback);
    	};
    	
    	service.refreshFromImdb = function(id, callback){
    		$http({
    		    url: 'api/refreshImdb/' + id, 
    		    method: "GET"
    		 }).success(callback);
    	};
    	return service;
    });