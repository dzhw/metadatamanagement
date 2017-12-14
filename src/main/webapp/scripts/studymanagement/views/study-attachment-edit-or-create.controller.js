/* global _*/
'use strict';

angular.module('metadatamanagementApp')
  .controller('StudyAttachmentEditOrCreateController',
    function($mdDialog, studyAttachmentMetadata, $scope, CommonDialogsService,
      LanguageService, isoLanguages, SimpleMessageToastService,
      StudyAttachmentUploadService) {
      var ctrl = this;
      $scope.currentLanguage = LanguageService.getCurrentInstantly();
      var isoLanguagesArray = Object.keys(isoLanguages).map(function(key) {
        return {
          code: key,
          'displayLanguage': isoLanguages[key]
        };
      });
      if (!studyAttachmentMetadata.id) {
        ctrl.isCreateMode = true;
        ctrl.studyAttachmentMetadata = studyAttachmentMetadata;
      } else {
        ctrl.studyAttachmentMetadata = angular.copy(studyAttachmentMetadata);
        ctrl.originalStudyAttachmentMetadata = studyAttachmentMetadata;
        ctrl.selectedLanguage = _.filter(isoLanguagesArray,
          function(isoLanguage) {
            return isoLanguage.code === ctrl.studyAttachmentMetadata.language;
          })[0];
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
            filename: ctrl.studyAttachmentMetadata.filename
          }, true);
      };

      ctrl.saveAttachment = function() {
        if (!ctrl.selectedFile) {
          ctrl.studyAttachmentMetadata.$save().then(ctrl.onSavedSuccessfully);
        } else {
          if (ctrl.originalStudyAttachmentMetadata) {
            ctrl.originalStudyAttachmentMetadata.$delete().then(function() {
              StudyAttachmentUploadService.uploadAttachment(
                ctrl.selectedFile, ctrl.studyAttachmentMetadata
              ).then(ctrl.onSavedSuccessfully);
            });
          } else {
            StudyAttachmentUploadService.uploadAttachment(
              ctrl.selectedFile, ctrl.studyAttachmentMetadata
            ).then(ctrl.onSavedSuccessfully);
          }
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
        $scope.studyAttachmentForm.$setDirty();
      };

      ctrl.upload = function(file) {
        ctrl.selectedFile = file;
        ctrl.studyAttachmentMetadata.fileName = file.name;
        $scope.studyAttachmentForm.filename.$setDirty();
      };
    });
