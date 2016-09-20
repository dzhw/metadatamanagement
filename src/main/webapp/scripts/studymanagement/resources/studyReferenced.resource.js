'use strict';

angular.module('metadatamanagementApp').factory('StudyReferencedResource',
function($resource) {
  return $resource('', {},
  {
    'getReferencedStudy':
    {
      method: 'GET',
      url: '/api/studies/:id',
      params: {
        projection: 'referenced',
        ids: '@ids'
      }
    }
  });
});
