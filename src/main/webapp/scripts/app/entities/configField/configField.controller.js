'use strict';

angular.module('videothequeApp')
    .controller('ConfigFieldController', function ($scope, ConfigField) {
        $scope.configFields = [];
        $scope.loadAll = function() {
            ConfigField.query(function(result) {
               $scope.configFields = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            ConfigField.update($scope.configField,
                function () {
                    $scope.loadAll();
                    $('#saveConfigFieldModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            ConfigField.get({id: id}, function(result) {
                $scope.configField = result;
                $('#saveConfigFieldModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            ConfigField.get({id: id}, function(result) {
                $scope.configField = result;
                $('#deleteConfigFieldConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            ConfigField.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteConfigFieldConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.configField = {key: null, content: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
