/* global _, bowser */
'use strict';

angular.module('metadatamanagementApp')
  .controller('InstrumentAttachmentEditOrCreateController',
    function($mdDialog, instrumentAttachmentMetadata, $scope,
      CommonDialogsService,
      LanguageService, isoLanguages, SimpleMessageToastService,
      InstrumentAttachmentUploadService, InstrumentAttachmentResource) {
      $scope.bowser = bowser;
      var ctrl = this;
      $scope.translationParams = {
        instrumentId: instrumentAttachmentMetadata.instrumentId,
        filename: instrumentAttachmentMetadata.fileName
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
              ctrl.instrumentAttachmentMetadata.language;
          })[0];
      };

      if (!instrumentAttachmentMetadata.id) {
        ctrl.isCreateMode = true;
        ctrl.instrumentAttachmentMetadata = instrumentAttachmentMetadata;
      } else {
        ctrl.instrumentAttachmentMetadata =
          angular.copy(instrumentAttachmentMetadata);
        ctrl.originalInstrumentAttachmentMetadata =
          instrumentAttachmentMetadata;
        ctrl.initSelectedLanguage();
      }

      ctrl.cancel = function() {
        if ($scope.instrumentAttachmentForm.$dirty) {
          CommonDialogsService.showConfirmOnDirtyDialog()
            .then($mdDialog.cancel);
        } else {
          $mdDialog.cancel();
        }
      };

      ctrl.onSavedSuccessfully = function() {
        $mdDialog.hide(ctrl.instrumentAttachmentMetadata);
        SimpleMessageToastService.openSimpleMessageToast(
          'instrument-management.detail.attachments.attachment-saved-toast',
          {
            filename: ctrl.instrumentAttachmentMetadata.fileName
          });
      };

      ctrl.onUploadFailed = function(response) {
        if (response.invalidFile) {
          SimpleMessageToastService.openSimpleMessageToast(
            'instrument-management.log-messages.instrument-attachment.' +
            'file-not-found',
            {
              filename: ctrl.instrumentAttachmentMetadata.fileName
            });
          $scope.instrumentAttachmentForm.filename.$setValidity(
            'valid', false);
        }
        if (response.errors && response.errors.length > 0) {
          SimpleMessageToastService.openAlertMessageToast(
            response.errors[0].message,
            {
              filename: ctrl.instrumentAttachmentMetadata.fileName
            });
          $scope.instrumentAttachmentForm.filename.$setValidity(
            'unique', false);
        }
      };

      ctrl.saveAttachment = function() {
        if ($scope.instrumentAttachmentForm.$valid) {
          if (!ctrl.selectedFile) {
            ctrl.instrumentAttachmentMetadata
              .$save().then(ctrl.onSavedSuccessfully);
          } else {
            if (ctrl.originalInstrumentAttachmentMetadata) {
              ctrl.originalInstrumentAttachmentMetadata.$delete().then(
                function() {
                  InstrumentAttachmentUploadService.uploadAttachment(
                  ctrl.selectedFile, ctrl.instrumentAttachmentMetadata
                ).then(ctrl.onSavedSuccessfully).catch(ctrl.onUploadFailed);
                });
            } else {
              InstrumentAttachmentUploadService.uploadAttachment(
                ctrl.selectedFile, ctrl.instrumentAttachmentMetadata
              ).then(ctrl.onSavedSuccessfully).catch(ctrl.onUploadFailed);
            }
          }
        } else {
          // ensure that all validation errors are visible
          angular.forEach($scope.instrumentAttachmentForm.$error,
            function(field) {
              angular.forEach(field, function(errorField) {
                errorField.$setTouched();
              });
            });
          SimpleMessageToastService.openAlertMessageToast(
            'instrument-management.detail.attachments' +
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
          ctrl.instrumentAttachmentMetadata.language =
            ctrl.selectedLanguage.code;
        } else {
          delete ctrl.instrumentAttachmentMetadata.language;
        }
        if (!isInitialisingSelectedLanguage) {
          $scope.instrumentAttachmentForm.$setDirty();
        }
        isInitialisingSelectedLanguage = false;
      };

      ctrl.upload = function(file) {
        if (file.name !== ctrl.instrumentAttachmentMetadata.fileName &&
          !ctrl.isCreateMode) {
          CommonDialogsService.showConfirmFilenameChangedDialog(
            ctrl.instrumentAttachmentMetadata.fileName, file.name).then(
            function() {
              ctrl.isCreateMode = true;
              ctrl.selectedFile = file;
              ctrl.instrumentAttachmentMetadata.fileName = file.name;
              $scope.instrumentAttachmentForm.filename.$setDirty();
              $scope.instrumentAttachmentForm.filename.$setValidity(
                'valid', true);
              $scope.instrumentAttachmentForm.filename.$setValidity(
                'unique', true);
            }
          );
        } else {
          ctrl.selectedFile = file;
          ctrl.instrumentAttachmentMetadata.fileName = file.name;
          $scope.instrumentAttachmentForm.filename.$setDirty();
          $scope.instrumentAttachmentForm.filename.$setValidity(
            'valid', true);
          $scope.instrumentAttachmentForm.filename.$setValidity(
            'unique', true);
        }
      };

      ctrl.openRestorePreviousVersionDialog = function(event) {
        $mdDialog.show({
            controller: 'ChoosePreviousInstrumentAttachmentVersionController',
            templateUrl: 'scripts/instrumentmanagement/' +
              'views/choose-previous-instrument-attachment-version.html.tmpl',
            clickOutsideToClose: false,
            fullscreen: true,
            locals: {
              instrumentId: ctrl.instrumentAttachmentMetadata.instrumentId,
              filename: ctrl.instrumentAttachmentMetadata.fileName
            },
            multiple: true,
            targetEvent: event
          })
          .then(function(instrumentAttachmentWrapper) {
            ctrl.instrumentAttachmentMetadata =
              new InstrumentAttachmentResource(
                instrumentAttachmentWrapper.instrumentAttachment);
            ctrl.initSelectedLanguage();
            if (instrumentAttachmentWrapper.isCurrentVersion) {
              SimpleMessageToastService.openSimpleMessageToast(
                'instrument-management.detail.attachments' +
                '.current-version-restored-toast',
                {
                  filename: ctrl.instrumentAttachmentMetadata.fileName
                });
              $scope.instrumentAttachmentForm.$setPristine();
            } else {
              $scope.instrumentAttachmentForm.$setDirty();
              SimpleMessageToastService.openSimpleMessageToast(
                'instrument-management.detail.attachments' +
                '.previous-version-restored-toast',
                {
                  filename: ctrl.instrumentAttachmentMetadata.fileName
                });
            }
          });
      };
    });
