/* global _, bowser */
'use strict';

angular.module('metadatamanagementApp')
  .controller('ConceptAttachmentEditOrCreateController',
    function($mdDialog, conceptAttachmentMetadata, $scope, CommonDialogsService,
      LanguageService, isoLanguages, SimpleMessageToastService,
      ConceptAttachmentUploadService, ConceptAttachmentResource) {
      $scope.bowser = bowser;
      var ctrl = this;
      $scope.translationParams = {
        conceptId: conceptAttachmentMetadata.conceptId,
        filename: conceptAttachmentMetadata.fileName
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
            return isoLanguage.code === ctrl.conceptAttachmentMetadata.language;
          })[0];
      };

      if (!conceptAttachmentMetadata.id) {
        ctrl.isCreateMode = true;
        ctrl.conceptAttachmentMetadata = conceptAttachmentMetadata;
      } else {
        ctrl.conceptAttachmentMetadata =
          angular.copy(conceptAttachmentMetadata);
        ctrl.originalConceptAttachmentMetadata = conceptAttachmentMetadata;
        ctrl.initSelectedLanguage();
      }

      ctrl.cancel = function() {
        if ($scope.conceptAttachmentForm.$dirty) {
          CommonDialogsService.showConfirmOnDirtyDialog()
            .then($mdDialog.cancel);
        } else {
          $mdDialog.cancel();
        }
      };

      ctrl.onSavedSuccessfully = function() {
        $mdDialog.hide(ctrl.conceptAttachmentMetadata);
        SimpleMessageToastService.openSimpleMessageToast(
          'concept-management.detail.attachments.attachment-saved-toast',
          {
            filename: ctrl.conceptAttachmentMetadata.fileName
          });
      };

      ctrl.onUploadFailed = function(response) {
        if (response.invalidFile) {
          SimpleMessageToastService.openAlertMessageToast(
            'concept-management.log-messages.concept-attachment.file-not-found',
            {
              filename: ctrl.conceptAttachmentMetadata.fileName
            });
          $scope.conceptAttachmentForm.filename.$setValidity(
            'valid', false);
        }
        if (response.errors && response.errors.length > 0) {
          SimpleMessageToastService.openAlertMessageToast(
            response.errors[0].message,
            {
              filename: ctrl.conceptAttachmentMetadata.fileName
            });
          $scope.conceptAttachmentForm.filename.$setValidity(
            'unique', false);
        }
      };

      ctrl.saveAttachment = function() {
        if ($scope.conceptAttachmentForm.$valid) {
          if (!ctrl.selectedFile) {
            ctrl.conceptAttachmentMetadata.$save().then(
              ctrl.onSavedSuccessfully);
          } else {
            if (ctrl.originalConceptAttachmentMetadata) {
              ctrl.originalConceptAttachmentMetadata.$delete().then(function() {
                ConceptAttachmentUploadService.uploadAttachment(
                  ctrl.selectedFile, ctrl.conceptAttachmentMetadata
                ).then(ctrl.onSavedSuccessfully).catch(ctrl.onUploadFailed);
              });
            } else {
              ConceptAttachmentUploadService.uploadAttachment(
                ctrl.selectedFile, ctrl.conceptAttachmentMetadata
              ).then(ctrl.onSavedSuccessfully).catch(ctrl.onUploadFailed);
            }
          }
        } else {
          // ensure that all validation errors are visible
          angular.forEach($scope.conceptAttachmentForm.$error, function(field) {
            angular.forEach(field, function(errorField) {
              errorField.$setTouched();
            });
          });
          SimpleMessageToastService.openAlertMessageToast(
            'concept-management.detail.attachments' +
            '.attachment-has-validation-errors-toast',
            null);
        }
      };

      ctrl.conceptAttachmentTypes = [
        {de: 'Dokumentation', en: 'Documentation'},
        {de: 'Instrument', en: 'Instrument'},
        {de: 'Sonstiges', en: 'Other'}
      ];

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
          ctrl.conceptAttachmentMetadata.language = ctrl.selectedLanguage.code;
        } else {
          delete ctrl.conceptAttachmentMetadata.language;
        }
        if (!isInitialisingSelectedLanguage) {
          $scope.conceptAttachmentForm.$setDirty();
        }
        isInitialisingSelectedLanguage = false;
      };

      ctrl.upload = function(file) {
        if (file.name !== ctrl.conceptAttachmentMetadata.fileName &&
          !ctrl.isCreateMode) {
          CommonDialogsService.showConfirmFilenameChangedDialog(
            ctrl.conceptAttachmentMetadata.fileName, file.name).then(
            function() {
              ctrl.isCreateMode = true;
              ctrl.selectedFile = file;
              ctrl.conceptAttachmentMetadata.fileName = file.name;
              $scope.conceptAttachmentForm.filename.$setDirty();
              $scope.conceptAttachmentForm.filename.$setValidity(
                'valid', true);
              $scope.conceptAttachmentForm.filename.$setValidity(
                'unique', true);
            }
          );
        } else {
          ctrl.selectedFile = file;
          ctrl.conceptAttachmentMetadata.fileName = file.name;
          $scope.conceptAttachmentForm.filename.$setDirty();
          $scope.conceptAttachmentForm.filename.$setValidity(
            'valid', true);
          $scope.conceptAttachmentForm.filename.$setValidity(
            'unique', true);
        }
      };

      ctrl.openRestorePreviousVersionDialog = function(event) {
        $mdDialog.show({
            controller: 'ChoosePreviousConceptAttachmentVersionController',
            templateUrl: 'scripts/conceptmanagement/' +
              'views/choose-previous-concept-attachment-version.html.tmpl',
            clickOutsideToClose: false,
            fullscreen: true,
            locals: {
              conceptId: ctrl.conceptAttachmentMetadata.conceptId,
              filename: ctrl.conceptAttachmentMetadata.fileName
            },
            multiple: true,
            targetEvent: event
          })
          .then(function(conceptAttachmentWrapper) {
            ctrl.conceptAttachmentMetadata = new ConceptAttachmentResource(
              conceptAttachmentWrapper.conceptAttachment);
            ctrl.initSelectedLanguage();
            if (conceptAttachmentWrapper.isCurrentVersion) {
              SimpleMessageToastService.openSimpleMessageToast(
                'concept-management.detail.attachments' +
                '.current-version-restored-toast',
                {
                  filename: ctrl.conceptAttachmentMetadata.fileName
                });
              $scope.conceptAttachmentForm.$setPristine();
            } else {
              $scope.conceptAttachmentForm.$setDirty();
              SimpleMessageToastService.openSimpleMessageToast(
                'concept-management.detail.attachments' +
                '.previous-version-restored-toast',
                {
                  filename: ctrl.conceptAttachmentMetadata.fileName
                });
            }
          });
      };
    });
