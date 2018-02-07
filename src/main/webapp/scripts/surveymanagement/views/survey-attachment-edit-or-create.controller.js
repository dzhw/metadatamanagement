/* global _, bowser */
'use strict';

angular.module('metadatamanagementApp')
  .controller('SurveyAttachmentEditOrCreateController',
    function($mdDialog, surveyAttachmentMetadata, $scope, CommonDialogsService,
      LanguageService, isoLanguages, SimpleMessageToastService,
      SurveyAttachmentUploadService, SurveyAttachmentResource) {
      $scope.bowser = bowser;
      var ctrl = this;
      $scope.translationParams = {
        surveyId: surveyAttachmentMetadata.surveyId,
        filename: surveyAttachmentMetadata.fileName
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
            return isoLanguage.code === ctrl.surveyAttachmentMetadata.language;
          })[0];
      };

      if (!surveyAttachmentMetadata.id) {
        ctrl.isCreateMode = true;
        ctrl.surveyAttachmentMetadata = surveyAttachmentMetadata;
      } else {
        ctrl.surveyAttachmentMetadata = angular.copy(surveyAttachmentMetadata);
        ctrl.originalSurveyAttachmentMetadata = surveyAttachmentMetadata;
        ctrl.initSelectedLanguage();
      }

      ctrl.cancel = function() {
        if ($scope.surveyAttachmentForm.$dirty) {
          CommonDialogsService.showConfirmOnDirtyDialog()
            .then($mdDialog.cancel);
        } else {
          $mdDialog.cancel();
        }
      };

      ctrl.onSavedSuccessfully = function() {
        $mdDialog.hide(ctrl.surveyAttachmentMetadata);
        SimpleMessageToastService.openSimpleMessageToast(
          'survey-management.detail.attachments.attachment-saved-toast',
          {
            filename: ctrl.surveyAttachmentMetadata.fileName
          }, true);
      };

      ctrl.onUploadFailed = function(response) {
        if (response.invalidFile) {
          SimpleMessageToastService.openSimpleMessageToast(
            'survey-management.log-messages.survey-attachment.file-not-found',
            {
              filename: ctrl.surveyAttachmentMetadata.fileName
            }, true);
          $scope.surveyAttachmentForm.filename.$setValidity(
            'valid', false);
        }
        if (response.errors && response.errors.length > 0) {
          SimpleMessageToastService.openSimpleMessageToast(
            response.errors[0].message,
            {
              filename: ctrl.surveyAttachmentMetadata.fileName
            }, true);
          $scope.surveyAttachmentForm.filename.$setValidity(
            'unique', false);
        }
      };

      ctrl.saveAttachment = function() {
        if ($scope.surveyAttachmentForm.$valid) {
          if (!ctrl.selectedFile) {
            ctrl.surveyAttachmentMetadata
              .$save().then(ctrl.onSavedSuccessfully);
          } else {
            if (ctrl.originalSurveyAttachmentMetadata) {
              ctrl.originalSurveyAttachmentMetadata.$delete().then(function() {
                SurveyAttachmentUploadService.uploadAttachment(
                  ctrl.selectedFile, ctrl.surveyAttachmentMetadata
                ).then(ctrl.onSavedSuccessfully).catch(ctrl.onUploadFailed);
              });
            } else {
              SurveyAttachmentUploadService.uploadAttachment(
                ctrl.selectedFile, ctrl.surveyAttachmentMetadata
              ).then(ctrl.onSavedSuccessfully).catch(ctrl.onUploadFailed);
            }
          }
        } else {
          // ensure that all validation errors are visible
          angular.forEach($scope.surveyAttachmentForm.$error, function(field) {
            angular.forEach(field, function(errorField) {
              errorField.$setTouched();
            });
          });
          SimpleMessageToastService.openSimpleMessageToast(
            'survey-management.detail.attachments' +
            '.attachment-has-validation-errors-toast',
            null, true);
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
          ctrl.surveyAttachmentMetadata.language = ctrl.selectedLanguage.code;
        } else {
          delete ctrl.surveyAttachmentMetadata.language;
        }
        if (!isInitialisingSelectedLanguage) {
          $scope.surveyAttachmentForm.$setDirty();
        }
        isInitialisingSelectedLanguage = false;
      };

      ctrl.upload = function(file) {
        if (file.name !== ctrl.surveyAttachmentMetadata.fileName &&
          !ctrl.isCreateMode) {
          CommonDialogsService.showConfirmFilenameChangedDialog(
            ctrl.surveyAttachmentMetadata.fileName, file.name).then(
            function() {
              ctrl.isCreateMode = true;
              ctrl.selectedFile = file;
              ctrl.surveyAttachmentMetadata.fileName = file.name;
              $scope.surveyAttachmentForm.filename.$setDirty();
              $scope.surveyAttachmentForm.filename.$setValidity(
                'valid', true);
              $scope.surveyAttachmentForm.filename.$setValidity(
                'unique', true);
            }
          );
        } else {
          ctrl.selectedFile = file;
          ctrl.surveyAttachmentMetadata.fileName = file.name;
          $scope.surveyAttachmentForm.filename.$setDirty();
          $scope.surveyAttachmentForm.filename.$setValidity(
            'valid', true);
          $scope.surveyAttachmentForm.filename.$setValidity(
            'unique', true);
        }
      };

      ctrl.openRestorePreviousVersionDialog = function(event) {
        $mdDialog.show({
            controller: 'ChoosePreviousSurveyAttachmentVersionController',
            templateUrl: 'scripts/surveymanagement/' +
              'views/choose-previous-survey-attachment-version.html.tmpl',
            clickOutsideToClose: false,
            fullscreen: true,
            locals: {
              surveyId: ctrl.surveyAttachmentMetadata.surveyId,
              filename: ctrl.surveyAttachmentMetadata.fileName
            },
            multiple: true,
            targetEvent: event
          })
          .then(function(surveyAttachmentWrapper) {
            ctrl.surveyAttachmentMetadata = new SurveyAttachmentResource(
              surveyAttachmentWrapper.surveyAttachment);
            ctrl.initSelectedLanguage();
            if (surveyAttachmentWrapper.isCurrentVersion) {
              SimpleMessageToastService.openSimpleMessageToast(
                'survey-management.detail.attachments' +
                '.current-version-restored-toast',
                {
                  filename: ctrl.surveyAttachmentMetadata.fileName
                }, true);
              $scope.surveyAttachmentForm.$setPristine();
            } else {
              $scope.surveyAttachmentForm.$setDirty();
              SimpleMessageToastService.openSimpleMessageToast(
                'survey-management.detail.attachments' +
                '.previous-version-restored-toast',
                {
                  filename: ctrl.surveyAttachmentMetadata.fileName
                }, true);
            }
          });
      };
    });
