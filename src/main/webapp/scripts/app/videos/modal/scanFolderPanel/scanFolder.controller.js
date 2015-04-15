'use strict';

angular.module('videothequeApp')
    .controller('ScanFolderController', function ($scope, VideoService) {

    	//affichage de la création d'une video
        this.showScanModal = function () {      
            
        	VideoService.scanDirs()
				.then(function() {
					this.listFiles = VideoService.listFiles;
	        }.bind(this));
        	
            //on affiche le module de creation
            $('#scanFolderPanel').modal('show');
        };
        
    });
