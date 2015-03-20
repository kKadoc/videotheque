'use strict';

angular.module('videothequeApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('configField', {
                parent: 'entity',
                url: '/configField',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'videothequeApp.configField.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/configField/configFields.html',
                        controller: 'ConfigFieldController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('configField');
                        return $translate.refresh();
                    }]
                }
            })
            .state('configFieldDetail', {
                parent: 'entity',
                url: '/configField/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'videothequeApp.configField.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/configField/configField-detail.html',
                        controller: 'ConfigFieldDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('configField');
                        return $translate.refresh();
                    }]
                }
            });
    });
