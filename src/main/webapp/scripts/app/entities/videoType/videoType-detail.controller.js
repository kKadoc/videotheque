'use strict';

angular.module('videothequeApp')
    .controller('VideoTypeDetailController', function ($scope, $stateParams, VideoType) {
        $scope.videoType = {};
        $scope.load = function (id) {
            VideoType.get({id: id}, function(result) {
              $scope.videoType = result;
            });
        };
        $scope.load($stateParams.id);
    });
