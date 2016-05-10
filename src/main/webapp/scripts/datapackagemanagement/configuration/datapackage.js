'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('datapackage-detail', {
        parent: 'site',
        url: '/datapackages/{id}',
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'metadatamanagementApp.datapackage.home.title'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/datapackagemanagement/' +
              'views/datapackage-detail.html.tmpl',
            controller: 'DatapackageDetailController'
          }
        },
        resolve: {
          translatePartialLoader: ['$translate', '$translatePartialLoader',
            function($translate, $translatePartialLoader) {
              $translatePartialLoader.addPart('datapackage');
              return $translate.refresh();
            }
          ],
          dataAcquisitionProject: ['$stateParams',
          'DataAcquisitionProjectResource',
          function($stateParams, DataAcquisitionProjectResource) {
            return DataAcquisitionProjectResource.get({
              id: $stateParams.id
            });
          }],
          surveyCollection: ['$stateParams',
          'SurveyCollectionResource',
          function($stateParams, SurveyCollectionResource) {
            return SurveyCollectionResource.get({
              id: $stateParams.id
            });
          }],
          dataSetCollection: ['$stateParams',
          'DataSetCollectionResource',
          function($stateParams, DataSetCollectionResource) {
            return DataSetCollectionResource.get({
              id: $stateParams.id
            });
          }]
        }
      });
  });
