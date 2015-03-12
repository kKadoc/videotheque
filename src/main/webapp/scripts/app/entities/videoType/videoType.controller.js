'use strict';

angular.module('videothequeApp')
    .controller('VideoTypeController', function ($scope, VideoType) {
        $scope.videoTypes = [];
        $scope.loadAll = function() {
            VideoType.query(function(result) {
               $scope.videoTypes = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            VideoType.update($scope.videoType,
                function () {
                    $scope.loadAll();
                    $('#saveVideoTypeModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            VideoType.get({id: id}, function(result) {
                $scope.videoType = result;
                $('#saveVideoTypeModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            VideoType.get({id: id}, function(result) {
                $scope.videoType = result;
                $('#deleteVideoTypeConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            VideoType.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteVideoTypeConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.videoType = {name: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
