/* globals _, bowser */
'use strict';

angular.module('metadatamanagementApp')
  .controller('ScriptAttachmentDialogController',
    function(dialogConfig, CommonDialogsService,
             SimpleMessageToastService, $scope, $mdDialog) {
      $scope.bowser = bowser;
      var ctrl = this;
      var uploadFile = dialogConfig.uploadCallback;

      ctrl.isCreateMode = true;
      ctrl.scriptAttachmentForm = $scope.scriptAttachmentForm;
      ctrl.labels = dialogConfig.labels;
      ctrl.titleParams = _.get(ctrl, 'labels.createTitle.params');

      if (dialogConfig.scriptAttachmentMetadata) {
        ctrl.scriptAttachmentMetadata = angular.copy(
          dialogConfig.scriptAttachmentMetadata
        );
        ctrl.scriptAttachmentMetadata = dialogConfig.scriptAttachmentMetadata;
      } else {
        ctrl.scriptAttachmentMetadata = {};
      }

      ctrl.cancel = function() {
        if ($scope.scriptAttachmentForm.$dirty) {
          CommonDialogsService
            .showConfirmOnDirtyDialog()
            .then($mdDialog.cancel);
        } else {
          $mdDialog.cancel();
        }
      };

      ctrl.upload = function(file) {
        if (file.name !== ctrl.scriptAttachmentMetadata.fileName &&
          !ctrl.isCreateMode) {
          CommonDialogsService.showConfirmFilenameChangedDialog(
            ctrl.scriptAttachmentForm.fileName, file.name).then(
            function() {
              ctrl.isCreateMode = true;
              ctrl.selectedFile = file;
              ctrl.scriptAttachmentForm.fileName = file.name;
              $scope.scriptAttachmentForm.filename.$setDirty();
              $scope.scriptAttachmentForm.filename.$setValidity(
                'valid', true);
              $scope.scriptAttachmentForm.filename.$setValidity(
                'unique', true);
            }
          );
        } else {
          ctrl.selectedFile = file;
          ctrl.scriptAttachmentMetadata.fileName = file.name;
          $scope.scriptAttachmentForm.filename.$setDirty();
          $scope.scriptAttachmentForm.filename.$setValidity(
            'valid', true);
          $scope.scriptAttachmentForm.filename.$setValidity(
            'unique', true);
        }
      };

      ctrl.saveScriptAttachment = function() {
        if ($scope.scriptAttachmentForm.$valid) {
          uploadFile(ctrl.selectedFile, ctrl.scriptAttachmentMetadata)
            .then(ctrl.onSavedSuccessfully)
            .catch(ctrl.onUploadFailed);
        } else {
          // ensure that all validation errors are visible
          angular.forEach($scope.scriptAttachmentForm.$error,
            function(field) {
              angular.forEach(field, function(errorField) {
                errorField.$setTouched();
              });
            });
          SimpleMessageToastService.openAlertMessageToast(
            'attachment.error.attachment-has-validation-errors-toast'
          );
        }
      };

      ctrl.onSavedSuccessfully = function() {
        $mdDialog.hide(ctrl.scriptAttachmentMetadata);
        SimpleMessageToastService.openSimpleMessageToast(
          'attachment.attachment-saved-toast',
          {
            filename: ctrl.scriptAttachmentMetadata.fileName
          });
      };

      ctrl.onUploadFailed = function(response) {
        if (response.invalidFile) {
          SimpleMessageToastService.openAlertMessageToast(
            'attachment.error.file-not-found',
            {
              filename: ctrl.scriptAttachmentMetadata.fileName
            });
          $scope.scriptAttachmentForm.filename.$setValidity(
            'valid', false);
        }
        if (response.errors && response.errors.length > 0) {
          SimpleMessageToastService.openAlertMessageToast(
            response.errors[0].message,
            {
              filename: ctrl.scriptAttachmentMetadata.fileName
            });
          $scope.scriptAttachmentForm.filename.$setValidity(
            'unique', false);
        }
      };
    });
