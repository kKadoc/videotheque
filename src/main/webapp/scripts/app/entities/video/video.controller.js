'use strict';

angular.module('videothequeApp')
    .controller('VideoController', function ($scope, Video, VideoService, VideoType, File, ParseLinks) {
        $scope.videos = [];
        $scope.videotypes = VideoType.query();
        $scope.files = File.query();

        $scope.loadAll = function() {
            Video.query(function(result) {
            	$scope.videos = result;
            });
        };
        $scope.reset = function() {
            $scope.videos = [];
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            Video.update($scope.video,
                function () {
                    $scope.reset();
                    $('#saveVideoModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Video.get({id: id}, function(result) {
                $scope.video = result;
                $('#saveVideoModal').modal('show');
            });
        };
        
        $scope.refreshFromImdb = function (id) {
        	VideoService.refreshFromImdb(id)
        	.then(function() {
                $scope.reset();
            });
        };

        $scope.delete = function (id) {
            Video.get({id: id}, function(result) {
                $scope.video = result;
                $('#deleteVideoConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Video.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteVideoConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.video = {imdbId: null, title: null, year: null, rate: null, duration: null, favoris: null, addDate: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
