'use strict';

angular.module('videothequeApp')
    .controller('CreateVideoController', function (VideoService, $scope) {

        //affichage de la création d'une video
        this.showCreateModal = function () {
        	
        	this.videoFile = null;
            this.subFile = null;
            this.keyword = null;
            this.listGuess = null;
            this.customKeyword = false;
            
            
            //on affiche le module de creation
            $('#createVideoPanel').modal('show');
        };
        
        this.showCreateModalWithFile = function (file) {
        	
        	this.showCreateModal();
        	this.videoFile = file;
        	this.updateKeyword();
        	this.guess();
        };
        
        //mise à jour du mot clé de recherche imdb
        this.updateKeyword = function() {
        	console.log(this);
        	this.keyword = this.videoFile;
        	//on vire le dossier
        	if (this.videoFile != null && this.videoFile != "" && this.videoFile.indexOf("/") != -1) {
        		this.keyword = this.videoFile.substr(this.videoFile.lastIndexOf("/")+1) ;
        	}
        	if (this.videoFile != null && this.videoFile != "" && this.videoFile.indexOf("\\") != -1) {
        		this.keyword = this.videoFile.substr(this.videoFile.lastIndexOf("\\")+1) ;
        	}
        	//on vire l'extension
        	if (this.keyword != null && this.keyword != "" && this.keyword.indexOf(".") != -1) {
        		this.keyword = this.keyword.substr(0, this.keyword.lastIndexOf(".")) ;
        	}
        	
        	this.customKeyword = false;	
        };
        
        //tente de deviner la vidéo d'apres le mot clé
        this.guess = function() {
        	if (this.keyword != null && this.keyword != "") {
	        	VideoService.guess(this.keyword, this.customKeyword)
    				.then(function() {
    					this.listGuess = VideoService.listGuess;
	            }.bind(this));
	        }
        };
          
        //upload des fichiers et création de la vidéo
        this.createVideo = function(imdbId) {    	
        	VideoService.createVideo(this.videoFile, this.subFile, imdbId, function (data) {
        		if (data != null) {
        			alert(data);
        		}
        		this.refreshVideosList();
        		
        		$('#createVideoPanel').modal('hide');
            });
        };
         
        this.useImdbId = function() {
        	this.createVideo(this.imdbId);
        }
    });
