/* globals _ */
'use strict';
angular.module('metadatamanagementApp').service('AttachmentDialogService',
  function($mdDialog) {

    var showDialog = function(dialogConfig, event) {

      var defaultLabels = {
        createTitle: {
          key: 'attachment.create-title'
        },
        editTitle: {
          key: 'attachment.edit-title'
        },
        hints: {
          file: {
            key: 'attachment.hint.filename'
          }
        }
      };

      var labels = _.extend({}, defaultLabels, dialogConfig.labels);

      return $mdDialog.show({
        controller: 'AttachmentDialogController',
        controllerAs: 'ctrl',
        templateUrl: 'scripts/common/dialogs/attachment/' +
          'attachment-edit-or-create.html.tmpl',
        clickOutsideToClose: false,
        fullscreen: true,
        locals: {
          dialogConfig: {
            attachmentMetadata: dialogConfig.attachmentMetadata,
            attachmentTypes: dialogConfig.attachmentTypes,
            uploadCallback: dialogConfig.uploadCallback,
            attachmentDomainIdAttribute: dialogConfig
                .attachmentDomainIdAttribute,
            getAttachmentVersionsCallback: dialogConfig
                .getAttachmentVersionsCallback,
            createAttachmentResource: dialogConfig.createAttachmentResource,
            labels: labels,
            exclude: dialogConfig.exclude ? dialogConfig.exclude : [],
            dataPackageTitle: dialogConfig.dataPackageTitle
          }
        },
        targetEvent: event
      });
    };

    return {
      showDialog: showDialog
    };
  });
