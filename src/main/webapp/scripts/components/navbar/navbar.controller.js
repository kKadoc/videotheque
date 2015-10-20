'use strict';

angular.module('videothequeApp')
    .controller('NavbarController', function ($scope, $location, $rootScope, $state, Auth, Principal, VideoService) {
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.isInRole = Principal.isInRole;
        $scope.$state = $state;

        $scope.themes = ["standard", "dark"];
                
        
        $scope.changeTheme = function(theme) {
        	$rootScope.theme = theme;
        },
        
        $scope.logout = function () {
            Auth.logout();
            $state.go('videos');
        };
        
        $scope.clearAppDir = function() {
        	VideoService.clearAppDir();
        }
    });
