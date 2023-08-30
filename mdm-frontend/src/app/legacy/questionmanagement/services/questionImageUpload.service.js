'use strict';

angular.module('metadatamanagementApp').service('QuestionImageUploadService', ['Upload', '$q', '$http', 
  function(Upload, $q, $http) {
    var uploadImage = function(image, questionImageMetadata) {
      var deferred = $q.defer();
      Upload.upload({
        url: '/api/questions/images',
        fields: {
          'questionImageMetadata': Upload.jsonBlob(questionImageMetadata),
          'image': image
        },
      }).success(function() {
        deferred.resolve();
      }).error(function(error) {
        deferred.reject(error);
      });
      return deferred.promise;
    };

    var deleteAllImages = function(questionId) {
      return $http.delete('/api/questions/' + encodeURIComponent(questionId) +
      '/images');
    };

    return {
      deleteAllImages: deleteAllImages,
      uploadImage: uploadImage
    };
  }]);

