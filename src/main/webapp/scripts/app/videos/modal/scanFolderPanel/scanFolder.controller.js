'use strict';

angular.module('videothequeApp')
    .controller('ScanFolderController', function ($scope, VideoService) {

    	$scope.listFiles = [];
    	
    	//affichage de la création d'une video
    	$scope.showScanModal = function () {      
            
    		$scope.refreshVideosList();
    		
        	VideoService.scanDirs()
				.then(function() {
					$scope.listFiles = VideoService.listScanFiles;
				});
        	
            //on affiche le module de creation
            $('#scanFolderPanel').modal('show');
        };   
    });
