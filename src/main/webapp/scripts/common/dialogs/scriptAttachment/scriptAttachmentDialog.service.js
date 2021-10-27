/* globals _ */
'use strict';
angular.module('metadatamanagementApp')
  .service('ScriptAttachmentDialogService', function($mdDialog) {

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
        controller: 'ScriptAttachmentDialogController',
        controllerAs: 'ctrl',
        templateUrl: 'scripts/common/dialogs/scriptAttachment/' +
          'scriptAttachment-edit-or-create.html.tmpl',
        clickOutsideToClose: false,
        fullscreen: true,
        locals: {
          dialogConfig: {
            scriptAttachmentMetadata: dialogConfig.scriptAttachmentMetadata,
            uploadCallback: dialogConfig.uploadCallback,
            labels: labels,
            exclude: dialogConfig.exclude ? dialogConfig.exclude : [],
            analysisPackageTitle: dialogConfig.analysisPackageTitle
          }
        },
        targetEvent: event
      });
    };

    return {
      showDialog: showDialog
    };
  });
