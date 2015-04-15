'use strict';

angular.module('videothequeApp')
    .controller('VideosController', function ($scope, $cookies, $browser, localStorageService, Video, VideoType, VideoService) {
    	   	
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
