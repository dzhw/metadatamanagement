'use strict';

angular.module('metadatamanagementApp').directive('conceptSearchResult',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'concept-search-result.html.tmpl',
      scope: {
        searchResult: '=',
        currentLanguage: '=',
        bowser: '=',
        searchResultIndex: '='
      },
      controller: function($scope, CommonDialogsService, ConceptResource,
        ElasticSearchAdminService, $rootScope, SimpleMessageToastService,
        $q) {

        var handleError = function(data) {
          if (data.status === 400) {
            data.data.errors.forEach(function(error) {
              if (error.message
                  .match(/^concept-management\.error\.concept\.in-use\..*/)) {
                SimpleMessageToastService.openSimpleMessageToast(error.message,
                  {ids: error.invalidValue.join(', ')});
              } else {
                SimpleMessageToastService.openSimpleMessageToast(error.message);
              }
            });
          } else {
            SimpleMessageToastService
              .openSimpleMessageToast('global.error.server-error',
                {status: data.status});
          }
        };

        $scope.deleteConcept = function(conceptId) {
          CommonDialogsService.showConfirmDeletionDialog({
            type: 'concept',
            id: conceptId
          }).then(function() {
            return ConceptResource.delete({id: conceptId}).$promise;
          }).then(function() {
            return $q.all([
              ElasticSearchAdminService.
                processUpdateQueue('concepts')
            ]).promise;
          }).then(function() {
            $rootScope.$broadcast('deletion-completed');
            SimpleMessageToastService.openSimpleMessageToast(
              'concept-management.edit.concept-deleted-toast',
              {id: conceptId});
          }, handleError);
        };
      }
    };
  });
