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
    	
    	service.guess = function(keyword, useKeyword){
    		var data;
    		if (useKeyword) {
    			data = { k: keyword };
    		}
    		else {
    			data = { f: keyword }
    		}
    		return $http({
    		    url: 'api/guess', 
    		    method: "POST",
    		    data: data,
    		    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
    		    transformRequest: function(obj) {
    		      var str = [];
    		      for(var p in obj)
    		      str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
    		      return str.join("&");
    		    }
    		 }).success(function(data){
    			 service.listGuess = data;
    		 })
    		 .error(function() {
    			 alert("Error getting guess");
    		 });
    	};
    	
    	service.createVideo = function(videoFile, subFile, imdbId, callback){
    		$http({
    		    url: 'api/createVideo', 
    		    method: "POST",
    		    data: { videoFile: videoFile, subFile: subFile == null ? "" : subFile, imdbId: imdbId },
    		    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
    		    transformRequest: function(obj) {
    		      var str = [];
    		      for(var p in obj)
    		      str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
    		      return str.join("&");
    		    }
    		 }).success(callback)
    		 .error(function(data){
    			 alert(data);
    		});
    	};
    	
    	service.refreshFromImdb = function(id, callback){
    		$http({
    		    url: 'api/refreshImdb/' + id, 
    		    method: "GET"
    		 }).success(callback);
    	};
    	
    	service.clearAppDir = function(callback) {
    		$http({
    		    url: 'api/clearAppDir', 
    		    method: "GET"
    		 }).success(callback);
    	};
    	
    	service.scanDirs = function() {
	    	return $http({
			    url: 'api/scan', 
			    method: "GET",
			    
		    	}).success(function(data){
		    		service.listFiles = data;
				})
				.error(function() {
					alert("Error scanning ");
				});
    	};
    	
    	return service;
    });