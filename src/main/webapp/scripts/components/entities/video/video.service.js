'use strict';

angular.module('videothequeApp')
    .factory('Video', function ($resource) {
        return $resource('api/videos/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.addDate = new Date(data.addDate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
