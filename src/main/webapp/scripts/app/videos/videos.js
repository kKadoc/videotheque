'use strict';

angular.module('videothequeApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('videos', {
                parent: 'site',
                url: '/videos',
                data: {
                    roles: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/videos/videos.html',
                        controller: 'VideosController'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('main');
                        return $translate.refresh();
                    }]
                }
            });
    });
