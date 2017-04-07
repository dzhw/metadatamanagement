/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .service('DataSetAttachmentBuilderService',
  function(JobLoggingService, DataSetIdBuilderService) {

    var buildDataSetAttachment =
      function(attachmentsSheet, dataAcquisitionProjectId, filesMap) {
        var notFoundAttachmentsMap = {};
        var attachmentUploadObjects = [];
        attachmentsSheet.forEach(function(attachment) {
          if (attachment.fileName) {
            if (filesMap.dataSets
              .attachments[attachment.fileName]) {
              attachment.dataAcquisitionProjectId = dataAcquisitionProjectId;
              attachment.dataSetId =
                DataSetIdBuilderService.buildDataSetId(dataAcquisitionProjectId,
                  attachment.dataSetNumber);
              attachment.description = {
                'de': 'test de',
                'en': 'test en'
              };
              attachmentUploadObjects.push({
                'metadata': attachment,
                'file': filesMap.dataSets
                .attachments[attachment.fileName]
              });
            } else {
              if (!notFoundAttachmentsMap[attachment.fileName]) {
                JobLoggingService.error({
                  message: 'data-set-management.log-messages' +
                  '.data-set-attachment.file-not-found',
                  messageParams: {
                    filename: attachment.fileName
                  },
                  objectType: 'attachment'
                });
                notFoundAttachmentsMap[attachment.fileName] = true;
              }
            }
          }
        });

        return attachmentUploadObjects;
      };

    return {
      buildDataSetAttachment: buildDataSetAttachment
    };
  });
