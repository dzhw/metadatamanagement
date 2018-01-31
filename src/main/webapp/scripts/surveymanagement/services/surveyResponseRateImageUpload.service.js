'use strict';

angular.module('metadatamanagementApp').service(
  'SurveyResponseRateImageUploadService',
  function(Upload, $q, $http, $rootScope) {
    var buildImageFilename = function(surveyNumber, language) {
      return surveyNumber + '_responserate_' + language + '.svg';
    };

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

    var deleteImage = function(surveyId, surveyNumber, language) {
      var filename = buildImageFilename(surveyNumber, language);
      return $http.delete('/api/surveys/' + encodeURIComponent(surveyId) +
      '/images/' + encodeURIComponent(filename));
    };

    var getImage = function(surveyId, surveyNumber, language) {
      var filename = buildImageFilename(surveyNumber, language);
      $rootScope.$broadcast('start-ignoring-404');
      return $http.get('/public/files/surveys/' + encodeURIComponent(surveyId) +
      '/' + encodeURIComponent(filename)).then(function(response) {
        $rootScope.$broadcast('stop-ignoring-404');
        return response.data;
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
  });
