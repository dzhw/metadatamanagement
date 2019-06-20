/* globals _ */
'use strict';

angular.module('metadatamanagementApp').controller('AttachmentDialogController',
  function(dialogConfig, isoLanguages, CommonDialogsService, LanguageService,
            SimpleMessageToastService, $scope, $mdDialog) {

    var ctrl = this;
    var isInitialisingSelectedLanguage = false;
    var uploadFile = dialogConfig.uploadCallback;

    ctrl.isCreateMode = dialogConfig.attachmentMetadata === undefined ||
        dialogConfig.attachmentMetadata === null;
    ctrl.attachmentForm = $scope.attachmentForm;
    ctrl.currentLanguage = LanguageService.getCurrentInstantly();
    ctrl.attachmentTypes = dialogConfig.attachmentTypes;
    ctrl.labels = dialogConfig.labels;
    ctrl.titleParams = ctrl.isCreateMode ?
        _.get(ctrl, 'labels.createTitle.params') :
        _.get(ctrl, 'labels.editTitle.params');
    ctrl.excludedMetadataFields = dialogConfig.exclude;

    var isoLanguagesArray = Object.keys(isoLanguages).map(function(key) {
      return {
        code: key,
        'displayLanguage': isoLanguages[key]
      };
    });

    var initSelectedLanguage = function() {
      isInitialisingSelectedLanguage = true;
      ctrl.selectedLanguage = _.filter(isoLanguagesArray,
        function(isoLanguage) {
          return isoLanguage.code === ctrl.attachmentMetadata.language;
        })[0];
    };

    if (dialogConfig.attachmentMetadata) {
      ctrl.attachmentMetadata = angular.copy(dialogConfig.attachmentMetadata);
      ctrl.originalAttachmentMetadata = dialogConfig.attachmentMetadata;
      initSelectedLanguage();
    } else {
      ctrl.attachmentMetadata = {};
    }

    ctrl.cancel = function() {
      if ($scope.attachmentForm.$dirty) {
        CommonDialogsService.showConfirmOnDirtyDialog().then($mdDialog.cancel);
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
        ctrl.attachmentMetadata.language = ctrl.selectedLanguage.code;
      } else {
        delete ctrl.attachmentMetadata.language;
      }
      if (!isInitialisingSelectedLanguage) {
        $scope.attachmentForm.$setDirty();
      }
      isInitialisingSelectedLanguage = false;
    };

    ctrl.upload = function(file) {
      if (file.name !== ctrl.attachmentMetadata.fileName &&
        !ctrl.isCreateMode) {
        CommonDialogsService.showConfirmFilenameChangedDialog(
          ctrl.attachmentMetadata.fileName, file.name).then(
          function() {
            ctrl.isCreateMode = true;
            ctrl.selectedFile = file;
            ctrl.attachmentMetadata.fileName = file.name;
            $scope.attachmentForm.filename.$setDirty();
            $scope.attachmentForm.filename.$setValidity(
              'valid', true);
            $scope.attachmentForm.filename.$setValidity(
              'unique', true);
          }
        );
      } else {
        ctrl.selectedFile = file;
        ctrl.attachmentMetadata.fileName = file.name;
        $scope.attachmentForm.filename.$setDirty();
        $scope.attachmentForm.filename.$setValidity(
          'valid', true);
        $scope.attachmentForm.filename.$setValidity(
          'unique', true);
      }
    };

    ctrl.saveAttachment = function() {
      if ($scope.attachmentForm.$valid) {
        if (!ctrl.selectedFile) {
          ctrl.attachmentMetadata.$save().then(ctrl.onSavedSuccessfully);
        } else {
          if (ctrl.originalAttachmentMetadata) {
            ctrl.originalAttachmentMetadata.$delete().then(function() {
              uploadFile(ctrl.selectedFile, ctrl.attachmentMetadata)
                .then(ctrl.onSavedSuccessfully).catch(ctrl.onUploadFailed);
            });
          } else {
            uploadFile(ctrl.selectedFile, ctrl.attachmentMetadata)
                .then(ctrl.onSavedSuccessfully).catch(ctrl.onUploadFailed);
          }
        }
      } else {
        // ensure that all validation errors are visible
        angular.forEach($scope.attachmentForm.$error, function(field) {
          angular.forEach(field, function(errorField) {
            errorField.$setTouched();
          });
        });
        SimpleMessageToastService.openAlertMessageToast(
          'attachment.error.attachment-has-validation-errors-toast');
      }
    };

    ctrl.onSavedSuccessfully = function() {
      $mdDialog.hide(ctrl.attachmentMetadata);
      SimpleMessageToastService.openSimpleMessageToast(
        'attachment.attachment-saved-toast',
        {
          filename: ctrl.attachmentMetadata.fileName
        });
    };

    ctrl.onUploadFailed = function(response) {
      if (response.invalidFile) {
        SimpleMessageToastService.openAlertMessageToast(
          'attachment.error.file-not-found',
          {
            filename: ctrl.attachmentMetadata.fileName
          });
        $scope.attachmentForm.filename.$setValidity(
          'valid', false);
      }
      if (response.errors && response.errors.length > 0) {
        SimpleMessageToastService.openAlertMessageToast(
          response.errors[0].message,
          {
            filename: ctrl.attachmentMetadata.fileName
          });
        $scope.attachmentForm.filename.$setValidity(
          'unique', false);
      }
    };
  });
