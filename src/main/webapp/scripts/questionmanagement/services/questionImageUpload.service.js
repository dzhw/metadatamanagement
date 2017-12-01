'use strict';

angular.module('metadatamanagementApp').service('QuestionImageUploadService',
  function(Upload, $q, $http) {
    var uploadImage = function(image, questionImageMetadata) {
      var deferred = $q.defer();
      Upload.upload({
        url: '/api/questions/images',
        fields: {
          'questionImageMetadata': questionImageMetadata,
          'image': image
        },
      }).success(function() {
        deferred.resolve();
      }).error(function(error) {
        deferred.reject(error);
      });
      return deferred.promise;
    };

    var deleteAllImages = function(questionImageMetadata) {
      return $http.delete('/api/questions/' + encodeURIComponent(
        questionImageMetadata.questionId) +
      '/images');
    };

    return {
      deleteAllImages: deleteAllImages,
      uploadImage: uploadImage
    };
  });
