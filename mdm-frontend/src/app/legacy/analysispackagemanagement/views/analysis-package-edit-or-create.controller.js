/* global _, $, document */

'use strict';

angular.module('metadatamanagementApp')
  .controller('AnalysisPackageEditOrCreateController', [
  'entity',
  'PageMetadataService',
  '$document',
  '$timeout',
  '$state',
  'BreadcrumbService',
  'Principal',
  'SimpleMessageToastService',
  'CurrentProjectService',
  'AnalysisPackageIdBuilderService',
  'AnalysisPackageResource',
  '$scope',
  'ElasticSearchAdminService',
  '$transitions',
  'CommonDialogsService',
  'LanguageService',
  'AnalysisPackageSearchService',
  'AnalysisPackageAttachmentResource',
  '$q',
  'DataAcquisitionProjectResource',
  'ProjectUpdateAccessService',
  'AttachmentDialogService',
  'AnalysisPackageAttachmentUploadService',
  'AnalysisPackageAttachmentVersionsResource',
  'ChoosePreviousVersionService',
  'AnalysisPackageVersionsResource',
    function(entity, PageMetadataService, $document, $timeout,
             $state, BreadcrumbService, Principal, SimpleMessageToastService,
             CurrentProjectService, AnalysisPackageIdBuilderService,
             AnalysisPackageResource,
             $scope, ElasticSearchAdminService, $transitions,
             CommonDialogsService, LanguageService,
             AnalysisPackageSearchService, AnalysisPackageAttachmentResource,
             $q, DataAcquisitionProjectResource,
             ProjectUpdateAccessService,
             AttachmentDialogService, AnalysisPackageAttachmentUploadService,
             AnalysisPackageAttachmentVersionsResource,
             ChoosePreviousVersionService, AnalysisPackageVersionsResource) {

      var ctrl = this;
      ctrl.currentInstitutions = [];
      ctrl.currentSponsors = [];

      var getDialogLabels = function() {
        return {
          createTitle: {
            key: 'analysis-package-management.detail.attachments.create-title',
            params: {
              analysisPackageId: ctrl.analysisPackage.id
            }
          },
          editTitle: {
            key: 'analysis-package-management.detail.attachments.edit-title',
            params: {
              analysisPackageId: ctrl.analysisPackage.id
            }
          },
          hints: {
            file: {
              key: 'analysis-package-management.detail' +
                '.attachments.hints.filename'
            }
          }
        };
      };

      ctrl.translationKeysAuthors = {
        title: 'analysis-package-management.detail.label.authors',
        tooltips: {
          delete: 'analysis-package-management.' +
            'edit.delete-package-author-tooltip',
          add: 'analysis-package-management.edit.add-package-author-tooltip',
          moveUp: 'analysis-package-management.' +
            'edit.move-package-author-up-tooltip',
          moveDown: 'analysis-package-management.' +
            'edit.move-package-author-down-tooltip'
        },
        hints: {
          firstName: 'analysis-package-management.edit.hints.' +
            'authors.first-name',
          middleName: 'analysis-package-management.edit.hints.' +
            'authors.middle-name',
          lastName: 'analysis-package-management.edit.hints.' +
            'authors.last-name'
        }
      };

      ctrl.translationKeysDataCurators = {
        title: 'analysis-package-management.detail.label.data-curators',
        tooltips: {
          delete: 'analysis-package-management.edit.delete-curator-tooltip',
          add: 'analysis-package-management.edit.add-curator-tooltip',
          moveUp: 'analysis-package-management.edit.move-curator-up-tooltip',
          moveDown: 'analysis-package-management.edit.move-curator-down-tooltip'
        },
        hints: {
          firstName: 'analysis-package-management.edit.hints' +
            '.curators.first-name',
          middleName: 'analysis-package-management.edit.hints' +
            '.curators.middle-name',
          lastName: 'analysis-package-management.edit.hints.curators.last-name'
        }
      };

      ctrl.findTags = AnalysisPackageSearchService.findTags;

      var updateToolbarHeaderAndPageTitle = function() {
        if (ctrl.createMode) {
          PageMetadataService.setPageTitle(
            'analysis-package-management.edit.create-page-title', {
              analysisPackageId: ctrl.analysisPackage.id
            });
        } else {
          PageMetadataService.setPageTitle(
            'analysis-package-management.edit.edit-page-title', {
              analysisPackageId: ctrl.analysisPackage.id
            });
        }
        BreadcrumbService.updateToolbarHeader({
          'stateName': $state.current.name,
          'id': ctrl.analysisPackage.id,
          'analysisPackageId': ctrl.analysisPackage.id,
          'analysisPackageIsPresent': !ctrl.createMode,
          'projectId': ctrl.analysisPackage.dataAcquisitionProjectId,
          'enableLastItem': true
        });
      };

      var redirectToSearchView = function() {
        $timeout(function() {
          $state.go('search', {
            lang: LanguageService.getCurrentInstantly(),
            type: 'analysis_packages'
          });
        }, 1000);
      };

      var handleReleasedProject = function() {
        SimpleMessageToastService.openAlertMessageToast(
          'analysis-package-management.edit.choose-unreleased-project-toast');
        redirectToSearchView();
      };

      var initEditMode = function(analysisPackage) {
        ctrl.createMode = false;
        DataAcquisitionProjectResource.get({
          id: analysisPackage.dataAcquisitionProjectId
        }).$promise.then(function(project) {
          if (project.release != null) {
            handleReleasedProject();
          } else if (!ProjectUpdateAccessService
            .isUpdateAllowed(project, 'analysisPackages', true)) {
            redirectToSearchView();
          } else {
            CurrentProjectService.setCurrentProject(project);
            ctrl.analysisPackage = analysisPackage;
            ctrl.currentSponsors = angular.copy(
              ctrl.analysisPackage.sponsors);
            ctrl.currentInstitutions = angular.copy(
              ctrl.analysisPackage.institutions);
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
            entity.$promise.then(function(analysisPackage) {
              initEditMode(analysisPackage);
            });
          } else {
            if (CurrentProjectService.getCurrentProject() &&
              !CurrentProjectService.getCurrentProject().release) {
              if (!ProjectUpdateAccessService
                .isUpdateAllowed(CurrentProjectService.getCurrentProject(),
                  'analysisPackages', true)) {
                redirectToSearchView();
              } else {
                AnalysisPackageResource.get({
                  id: AnalysisPackageIdBuilderService.buildAnalysisPackageId(
                    CurrentProjectService.getCurrentProject().id)
                }).$promise.then(function(analysisPackage) {
                  initEditMode(analysisPackage);
                }).catch(function() {
                  ctrl.createMode = true;
                  ctrl.analysisPackage = new AnalysisPackageResource({
                    id: AnalysisPackageIdBuilderService.buildAnalysisPackageId(
                      CurrentProjectService.getCurrentProject().id),
                    dataAcquisitionProjectId: CurrentProjectService
                      .getCurrentProject()
                      .id,
                    authors: [{
                      firstName: '',
                      lastName: ''
                    }],
                    dataCurators: [{
                      firstName: '',
                      lastName: ''
                    }],
                    institutions: [],
                    sponsors: [],
                    scripts: [],
                    analysisDataPackages: []
                  });
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
        ctrl.analysisPackage.institutions.splice(index, 1);
        ctrl.currentInstitutions.splice(index, 1);
        if (ctrl.analysisPackage.institutions.length === 0) {
          ctrl.currentInstitutions = [];
          $scope.analysisPackageForm.$setUntouched();
        }
        $scope.analysisPackageForm.$setDirty();
      };

      ctrl.addInstitution = function() {
        if (!ctrl.analysisPackage.institutions) {
          ctrl.analysisPackage.institutions = [];
        }
        if (!ctrl.currentInstitutions) {
          ctrl.currentInstitutions = [];
        }
        ctrl.currentInstitutions.push(null);
        // console.table(ctrl.currentInstitutions);
        $timeout(function() {
          $document.find('input[name="institutionDe_' +
            (ctrl.analysisPackage.institutions.length - 1) + '"]')
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
        var a = ctrl.analysisPackage
          .institutions[ctrl.currentInstitutionIndex - 1];
        ctrl.analysisPackage.institutions[ctrl.currentInstitutionIndex - 1] =
          ctrl.analysisPackage.institutions[ctrl.currentInstitutionIndex];
        ctrl.analysisPackage.institutions[ctrl.currentInstitutionIndex] = a;
        a = ctrl.currentInstitutions[ctrl.currentInstitutionIndex - 1];
        ctrl.currentInstitutions[ctrl.currentInstitutionIndex - 1] =
          ctrl.currentInstitutions[ctrl.currentInstitutionIndex];
        ctrl.currentInstitutions[ctrl.currentInstitutionIndex] = a;
        ctrl.currentInstitutionInputName = ctrl.currentInstitutionInputName
          .replace('_' + ctrl.currentInstitutionIndex,
            '_' + (ctrl.currentInstitutionIndex - 1));
        $document.find('input[name="' + ctrl.currentInstitutionInputName + '"]')
          .focus();
        $scope.analysisPackageForm.$setDirty();
      };

      ctrl.moveCurrentInstitutionDown = function() {
        var a = ctrl.analysisPackage
          .institutions[ctrl.currentInstitutionIndex + 1];
        ctrl.analysisPackage.institutions[ctrl.currentInstitutionIndex + 1] =
          ctrl.analysisPackage.institutions[ctrl.currentInstitutionIndex];
        ctrl.analysisPackage.institutions[ctrl.currentInstitutionIndex] = a;
        a = ctrl.currentInstitutions[ctrl.currentInstitutionIndex + 1];
        ctrl.currentInstitutions[ctrl.currentInstitutionIndex + 1] =
          ctrl.currentInstitutions[ctrl.currentInstitutionIndex];
        ctrl.currentInstitutions[ctrl.currentInstitutionIndex] = a;
        ctrl.currentInstitutionInputName = ctrl.currentInstitutionInputName
          .replace('_' + ctrl.currentInstitutionIndex,
            '_' + (ctrl.currentInstitutionIndex + 1));
        $document.find('input[name="' + ctrl.currentInstitutionInputName + '"]')
          .focus();
        $scope.analysisPackageForm.$setDirty();
      };

      ctrl.deleteSponsor = function(index) {
        ctrl.analysisPackage.sponsors.splice(index, 1);
        ctrl.currentSponsors.splice(index, 1);
        if (ctrl.analysisPackage.sponsors.length === 0) {
          ctrl.currentSponsors = [];
          $scope.analysisPackageForm.$setUntouched();
        }
        $scope.analysisPackageForm.$setDirty();
      };

      ctrl.addSponsor = function() {
        if (!ctrl.analysisPackage.sponsors) {
          ctrl.analysisPackage.sponsors = [];
        }
        if (!ctrl.currentSponsors) {
          ctrl.currentSponsors = [];
        }
        ctrl.currentSponsors.push(null);
        $timeout(function() {
          $document.find('input[name="sponsorDe_' +
            (ctrl.analysisPackage.sponsors.length - 1) + '"]')
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
        var a = ctrl.analysisPackage.sponsors[ctrl.currentSponsorIndex - 1];
        ctrl.analysisPackage.sponsors[ctrl.currentSponsorIndex - 1] =
          ctrl.analysisPackage.sponsors[ctrl.currentSponsorIndex];
        ctrl.analysisPackage.sponsors[ctrl.currentSponsorIndex] = a;
        a = ctrl.currentSponsors[ctrl.currentSponsorIndex - 1];
        ctrl.currentSponsors[ctrl.currentSponsorIndex - 1] =
          ctrl.currentSponsors[ctrl.currentSponsorIndex];
        ctrl.currentSponsors[ctrl.currentSponsorIndex] = a;
        ctrl.currentSponsorInputName = ctrl.currentSponsorInputName
          .replace('_' + ctrl.currentSponsorIndex,
            '_' + (ctrl.currentSponsorIndex - 1));
        $document.find('input[name="' + ctrl.currentSponsorInputName + '"]')
          .focus();
        $scope.analysisPackageForm.$setDirty();
      };

      ctrl.moveCurrentSponsorDown = function() {
        var a = ctrl.analysisPackage.sponsors[ctrl.currentSponsorIndex + 1];
        ctrl.analysisPackage.sponsors[ctrl.currentSponsorIndex + 1] =
          ctrl.analysisPackage.sponsors[ctrl.currentSponsorIndex];
        ctrl.analysisPackage.sponsors[ctrl.currentSponsorIndex] = a;
        a = ctrl.currentSponsors[ctrl.currentSponsorIndex + 1];
        ctrl.currentSponsors[ctrl.currentSponsorIndex + 1] =
          ctrl.currentSponsors[ctrl.currentSponsorIndex];
        ctrl.currentSponsors[ctrl.currentSponsorIndex] = a;
        ctrl.currentSponsorInputName = ctrl.currentSponsorInputName
          .replace('_' + ctrl.currentSponsorIndex,
            '_' + (ctrl.currentSponsorIndex + 1));
        $document.find('input[name="' + ctrl.currentSponsorInputName + '"]')
          .focus();
        $scope.analysisPackageForm.$setDirty();
      };

      ctrl.deleteLink = function(index) {
        ctrl.analysisPackage.additionalLinks.splice(index, 1);
        $scope.analysisPackageForm.$setDirty();
      };

      ctrl.addLink = function() {
        if (!ctrl.analysisPackage.additionalLinks) {
          ctrl.analysisPackage.additionalLinks = [];
        }
        ctrl.analysisPackage.additionalLinks.push({
          url: '',
          displayText: {
            de: '',
            en: ''
          }
        });
        $timeout(function() {
          $document.find('input[name="linkUrl_' +
            (ctrl.analysisPackage.additionalLinks.length - 1) + '"]')
            .focus();
        }, 200);
      };

      ctrl.setCurrentLink = function(index, event) {
        ctrl.currentLinkInputName = event.target.name;
        ctrl.currentLinkIndex = index;
      };

      ctrl.deleteCurrentLink = function(event) {
        if (document.activeElement &&
          $(document.activeElement).parents('#move-link-container')
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
        var a = ctrl.analysisPackage.additionalLinks[ctrl.currentLinkIndex - 1];
        ctrl.analysisPackage.additionalLinks[ctrl.currentLinkIndex - 1] =
          ctrl.analysisPackage.additionalLinks[ctrl.currentLinkIndex];
        ctrl.analysisPackage.additionalLinks[ctrl.currentLinkIndex] = a;
        ctrl.currentLinkInputName = ctrl.currentLinkInputName
          .replace('_' + ctrl.currentLinkIndex,
            '_' + (ctrl.currentLinkIndex - 1));
        $document.find('input[name="' + ctrl.currentLinkInputName + '"]')
          .focus();
        $scope.analysisPackage.$setDirty();
      };

      ctrl.moveCurrentLinkDown = function() {
        var a = ctrl.analysisPackage.additionalLinks[ctrl.currentLinkIndex + 1];
        ctrl.analysisPackage.additionalLinks[ctrl.currentLinkIndex + 1] =
          ctrl.analysisPackage.additionalLinks[ctrl.currentLinkIndex];
        ctrl.analysisPackage.additionalLinks[ctrl.currentLinkIndex] = a;
        ctrl.currentLinkInputName = ctrl.currentLinkInputName
          .replace('_' + ctrl.currentLinkIndex,
            '_' + (ctrl.currentLinkIndex + 1));
        $document.find('input[name="' + ctrl.currentLinkInputName + '"]')
          .focus();
        $scope.analysisPackageForm.$setDirty();
      };

      ctrl.saveAnalysisPackage = function() {
        if ($scope.analysisPackageForm.$valid) {
          if (angular.isUndefined(ctrl.analysisPackage.masterId)) {
            ctrl.analysisPackage.masterId = ctrl.analysisPackage.id;
          }
          ctrl.analysisPackage.$save()
            .then(ctrl.updateElasticSearchIndex)
            .then(ctrl.onSavedSuccessfully)
            .catch(function() {
              SimpleMessageToastService.openAlertMessageToast(
                'analysis-package-management.edit.error-on-save-toast', {
                  analysisPackageId: ctrl.analysisPackage.id
                });
            });
        } else {
          // ensure that all validation errors are visible
          angular.forEach($scope.analysisPackageForm.$error, function(field) {
            angular.forEach(field, function(errorField) {
              if (errorField.$setTouched) {
                errorField.$setTouched();
              } else if (errorField.$setDirty) {
                errorField.$setDirty();
              }
            });
          });
          SimpleMessageToastService.openAlertMessageToast(
            'analysis-package-management.edit.' +
            'analysis-package-has-validation-errors-toast',
            null);
        }
      };

      ctrl.updateElasticSearchIndex = function() {
        return ElasticSearchAdminService
          .processUpdateQueue('analysis_packages');
      };

      ctrl.onSavedSuccessfully = function() {
        $scope.analysisPackageForm.$setPristine();
        SimpleMessageToastService.openSimpleMessageToast(
          'analysis-package-management.edit.success-on-save-toast', {
            analysisPackageId: ctrl.analysisPackage.id
          });
        if (ctrl.createMode) {
          $state.go('analysisPackageEdit', {
            id: ctrl.analysisPackage.id
          });
        }
      };

      ctrl.openRestorePreviousVersionDialog = function(event) {
        var getVersions = function(id, limit, skip) {
          return AnalysisPackageVersionsResource.get({
            id: id,
            limit: limit,
            skip: skip
          }).$promise;
        };

        var dialogConfig = {
          domainId: ctrl.analysisPackage.id,
          getPreviousVersionsCallback: getVersions,
          labels: {
            title: {
              key: 'analysis-package-management.edit.' +
                'choose-previous-version.title',
              params: {
                analysisPackageId: ctrl.analysisPackage.id
              }
            },
            text: {
              key: 'analysis-package-management.edit.' +
                'choose-previous-version.text'
            },
            cancelTooltip: {
              key: 'analysis-package-management.edit.choose-previous-version.' +
                'cancel-tooltip'
            },
            noVersionsFound: {
              key: 'analysis-package-management.edit.choose-previous-version.' +
                'no-versions-found',
              params: {
                analysisPackageId: ctrl.analysisPackage.id
              }
            },
            deleted: {
              key: 'analysis-package-management.edit.' +
                'choose-previous-version.analysis-package-deleted'
            }
          }
        };

        ChoosePreviousVersionService.showDialog(dialogConfig, event)
          .then(function(wrapper) {
            ctrl.analysisPackage = new AnalysisPackageResource(wrapper
              .selection);
            if (!ctrl.analysisPackage.authors) {
              ctrl.analysisPackage.authors = [{
                firstName: '',
                lastName: ''
              }];
            }
            if (!ctrl.analysisPackage.analysisCurators) {
              ctrl.analysisPackage.analysisCurators = [{
                firstName: '',
                lastName: ''
              }];
            }
            if (ctrl.analysisPackage.institutions &&
              ctrl.analysisPackage.institutions.length > 0) {
              ctrl.currentInstitutions = angular.copy(
                ctrl.analysisPackage.institutions);
            } else {
              ctrl.currentInstitutions = [];
            }
            if (ctrl.analysisPackage.sponsors &&
              ctrl.analysisPackage.sponsors.length > 0) {
              ctrl.currentSponsors = angular.copy(
                ctrl.analysisPackage.sponsors);
            } else {
              ctrl.currentSponsors = [{
                name: {
                  de: '',
                  en: ''
                }
              }];
            }

            $timeout(function() {
              $scope.$broadcast('LoadScriptEvent');
            }, 1000);

            if (wrapper.isCurrentVersion) {
              $scope.analysisPackageForm.$setPristine();
              SimpleMessageToastService.openSimpleMessageToast(
                'analysis-package-management.edit.' +
                'current-version-restored-toast', {
                  analysisPackageId: ctrl.analysisPackage.id
                });
            } else {
              $scope.analysisPackageForm.$setDirty();
              SimpleMessageToastService.openSimpleMessageToast(
                'analysis-package-management.edit.' +
                'previous-version-restored-toast', {
                  analysisPackageId: ctrl.analysisPackage.id
                });
            }
          });
      };

      $scope.registerConfirmOnDirtyHook = function() {
        var unregisterTransitionHook = $transitions.onBefore({}, function() {
          if ($scope.analysisPackageForm.$dirty ||
            ctrl.attachmentOrderIsDirty) {
            return CommonDialogsService.showConfirmOnDirtyDialog();
          }
        });

        $scope.$on('$destroy', unregisterTransitionHook);
      };

      $scope.searchSponsors = function(searchText, language) {
        //Search Call to Elasticsearch
        return AnalysisPackageSearchService.findSponsors(searchText, {},
          language, true, ctrl.currentSponsors)
          .then(function(sponsors) {
            return sponsors;
          });
      };

      $scope.searchInstitutions = function(searchText, language) {
        //Search Call to Elasticsearch
        return AnalysisPackageSearchService.findInstitutions(
          searchText, {}, language, true,
          ctrl.currentInstitutions)
          .then(function(institutions) {
            return institutions;
          });
      };

      ctrl.loadAttachments = function(selectLastAttachment) {
        AnalysisPackageAttachmentResource.findByAnalysisPackageId({
          analysisPackageId: ctrl.analysisPackage.id
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
                'analysis-package-management.detail.' +
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
          return AnalysisPackageAttachmentUploadService.uploadAttachment(file,
            metadata);
        };

        var labels = getDialogLabels();
        labels.editTitle.params.filename = attachment.fileName;

        var getAttachmentVersions = function(id, filename, limit, skip) {
          return AnalysisPackageAttachmentVersionsResource.get({
            analysisPackageId: id,
            filename: filename,
            limit: limit,
            skip: skip
          }).$promise;
        };

        var createAnalysisPackageAttachmentResource =
          function(attachmentWrapper) {
            return new AnalysisPackageAttachmentResource(
              attachmentWrapper.dataPackageAttachment);
          };

        var dialogConfig = {
          attachmentMetadata: attachment,
          uploadCallback: upload,
          attachmentDomainIdAttribute: 'analysisPackageId',
          getAttachmentVersionsCallback: getAttachmentVersions,
          createAttachmentResource: createAnalysisPackageAttachmentResource,
          analysisPackageTitle: ctrl.analysisPackage.title,
          labels: labels
        };

        AttachmentDialogService.showDialog(dialogConfig, event)
          .then(ctrl.loadAttachments);
      };

      ctrl.getNextIndexInAnalysisPackage = function() {
        if (!ctrl.attachments || ctrl.attachments.length === 0) {
          return 0;
        }
        return _.maxBy(ctrl.attachments, function(attachment) {
          return attachment.indexInAnalysisPackage;
        }).indexInAnalysisPackage + 1;
      };

      ctrl.addAttachment = function(event) {
        var upload = function(file, attachmentMetadata) {
          var metadata = _.extend({}, attachmentMetadata, {
            analysisPackageId: ctrl.analysisPackage.id,
            dataAcquisitionProjectId:
            ctrl.analysisPackage.dataAcquisitionProjectId,
            indexInAnalysisPackage: ctrl.getNextIndexInAnalysisPackage()
          });
          return AnalysisPackageAttachmentUploadService.uploadAttachment(
            file, metadata);
        };

        var dialogConfig = {
          attachmentMetadata: null,
          // attachmentTypes: attachmentTypes,
          uploadCallback: upload,
          analysisPackageTitle: ctrl.analysisPackage.title,
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
          attachment.indexInAnalysisPackage = index;
          promises.push(attachment.$save());
        });
        $q.all(promises).then(function() {
          SimpleMessageToastService.openSimpleMessageToast(
            'analysis-package-management.detail.attachments.' +
            'attachment-order-saved-toast',
            {});
          ctrl.attachmentOrderIsDirty = false;
        });
      };

      ctrl.selectAttachment = function(index) {
        if (Principal
          .hasAnyAuthority(['ROLE_PUBLISHER',
            'ROLE_DATA_PROVIDER'])) {
          ctrl.currentAttachmentIndex = index;
        }
      };

      init();
    }]);

