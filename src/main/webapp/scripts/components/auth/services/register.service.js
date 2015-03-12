'use strict';

angular.module('videothequeApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


