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
            
    		$scope.scanDirs();
        	
            //on affiche le module de creation
            $('#scanFolderPanel').modal('show');
        };   
        
        $scope.scanDirs = function() {
        	VideoService.scanDirs()
				.then(function() {
					$scope.listScanFiles = VideoService.listScanFiles;
				});
        };
        
        
        
        
        
        
        //CREATE MODAL
        
        $scope.creation = {
        	videoFile:null,
        	subFile:null,
        	keyword:null,
        	listGuess:null,
        	customKeyword:null,
        	imdbId:null
        };
        //affichage de la création d'une video
        $scope.showCreateModal = function () {
        	
        	$scope.creation.videoFile = null;
            $scope.creation.subFile = null;
            $scope.creation.keyword = null;
            $scope.creation.imdbId = null;
            $scope.creation.listGuess = [];
            $scope.creation.customKeyword = false;
            
            //on affiche le module de creation
            $('#createVideoPanel').modal('show');
        };
        
        //affichage de la création d'une vidéo avec fichier vidéo fourni
        $scope.showCreateModalWithFile = function (file) {
        	$scope.showCreateModal();
        	$scope.creation.videoFile = file;
        	$scope.updateKeyword();
        	$scope.guess();
        };
        
        //affichage de la création d'une vidéo avec fichier vidéo fourni        
        $scope.showCreateModalWithFileAndSubs = function (video, sub) {
        	console.log(video + ' - ' + sub);
        	$scope.showCreateModal();
        	$scope.creation.videoFile = video;
        	$scope.creation.subFile = sub;
        	$scope.updateKeyword();
        	$scope.guess();
        };
        
        //mise à jour du mot clé de recherche imdb
        $scope.updateKeyword = function() {
        	$scope.creation.keyword = $scope.creation.videoFile;
        	//on vire le dossier
        	if ($scope.creation.videoFile != null && $scope.creation.videoFile != "" && $scope.creation.videoFile.indexOf("/") != -1) {
        		$scope.creation.keyword = $scope.creation.videoFile.substr($scope.creation.videoFile.lastIndexOf("/")+1) ;
        	}
        	if ($scope.creation.videoFile != null && $scope.creation.videoFile != "" && $scope.creation.videoFile.indexOf("\\") != -1) {
        		$scope.creation.keyword = $scope.creation.videoFile.substr($scope.creation.videoFile.lastIndexOf("\\")+1) ;
        	}
        	//on vire l'extension
        	if ($scope.creation.keyword != null && $scope.creation.keyword != "" && $scope.creation.keyword.indexOf(".") != -1) {
        		$scope.creation.keyword = $scope.creation.keyword.substr(0, $scope.creation.keyword.lastIndexOf(".")) ;
        	}

        	$scope.creation.customKeyword = false;	
        };
        
        //tente de deviner la vidéo d'apres le mot clé
        $scope.guess = function() {
        	if ($scope.creation.keyword != null && $scope.creation.keyword != "") {
	        	VideoService.guess($scope.creation.keyword, $scope.creation.customKeyword)
				.then(function() {
					$scope.creation.listGuess = VideoService.listGuess;
				});
	        }
        };
          
        //création de la vidéo d'apres les fichiers en cours et l'id imdb fourni
        $scope.createVideo = function(imdbId) {    	
        	VideoService.createVideo($scope.creation.videoFile, $scope.creation.subFile, imdbId)
			.then(function () {
				$('#createVideoPanel').modal('hide');
				$scope.refreshVideosList();
				$scope.scanDirs();
			});
        };
        
        //lance la création de la vidéo en utilisant l'id imdb saisie par l'utilisateur
        $scope.useImdbId = function() {
        	$scope.createVideo($scope.creation.imdbId);
        }

    });
