'use strict';

angular.module('metadatamanagementApp')
  .factory('VariableResource', ['$resource',  function($resource) {
    return $resource('api/variables/:id', {
      id: '@id'
    }, {
      'get': {
        method: 'GET'
      },
      'save': {
        method: 'PUT'
      },
      'delete': {
        method: 'DELETE',
      }
    });
  }]);

   /**
   * Method to request the endpoint that downloads all variables of the study as a DDI-formatted XML file.
   * We could not put this into the "VariableResource"-schema (above), because the URLs would not match.
   */
   angular.module('metadatamanagementApp').factory('ExportDdiVariablesResource', ['$http', 
    function($http) {
    return {
      exportVariablesAsXml: function() {
            return $http.get('api/variables/exportDDI/xml/:studyId').then(function(response) {
              return response.data;
            });
        }
    }
  }]);

