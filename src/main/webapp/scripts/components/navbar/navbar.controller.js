'use strict';

angular.module('videothequeApp')
    .controller('NavbarController', function ($scope, $location, $state, Auth, Principal, VideoService) {
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.isInRole = Principal.isInRole;
        $scope.$state = $state;

        $scope.logout = function () {
            Auth.logout();
            $state.go('videos');
        };
        
        $scope.clearAppDir = function() {
        	VideoService.clearAppDir();
        }
    });
