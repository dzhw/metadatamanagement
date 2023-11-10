/* global _, $, document */
'use strict';

angular.module('metadatamanagementApp')
  .controller('DataPackageEditOrCreateController', [
  'entity',
  'PageMetadataService',
  '$document',
  '$timeout',
  '$state',
  'BreadcrumbService',
  'Principal',
  'SimpleMessageToastService',
  'CurrentProjectService',
  'DataPackageIdBuilderService',
  'DataPackageResource',
  '$scope',
  'ElasticSearchAdminService',
  '$transitions',
  'CommonDialogsService',
  'LanguageService',
  'DataPackageSearchService',
  'DataPackageAttachmentResource',
  '$q',
  'CleanJSObjectService',
  'DataAcquisitionProjectResource',
  'ProjectUpdateAccessService',
  'AttachmentDialogService',
  'DataPackageAttachmentUploadService',
  'DataPackageAttachmentVersionsResource',
  'ChoosePreviousVersionService',
  'DataPackageVersionsResource',
    function(entity, PageMetadataService, $document, $timeout,
      $state, BreadcrumbService, Principal, SimpleMessageToastService,
      CurrentProjectService, DataPackageIdBuilderService, DataPackageResource,
      $scope, ElasticSearchAdminService, $transitions,
      CommonDialogsService, LanguageService, DataPackageSearchService,
      DataPackageAttachmentResource, $q, CleanJSObjectService,
      DataAcquisitionProjectResource, ProjectUpdateAccessService,
      AttachmentDialogService, DataPackageAttachmentUploadService,
      DataPackageAttachmentVersionsResource, ChoosePreviousVersionService,
      DataPackageVersionsResource) {

      var ctrl = this;
      var studySeriesCache = {};
      ctrl.currentInstitutions = [];
      ctrl.currentSponsors = [];
      var attachmentTypes = [
        {de: 'Daten- und Methodenbericht', en: 'Method Report'},
        {de: 'Release Notes', en: 'Release Notes'},
        {de: 'Sonstiges', en: 'Other'}
      ];

      var getDialogLabels = function() {
        return {
          createTitle: {
            key: 'data-package-management.detail.attachments.create-title',
            params: {
              dataPackageId: ctrl.dataPackage.id
            }
          },
          editTitle: {
            key: 'data-package-management.detail.attachments.edit-title',
            params: {
              dataPackageId: ctrl.dataPackage.id
            }
          },
          hints: {
            file: {
              key: 'data-package-management.detail.attachments.hints.filename'
            }
          }
        };
      };

      ctrl.translationKeysProjectContributers = {
        title: 'data-package-management.detail.label.projectContributors',
        tooltips: {
          delete: 'data-package-management.edit.delete-contributor-tooltip',
          add: 'data-package-management.edit.add-contributor-tooltip',
          moveUp: 'data-package-management.edit.move-contributor-up-tooltip',
          moveDown: 'data-package-management.edit.move-contributor-down-tooltip'
        },
        hints: {
          firstName: 'data-package-management.edit.hints.' +
            'project-contributors.first-name',
          middleName: 'data-package-management.edit.hints.' +
            'project-contributors.middle-name',
          lastName: 'data-package-management.edit.hints.' +
            'project-contributors.last-name'
        }
      };

      ctrl.translationKeysDataCurators = {
        title: 'data-package-management.detail.label.data-curators',
        tooltips: {
          delete: 'data-package-management.edit.delete-curator-tooltip',
          add: 'data-package-management.edit.add-curator-tooltip',
          moveUp: 'data-package-management.edit.move-curator-up-tooltip',
          moveDown: 'data-package-management.edit.move-curator-down-tooltip'
        },
        hints: {
          firstName: 'data-package-management.edit.hints' +
            '.curators.first-name',
          middleName: 'data-package-management.edit.hints' +
            '.curators.middle-name',
          lastName: 'data-package-management.edit.hints.curators.last-name'
        }
      };

      ctrl.findTags = DataPackageSearchService.findTags;

      $scope.$watch('ctrl.dataPackage.studySeries', function() {
        ctrl.onStudySeriesChanged();
      }, true);

      var updateToolbarHeaderAndPageTitle = function() {
        if (ctrl.createMode) {
          PageMetadataService.setPageTitle(
            'data-package-management.edit.create-page-title', {
              dataPackageId: ctrl.dataPackage.id
            });
        } else {
          PageMetadataService.setPageTitle(
            'data-package-management.edit.edit-page-title', {
              dataPackageId: ctrl.dataPackage.id
            });
        }
        BreadcrumbService.updateToolbarHeader({
          'stateName': $state.current.name,
          'id': ctrl.dataPackage.id,
          'dataPackageId': ctrl.dataPackage.id,
          'dataPackageIsPresent': !ctrl.createMode,
          'projectId': ctrl.dataPackage.dataAcquisitionProjectId,
          'enableLastItem': true
        });
      };

      var redirectToSearchView = function() {
        $timeout(function() {
          $state.go('search', {
            lang: LanguageService.getCurrentInstantly(),
            type: 'data_packages'
          });
        }, 1000);
      };

      var handleReleasedProject = function() {
        SimpleMessageToastService.openAlertMessageToast(
          'data-package-management.edit.choose-unreleased-project-toast');
        redirectToSearchView();
      };

      var initEditMode = function(dataPackage) {
        ctrl.createMode = false;
        ctrl.isInitializingStudySeries = true;
        DataAcquisitionProjectResource.get({
          id: dataPackage.dataAcquisitionProjectId
        }).$promise.then(function(project) {
          if (project.release != null) {
            handleReleasedProject();
          } else if (!ProjectUpdateAccessService
              .isUpdateAllowed(project, 'dataPackages', true)) {
            redirectToSearchView();
          } else {
            CurrentProjectService.setCurrentProject(project);
            ctrl.dataPackage = dataPackage;
            ctrl.currentApprovedUsage = dataPackage.approvedUsage;
            ctrl.currentStudySeries = dataPackage.studySeries;
            ctrl.remarksUserService = dataPackage.remarksUserService;
            ctrl.currentSponsors = angular.copy(
              ctrl.dataPackage.sponsors);
            ctrl.currentInstitutions = angular.copy(
              ctrl.dataPackage.institutions);
            ctrl.loadAttachments();
            updateToolbarHeaderAndPageTitle();
            $scope.registerConfirmOnDirtyHook();
          }
        });
      };

      var init = function() {
        if (Principal
            .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER'])) {
          if (entity) {
            entity.$promise.then(function(dataPackage) {
              initEditMode(dataPackage);
            });
          } else {
            if (CurrentProjectService.getCurrentProject() &&
              !CurrentProjectService.getCurrentProject().release) {
              if (!ProjectUpdateAccessService
                   .isUpdateAllowed(CurrentProjectService.getCurrentProject(),
                    'dataPackages', true)) {
                redirectToSearchView();
              } else {
                DataPackageResource.get({
                  id: DataPackageIdBuilderService.buildDataPackageId(
                    CurrentProjectService.getCurrentProject().id)
                }).$promise.then(function(dataPackage) {
                  initEditMode(dataPackage);
                }).catch(function() {
                    ctrl.isInitializingStudySeries = true;
                    ctrl.createMode = true;
                    ctrl.dataPackage = new DataPackageResource({
                      id: DataPackageIdBuilderService.buildDataPackageId(
                        CurrentProjectService.getCurrentProject().id),
                      dataAcquisitionProjectId: CurrentProjectService
                      .getCurrentProject()
                      .id,
                      projectContributors: [{
                        firstName: '',
                        lastName: ''
                      }],
                      dataCurators: [{
                        firstName: '',
                        lastName: ''
                      }],
                      institutions: [{
                        de: '',
                        en: ''
                      }],
                      sponsors: [{
                        name: {
                          de: '',
                          en: ''
                        }
                      }]
                    });
                    ctrl.currentInstitutions = new Array(1);
                    ctrl.currentSponsors = new Array(1);
                    updateToolbarHeaderAndPageTitle();
                    $scope.registerConfirmOnDirtyHook();
                  });
              }
            } else {
              handleReleasedProject();
            }
          }
        } else {
          SimpleMessageToastService.openAlertMessageToast(
            'data-package-management.edit.not-authorized-toast');
        }
      };

      ctrl.surveyDesigns = [{
        de: 'Panel',
        en: 'Panel'
      }, {
        de: 'Querschnitt',
        en: 'Cross-Section'
      }];

      ctrl.deleteInstitution = function(index) {
        ctrl.dataPackage.institutions.splice(index, 1);
        ctrl.currentInstitutions.splice(index, 1);
        $scope.dataPackageForm.$setDirty();
      };

      ctrl.addInstitution = function() {
        ctrl.currentInstitutions.push(null);
        $timeout(function() {
          $document.find('input[name="institutionDe_' +
              (ctrl.dataPackage.institutions.length - 1) + '"]')
            .focus();
        }, 200);
      };

      ctrl.setCurrentInstitution = function(index, event) {
        ctrl.currentInstitutionInputName = event.target.name;
        ctrl.currentInstitutionIndex = index;
      };

      var timeoutActive = null;
      ctrl.deleteCurrentInstitution = function(event) {
        if (timeoutActive) {
          $timeout.cancel(timeoutActive);
        }
        timeoutActive = $timeout(function() {
          timeoutActive = false;
          // msie workaround: inputs unfocus on button mousedown
          if (document.activeElement &&
            (document.activeElement.tagName === 'BODY' ||
            $(document.activeElement).parents('#move-institution-container')
              .length)) {
            return;
          }
          if (event.relatedTarget && (
            event.relatedTarget.id === 'move-institution-up-button' ||
            event.relatedTarget.id === 'move-institution-down-button')) {
            return;
          }
          delete ctrl.currentInstitutionIndex;
          timeoutActive = null;
        });
      };

      ctrl.moveCurrentInstitutionUp = function() {
        var a = ctrl.dataPackage.institutions[ctrl.currentInstitutionIndex - 1];
        ctrl.dataPackage.institutions[ctrl.currentInstitutionIndex - 1] =
          ctrl.dataPackage.institutions[ctrl.currentInstitutionIndex];
        ctrl.dataPackage.institutions[ctrl.currentInstitutionIndex] = a;
        a = ctrl.currentInstitutions[ctrl.currentInstitutionIndex - 1];
        ctrl.currentInstitutions[ctrl.currentInstitutionIndex - 1] =
          ctrl.currentInstitutions[ctrl.currentInstitutionIndex];
        ctrl.currentInstitutions[ctrl.currentInstitutionIndex] = a;
        ctrl.currentInstitutionInputName = ctrl.currentInstitutionInputName
          .replace('_' + ctrl.currentInstitutionIndex,
            '_' + (ctrl.currentInstitutionIndex - 1));
        $document.find('input[name="' + ctrl.currentInstitutionInputName + '"]')
          .focus();
        $scope.dataPackageForm.$setDirty();
      };

      ctrl.moveCurrentInstitutionDown = function() {
        var a = ctrl.dataPackage.institutions[ctrl.currentInstitutionIndex + 1];
        ctrl.dataPackage.institutions[ctrl.currentInstitutionIndex + 1] =
          ctrl.dataPackage.institutions[ctrl.currentInstitutionIndex];
        ctrl.dataPackage.institutions[ctrl.currentInstitutionIndex] = a;
        a = ctrl.currentInstitutions[ctrl.currentInstitutionIndex + 1];
        ctrl.currentInstitutions[ctrl.currentInstitutionIndex + 1] =
          ctrl.currentInstitutions[ctrl.currentInstitutionIndex];
        ctrl.currentInstitutions[ctrl.currentInstitutionIndex] = a;
        ctrl.currentInstitutionInputName = ctrl.currentInstitutionInputName
          .replace('_' + ctrl.currentInstitutionIndex,
            '_' + (ctrl.currentInstitutionIndex + 1));
        $document.find('input[name="' + ctrl.currentInstitutionInputName + '"]')
          .focus();
        $scope.dataPackageForm.$setDirty();
      };

      ctrl.deleteSponsor = function(index) {
        ctrl.dataPackage.sponsors.splice(index, 1);
        ctrl.currentSponsors.splice(index, 1);
        $scope.dataPackageForm.$setDirty();
      };

      ctrl.addSponsor = function() {
        ctrl.currentSponsors.push(null);
        $timeout(function() {
          $document.find('input[name="sponsorDe_' +
              (ctrl.dataPackage.sponsors.length - 1) + '"]')
            .focus();
        }, 200);
      };

      ctrl.setCurrentSponsor = function(index, event) {
        ctrl.currentSponsorInputName = event.target.name;
        ctrl.currentSponsorIndex = index;
      };

      ctrl.deleteCurrentSponsor = function(event) {
        if (timeoutActive) {
          $timeout.cancel(timeoutActive);
        }
        timeoutActive = $timeout(function() {
          timeoutActive = false;
          // msie workaround: inputs unfocus on button mousedown
          if (document.activeElement &&
            (document.activeElement.tagName === 'BODY' ||
            $(document.activeElement).parents('#move-sponsor-container')
              .length)) {
            return;
          }
          if (event.relatedTarget && (
            event.relatedTarget.id === 'move-sponsor-up-button' ||
            event.relatedTarget.id === 'move-sponsor-down-button')) {
            return;
          }
          delete ctrl.currentSponsorIndex;
          timeoutActive = null;
        });
      };

      ctrl.moveCurrentSponsorUp = function() {
        var a = ctrl.dataPackage.sponsors[ctrl.currentSponsorIndex - 1];
        ctrl.dataPackage.sponsors[ctrl.currentSponsorIndex - 1] =
          ctrl.dataPackage.sponsors[ctrl.currentSponsorIndex];
        ctrl.dataPackage.sponsors[ctrl.currentSponsorIndex] = a;
        a = ctrl.currentSponsors[ctrl.currentSponsorIndex - 1];
        ctrl.currentSponsors[ctrl.currentSponsorIndex - 1] =
          ctrl.currentSponsors[ctrl.currentSponsorIndex];
        ctrl.currentSponsors[ctrl.currentSponsorIndex] = a;
        ctrl.currentSponsorInputName = ctrl.currentSponsorInputName
          .replace('_' + ctrl.currentSponsorIndex,
            '_' + (ctrl.currentSponsorIndex - 1));
        $document.find('input[name="' + ctrl.currentSponsorInputName + '"]')
          .focus();
        $scope.dataPackageForm.$setDirty();
      };

      ctrl.moveCurrentSponsorDown = function() {
        var a = ctrl.dataPackage.sponsors[ctrl.currentSponsorIndex + 1];
        ctrl.dataPackage.sponsors[ctrl.currentSponsorIndex + 1] =
          ctrl.dataPackage.sponsors[ctrl.currentSponsorIndex];
        ctrl.dataPackage.sponsors[ctrl.currentSponsorIndex] = a;
        a = ctrl.currentSponsors[ctrl.currentSponsorIndex + 1];
        ctrl.currentSponsors[ctrl.currentSponsorIndex + 1] =
          ctrl.currentSponsors[ctrl.currentSponsorIndex];
        ctrl.currentSponsors[ctrl.currentSponsorIndex] = a;
        ctrl.currentSponsorInputName = ctrl.currentSponsorInputName
          .replace('_' + ctrl.currentSponsorIndex,
            '_' + (ctrl.currentSponsorIndex + 1));
        $document.find('input[name="' + ctrl.currentSponsorInputName + '"]')
          .focus();
        $scope.dataPackageForm.$setDirty();
      };

      ctrl.deleteLink = function(index) {
        ctrl.dataPackage.additionalLinks.splice(index, 1);
        $scope.dataPackageForm.$setDirty();
      };

      ctrl.addLink = function() {
        if (!ctrl.dataPackage.additionalLinks) {
          ctrl.dataPackage.additionalLinks = [];
        }
        ctrl.dataPackage.additionalLinks.push({
          url: '',
          displayText: {
            de: '',
            en: ''
          }
        });
        $timeout(function() {
          $document.find('input[name="linkUrl_' +
              (ctrl.dataPackage.additionalLinks.length - 1) + '"]')
            .focus();
        }, 200);
      };

      ctrl.setCurrentLink = function(index, event) {
        ctrl.currentLinkInputName = event.target.name;
        ctrl.currentLinkIndex = index;
      };

      ctrl.deleteCurrentLink = function(index, event) {
        if (document.activeElement &&
          $(document.activeElement).parents('#link-' + index)
            .length) {
          return;
        }
        if (event.relatedTarget && (
            event.relatedTarget.id === 'move-link-up-button' ||
            event.relatedTarget.id === 'move-link-down-button')) {
          return;
        }
        delete ctrl.currentLinkIndex;
      };

      ctrl.moveCurrentLinkUp = function() {
        var a = ctrl.dataPackage.additionalLinks[ctrl.currentLinkIndex - 1];
        ctrl.dataPackage.additionalLinks[ctrl.currentLinkIndex - 1] =
          ctrl.dataPackage.additionalLinks[ctrl.currentLinkIndex];
        ctrl.dataPackage.additionalLinks[ctrl.currentLinkIndex] = a;
        ctrl.currentLinkInputName = ctrl.currentLinkInputName
          .replace('_' + ctrl.currentLinkIndex,
            '_' + (ctrl.currentLinkIndex - 1));
        $document.find('input[name="' + ctrl.currentLinkInputName + '"]')
          .focus();
        $scope.dataPackageForm.$setDirty();
      };

      ctrl.moveCurrentLinkDown = function() {
        var a = ctrl.dataPackage.additionalLinks[ctrl.currentLinkIndex + 1];
        ctrl.dataPackage.additionalLinks[ctrl.currentLinkIndex + 1] =
          ctrl.dataPackage.additionalLinks[ctrl.currentLinkIndex];
        ctrl.dataPackage.additionalLinks[ctrl.currentLinkIndex] = a;
        ctrl.currentLinkInputName = ctrl.currentLinkInputName
          .replace('_' + ctrl.currentLinkIndex,
            '_' + (ctrl.currentLinkIndex + 1));
        $document.find('input[name="' + ctrl.currentLinkInputName + '"]')
          .focus();
        $scope.dataPackageForm.$setDirty();
      };

      ctrl.saveDataPackage = function() {
        if ($scope.dataPackageForm.$valid) {
          if (angular.isUndefined(ctrl.dataPackage.masterId)) {
            ctrl.dataPackage.masterId = ctrl.dataPackage.id;
          }
          ctrl.dataPackage.$save()
            .then(ctrl.updateElasticSearchIndex)
            .then(ctrl.onSavedSuccessfully)
            .catch(function() {
              SimpleMessageToastService.openAlertMessageToast(
                'data-package-management.edit.error-on-save-toast', {
                  dataPackageId: ctrl.dataPackage.id
                });
            });
        } else {
          // ensure that all validation errors are visible
          angular.forEach($scope.dataPackageForm.$error, function(field) {
            angular.forEach(field, function(errorField) {
              if (errorField.$setTouched) {
                errorField.$setTouched();
              } else if (errorField.$setDirty) {
                errorField.$setDirty();
              }
            });
          });
          SimpleMessageToastService.openAlertMessageToast(
            'data-package-management.edit.' +
            'data-package-has-validation-errors-toast',
            null);
        }
      };

      ctrl.updateElasticSearchIndex = function() {
        return ElasticSearchAdminService.processUpdateQueue('data_packages');
      };

      ctrl.onSavedSuccessfully = function() {
        $scope.dataPackageForm.$setPristine();
        SimpleMessageToastService.openSimpleMessageToast(
          'data-package-management.edit.success-on-save-toast', {
            dataPackageId: ctrl.dataPackage.id
          });
        if (ctrl.createMode) {
          $state.go('dataPackageEdit', {
            id: ctrl.dataPackage.id
          });
        }
      };

      ctrl.openRestorePreviousVersionDialog = function(event) {
        var getVersions = function(id, limit, skip) {
          return DataPackageVersionsResource.get({
            id: id,
            limit: limit,
            skip: skip
          }).$promise;
        };

        var dialogConfig = {
          domainId: ctrl.dataPackage.id,
          getPreviousVersionsCallback: getVersions,
          labels: {
            title: {
              key: 'data-package-management.edit.choose-previous-version.title',
              params: {
                dataPackageId: ctrl.dataPackage.id
              }
            },
            text: {
              key: 'data-package-management.edit.choose-previous-version.text'
            },
            cancelTooltip: {
              key: 'data-package-management.edit.choose-previous-version.' +
                  'cancel-tooltip'
            },
            noVersionsFound: {
              key: 'data-package-management.edit.choose-previous-version.' +
                  'no-versions-found',
              params: {
                dataPackageId: ctrl.dataPackage.id
              }
            },
            deleted: {
              key: 'data-package-management.edit.' +
                'choose-previous-version.data-package-deleted'
            }
          }
        };

        ChoosePreviousVersionService.showDialog(dialogConfig, event)
          .then(function(wrapper) {
            ctrl.dataPackage = new DataPackageResource(wrapper.selection);
            if (!ctrl.dataPackage.projectContributors) {
              ctrl.dataPackage.projectContributors = [{
                firstName: '',
                lastName: ''
              }];
            }
            if (!ctrl.dataPackage.dataCurators) {
              ctrl.dataPackage.dataCurators = [{
                firstName: '',
                lastName: ''
              }];
            }
            if (ctrl.dataPackage.institutions &&
                ctrl.dataPackage.institutions.length > 0) {
              ctrl.currentInstitutions = angular.copy(
                ctrl.dataPackage.institutions);
            } else {
              ctrl.currentInstitutions = new Array(1);
              ctrl.dataPackage.institutions = [{
                de: '',
                en: ''
              }];
            }
            if (ctrl.dataPackage.sponsors &&
                ctrl.dataPackage.sponsors.length > 0) {
              ctrl.currentSponsors = angular.copy(
                ctrl.dataPackage.sponsors);
            } else {
              ctrl.currentSponsors = new Array(1);
              ctrl.dataPackage.sponsors = [{
                name: {
                  de: '',
                  en: ''
                }
              }];
            }
            if (wrapper.isCurrentVersion) {
              $scope.dataPackageForm.$setPristine();
              SimpleMessageToastService.openSimpleMessageToast(
                  'data-package-management.edit.' +
                  'current-version-restored-toast', {
                    dataPackageId: ctrl.dataPackage.id
                  });
            } else {
              $scope.dataPackageForm.$setDirty();
              SimpleMessageToastService.openSimpleMessageToast(
                  'data-package-management.edit.' +
                  'previous-version-restored-toast', {
                    dataPackageId: ctrl.dataPackage.id
                  });
            }
          });
      };

      $scope.registerConfirmOnDirtyHook = function() {
        var unregisterTransitionHook = $transitions.onBefore({}, function() {
          if ($scope.dataPackageForm.$dirty || ctrl.attachmentOrderIsDirty) {
            return CommonDialogsService.showConfirmOnDirtyDialog();
          }
        });

        $scope.$on('$destroy', unregisterTransitionHook);
      };

      $scope.searchStudySeries = function(searchText, language) {
        if (searchText === studySeriesCache.searchText &&
          language === studySeriesCache.language) {
          return studySeriesCache.searchResult;
        }

        //Search Call to Elasticsearch
        return DataPackageSearchService.findStudySeries(searchText, {},
            language, null, null, null, true)
          .then(function(studySeries) {
            studySeriesCache.searchText = searchText;
            studySeriesCache.language = language;
            studySeriesCache.searchResult = studySeries;
            return studySeries;
          });
      };

      $scope.searchApprovedUsage = function(searchText) {
        //Search Call to Elasticsearch
        return DataPackageSearchService.findApprovedUsage(searchText, {},
            true)
          .then(function(approvedUsage) {
            return approvedUsage;
          });
      };

      $scope.searchSponsors = function(searchText, language) {
        //Search Call to Elasticsearch
        console.log('CURRENTSPONSORS: ' + JSON.stringify(ctrl.currentSponsors));
        return DataPackageSearchService.findSponsors(searchText, {},
            language, true, ctrl.currentSponsors)
          .then(function(sponsors) {
            return sponsors;
          });
      };

      $scope.searchInstitutions = function(searchText, language) {
        //Search Call to Elasticsearch
        return DataPackageSearchService.findInstitutions(searchText, {},
            language, true, ctrl.currentInstitutions)
          .then(function(institutions) {
            return institutions;
          });
      };

      ctrl.loadAttachments = function(selectLastAttachment) {
        DataPackageAttachmentResource.findByDataPackageId({
          dataPackageId: ctrl.dataPackage.id
        }).$promise.then(
          function(attachments) {
            if (attachments.length > 0) {
              ctrl.attachments = attachments;
              if (selectLastAttachment) {
                ctrl.currentAttachmentIndex = attachments.length - 1;
              }
            }
          });
      };

      ctrl.deleteAttachment = function(attachment, index) {
        CommonDialogsService.showConfirmFileDeletionDialog(attachment.fileName)
          .then(function() {
            attachment.$delete().then(function() {
              SimpleMessageToastService.openSimpleMessageToast(
                'data-package-management.detail.' +
                'attachments.attachment-deleted-toast', {
                  filename: attachment.fileName
                }
              );
              ctrl.attachments.splice(index, 1);
              delete ctrl.currentAttachmentIndex;
            });
          });
      };

      ctrl.editAttachment = function(attachment, event) {

        var upload = function(file, newAttachmentMetadata) {
          var metadata = _.extend(attachment, newAttachmentMetadata);
          return DataPackageAttachmentUploadService.uploadAttachment(file,
            metadata);
        };

        var labels = getDialogLabels();
        labels.editTitle.params.filename = attachment.fileName;

        var getAttachmentVersions = function(id, filename, limit, skip) {
          return DataPackageAttachmentVersionsResource.get({
                dataPackageId: id,
                filename: filename,
                limit: limit,
                skip: skip
              }).$promise;
        };

        var createDataPackageAttachmentResource = function(attachmentWrapper) {
          return new DataPackageAttachmentResource(
            attachmentWrapper.dataPackageAttachment);
        };

        var dialogConfig = {
          attachmentMetadata: attachment,
          attachmentTypes: attachmentTypes,
          uploadCallback: upload,
          attachmentDomainIdAttribute: 'dataPackageId',
          getAttachmentVersionsCallback: getAttachmentVersions,
          createAttachmentResource: createDataPackageAttachmentResource,
          dataPackageTitle: ctrl.dataPackage.title,
          labels: labels
        };

        AttachmentDialogService.showDialog(dialogConfig, event)
          .then(ctrl.loadAttachments);
      };

      ctrl.getNextIndexInDataPackage = function() {
        if (!ctrl.attachments || ctrl.attachments.length === 0) {
          return 0;
        }
        return _.maxBy(ctrl.attachments, function(attachment) {
          return attachment.indexInDataPackage;
        }).indexInDataPackage + 1;
      };

      ctrl.addAttachment = function(event) {

        var upload = function(file, attachmentMetadata) {
          var metadata = _.extend({}, attachmentMetadata, {
            dataPackageId: ctrl.dataPackage.id,
            dataAcquisitionProjectId: ctrl.dataPackage.dataAcquisitionProjectId,
            indexInDataPackage: ctrl.getNextIndexInDataPackage()
          });
          return DataPackageAttachmentUploadService.uploadAttachment(
            file, metadata);
        };

        var dialogConfig = {
          attachmentMetadata: null,
          attachmentTypes: attachmentTypes,
          uploadCallback: upload,
          dataPackageTitle: ctrl.dataPackage.title,
          labels: getDialogLabels()
        };

        AttachmentDialogService
          .showDialog(dialogConfig, event)
          .then(function() {
            ctrl.loadAttachments(true);
          });
      };

      ctrl.moveAttachmentUp = function() {
        var a = ctrl.attachments[ctrl.currentAttachmentIndex - 1];
        ctrl.attachments[ctrl.currentAttachmentIndex - 1] =
          ctrl.attachments[ctrl.currentAttachmentIndex];
        ctrl.attachments[ctrl.currentAttachmentIndex] = a;
        ctrl.currentAttachmentIndex--;
        ctrl.attachmentOrderIsDirty = true;
      };

      ctrl.moveAttachmentDown = function() {
        var a = ctrl.attachments[ctrl.currentAttachmentIndex + 1];
        ctrl.attachments[ctrl.currentAttachmentIndex + 1] =
          ctrl.attachments[ctrl.currentAttachmentIndex];
        ctrl.attachments[ctrl.currentAttachmentIndex] = a;
        ctrl.currentAttachmentIndex++;
        ctrl.attachmentOrderIsDirty = true;
      };

      ctrl.saveAttachmentOrder = function() {
        var promises = [];
        ctrl.attachments.forEach(function(attachment, index) {
          attachment.indexInDataPackage = index;
          promises.push(attachment.$save());
        });
        $q.all(promises).then(function() {
          SimpleMessageToastService.openSimpleMessageToast(
            'data-package-management.detail.attachments.' +
            'attachment-order-saved-toast',
            {});
          ctrl.attachmentOrderIsDirty = false;
        });
      };

      ctrl.selectAttachment = function(index) {
        if (Principal
            .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER'])) {
          ctrl.currentAttachmentIndex = index;
        }
      };

      ctrl.onStudySeriesChanged = function() {
        //The fields of study series are undefined
        //at the moment of the first initial Call
        if (!$scope.dataPackageForm.studySeriesDe ||
            !$scope.dataPackageForm.studySeriesEn ||
            !ctrl.dataPackage ||
            !ctrl.dataPackage.studySeries) {
          return;
        }

        if (CleanJSObjectService
          .isNullOrEmpty(ctrl.dataPackage.studySeries.de) &&
          !CleanJSObjectService
          .isNullOrEmpty(ctrl.dataPackage.studySeries.en)) {
          $scope.dataPackageForm.studySeriesDe.$setValidity(
            'fdz-required', false);
        }

        if (CleanJSObjectService
          .isNullOrEmpty(ctrl.dataPackage.studySeries.en) &&
          !CleanJSObjectService
          .isNullOrEmpty(ctrl.dataPackage.studySeries.de)) {
          $scope.dataPackageForm.studySeriesEn.$setValidity(
            'fdz-required', false);
        }

        if ((CleanJSObjectService
            .isNullOrEmpty(ctrl.dataPackage.studySeries.de) &&
            CleanJSObjectService
            .isNullOrEmpty(ctrl.dataPackage.studySeries.en)) ||
          (!CleanJSObjectService
            .isNullOrEmpty(ctrl.dataPackage.studySeries.de) &&
            !CleanJSObjectService
            .isNullOrEmpty(ctrl.dataPackage.studySeries.en))) {
          $scope.dataPackageForm.studySeriesDe.$setValidity(
            'fdz-required', true);
          $scope.dataPackageForm.studySeriesEn.$setValidity(
            'fdz-required', true);
        }

        if (!ctrl.isInitializingStudySeries) {
          $scope.dataPackageForm.$setDirty();
        } else {
          ctrl.isInitializingStudySeries = false;
        }
      };

      init();
    }]);

