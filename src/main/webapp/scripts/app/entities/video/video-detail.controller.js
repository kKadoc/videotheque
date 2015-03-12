'use strict';

angular.module('videothequeApp')
    .controller('VideoDetailController', function ($scope, $stateParams, Video, VideoType, File) {
        $scope.video = {};
        $scope.load = function (id) {
            Video.get({id: id}, function(result) {
              $scope.video = result;
            });
        };
        $scope.load($stateParams.id);
    });
