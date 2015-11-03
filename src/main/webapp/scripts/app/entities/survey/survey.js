'use strict';

angular.module('metadatamanagementApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('survey', {
                parent: 'entity',
                url: '/surveys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'metadatamanagementApp.survey.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/survey/surveys.html',
                        controller: 'SurveyController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('survey');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('survey.detail', {
                parent: 'entity',
                url: '/survey/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'metadatamanagementApp.survey.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/survey/survey-detail.html',
                        controller: 'SurveyDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('survey');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Survey', function($stateParams, Survey) {
                        return Survey.get({id : $stateParams.id});
                    }]
                }
            })
            .state('survey.new', {
                parent: 'survey',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/survey/survey-dialog.html',
                        controller: 'SurveyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    title: null,
                                    begin: null,
                                    endDate: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('survey', null, { reload: true });
                    }, function() {
                        $state.go('survey');
                    })
                }]
            })
            .state('survey.edit', {
                parent: 'survey',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/survey/survey-dialog.html',
                        controller: 'SurveyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Survey', function(Survey) {
                                return Survey.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('survey', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
