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
          var metadata = attachment;
          if (attachment.fileName) {
            if (filesMap.dataSets
              .attachments[attachment.fileName]) {
              metadata.fileName = attachment.fileName;
              metadata.title = attachment.title;
              metadata.language = attachment.language;
              metadata.dataSetNumber = attachment.dataSetNumber;
              metadata.dataAcquisitionProjectId = dataAcquisitionProjectId;
              metadata.dataSetId =
                DataSetIdBuilderService.buildDataSetId(dataAcquisitionProjectId,
                  attachment.dataSetNumber);
              metadata.description = {
                'de': 'test de',
                'en': 'test en'
              };
              attachmentUploadObjects.push({
                'metadata': metadata,
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
