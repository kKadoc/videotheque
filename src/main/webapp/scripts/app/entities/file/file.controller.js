'use strict';

angular.module('videothequeApp')
    .controller('FileController', function ($scope, File) {
        $scope.files = [];
        $scope.loadAll = function() {
            File.query(function(result) {
               $scope.files = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            File.update($scope.file,
                function () {
                    $scope.loadAll();
                    $('#saveFileModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            File.get({id: id}, function(result) {
                $scope.file = result;
                $('#saveFileModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            File.get({id: id}, function(result) {
                $scope.file = result;
                $('#deleteFileConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            File.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteFileConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.file = {path: null, type: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
