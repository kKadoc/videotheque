'use strict';

angular.module('videothequeApp')
    .controller('VideosController', function ($scope, $cookies, $browser, localStorageService, Video, VideoService) {
    	
    	var videoExt = ["mp4", "divx", "avi", "mov", "mpg"];
    	var subExt = ["srt"];
    	
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
        
        $scope.videoFile;
        $scope.subFile;
        //affichage de la création d'une video
        $scope.dropFiles = function (fileList) {
        	
        	//on identifie les fichiers à importer
        	$scope.videoFile = null;
            $scope.subFile = null;
            
            var i = 0;
            for (i = 0; i < fileList.length; i++) {
            	var file = fileList[i];
            	
            	var ext = file.name.substr(file.name.lastIndexOf('.') + 1);
            	if ($scope.videoFile == null && videoExt.indexOf(ext) != -1) {
            		$scope.videoFile = file;
            	}
            	if ($scope.subFile == null && subExt.indexOf(ext) != -1) {
            		$scope.subFile = file;
            	}
            }
            
            if ($scope.videoFile == null) {
            	alert("Aucun fichier vidéo exploitable n'a été trouvé");
            	return;
            }
            
            //on tente de deviner le film
            $scope.listGuess = null;
            VideoService.guess(file.name, function (data) {
                $scope.listGuess = data;
            });
            
            //on affiche le module de creation
            $('#createVideoPanel').modal('show');
        };
           
        $scope.createVideo = function(imdbId) {
        	var csrfValue = $browser.cookies()["CSRF-TOKEN"];
        	
            var fd = new FormData();
            fd.append("videoFile", $scope.videoFile);
            if ($scope.subFile != null) {
            	fd.append("subFile", $scope.subFile);
            }
            fd.append("_csrf", csrfValue);
            fd.append("imdbId", imdbId);

            var xhr = new XMLHttpRequest();
            
            xhr.upload.addEventListener("progress", uploadProgress, false);
            xhr.addEventListener("load", uploadComplete, false);
            xhr.addEventListener("error", uploadFailed, false);
            xhr.addEventListener("abort", uploadCanceled, false);
            xhr.open("POST", "/api/uploadVideo");
            $scope.progressVisible = true;
            xhr.send(fd);
            
            return false;
        };

        function uploadProgress(evt) {
            $scope.$apply(function(){
                if (evt.lengthComputable) {
                    $scope.progress = Math.round(evt.loaded * 100 / evt.total);
                } else {
                    $scope.progress = 'unable to compute';
                }
            })
        };

        function uploadComplete(evt) {
            /* This event is raised when the server send back a response */
            alert(evt.target.responseText);
        };

        function uploadFailed(evt) {
            alert("There was an error attempting to upload the file.");
        };

        function uploadCanceled(evt) {
            $scope.$apply(function(){
                $scope.progressVisible = false;
            })
            alert("The upload has been canceled by the user or the browser dropped the connection.");
        };
    });
