/*jshint loopfunc: true */
/* @author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .service('RelatedPublicationUploadService',
  function(ExcelReaderService, RelatedPublicationBuilderService,
    RelatedPublicationDeleteResource, JobLoggingService,
    ErrorMessageResolverService, ElasticSearchAdminService, $rootScope,
    $translate, $mdDialog) {
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
          JobLoggingService.error({
            message: 'related-publication-management.log-messages.' +
            'related-publication.missing-id',
            messageParams: {index: index + 1}
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
              .getErrorMessage(error, 'related-publication');
            JobLoggingService.error({message: errorMessages.message,
              messageParams: errorMessages.translationParams,
              subMessages: errorMessages.subMessages
            });
            uploadCount++;
            return upload();
          });
        }
      }
    };
    var uploadRelatedPublications = function(file) {
      if (!file || !file.name.endsWith('.xls')) {
        return;
      }
      var confirm = $mdDialog.confirm()
        .title($translate.instant(
          'search-management.delete-messages.' +
          'delete-related-publications-title'))
        .textContent($translate.instant(
          'search-management.delete-messages.delete-related-publications'
        ))
        .ariaLabel($translate.instant(
          'search-management.delete-messages.delete-related-publications'
        ))
        .ok($translate.instant('global.buttons.ok'))
        .cancel($translate.instant('global.buttons.cancel'));
      $mdDialog.show(confirm).then(function() {
        uploadCount = 0;
        objects = [];
        JobLoggingService.start('related-publication');
        RelatedPublicationDeleteResource.deleteAll().$promise.then(
          function() {
            ExcelReaderService.readFileAsync(file)
            .then(function(relatedPublications) {
                objects = RelatedPublicationBuilderService
                .getRelatedPublications(relatedPublications);
                upload();
              }, function() {
              JobLoggingService
              .cancel('global.log-messages.unable-to-read-file',
                {file: 'relatedPublications.xls'});
            });
          }, function() {
            JobLoggingService.cancel(
              'related-publication.log-messages.' +
              'related-publication.unable-to-delete');
          }
        );
      }, function() {});
    };
    return {
      uploadRelatedPublications: uploadRelatedPublications
    };
  });
