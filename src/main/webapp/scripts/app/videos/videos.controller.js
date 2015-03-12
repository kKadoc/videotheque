'use strict';

angular.module('videothequeApp')
    .controller('VideosController', function ($scope, Video) {
    	$scope.videos = [];

    	$scope.loadAll = function() {
    		Video.query(function(result) {
               $scope.videos = result;
            });
        };
        $scope.loadAll();
    });
