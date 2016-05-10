'use strict';

angular.module('metadatamanagementApp').factory(
    'ElasticSearchAdminService',
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
