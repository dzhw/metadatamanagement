'use strict';
/*
  Resource url definition for the relase process.
  @author Daniel Katzberg
*/

angular.module('metadatamanagementApp')
  .factory('DaraReleaseResource', ['$resource',  function($resource) {
    return $resource('/api/data-acquisition-projects/:id/release', {
      id: '@id'
    }, {
      'release': {
        method: 'POST'
      }
    });
  }]);

/**
 * Method to request the endpoint that updates all projects in dara-template.
 * The response is a list (List<String>) of possible error messages, that occurre during update.
 */
angular.module('metadatamanagementApp').factory('DaraReleaseResource', ['$http', 
function($http) {
  return {
    updateDaraProjects: function() {
      return $http.put('/api/data-acquisition-projects/update-dara').then(function(response) {
        return response.data;
      });
    }
  };
}]);
