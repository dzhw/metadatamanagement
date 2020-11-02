'use strict';

angular.module('metadatamanagementApp').factory('PinnedDataPackagesService',
  function($http) {
    var getPinnedDataPackage = function() {
      return $http({
        method: 'GET',
        url: '/api/data-packages',
        params: {
          pinned: true
        },
        transformResponse: function(data) {
          var response = angular.fromJson(data);
          if (response.content && response.content.length > 0) {
            return response.content[0];
          }
          return null;
        }
      });
    };

    return {
      getPinnedDataPackage: getPinnedDataPackage
    };
  });
