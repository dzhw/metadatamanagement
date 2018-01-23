/* global _*/
'use strict';

angular.module('metadatamanagementApp')
  .controller('StudyAttachmentEditOrCreateController',
    function($mdDialog, studyAttachmentMetadata, $scope, CommonDialogsService,
      LanguageService, isoLanguages, SimpleMessageToastService,
      StudyAttachmentUploadService, StudyAttachmentResource) {
      var ctrl = this;
      $scope.translationParams = {
        studyId: studyAttachmentMetadata.studyId,
        filename: studyAttachmentMetadata.fileName
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
            return isoLanguage.code === ctrl.studyAttachmentMetadata.language;
          })[0];
      };

      if (!studyAttachmentMetadata.id) {
        ctrl.isCreateMode = true;
        ctrl.studyAttachmentMetadata = studyAttachmentMetadata;
      } else {
        ctrl.studyAttachmentMetadata = angular.copy(studyAttachmentMetadata);
        ctrl.originalStudyAttachmentMetadata = studyAttachmentMetadata;
        ctrl.initSelectedLanguage();
      }

      ctrl.cancel = function() {
        if ($scope.studyAttachmentForm.$dirty) {
          CommonDialogsService.showConfirmOnDirtyDialog()
            .then($mdDialog.cancel);
        } else {
          $mdDialog.cancel();
        }
      };

      ctrl.onSavedSuccessfully = function() {
        $mdDialog.hide(ctrl.studyAttachmentMetadata);
        SimpleMessageToastService.openSimpleMessageToast(
          'study-management.detail.attachments.attachment-saved-toast',
          {
            filename: ctrl.studyAttachmentMetadata.fileName
          }, true);
      };

      ctrl.onUploadFailed = function(response) {
        if (response.invalidFile) {
          SimpleMessageToastService.openSimpleMessageToast(
            'study-management.log-messages.study-attachment.file-not-found',
            {
              filename: ctrl.studyAttachmentMetadata.fileName
            }, true);
          $scope.studyAttachmentForm.filename.$setValidity(
            'valid', false);
        }
        if (response.errors && response.errors.length > 0) {
          SimpleMessageToastService.openSimpleMessageToast(
            response.errors[0].message,
            {
              filename: ctrl.studyAttachmentMetadata.fileName
            }, true);
          $scope.studyAttachmentForm.filename.$setValidity(
            'unique', false);
        }
      };

      ctrl.saveAttachment = function() {
        if ($scope.studyAttachmentForm.$valid) {
          if (!ctrl.selectedFile) {
            ctrl.studyAttachmentMetadata.$save().then(ctrl.onSavedSuccessfully);
          } else {
            if (ctrl.originalStudyAttachmentMetadata) {
              ctrl.originalStudyAttachmentMetadata.$delete().then(function() {
                StudyAttachmentUploadService.uploadAttachment(
                  ctrl.selectedFile, ctrl.studyAttachmentMetadata
                ).then(ctrl.onSavedSuccessfully).catch(ctrl.onUploadFailed);
              });
            } else {
              StudyAttachmentUploadService.uploadAttachment(
                ctrl.selectedFile, ctrl.studyAttachmentMetadata
              ).then(ctrl.onSavedSuccessfully).catch(ctrl.onUploadFailed);
            }
          }
        } else {
          // ensure that all validation errors are visible
          angular.forEach($scope.studyAttachmentForm.$error, function(field) {
            angular.forEach(field, function(errorField) {
              errorField.$setTouched();
            });
          });
          SimpleMessageToastService.openSimpleMessageToast(
            'study-management.detail.attachments' +
            '.attachment-has-validation-errors-toast',
            null, true);
        }
      };

      ctrl.studyAttachmentTypes = [
        {de: 'Daten- und Methodenbericht', en: 'Method Report'},
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
          ctrl.studyAttachmentMetadata.language = ctrl.selectedLanguage.code;
        } else {
          delete ctrl.studyAttachmentMetadata.language;
        }
        if (!isInitialisingSelectedLanguage) {
          $scope.studyAttachmentForm.$setDirty();
        }
        isInitialisingSelectedLanguage = false;
      };

      ctrl.upload = function(file) {
        if (file.name !== ctrl.studyAttachmentMetadata.fileName &&
          !ctrl.isCreateMode) {
          CommonDialogsService.showConfirmFilenameChangedDialog(
            ctrl.studyAttachmentMetadata.fileName, file.name).then(
            function() {
              ctrl.isCreateMode = true;
              ctrl.selectedFile = file;
              ctrl.studyAttachmentMetadata.fileName = file.name;
              $scope.studyAttachmentForm.filename.$setDirty();
              $scope.studyAttachmentForm.filename.$setValidity(
                'valid', true);
              $scope.studyAttachmentForm.filename.$setValidity(
                'unique', true);
            }
          );
        } else {
          ctrl.selectedFile = file;
          ctrl.studyAttachmentMetadata.fileName = file.name;
          $scope.studyAttachmentForm.filename.$setDirty();
          $scope.studyAttachmentForm.filename.$setValidity(
            'valid', true);
          $scope.studyAttachmentForm.filename.$setValidity(
            'unique', true);
        }
      };

      ctrl.openRestorePreviousVersionDialog = function(event) {
        $mdDialog.show({
            controller: 'ChoosePreviousStudyAttachmentVersionController',
            templateUrl: 'scripts/studymanagement/' +
              'views/choose-previous-study-attachment-version.html.tmpl',
            clickOutsideToClose: false,
            fullscreen: true,
            locals: {
              studyId: ctrl.studyAttachmentMetadata.studyId,
              filename: ctrl.studyAttachmentMetadata.fileName
            },
            multiple: true,
            targetEvent: event
          })
          .then(function(studyAttachmentWrapper) {
            ctrl.studyAttachmentMetadata = new StudyAttachmentResource(
              studyAttachmentWrapper.studyAttachment);
            ctrl.initSelectedLanguage();
            if (studyAttachmentWrapper.isCurrentVersion) {
              SimpleMessageToastService.openSimpleMessageToast(
                'study-management.detail.attachments' +
                '.current-version-restored-toast',
                {
                  filename: ctrl.studyAttachmentMetadata.fileName
                }, true);
              $scope.studyAttachmentForm.$setPristine();
            } else {
              $scope.studyAttachmentForm.$setDirty();
              SimpleMessageToastService.openSimpleMessageToast(
                'study-management.detail.attachments' +
                '.previous-version-restored-toast',
                {
                  filename: ctrl.studyAttachmentMetadata.fileName
                }, true);
            }
          });
      };
    });
