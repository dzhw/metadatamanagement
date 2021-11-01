/* globals _ */
(function() {
    'use strict';

    function Controller(
      uuid,
      isoLanguages,
      LanguageService,
      CommonDialogsService,
      SimpleMessageToastService,
      ScriptSoftwarePackagesResource,
      ScriptAttachmentDialogService,
      ScriptAttachmentUploadService,
      ScriptAttachmentResource
    ) {
      var $ctrl = this;
      var isInitialisingSelectedLanguage = false;
      var isInitialisingSelectedSoftwarePackage = false;
      var softwarePackages = [];

      $ctrl.currentLanguage = LanguageService.getCurrentInstantly();
      $ctrl.selectedFile = {};

      var isoLanguagesArray = Object.keys(isoLanguages)
        .map(function(key) {
          return {
            code: key,
            'displayLanguage': isoLanguages[key]
          };
        });

      var getDialogLabels = function(index) {
        return {
          createTitle: {
            key: 'analysis-package-management.detail' +
              '.script-attachments.create-title',
            params: {
              scriptTitle: $ctrl.scripts[index].title[$ctrl.currentLanguage]
            }
          }
        };
      };

      var initSelectedLanguages = function() {
        isInitialisingSelectedLanguage = true;
        _.forEach($ctrl.scripts, function(value, index) {
          var result = _.filter(isoLanguagesArray,
            function(isoLanguage) {
              return isoLanguage.code === value.usedLanguage;
            });
          $ctrl['selectedLanguage_' + index] = result[0]
            .displayLanguage[$ctrl.currentLanguage];
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

      $ctrl.$onInit = function() {
        $ctrl.scripts = $ctrl.scripts || [];
        $ctrl.attachments = $ctrl.attachments || [];
        $ctrl.currentLanguage = LanguageService.getCurrentInstantly();
        initSelectedLanguages();
        getSoftwarePackages();
        initSelectedSoftwarePackages();
        $ctrl.loadScriptAttachments();
      };

      $ctrl.deleteScript = function(index, uuid) {
        if ($ctrl.scriptAttachments.length > 0) {
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
          usedLanguage: ''
        });
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
            }
          });
      };

      $ctrl.addScriptAttachment = function(index, event) {
        var scriptAttachmentUpload = function(file, scriptAttachmentMetadata) {
          var metadata = _.extend({}, scriptAttachmentMetadata, {
            analysisPackageId: $ctrl.packageId,
            dataAcquisitionProjectId: $ctrl.projectId,
            scriptUuid: $ctrl.scripts[index].uuid,
            fileName: file.name
          });
          return ScriptAttachmentUploadService
            .uploadScriptAttachment(file, metadata);
        };

        var dialogConfig = {
          scriptAttachmentMetadata: null,
          uploadCallback: scriptAttachmentUpload,
          scriptTitle: $ctrl.scripts[index].title,
          labels: getDialogLabels(index)
        };

        ScriptAttachmentDialogService
          .showDialog(dialogConfig, event)
          .then(function() {
            $ctrl.loadScriptAttachments();
          });
      };

      $ctrl.deleteScriptAttachment = function(attachment, index, dialog) {
        if (dialog) {
          CommonDialogsService
            .showConfirmFileDeletionDialog(attachment.fileName)
            .then(function() {
              attachment.$delete().then(function() {
                SimpleMessageToastService.openSimpleMessageToast(
                  'analysis-package-management.detail.' +
                  'script-attachments.attachment-deleted-toast', {
                    filename: attachment.fileName
                  }
                );
                $ctrl.scriptAttachments.splice(index, 1);
              });
            });
        } else {
          attachment.$delete();
          $ctrl.scriptAttachments.splice(index, 1);
        }
      };
    }

    angular
      .module('metadatamanagementApp')
      .controller('editScriptSectionController', Controller);
  }

)
();
