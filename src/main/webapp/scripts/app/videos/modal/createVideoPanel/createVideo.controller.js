'use strict';

angular.module('videothequeApp')
    .controller('CreateVideoController', function (VideoService, $scope) {

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
