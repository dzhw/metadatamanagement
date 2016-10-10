/*jshint loopfunc: true */
/* @author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .service('RelatedPublicationUploadService',
  function(ExcelReaderService, RelatedPublicationBuilderService,
    RelatedPublicationDeleteResource, JobLoggingService,
    ErrorMessageResolverService, ElasticSearchAdminService, $rootScope) {
    var objects;
    var uploadCount;
    var upload = function() {
      if (uploadCount === objects.length) {
        ElasticSearchAdminService.processUpdateQueue().finally(function() {
          JobLoggingService.finish(
            'related-publication-management.log-messages.' +
            'related-publication.upload-terminated', {
              total: JobLoggingService.getCurrentJob().total,
              errors: JobLoggingService.getCurrentJob().errors
            });
          $rootScope.$broadcast('upload-completed');
        });
      } else {
        if (!objects[uploadCount].id || objects[uploadCount].id === '') {
          var index = uploadCount;
          JobLoggingService.error(
            'related-publication-management.log-messages.' +
            'related-publication.missing-id', {
              index: index + 1
            });
          uploadCount++;
          return upload();
        } else {
          objects[uploadCount].$save().then(function() {
            JobLoggingService.success();
            uploadCount++;
            return upload();
          }).catch(function(error) {
            var errorMessages = ErrorMessageResolverService
              .getErrorMessages(error, 'related-publication');
            JobLoggingService.error(errorMessages.message,
              errorMessages.translationParams, errorMessages.subMessages
            );
            uploadCount++;
            return upload();
          });
        }
      }
    };
    var uploadRelatedPublications = function(file, dataAcquisitionProjectId) {
      uploadCount = 0;
      JobLoggingService.start('related-publication');
      ExcelReaderService.readFileAsync(file).then(function(data) {
        objects = RelatedPublicationBuilderService.getRelatedPublications(data,
          dataAcquisitionProjectId);
        RelatedPublicationDeleteResource.deleteAll({},
          upload,
          function(error) {
            var errorMessages = ErrorMessageResolverService
              .getErrorMessages(error, 'related-publication');
            errorMessages.forEach(function(errorMessage) {
              JobLoggingService.error(errorMessage.message,
                errorMessage.translationParams);
            });
          });
      }, function(error) {
        console.log(error);
        JobLoggingService.cancel(
          'global.log-messages.unsupported-excel-file', {});
      });
    };
    return {
      uploadRelatedPublications: uploadRelatedPublications
    };
  });
