/* Author: Daniel Katzberg */
'use strict';

/* Data Package Resource */
angular.module('metadatamanagementApp')
  .factory('DataPackageResource', function($resource, CleanJSObjectService) {
    return $resource('/api/data-packages/:id', {
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
