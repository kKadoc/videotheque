'use strict';

angular.module('videothequeApp')
    .controller('VideosController', function ($scope, localStorageService, Video) {
    	
    	// récupération de la liste
    	$scope.videos = [];
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
        
        
    });
