'use strict';

angular.module('videothequeApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
