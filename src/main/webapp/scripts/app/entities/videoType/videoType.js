'use strict';

angular.module('videothequeApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('videoType', {
                parent: 'entity',
                url: '/videoType',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'videothequeApp.videoType.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/videoType/videoTypes.html',
                        controller: 'VideoTypeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('videoType');
                        return $translate.refresh();
                    }]
                }
            })
            .state('videoTypeDetail', {
                parent: 'entity',
                url: '/videoType/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'videothequeApp.videoType.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/videoType/videoType-detail.html',
                        controller: 'VideoTypeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('videoType');
                        return $translate.refresh();
                    }]
                }
            });
    });
