/* global document*/

'use strict';
angular.module('metadatamanagementApp')
  .service('RelatedPublicationSearchDialogService',
    function($mdDialog) {
      var findRelatedPublications = function(methodName, methodParams, count) {
        var dialogParent = angular.element(document.body);
        $mdDialog.show({
          controller: 'RelatedPublicationSearchDialogController',
          controllerAs: 'RelatedPublicationSearchDialogController',
          parent: dialogParent,
          clickOutsideToClose: true,
          locals: {
            methodName: methodName,
            methodParams: methodParams,
            count: count
          },
          templateUrl: 'scripts/relatedpublicationmanagement/views/' +
            'relatedPublicationSearchDialog.html.tmpl',
        });
      };
      return {
        findRelatedPublications: findRelatedPublications
      };
    });
