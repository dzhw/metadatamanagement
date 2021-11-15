/* globals _, document */
(function() {
    'use strict';

    function Controller(
      $scope,
      uuid,
      isoLanguages,
      LanguageService,
      CommonDialogsService,
      $mdDialog,
      SimpleMessageToastService,
      ScriptSoftwarePackagesResource,
      ScriptAttachmentUploadService,
      ScriptAttachmentResource
    ) {
      var $ctrl = this;
      var isInitialisingSelectedLanguage = false;
      var isInitialisingSelectedSoftwarePackage = false;
      var softwarePackages = [];
      var formElements = ['INPUT', 'SELECT', 'TEXTAREA'];

      $ctrl.currentLanguage = LanguageService.getCurrentInstantly();
      $ctrl.selectedFile = '';
      $ctrl.scriptAttachmentMetadata = [];
      $ctrl.currentScriptIndex = null;
      $ctrl.currentFocusElement = null;
      $ctrl.focus = false;
      $ctrl.scriptElement = document.getElementById('scripts');

      var isoLanguagesArray = Object.keys(isoLanguages)
        .map(function(key) {
          return {
            code: key,
            'displayLanguage': isoLanguages[key]
          };
        });

      var initSelectedLanguages = function() {
        isInitialisingSelectedLanguage = true;
        _.forEach($ctrl.scripts, function(value, index) {
          var result = _.filter(isoLanguagesArray,
            function(isoLanguage) {
              return isoLanguage.code === value.usedLanguage;
            });
          if (result.length > 0) {
            $ctrl['selectedLanguage_' + index] = result[0]
              .displayLanguage[$ctrl.currentLanguage];
          }
        });

      };

      var getScriptAttachmentMetadata = function() {
        _.forEach($ctrl.scripts, function(value, index) {
          _.filter($ctrl.scriptAttachments, function(item) {
            if (item.scriptUuid === value.uuid) {
              $ctrl.scripts[index].scriptAttachmentMetadata = item;
            }
          });
        });
      };

      var getSoftwarePackages = function() {
        ScriptSoftwarePackagesResource.get()
          .$promise.then(function(softwarePackage) {
          angular.copy(softwarePackage, softwarePackages);
        });
      };

      var initSelectedSoftwarePackages = function() {
        _.forEach($ctrl.scripts, function(value, index) {
          $ctrl['selectedSoftwarePackage_' + index] = value.softwarePackage;
        });
      };

      $scope.$on('LoadScriptEvent', function() {
        $ctrl.$onInit();
      });

      $ctrl.$onInit = function() {
        if ($ctrl.scripts && $ctrl.scripts.length === 0) {
          $ctrl.scripts = [{
            uuid: uuid.v4(),
            title: {
              de: '',
              en: ''
            },
            softwarePackage: '',
            softwarePackageVersion: '',
            usedLanguage: '',
            scriptAttachmentMetadata: {
              fileName: ''
            }
          }];
        }
        $ctrl.attachments = $ctrl.attachments || [];
        $ctrl.currentLanguage = LanguageService.getCurrentInstantly();
        $ctrl.loadScriptAttachments();
        initSelectedLanguages();
        getSoftwarePackages();
        initSelectedSoftwarePackages();
      };

      $ctrl.deleteScript = function(index, uuid) {
        if ($ctrl.scriptAttachments && $ctrl.scriptAttachments.length > 0) {
          var scriptAttachment = _.filter($ctrl.scriptAttachments,
            function(attachment) {
              if (attachment.scriptUuid === uuid) {
                var metadata = {
                  analysisPackageId: $ctrl.packageId,
                  dataAcquisitionProjectId: $ctrl.projectId,
                  masterId: $ctrl.masterId,
                  scriptUuid: $ctrl.scripts[index].uuid,
                  fileName: attachment.name
                };
                return metadata;
              }
            });
          if (scriptAttachment.length > 0) {
            $ctrl.deleteScriptAttachment(
              scriptAttachment[0], 0, false
            );
            $ctrl.loadScriptAttachments();
          }
        }

        $ctrl.scripts.splice(index, 1);
        // Remove content from md-autocomplete components
        $ctrl['languageSearchText_' + index] = '';
        $ctrl['selectedLanguage_' + index] = null;
        $ctrl['softwarePackageSearchText_' + index] = '';
        $ctrl['selectedSoftwarePackage_' + index] = null;
        $ctrl.currentForm.$setDirty();
      };

      $ctrl.addScript = function() {
        $ctrl.scripts.push({
          uuid: uuid.v4(),
          title: {
            de: '',
            en: ''
          },
          softwarePackage: '',
          softwarePackageVersion: '',
          usedLanguage: '',
          scriptAttachmentMetadata: {
            fileName: ''
          }
        });
      };

      $ctrl.moveScript = function(dir) {
        var move = {
          up: -1,
          down: +1
        };
        $ctrl.scripts
          .splice($ctrl.currentScriptIndex + parseInt(move[dir]), 0,
            $ctrl.scripts.splice($ctrl.currentScriptIndex, 1)[0]);
        $ctrl.currentScriptIndex = $ctrl.currentScriptIndex + parseInt(
          move[dir]
        );
        document.getElementsByName(
          $ctrl.currentFocusElement.name.split('_')[0] +
          '_' + $ctrl.currentScriptIndex)[0].focus();
        $ctrl.currentForm.$setDirty();
        $ctrl.$onInit();
      };

      $ctrl.searchLanguages = function(searchText) {
        if (!searchText || searchText === '') {
          return isoLanguagesArray;
        }
        var lowerCasedSearchText = searchText.toLowerCase();
        return _.filter(isoLanguagesArray, function(isoLanguage) {
          return isoLanguage.displayLanguage[$ctrl.currentLanguage]
            .toLowerCase().indexOf(lowerCasedSearchText) > -1;
        });
      };

      $ctrl.selectedLanguageChanged = function(index) {
        if ($ctrl['selectedLanguage_' + index]) {
          $ctrl.scripts[index]
            .usedLanguage = $ctrl['selectedLanguage_' + index].code;
        } else {
          delete $ctrl.scripts[index].usedLanguage;
        }
        if (!isInitialisingSelectedLanguage) {
          $ctrl.currentForm.$setDirty();
        }
        isInitialisingSelectedLanguage = false;
      };

      $ctrl.searchSoftwarePackages = function(searchText) {
        if (!searchText || searchText === '') {
          return softwarePackages;
        }
        var lowerCasedSearchText = searchText.toLowerCase();
        return _.filter(softwarePackages, function(softwarePackage) {
          return softwarePackage
            .toLowerCase().indexOf(lowerCasedSearchText) > -1;
        });
      };

      $ctrl.selectedSoftwarePackageChanged = function(index) {
        if ($ctrl['selectedSoftwarePackage_' + index]) {
          $ctrl.scripts[index].softwarePackage =
            $ctrl['selectedSoftwarePackage_' + index];
        } else {
          delete $ctrl.scripts[index].softwarePackage;
        }
        if (!isInitialisingSelectedLanguage) {
          $ctrl.currentForm.$setDirty();
        }
        isInitialisingSelectedSoftwarePackage = false;
      };

      $ctrl.loadScriptAttachments = function() {
        ScriptAttachmentResource.findByAnalysisPackageId({
          analysisPackageId: $ctrl.packageId
        }).$promise.then(
          function(attachments) {
            if (attachments.length > 0) {
              $ctrl.scriptAttachments = attachments;
              getScriptAttachmentMetadata();
            }
          });
      };
      $ctrl.addScriptAttachmentButton = function(index) {
        angular.element(
          document
            .querySelector('input[name="filename_' + index + '"]'))
          .click();
      };
      $ctrl.addScriptAttachment = function(file, index) {
        $ctrl.currentScriptIndex = index;
        var scriptAttachmentUpload = function(file) {
          var metadata = _.extend({}, {
            analysisPackageId: $ctrl.packageId,
            dataAcquisitionProjectId: $ctrl.projectId,
            scriptUuid: $ctrl.scripts[index].uuid,
            fileName: file.name
          });
          $ctrl.scripts[index].scriptAttachmentMetadata = metadata;
          return ScriptAttachmentUploadService
            .uploadScriptAttachment(file, metadata);
        };
        $ctrl.selectedFile = file;

        $ctrl.currentForm['filename_' + index].$setDirty();
        $ctrl.currentForm['filename_' + index]
          .$setValidity('valid', true);
        $ctrl.currentForm['filename_' + index]
          .$setValidity('unique', true);

        scriptAttachmentUpload($ctrl.selectedFile)
          .then($ctrl.onSavedSuccessfully)
          .catch($ctrl.onUploadFailed);
      };

      $ctrl.onSavedSuccessfully = function() {
        $mdDialog.hide($ctrl.scriptAttachmentMetadata);
        SimpleMessageToastService.openSimpleMessageToast(
          'attachment.attachment-saved-toast',
          {
            filename: $ctrl.selectedFile.name
          });
        $ctrl.loadScriptAttachments();
      };

      $ctrl.onUploadFailed = function(response) {
        if (response.invalidFile) {
          SimpleMessageToastService.openAlertMessageToast(
            'attachment.error.file-not-found',
            {
              filename: $ctrl.selectedFile.name
            });
          $ctrl.currentForm['filename_' + $ctrl.currentScriptIndex]
            .$setValidity('valid', false);
        }
        if (response.errors && response.errors.length > 0) {
          SimpleMessageToastService.openAlertMessageToast(
            response.errors[0].message,
            {
              filename: $ctrl.selectedFile.name
            });
          delete $ctrl.scripts[$ctrl.currentScriptIndex]
            .scriptAttachmentMetadata;
          $ctrl.currentForm['filename_' + $ctrl.currentScriptIndex]
            .$setValidity('valid', true);
        }
      };

      $ctrl.deleteScriptAttachment = function(attachment, index, dialog) {
        if (dialog) {
          CommonDialogsService
            .showConfirmFileDeletionDialog(attachment.fileName)
            .then(function() {
              attachment.$delete().then(function() {
                SimpleMessageToastService.openSimpleMessageToast(
                  'analysis-package-management.detail.' +
                  'script-attachment-metadata.attachment-deleted-toast', {
                    filename: attachment.fileName
                  }
                );
                $ctrl.scriptAttachments.splice(index, 1);
                delete $ctrl.scripts[index].scriptAttachmentMetadata;
                $ctrl.loadScriptAttachments();
              });
            });
        } else {
          attachment.$delete();
          $ctrl.scriptAttachments.splice(index, 1);
          delete $ctrl.scripts[index].scriptAttachmentMetadata;
          $ctrl.loadScriptAttachments();
        }
      };

      $scope.$watch(function() {
        return document.activeElement;
      }, function(value) {
        if ($ctrl.scriptElement.contains(value) && formElements
          .indexOf(value.tagName) > -1) {
          $ctrl.focus = true;
          $ctrl.currentFocusElement = value;
          $ctrl.currentScriptIndex = parseInt(value.name.slice(-1));
        } else {
          $ctrl.focus = false;
        }
      });
    }

    angular
      .module('metadatamanagementApp')
      .controller('editScriptSectionController', Controller);
  }

)
();
