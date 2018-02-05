'use strict';

angular.module('metadatamanagementApp').factory('SurveyRepositoryClient',
  function($http) {
    var findByDataAcquisitionProjectId = function(projectId) {
      return $http({
        method: 'GET',
        url: '/api/surveys/search/findByDataAcquisitionProjectId',
        params: {
          dataAcquisitionProjectId: projectId,
          projection: 'id-and-version'
        },
        transformResponse: function(data) {
          var response = angular.fromJson(data);
          if (response._embedded && response._embedded.surveys) {
            return response._embedded.surveys;
          }
          return response;
        }
      });
    };

    return {
      findByDataAcquisitionProjectId: findByDataAcquisitionProjectId
    };
  });
