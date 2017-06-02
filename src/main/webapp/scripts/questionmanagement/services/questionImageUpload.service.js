'use strict';

angular.module('metadatamanagementApp').service('QuestionImageUploadService',
  function(Upload, $q, $http) {
    var uploadImage = function(image, questionId) {
      var deferred = $q.defer();
      Upload.upload({
        url: '/api/questions/images',
        fields: {
          'questionId': questionId,
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
  });
