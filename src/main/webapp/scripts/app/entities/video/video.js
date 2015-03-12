'use strict';

angular.module('videothequeApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('video', {
                parent: 'entity',
                url: '/video',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'videothequeApp.video.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/video/videos.html',
                        controller: 'VideoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('video');
                        return $translate.refresh();
                    }]
                }
            })
            .state('videoDetail', {
                parent: 'entity',
                url: '/video/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'videothequeApp.video.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/video/video-detail.html',
                        controller: 'VideoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('video');
                        return $translate.refresh();
                    }]
                }
            });
    });
