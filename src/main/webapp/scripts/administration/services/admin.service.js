'use strict';

angular.module('metadatamanagementApp').factory(
    'AdminService',
    function($http) {
      return {
        recreateAllElasticsearchIndices: function() {
          return $http.post('/api/search/recreate')
          .then(function(response) {
            return response.data;
          });
        }
      };
    }
  );
