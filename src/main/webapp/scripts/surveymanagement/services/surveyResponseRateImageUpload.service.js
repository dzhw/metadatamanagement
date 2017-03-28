'use strict';

angular.module('metadatamanagementApp').service(
  'SurveyResponseRateImageUploadService',
  function(Upload, $q, $http) {
    var uploadImage = function(image, surveyId) {
      var deferred = $q.defer();
      Upload.upload({
        url: '/api/surveys/images',
        fields: {
          'surveyId': surveyId,
          'image': image
        },
      }).success(function() {
        deferred.resolve();
      }).error(function(error) {
        deferred.reject(error);
      });
      return deferred.promise;
    };

    var deleteAllImages = function(surveyId) {
      return $http.delete('/api/surveys/' + encodeURIComponent(surveyId) +
      '/images');
    };

    return {
      deleteAllImages: deleteAllImages,
      uploadImage: uploadImage
    };
  });
