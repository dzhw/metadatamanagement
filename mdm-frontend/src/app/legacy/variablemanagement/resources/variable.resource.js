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
 * Method to request the endpoint that downloads all variables as a PID-formatted json file.
 * The response is a list (List<String>) of variable metadata.
 * We could not put this into the "VariableResource"-schema (above), because the URLs would not match.
 */
  angular.module('metadatamanagementApp').factory('ExportAllVariablesResource', ['$http', 
    function($http) {
    return {
        exportAll: function() {
            return $http.post('/api/variables/exportAll').then(function(response) {
              return response.data;
              var blob = new Blob([ response.data ], { type : 'application/json' });
              return  blob; //{ blob: response.data, filename: "filename.json" };
            });
        }
    }
  }]);

