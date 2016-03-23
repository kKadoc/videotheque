'use strict';

angular.module('videothequeApp')
    .controller('VideosController', function ($scope, $location, $cookies, $browser, $window, localStorageService, Video, VideoType, VideoService) {
    	   	
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
        	//$location.path('/play').search({id: id});
        };
        
        //ouverture de la page imdb
        $scope.info = function(imdbid) {
        	$window.open('http://www.imdb.com/title/'+imdbid, '_blank');
        };
        
		$scope.toggleFav = function(video) {
			video.favoris = !video.favoris;
			Video.update(video, function () {});
		}

		
		
		
		//SCAN MODAL
		
		$scope.listScanFiles = [];
    	
    	//affichage de la création d'une video
    	$scope.showScanModal = function () {      
            
    		$scope.refreshVideosList();
    		
        	VideoService.scanDirs()
				.then(function() {
					$scope.listScanFiles = VideoService.listScanFiles;
				});
        	
            //on affiche le module de creation
            $('#scanFolderPanel').modal('show');
        };   
        
        
        
        
        
        
        //CREATE MODAL
      //affichage de la création d'une video
        $scope.showCreateModal = function () {
        	
        	$scope.videoFile = null;
            $scope.subFile = null;
            $scope.keyword = null;
            $scope.listGuess = [];
            $scope.customKeyword = false;
            
            //on affiche le module de creation
            $('#createVideoPanel').modal('show');
        };
        
        //affichage de la création d'une vidéo avec fichier vidéo fourni
        $scope.showCreateModalWithFile = function (file) {
        	$scope.showCreateModal();
        	$scope.videoFile = file;
        	$scope.updateKeyword();
        	$scope.guess();
        };
        
        //affichage de la création d'une vidéo avec fichier vidéo fourni        
        $scope.showCreateModalWithFileAndSubs = function (video, sub) {
        	console.log(video + ' - ' + sub);
        	$scope.showCreateModal();
        	$scope.videoFile = video;
        	$scope.subFile = sub;
        	$scope.updateKeyword();
        	$scope.guess();
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
        	if ($scope.keyword != null && $scope.keyword != "") {
	        	VideoService.guess($scope.keyword, $scope.customKeyword)
				.then(function() {
					$scope.listGuess = VideoService.listGuess;
				});
	        }
        };
          
        //création de la vidéo d'apres les fichiers en cours et l'id imdb fourni
        $scope.createVideo = function(imdbId) {    	
        	VideoService.createVideo($scope.videoFile, $scope.subFile, imdbId)
			.then(function () {
				$('#createVideoPanel').modal('hide');
			});
        };
        
        //lance la création de la vidéo en utilisant l'id imdb saisie par l'utilisateur
        $scope.useImdbId = function() {
        	$scope.createVideo($scope.imdbId);
        }

    });
