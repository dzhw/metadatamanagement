/* global document*/

'use strict';
angular.module('metadatamanagementApp')
  .service('RelatedPublicationSearchDialogService',
    function($mdDialog) {
      var findRelatedPublications = function(paramObject) {
        var dialogParent = angular.element(document.body);
        $mdDialog.show({
          controller: 'RelatedPublicationSearchDialogController',
          controllerAs: 'ctrl',
          parent: dialogParent,
          clickOutsideToClose: true,
          locals: {
            paramObject: paramObject
          },
          templateUrl: 'scripts/relatedpublicationmanagement/views/' +
            'relatedPublicationSearchDialog.html.tmpl',
        });
      };
      return {
        findRelatedPublications: findRelatedPublications
      };
    });
