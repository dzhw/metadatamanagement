/* global _, bowser */
'use strict';

angular.module('metadatamanagementApp')
  .controller('DataSetAttachmentEditOrCreateController',
    function($mdDialog, dataSetAttachmentMetadata, $scope,
      CommonDialogsService,
      LanguageService, isoLanguages, SimpleMessageToastService,
      DataSetAttachmentUploadService, DataSetAttachmentResource) {
      $scope.bowser = bowser;
      var ctrl = this;
      $scope.translationParams = {
        dataSetId: dataSetAttachmentMetadata.dataSetId,
        filename: dataSetAttachmentMetadata.fileName
      };
      var isInitialisingSelectedLanguage = false;

      $scope.currentLanguage = LanguageService.getCurrentInstantly();

      var isoLanguagesArray = Object.keys(isoLanguages).map(function(key) {
        return {
          code: key,
          'displayLanguage': isoLanguages[key]
        };
      });

      ctrl.initSelectedLanguage = function() {
        isInitialisingSelectedLanguage = true;
        ctrl.selectedLanguage = _.filter(isoLanguagesArray,
          function(isoLanguage) {
            return isoLanguage.code ===
              ctrl.dataSetAttachmentMetadata.language;
          })[0];
      };

      if (!dataSetAttachmentMetadata.id) {
        ctrl.isCreateMode = true;
        ctrl.dataSetAttachmentMetadata = dataSetAttachmentMetadata;
      } else {
        ctrl.dataSetAttachmentMetadata =
          angular.copy(dataSetAttachmentMetadata);
        ctrl.originalDataSetAttachmentMetadata =
          dataSetAttachmentMetadata;
        ctrl.initSelectedLanguage();
      }

      ctrl.cancel = function() {
        if ($scope.dataSetAttachmentForm.$dirty) {
          CommonDialogsService.showConfirmOnDirtyDialog()
            .then($mdDialog.cancel);
        } else {
          $mdDialog.cancel();
        }
      };

      ctrl.onSavedSuccessfully = function() {
        $mdDialog.hide(ctrl.dataSetAttachmentMetadata);
        SimpleMessageToastService.openSimpleMessageToast(
          'data-set-management.detail.attachments.attachment-saved-toast',
          {
            filename: ctrl.dataSetAttachmentMetadata.fileName
          });
      };

      ctrl.onUploadFailed = function(response) {
        if (response.invalidFile) {
          SimpleMessageToastService.openSimpleMessageToast(
            'data-set-management.log-messages.data-set-attachment.' +
            'file-not-found',
            {
              filename: ctrl.dataSetAttachmentMetadata.fileName
            });
          $scope.dataSetAttachmentForm.filename.$setValidity(
            'valid', false);
        }
        if (response.errors && response.errors.length > 0) {
          SimpleMessageToastService.openAlertMessageToast(
            response.errors[0].message,
            {
              filename: ctrl.dataSetAttachmentMetadata.fileName
            });
          $scope.dataSetAttachmentForm.filename.$setValidity(
            'unique', false);
        }
      };

      ctrl.saveAttachment = function() {
        if ($scope.dataSetAttachmentForm.$valid) {
          if (!ctrl.selectedFile) {
            ctrl.dataSetAttachmentMetadata
              .$save().then(ctrl.onSavedSuccessfully);
          } else {
            if (ctrl.originalDataSetAttachmentMetadata) {
              ctrl.originalDataSetAttachmentMetadata.$delete().then(
                function() {
                  DataSetAttachmentUploadService.uploadAttachment(
                  ctrl.selectedFile, ctrl.dataSetAttachmentMetadata
                ).then(ctrl.onSavedSuccessfully).catch(ctrl.onUploadFailed);
                });
            } else {
              DataSetAttachmentUploadService.uploadAttachment(
                ctrl.selectedFile, ctrl.dataSetAttachmentMetadata
              ).then(ctrl.onSavedSuccessfully).catch(ctrl.onUploadFailed);
            }
          }
        } else {
          // ensure that all validation errors are visible
          angular.forEach($scope.dataSetAttachmentForm.$error,
            function(field) {
              angular.forEach(field, function(errorField) {
                errorField.$setTouched();
              });
            });
          SimpleMessageToastService.openAlertMessageToast(
            'data-set-management.detail.attachments' +
            '.attachment-has-validation-errors-toast',
            null);
        }
      };

      ctrl.searchLanguages = function(searchText) {
        if (!searchText || searchText === '') {
          return isoLanguagesArray;
        }
        var lowerCasedSearchText = searchText.toLowerCase();
        return _.filter(isoLanguagesArray, function(isoLanguage) {
          return isoLanguage.displayLanguage[$scope.currentLanguage]
            .toLowerCase().indexOf(lowerCasedSearchText) > -1;
        });
      };

      ctrl.selectedLanguageChanged = function() {
        if (ctrl.selectedLanguage) {
          ctrl.dataSetAttachmentMetadata.language =
            ctrl.selectedLanguage.code;
        } else {
          delete ctrl.dataSetAttachmentMetadata.language;
        }
        if (!isInitialisingSelectedLanguage) {
          $scope.dataSetAttachmentForm.$setDirty();
        }
        isInitialisingSelectedLanguage = false;
      };

      ctrl.upload = function(file) {
        if (file.name !== ctrl.dataSetAttachmentMetadata.fileName &&
          !ctrl.isCreateMode) {
          CommonDialogsService.showConfirmFilenameChangedDialog(
            ctrl.dataSetAttachmentMetadata.fileName, file.name).then(
            function() {
              ctrl.isCreateMode = true;
              ctrl.selectedFile = file;
              ctrl.dataSetAttachmentMetadata.fileName = file.name;
              $scope.dataSetAttachmentForm.filename.$setDirty();
              $scope.dataSetAttachmentForm.filename.$setValidity(
                'valid', true);
              $scope.dataSetAttachmentForm.filename.$setValidity(
                'unique', true);
            }
          );
        } else {
          ctrl.selectedFile = file;
          ctrl.dataSetAttachmentMetadata.fileName = file.name;
          $scope.dataSetAttachmentForm.filename.$setDirty();
          $scope.dataSetAttachmentForm.filename.$setValidity(
            'valid', true);
          $scope.dataSetAttachmentForm.filename.$setValidity(
            'unique', true);
        }
      };

      ctrl.dataSetAttachmentTypes = [
        {de: 'Fragebogen', en: 'Questionnaire'},
        {de: 'Filterführungsdiagramm', en: 'Question Flow'},
        {de: 'Variablenfragebogen', en: 'Variable Questionnaire'},
        {de: 'Sonstige', en: 'Other'}
      ];

      ctrl.openRestorePreviousVersionDialog = function(event) {
        $mdDialog.show({
            controller: 'ChoosePreviousDataSetAttachmentVersionController',
            templateUrl: 'scripts/datasetmanagement/' +
              'views/choose-previous-data-set-attachment-version.html.tmpl',
            clickOutsideToClose: false,
            fullscreen: true,
            locals: {
              dataSetId: ctrl.dataSetAttachmentMetadata.dataSetId,
              filename: ctrl.dataSetAttachmentMetadata.fileName
            },
            multiple: true,
            targetEvent: event
          })
          .then(function(dataSetAttachmentWrapper) {
            ctrl.dataSetAttachmentMetadata =
              new DataSetAttachmentResource(
                dataSetAttachmentWrapper.dataSetAttachment);
            ctrl.initSelectedLanguage();
            if (dataSetAttachmentWrapper.isCurrentVersion) {
              SimpleMessageToastService.openSimpleMessageToast(
                'data-set-management.detail.attachments' +
                '.current-version-restored-toast',
                {
                  filename: ctrl.dataSetAttachmentMetadata.fileName
                });
              $scope.dataSetAttachmentForm.$setPristine();
            } else {
              $scope.dataSetAttachmentForm.$setDirty();
              SimpleMessageToastService.openSimpleMessageToast(
                'data-set-management.detail.attachments' +
                '.previous-version-restored-toast',
                {
                  filename: ctrl.dataSetAttachmentMetadata.fileName
                });
            }
          });
      };
    });
