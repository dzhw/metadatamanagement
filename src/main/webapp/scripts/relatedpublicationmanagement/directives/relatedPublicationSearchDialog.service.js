/* global document*/

'use strict';
angular.module('metadatamanagementApp')
  .service('RelatedPublicationSearchDialogService',
    function($mdDialog, blockUI, RelatedPublicationSearchResource,
      CleanJSObjectService) {
      var relatedPublications = [];
      var showDialog = function() {
        var dialogParent = angular.element(document.body);
        $mdDialog.show({
          controller: 'RelatedPublicationSearchDialogController',
          controllerAs: 'ctrl',
          parent: dialogParent,
          clickOutsideToClose: true,
          locals: {
            relatedPublications: relatedPublications
          },
          templateUrl: 'scripts/relatedpublicationmanagement/directives/' +
            'relatedPublicationSearchDialog.html.tmpl',
        });
      };
      var findBySurveyId = function(surveyId) {
        blockUI.start();
        RelatedPublicationSearchResource.findBySurveyId(surveyId)
          .then(function(items) {
            if (!CleanJSObjectService.isNullOrEmpty(items)) {
              relatedPublications = items.hits.hits;
              blockUI.stop();
              showDialog();
            } else {
              blockUI.stop();
            }
          });
      };
      return {
        findBySurveyId: findBySurveyId
      };
    });
