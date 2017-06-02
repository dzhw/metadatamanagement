/*jshint loopfunc: true */
/* global _*/
/* @author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .service('RelatedPublicationUploadService',
    function(ExcelReaderService, RelatedPublicationBuilderService,
      RelatedPublicationRepositoryClient, JobLoggingService, $q,
      ErrorMessageResolverService, ElasticSearchAdminService, $rootScope,
      $translate, $mdDialog, RelatedPublicationResource) {
      var objects;
      var uploadCount;
      // a map publication.id -> true
      var previouslyUploadedPublicationIds;
      // map relatedPublicationId -> presentInExcel true/false
      var existingPublications = {};

      var deleteAllPublicationsNotPresentInExcel = function() {
        var promiseChain = $q.when();
        _.each(existingPublications, function(existingPublication) {
          if (!existingPublication.presentInExcel) {
            promiseChain = promiseChain.then(function() {
              return RelatedPublicationResource.delete(
                {id: existingPublication.id})
                .$promise.catch(
                function(error) {
                  console.log('Error when deleting publication:', error);
                });
            });
          }
        });
        return promiseChain;
      };

      var upload = function() {
        if (uploadCount === objects.length) {
          deleteAllPublicationsNotPresentInExcel().finally(function() {
            ElasticSearchAdminService.processUpdateQueue('related_publications')
            .finally(function() {
                JobLoggingService.finish(
                  'related-publication-management.log-messages.' +
                  'related-publication.upload-terminated', {
                    total: JobLoggingService.getCurrentJob().total,
                    warnings: JobLoggingService.getCurrentJob().warnings,
                    errors: JobLoggingService.getCurrentJob().errors
                  });
                $rootScope.$broadcast('upload-completed');
              });
          });
        } else {
          if (!objects[uploadCount].id || objects[uploadCount].id === '') {
            var index = uploadCount;
            JobLoggingService.error({
              message: 'related-publication-management.log-messages.' +
                'related-publication.missing-id',
              messageParams: {
                index: index + 1
              }
            });
            uploadCount++;
            return upload();
          } else if (previouslyUploadedPublicationIds[
              objects[uploadCount].id]) {
            JobLoggingService.error({
              message: 'related-publication-management.log-messages.' +
                'related-publication.duplicate-id',
              messageParams: {
                index: uploadCount + 1,
                id: objects[uploadCount].id
              }
            });
            uploadCount++;
            return upload();
          } else {
            if (existingPublications[objects[uploadCount].id]) {
              existingPublications[objects[uploadCount].id].presentInExcel =
                true;
            }
            objects[uploadCount].$save().then(function() {
              JobLoggingService.success();
              previouslyUploadedPublicationIds[objects[uploadCount].id] =
                true;
              uploadCount++;
              return upload();
            }).catch(function(error) {
              var errorMessages = ErrorMessageResolverService
                .getErrorMessage(error, 'related-publication');
              JobLoggingService.error({
                message: errorMessages.message,
                messageParams: errorMessages.translationParams,
                subMessages: errorMessages.subMessages
              });
              uploadCount++;
              return upload();
            });
          }
        }
      };

      var startJob = function(file) {
        uploadCount = 0;
        objects = [];
        previouslyUploadedPublicationIds = {};
        JobLoggingService.start('related-publication');
        ExcelReaderService.readFileAsync(file)
          .then(function(relatedPublications) {
            objects = RelatedPublicationBuilderService
              .getRelatedPublications(relatedPublications);
            upload();
          }, function() {
            JobLoggingService
              .cancel('global.log-messages.unable-to-read-file', {
                file: 'relatedPublications.xls'
              });
          });
      };

      var uploadRelatedPublications = function(file) {
        if (!file || !file.name.endsWith('.xls')) {
          return;
        }
        existingPublications = {};
        RelatedPublicationRepositoryClient.findAll().then(function(result) {
            result.data.forEach(function(publication) {
              existingPublications[publication.id] = publication;
            });
            if (result.data.length > 0) {
              var confirm = $mdDialog.confirm()
                .title($translate.instant(
                  'search-management.delete-messages.' +
                  'delete-related-publications-title'))
                .textContent($translate.instant(
                  'search-management.delete-messages.' +
                  'delete-related-publications'
                ))
                .ariaLabel($translate.instant(
                  'search-management.delete-messages.' +
                  'delete-related-publications'
                ))
                .ok($translate.instant('global.buttons.ok'))
                .cancel($translate.instant('global.buttons.cancel'));
              $mdDialog.show(confirm).then(function() {
                  startJob(file);
                });
            } else {
              startJob(file);
            }
          });
      };
      return {
        uploadRelatedPublications: uploadRelatedPublications
      };
    });
