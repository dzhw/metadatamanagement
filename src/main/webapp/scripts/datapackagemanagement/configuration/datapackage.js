'use strict';

angular.module('metadatamanagementApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('datapackage-detail', {
        parent: 'site',
        reloadOnSearch: false,
        url: '/datapackages/{id}?tab',
        data: {
          authorities: [],
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
            }
          ]
        }
      });
  });
