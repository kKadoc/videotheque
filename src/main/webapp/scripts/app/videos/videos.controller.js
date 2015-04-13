'use strict';

angular.module('videothequeApp')
    .controller('VideosController', function ($scope, $cookies, $browser, localStorageService, Video, VideoType, VideoService) {
    	
    	var videoExt = ["mp4", "divx", "avi", "mov", "mpg"];
    	var subExt = ["srt"];
    	
    	//récupération des types de vidéo
    	$scope.types = [];
    	$scope.refreshTypesList = function() {
    		VideoType.query(function(result) {
               $scope.types = result;
            });
        };
        $scope.refreshTypesList();
        
    	// récupération de la liste
    	
    	$scope.refreshVideosList = function() {
    		Video.query(function(result) {
               $scope.videos = result;
            });
        };
        $scope.refreshVideosList();
        
        // gestion du type d'affichage avec stockage en localStorage
        $scope.display = localStorageService.get('display');
        if ($scope.display == null) {
        	$scope.display = "list";
        }
        $scope.changeDisplay = function(display) {
        	$scope.display = display;
        	localStorageService.set('display', display);
        };
        
        // methode d'affichage de la durée
        $scope.displayDuration = function(duration) {
        	var res = "";
        	var hours = Math.floor(duration / 60);
        	var min = duration % 60;
        	if (hours > 0) {
        		res += hours + "h"
        	}
        	if (min > 0){
        		res += min + "min"
        	}
        	return res;
        };
        
        // methode d'affichage de la date
        $scope.displayDate = function(in_date) {
        	var res = "";
        	if (in_date != null && in_date.length > 10) {
	        	var year = in_date.substring(0,4);
	        	var month = in_date.substring(5,7);
	        	var day = in_date.substring(8, 10);
	        	res = day + "/" + month + "/" + year;
	        }
	        else {
	        	res = in_date;
	        }
        	
        	return res;
        };
        
        //methode d'affichage de la notation
        $scope.displayRate = function(in_rate) {
        	in_rate = in_rate/2;
        	var res = "rate";
        	var int = Math.floor(in_rate);
        	res += int;
        	var dec = in_rate - int;
        	if (dec >= 0.5) {
        		res+="_5";
        	}
        	return res;
        };
        
        //gestion du filtre
        $scope.filterFunction = function(element) {
        	if ($scope.filterName != null && $scope.filterName != ""){
        		var fN = $scope.filterName.toLowerCase();
        		if (element.title.toLowerCase().indexOf(fN) === -1) {
        			return false;
        		}
        	}
        	
        	if ($scope.filterType != null && $scope.filterType != ""){
        		for (var i = 0; i < element.videoTypes.length; i++) {
        	        if (element.videoTypes[i].name === $scope.filterType) {
        	            return true;
        	        }
        	    }
        		
        		return false;
        	}
    		return true;
    	};
        	
        // gestion du tri
        $scope.orderField = "title";
        $scope.reverseOrder = false;
        
        $scope.orderBy = function(in_order) {
        	if (in_order === $scope.orderField) {
        		 $scope.reverseOrder = ! $scope.reverseOrder;
        	}
        	else {
        		$scope.orderField = in_order;
        		$scope.reverseOrder = false;
        	}
        };

        //lecture d'une video
        $scope.play = function(id) {
        	VideoService.play(id);
        };
        
        //affichage de la création d'une video
        $scope.showCreateModal = function () {
        	
        	$scope.videoFile = null;
            $scope.subFile = null;
            $scope.keyword = null;
            $scope.listGuess = null;
            $scope.customKeyword = false;
            
            
            //on affiche le module de creation
            $('#createVideoPanel').modal('show');
        };
        
        //mise à jour du mot clé de recherche imdb
        $scope.updateKeyword = function() {
        	$scope.keyword = $scope.videoFile;
        	//on vire le dossier
        	if ($scope.videoFile != null && $scope.videoFile != "" && $scope.videoFile.indexOf("/") != -1) {
        		$scope.keyword = $scope.videoFile.substr($scope.videoFile.lastIndexOf("/")+1) ;
        	}
        	if ($scope.videoFile != null && $scope.videoFile != "" && $scope.videoFile.indexOf("\\") != -1) {
        		$scope.keyword = $scope.videoFile.substr($scope.videoFile.lastIndexOf("\\")+1) ;
        	}
        	//on vire l'extension
        	if ($scope.keyword != null && $scope.keyword != "" && $scope.keyword.indexOf(".") != -1) {
        		$scope.keyword = $scope.keyword.substr(0, $scope.keyword.lastIndexOf(".")) ;
        	}
        	
        	$scope.customKeyword = false;
        	
        };
        
        //tente de deviner la vidéo d'apres le mot clé
        $scope.guess = function() {
        	console.log($scope.customKeyword);
        	if ($scope.keyword != null && $scope.keyword != "") {
	        	VideoService.guess($scope.keyword, $scope.customKeyword, function (data) {
	                $scope.listGuess = data;
	            });
	        }
        };
          
        //upload des fichiers et création de la vidéo
        $scope.createVideo = function(imdbId) {    	
        	VideoService.createVideo($scope.videoFile, $scope.subFile, imdbId, function (data) {
        		if (data != null) {
        			alert(data);
        		}
        		$scope.refreshVideosList();
        		
        		$('#createVideoPanel').modal('hide');
            });
        };
        
        $scope.useImdbId = function() {
        	$scope.createVideo($scope.imdbId);
        }
        
       $scope.toggleFav = function(video) {
		   video.favoris = !video.favoris;
		   Video.update(video, function () {});
       }
       
       
       
       $scope.watchcount = function () { return getWatchCount($scope); };
       
       $scope.$watch('$viewContentLoaded', function() {
    	    //console.profileEnd();
    	    //console.log("done");
    	  });
       
       function getWatchCount (scope, scopeHash) {
    	    // default for scopeHash
    	    if (scopeHash === undefined) {
    	        scopeHash = {};
    	    }
    	    
    	    // make sure scope is defined and we haven't already processed this scope
    	    if (!scope || scopeHash[scope.$id] !== undefined) {
    	        return 0;
    	    }
    	    
    	    var watchCount = 0;
    	    
    	    if (scope.$$watchers) {
    	        watchCount = scope.$$watchers.length;
    	    }
    	    scopeHash[scope.$id] = watchCount;
    	    
    	    // get the counts of children and sibling scopes
    	    // we only need childHead and nextSibling (not childTail or prevSibling)
    	    watchCount+= getWatchCount(scope.$$childHead, scopeHash);
    	    watchCount+= getWatchCount(scope.$$nextSibling, scopeHash);

    	    return watchCount;
    	}

    });
