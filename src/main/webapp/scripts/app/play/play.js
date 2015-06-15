'use strict';

angular.module('videothequeApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('play', {
                parent: 'site',
                url: '/play',
                data: {
                    roles: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/play/play.html',
                        controller: 'PlayController'
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
