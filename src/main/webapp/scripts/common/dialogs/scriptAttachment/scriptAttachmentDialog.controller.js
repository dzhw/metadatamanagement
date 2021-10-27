/* globals _, bowser */
'use strict';

angular.module('metadatamanagementApp')
  .controller('ScriptAttachmentDialogController',
    function(dialogConfig, isoLanguages, CommonDialogsService, LanguageService,
             SimpleMessageToastService, $scope, $mdDialog) {
      $scope.bowser = bowser;
      var ctrl = this;
      var isInitialisingSelectedLanguage = false;
      var uploadFile = dialogConfig.uploadCallback;

      ctrl.isCreateMode = dialogConfig.scriptAttachmentMetadata === undefined ||
        dialogConfig.scriptAttachmentMetadata === null;
      ctrl.scriptAttachmentForm = $scope.scriptAttachmentForm;
      ctrl.currentLanguage = LanguageService.getCurrentInstantly();
      ctrl.labels = dialogConfig.labels;
      ctrl.titleParams = ctrl.isCreateMode ?
        _.get(ctrl, 'labels.createTitle.params') :
        _.get(ctrl, 'labels.editTitle.params');
      ctrl.excludedMetadataFields = dialogConfig.exclude;

      ctrl.translationKeys = {
        title: 'analysis-package-management.detail.label.attachments.authors',
        tooltips: {
          delete: 'analysis-package-management.edit.delete-author-tooltip',
          add: 'analysis-package-management.edit.add-author-tooltip',
          moveUp: 'analysis-package-management.edit.move-author-up-tooltip',
          moveDown: 'analysis-package-management.edit.move-author-down-tooltip'
        },
        hints: {
          firstName: 'analysis-package-management.edit' +
            '.hints.authors.first-name',
          middleName: 'analysis-package-management.edit' +
            '.hints.authors.middle-name',
          lastName: 'analysis-package-management.edit.hints.authors.last-name'
        }
      };

      var isoLanguagesArray = Object.keys(isoLanguages)
        .map(function(key) {
          return {
            code: key,
            'displayLanguage': isoLanguages[key]
          };
        });

      var initSelectedLanguage = function() {
        isInitialisingSelectedLanguage = true;
        ctrl.selectedLanguage = _.filter(isoLanguagesArray,
          function(isoLanguage) {
            return isoLanguage.code === ctrl.scriptAttachmentMetadata.language;
          })[0];
      };

      if (dialogConfig.scriptAttachmentMetadata) {
        ctrl.scriptAttachmentMetadata = angular.copy(
          dialogConfig.scriptAttachmentMetadata
        );
        ctrl.scriptAttachmentMetadata = dialogConfig.scriptAttachmentMetadata;
        initSelectedLanguage();
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

      ctrl.searchLanguages = function(searchText) {
        if (!searchText || searchText === '') {
          return isoLanguagesArray;
        }
        var lowerCasedSearchText = searchText.toLowerCase();
        return _.filter(isoLanguagesArray, function(isoLanguage) {
          return isoLanguage.displayLanguage[ctrl.currentLanguage]
            .toLowerCase().indexOf(lowerCasedSearchText) > -1;
        });
      };

      ctrl.selectedLanguageChanged = function() {
        if (ctrl.selectedLanguage) {
          ctrl.scriptAttachmentForm.language = ctrl.selectedLanguage.code;
        } else {
          delete ctrl.scriptAttachmentForm.language;
        }
        if (!isInitialisingSelectedLanguage) {
          $scope.scriptAttachmentForm.$setDirty();
        }
        isInitialisingSelectedLanguage = false;
      };

      ctrl.upload = function(file) {
        console.log(file);
        console.log(ctrl.scriptAttachmentMetadata);
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
          if (!ctrl.selectedFile) {
            ctrl.scriptAttachmentMetadata
              .$save()
              .then(ctrl.onSavedSuccessfully);
          } else {
            if (ctrl.originalScriptAttachmentMetadata) {
              ctrl.originalScriptAttachmentMetadata
                .$delete()
                .then(function() {
                  uploadFile(ctrl.selectedFile, ctrl.scriptAttachmentMetadata)
                    .then(ctrl.onSavedSuccessfully)
                    .catch(ctrl.onUploadFailed);
                });
            } else {
              uploadFile(ctrl.selectedFile, ctrl.scriptAttachmentMetadata)
                .then(ctrl.onSavedSuccessfully)
                .catch(ctrl.onUploadFailed);
            }
          }
        } else {
          // ensure that all validation errors are visible
          angular.forEach($scope.scriptAttachmentForm.$error,
            function(field) {
              angular.forEach(field, function(errorField) {
                errorField.$setTouched();
              });
            });
          SimpleMessageToastService.openAlertMessageToast(
            'attachment.error.attachment-has-validation-errors-toast');
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
