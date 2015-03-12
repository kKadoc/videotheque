'use strict';

angular.module('videothequeApp')
    .controller('VideoController', function ($scope, Video, VideoType, File, ParseLinks) {
        $scope.videos = [];
        $scope.videotypes = VideoType.query();
        $scope.files = File.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Video.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.videos.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.videos = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
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
