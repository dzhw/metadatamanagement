/* globals _, bowser */
'use strict';

angular.module('metadatamanagementApp').controller('AttachmentDialogController', [
  'dialogConfig',
  'isoLanguages',
  'CommonDialogsService',
  'LanguageService',
  'SimpleMessageToastService',
  '$scope',
  '$mdDialog',
  '$translate',
  function(dialogConfig, isoLanguages, CommonDialogsService, LanguageService,
            SimpleMessageToastService, $scope, $mdDialog, $translate) {
    $scope.bowser = bowser;
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

    ctrl.translationKeys = {
      title: 'data-package-management.detail.label.attachments.authors',
      tooltips: {
        delete: 'data-package-management.edit.delete-author-tooltip',
        add: 'data-package-management.edit.add-author-tooltip',
        moveUp: 'data-package-management.edit.move-author-up-tooltip',
        moveDown: 'data-package-management.edit.move-author-down-tooltip'
      },
      hints: {
        firstName: 'data-package-management.edit.hints.authors.first-name',
        middleName: 'data-package-management.edit.hints.authors.middle-name',
        lastName: 'data-package-management.edit.hints.authors.last-name'
      }
    };

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

    ctrl.setTitleForMethodReports = function() {
      if (ctrl.attachmentMetadata.language === 'de') {
        ctrl.attachmentMetadata.title =
          'Daten- und Methodenbericht des Datenpakets "' +
          dialogConfig.dataPackageTitle.de + '"';
      } else {
        ctrl.attachmentMetadata.title =
          'Data and Methods Report of the data package "' +
          dialogConfig.dataPackageTitle.en + '"';
      }
    };

    ctrl.selectedLanguageChanged = function() {
      if (ctrl.selectedLanguage) {
        ctrl.attachmentMetadata.language = ctrl.selectedLanguage.code;
        if (ctrl.titleParams.dataPackageId) {
          if (ctrl.attachmentMetadata.type.en === 'Method Report') {
            ctrl.setTitleForMethodReports();
          }
        }
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
        // before saving, remove empty author array entries; we assume that entry is null if 'firstName'
        // is null because it is one of the mandatory fields
        if (ctrl.attachmentMetadata.type.en === "Questionnaire" || ctrl.attachmentMetadata.type.en === "Variable Questionnaire") {
          ctrl.attachmentMetadata.citationDetails.authors = ctrl.attachmentMetadata.citationDetails.authors.filter(
            author => author.firstName && author.firstName.trim() !== "");
        }
        
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

    ctrl.openRestorePreviousVersionDialog = function(event) {
      $mdDialog.show({
        controller: 'ChoosePreviousAttachmentVersionController',
        templateUrl: 'scripts/common/dialogs/choose-previous-attachment' +
            '-version/choose-attachment-version.html.tmpl',
        clickOutsideToClose: false,
        fullscreen: true,
        locals: {
          domainId: ctrl.attachmentMetadata[dialogConfig
              .attachmentDomainIdAttribute],
          filename: dialogConfig.attachmentMetadata.fileName,
          getAttachmentVersionsCallback: dialogConfig
              .getAttachmentVersionsCallback
        },
        multiple: true,
        targetEvent: event
      })
      .then(function(attachmentWrapper) {
        ctrl.attachmentMetadata = dialogConfig
            .createAttachmentResource(attachmentWrapper);
        initSelectedLanguage();
        if (ctrl.attachmentMetadata.type &&
          ctrl.attachmentMetadata.type.en === 'Method Report') {
          ctrl.attachmentMetadata.citationDetails =
            ctrl.attachmentMetadata.citationDetails || {};
          ctrl.attachmentMetadata.citationDetails.authors =
            ctrl.attachmentMetadata.citationDetails.authors || [];
          if (ctrl.attachmentMetadata.citationDetails.authors.length === 0) {
            ctrl.attachmentMetadata.citationDetails.authors.push({
              firstName: '',
              lastName: ''
            });
          }
        }
        if (attachmentWrapper.isCurrentVersion) {
          SimpleMessageToastService.openSimpleMessageToast(
              'attachment.current-version-restored-toast',
              {
                filename: ctrl.attachmentMetadata.fileName
              });
          $scope.attachmentForm.$setPristine();
        } else {
          $scope.attachmentForm.$setDirty();
          SimpleMessageToastService.openSimpleMessageToast(
              'attachment.previous-version-restored-toast',
              {
                filename: ctrl.attachmentMetadata.fileName
              });
        }
      });
    };

    ctrl.onTypeChanged = function() {
      if (ctrl.titleParams.dataPackageId) {
        ctrl.attachmentMetadata.description = {
          de: '',
          en: ''
        }
        if (ctrl.attachmentMetadata.type.en === 'Method Report') {
          ctrl.setTitleForMethodReports();
          $translate('attachment.predefined-content.description.method-report.de').then(function(descriptionDe) {
            ctrl.attachmentMetadata.description.de = descriptionDe;
          });
          $translate('attachment.predefined-content.description.method-report.en').then(function(descriptionEn) {
            ctrl.attachmentMetadata.description.en = descriptionEn;
          });
        } else {
          delete ctrl.attachmentMetadata.citationDetails;
        }
        if (ctrl.attachmentMetadata.type.en === 'Release Notes') {
          ctrl.attachmentMetadata.title = 'Release Notes';
          ctrl.attachmentMetadata.language = 'de';
          ctrl.selectedLanguage = _.filter(isoLanguagesArray,
            function(isoLanguage) {
              return isoLanguage.code === ctrl.attachmentMetadata.language;
            })[0];
          $translate('attachment.predefined-content.description.release-notes.de').then(function(descriptionDe) {
            ctrl.attachmentMetadata.description.de = descriptionDe;
          });
          $translate('attachment.predefined-content.description.release-notes.en').then(function(descriptionEn) {
            ctrl.attachmentMetadata.description.en = descriptionEn;
          });
        }
      }
    };
  }]);

