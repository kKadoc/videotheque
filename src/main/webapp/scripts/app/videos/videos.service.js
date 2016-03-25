'use strict';

angular.module('videothequeApp')
    .factory('VideoService', function ($http) {
    	var service = {};
        
    	//liste des films potentiels pour une vidéo
    	service.listGuess = [];
    	//liste des fichiers scannés
    	service.listScanFiles = [];
    	
    	/**
    	 * Lecture de la video {id}
    	 * @param : id : id de la video à lire
    	 */
    	service.play = function(id){
    		return $http({
    		    url: 'api/play/' + id, 
    		    method: "GET"
    		 }).error(function(response) {
    			 alert("Error playing vidéo : "+response);
    		 });
    	};
    	
    	/**
    	 * Récupération de la liste de films potentiels correspondant au fichier vidéo
    	 * @param : keyword : le mot clé à utiliser
    	 * @param : customKeyword : indique si le mot clé fourni a été calculé automatiquement ou fourni par l'utilisateur
    	 * @return : alimente listGuess
    	 */
    	service.guess = function(keyword, customKeyword){
    		var data;
    		if (customKeyword) {
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
    		 }).success(function(response){
    			 service.listGuess = response.Search;
    		 })
    		 .error(function() {
    			 alert("Error getting guess");
    		 });
    	};
    	
    	/**
    	 * Enregistre une vidéo en base de donnée
    	 * @param : videoFile : le fichier vidéo
    	 * @param : subFile : le fichier de sous-titres
    	 * @param : imdbId : l'id imdb choisi par l'utilisateur
    	 */
    	service.createVideo = function(videoFile, subFile, imdbId){
    		return $http({
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
    		 }).success(function(data){
    			 console.log('vidéo créée pour ' + videoFile);
    		 })
    		 .error(function() {
    			 alert("Error creating vidéo");
    		 });
    	};
    	
    	/**
    	 * Recharge les informations de la vidéo depuis imdb
    	 * @param : id : id de la vidéo à mettre à jour
    	 */
    	service.refreshFromImdb = function(id){
    		return $http({
    		    url: 'api/refreshImdb/' + id, 
    		    method: "GET"
    		 }).success(function(data){
    			 console.log('vidéo mise à jour');
    		 })
    		 .error(function() {
    			 alert("Error refreshing video");
    		 });
    	};
    	
    	/**
    	 * Nettoie le fichier bibliothèque
    	 */
    	service.clearAppDir = function() {
    		return $http({
    		    url: 'api/clearAppDir', 
    		    method: "GET"
    		 }).success(function(data){
    			 console.log('netoyage réussi');
    		 })
    		 .error(function() {
    			 alert("Error clearing directory");
    		 });
    	};
    	
    	/**
    	 * Recherche les fichiers vidéos dans les répertoires d'entrée
    	 * @return : alimente listScanFiles
    	 */
    	service.scanDirs = function() {
	    	return $http({
			    url: 'api/scan', 
			    method: "GET", 
	    	}).success(function(response){
	    		service.listScanFiles = response;
			})
			.error(function(response) {
				console.log(response);
				alert("Error scanning : "+ response);
			});
    	};
    	

    	/**
    	 * Supprime le fichier indiqué
    	 */
    	service.eraseFile = function(file) {
    		return $http({
    		    url: 'api/eraseFile', 
    		    method: "POST",
    		    data: { file: file},
    		    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
    		    transformRequest: function(obj) {
    		      var str = [];
    		      for(var p in obj)
    		      str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
    		      return str.join("&");
    		    }
    		 }).success(function(data){
    			 console.log('fichier supprimé');
    		 })
    		 .error(function() {
    			 alert("Error erasing file");
    		 });
    	};
    	
    	/**
    	 * Ajoute le fichier indiqué à la liste des fichiers ignorés
    	 */
    	service.ignoreFile = function(file) {
    		return $http({
    		    url: 'api/ignoreFile', 
    		    method: "POST",
    		    data: { file: file},
    		    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
    		    transformRequest: function(obj) {
    		      var str = [];
    		      for(var p in obj)
    		      str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
    		      return str.join("&");
    		    }
    		 }).success(function(data){
    			 console.log('fichier ignoré');
    		 })
    		 .error(function() {
    			 alert("Error ignoring file");
    		 });
    	};

    	
    	return service;
    });