'use strict';

angular.module('metadatamanagementApp')
  .factory('AnalysisPackageResource', ['$resource', 'CleanJSObjectService', 
    function($resource, CleanJSObjectService) {
      return $resource('/api/analysis-packages/:id', {
        id: '@id'
      }, {
        'get': {
          method: 'GET'
        },
        'save': {
          method: 'PUT',
          transformRequest: function(analysisPackage) {
            var copy = angular.copy(analysisPackage);
            CleanJSObjectService.deleteEmptyStrings(copy);
            CleanJSObjectService.removeEmptyJsonObjects(copy);
            return angular.toJson(copy);
          }
        },
        'delete': {
          method: 'DELETE'
        }
      });
    }]);

