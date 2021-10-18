'use strict';

angular.module('metadatamanagementApp')
  .factory('AnalysisPackageResource',
    function($resource, CleanJSObjectService) {
      return $resource('/api/analysis-packages/:id', {
        id: '@id'
      }, {
        'get': {
          method: 'GET'
        },
        'save': {
          method: 'PUT',
          transformRequest: function(dataPackage) {
            var copy = angular.copy(dataPackage);
            CleanJSObjectService.deleteEmptyStrings(copy);
            CleanJSObjectService.removeEmptyJsonObjects(copy);
            return angular.toJson(copy);
          }
        },
        'delete': {
          method: 'DELETE'
        }
      });
    });
