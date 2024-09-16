/* Author: Daniel Katzberg */
'use strict';

/* Data Package Resource */
angular.module('metadatamanagementApp')
  .factory('DataPackageResource', ['$resource', 'CleanJSObjectService',  function($resource, CleanJSObjectService) {
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
  }]);


  /**
   * Method to request the endpoint that downloads all variables of the data package as a DDI-formatted XML file.
   * We could not put this into the "DataPackageResource"-schema (above), because the URLs would not match.
   */
  angular.module('metadatamanagementApp').factory('ExportDdiVariablesResource', ['$http', 
    function($http) {
    return {
      exportVariablesAsXml: function(dataPackageId) {
            return $http.get('api/data-packages/exportDDI/xml/' + dataPackageId).then(function(response) {
              return response.data;
            });
        }
    }
  }]);

