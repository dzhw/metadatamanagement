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
        method: 'POST',
      },
      'preRelease': {
        method: 'POST',
        url: '/api/data-acquisition-projects/:id/pre-release'
      },
      variablesCheck: {
        method: 'GET',
        url: '/api/data-acquisition-projects/:id/variables-check'
      },
      variablesRegister: {
        method: 'POST',
        url: '/api/data-acquisition-projects/:id/variables-register'
      }
    });
  }]);

/**
 * Method to request the endpoint that updates all projects in dara-template.
 * The response is a list (List<String>) of possible error messages, that occurre during update.
 * We could not put this into the "DaraReleaseResource"-schema (above), because the URLs would not match.
 */
angular.module('metadatamanagementApp').factory('DaraReleaseCustomResource', ['$http', 
function($http) {
  return {
    updateDaraProjects: function() {
      return $http.put('/api/data-acquisition-projects/update-dara').then(function(response) {
        return response.data;
      });
    }
  };
}]);
