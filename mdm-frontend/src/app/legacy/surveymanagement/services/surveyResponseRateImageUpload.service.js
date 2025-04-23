/* global Blob */
'use strict';

angular.module('metadatamanagementApp').service('SurveyResponseRateImageUploadService', [
  'Upload', 
  '$q', 
  '$http', 
  '$rootScope', 
  function(Upload, $q, $http, $rootScope) {
    var buildImageFilename = function(surveyNumber, language) {
      return surveyNumber + '_responserate_' + language;
    };

    var uploadImage = function(image, metadata, surveyNumber, language) {
      var deferred = $q.defer();
      var filename = buildImageFilename(surveyNumber, language);
      metadata.fileName = filename;
      metadata.language = language;
      image = Upload.rename(image, filename);
      Upload.upload({
        url: '/api/surveys/images',
        fields: {
          surveyResponseRateImageMetadata: Upload.jsonBlob(metadata),
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

    var deleteImage = function(surveyId, surveyNumber, language) {
      var filename = buildImageFilename(surveyNumber, language);
      return $http.delete('/api/surveys/' + encodeURIComponent(surveyId) +
      '/images/' + encodeURIComponent(filename));
    };

    var getImage = function(surveyId, surveyNumber, language) {
      var filename = buildImageFilename(surveyNumber, language);
      $rootScope.$broadcast('start-ignoring-404');
      return $http.get('/public/files/surveys/' + encodeURIComponent(surveyId) +
        '/' + encodeURIComponent(filename),
        {responseType: 'arraybuffer'}).then(function(response) {
        $rootScope.$broadcast('stop-ignoring-404');
        return new Blob([response.data],
          {type: response.headers('content-type')});
      }).catch(function(error) {
        $rootScope.$broadcast('stop-ignoring-404');
        return $q.reject(error);
      });
    };

    return {
      deleteAllImages: deleteAllImages,
      uploadImage: uploadImage,
      deleteImage: deleteImage,
      getImage: getImage,
      buildImageFilename: buildImageFilename
    };
  }]);

