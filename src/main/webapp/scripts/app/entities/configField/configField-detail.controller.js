'use strict';

angular.module('videothequeApp')
    .controller('ConfigFieldDetailController', function ($scope, $stateParams, ConfigField) {
        $scope.configField = {};
        $scope.load = function (id) {
            ConfigField.get({id: id}, function(result) {
              $scope.configField = result;
            });
        };
        $scope.load($stateParams.id);
    });
