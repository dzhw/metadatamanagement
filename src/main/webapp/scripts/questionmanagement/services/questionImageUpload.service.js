'use strict';

angular.module('metadatamanagementApp').service('QuestionImageUploadService',
  function(Upload, $q) {
    var deferred = $q.defer();
    var uploadImage = function(image, imageId) {
        Upload.upload({
          url: '/api/questions/images',
          fields: {
            'id': imageId,
            'image': image
          },
        }).success(function() {
          deferred.resolve();
        }).error(function(error) {
          deferred.reject(error);
        });
        return deferred.promise;
      };
    return {
      uploadImage: uploadImage
    };
  });
