/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('InstrumentEditOrCreateController', [
  'entity',
  'PageMetadataService',
  '$timeout',
  '$state',
  'BreadcrumbService',
  'Principal',
  'SimpleMessageToastService',
  'CurrentProjectService',
  'InstrumentIdBuilderService',
  'InstrumentResource',
  '$scope',
  'SurveyIdBuilderService',
  'AttachmentDialogService',
  'ElasticSearchAdminService',
  '$mdDialog',
  '$transitions',
  'DataPackageResource',
  'CommonDialogsService',
  'LanguageService',
  'AvailableInstrumentNumbersResource',
  'InstrumentAttachmentResource',
  '$q',
  'DataPackageIdBuilderService',
  'SearchDao',
  'DataAcquisitionProjectResource',
  '$rootScope',
  'ProjectUpdateAccessService',
  'InstrumentAttachmentUploadService',
  'InstrumentAttachmentVersionsResource',
  'ChoosePreviousVersionService',
  'InstrumentVersionsResource',
    function(entity, PageMetadataService, $timeout,
      $state, BreadcrumbService, Principal, SimpleMessageToastService,
      CurrentProjectService, InstrumentIdBuilderService, InstrumentResource,
      $scope, SurveyIdBuilderService, AttachmentDialogService,
      ElasticSearchAdminService, $mdDialog, $transitions, DataPackageResource,
      CommonDialogsService, LanguageService, AvailableInstrumentNumbersResource,
      InstrumentAttachmentResource, $q, DataPackageIdBuilderService, SearchDao,
      DataAcquisitionProjectResource, $rootScope, ProjectUpdateAccessService,
      InstrumentAttachmentUploadService, InstrumentAttachmentVersionsResource,
      ChoosePreviousVersionService, InstrumentVersionsResource) {
      var ctrl = this;
      ctrl.surveyChips = [];
      ctrl.conceptChips = [];
      var instrumentAttachmentTypes = [
        {de: 'Fragebogen', en: 'Questionnaire'},
        {de: 'Filterführungsdiagramm', en: 'Question Flow'},
        {de: 'Variablenfragebogen', en: 'Variable Questionnaire'},
        {de: 'Sonstige', en: 'Other'}
      ];
      var updateToolbarHeaderAndPageTitle = function() {
        if (ctrl.createMode) {
          PageMetadataService.setPageTitle(
            'instrument-management.edit.create-page-title', {
            instrumentId: ctrl.instrument.id
          });
        } else {
          PageMetadataService.setPageTitle(
            'instrument-management.edit.edit-page-title', {
            instrumentId: ctrl.instrument.id
          });
        }
        $rootScope.$broadcast('start-ignoring-404');
        DataPackageResource.get({id: ctrl.instrument.dataPackageId}).$promise
          .then(function(dataPackage) {
          BreadcrumbService.updateToolbarHeader({
            'stateName': $state.current.name,
            'instrumentId': ctrl.instrument.id,
            'dataPackageId': ctrl.instrument.dataPackageId,
            'instrumentNumber': ctrl.instrument.number,
            'dataPackageIsPresent': dataPackage != null,
            'projectId': ctrl.instrument.dataAcquisitionProjectId,
            'enableLastItem': !ctrl.createMode,
            'instrumentIsPresent': !ctrl.createMode
          });
        }).catch(function() {
          BreadcrumbService.updateToolbarHeader({
            'stateName': $state.current.name,
            'instrumentId': ctrl.instrument.id,
            'dataPackageId': ctrl.instrument.dataPackageId,
            'instrumentNumber': ctrl.instrument.number,
            'dataPackageIsPresent': false,
            'projectId': ctrl.instrument.dataAcquisitionProjectId,
            'enableLastItem': !ctrl.createMode,
            'instrumentIsPresent': !ctrl.createMode
          });
        }).finally(function() {
          $rootScope.$broadcast('stop-ignoring-404');
        });
      };

      var redirectToSearchView = function() {
        $timeout(function() {
          $state.go('search', {
            lang: LanguageService.getCurrentInstantly(),
            type: 'instruments'
          });
        }, 1000);
      };

      var handleReleasedProject = function() {
        SimpleMessageToastService.openAlertMessageToast(
          'instrument-management.edit.choose-unreleased-project-toast');
        redirectToSearchView();
      };

      var getDialogLabels = function() {
        return {
          createTitle: {
            key: 'instrument-management.detail.attachments.create-title',
            params: {
              instrumentId: ctrl.instrument.id
            }
          },
          editTitle: {
            key: 'instrument-management.detail.attachments.edit-title',
            params: {
              instrumentId: ctrl.instrument.id
            }
          },
          hints: {
            file: {
              key: 'instrument-management.detail.attachments.hints.filename'
            }
          }
        };
      };

      ctrl.initSurveyChips = function() {
        ctrl.surveyChips = [];
        ctrl.instrument.surveyNumbers.forEach(
          function(surveyNumber) {
            ctrl.surveyChips.push({
              id: SurveyIdBuilderService.buildSurveyId(
                ctrl.instrument.dataAcquisitionProjectId,
                surveyNumber
              ),
              number: surveyNumber
            });
          });
      };

      ctrl.initConceptChips = function() {
        ctrl.conceptChips = [];
        if (ctrl.instrument.conceptIds) {
          ctrl.instrument.conceptIds.forEach(
            function(id) {
              ctrl.conceptChips.push({
                id: id
              });
            });
        }
      };

      var init = function() {
        if (Principal.hasAnyAuthority(['ROLE_PUBLISHER',
            'ROLE_DATA_PROVIDER'])) {
          if (entity) {
            entity.$promise.then(function(instrument) {
              ctrl.dataPackageTitle = instrument.dataPackageTitle;
              ctrl.createMode = false;
              DataAcquisitionProjectResource.get({
                id: instrument.dataAcquisitionProjectId
              }).$promise.then(function(project) {
                if (project.release != null && !project.release.isPreRelease) {
                  handleReleasedProject();
                } else if (!ProjectUpdateAccessService
                  .isUpdateAllowed(project, 'instruments', true)) {
                  redirectToSearchView();
                } else {
                  ProjectUpdateAccessService.isPrerequisiteFulfilled(
                    project, 'instruments'
                  ).catch(redirectToSearchView);
                  CurrentProjectService.setCurrentProject(project);
                  ctrl.instrument = instrument;
                  ctrl.initSurveyChips();
                  ctrl.initConceptChips();
                  ctrl.loadAttachments();
                  updateToolbarHeaderAndPageTitle();
                  $scope.registerConfirmOnDirtyHook();
                }
              });
            });
          } else {
            if (CurrentProjectService.getCurrentProject() &&
            (!CurrentProjectService.getCurrentProject().release 
              || CurrentProjectService.getCurrentProject().release.isPreRelease)) {
              if (!ProjectUpdateAccessService
                .isUpdateAllowed(CurrentProjectService.getCurrentProject(),
                  'instruments', true)) {
                redirectToSearchView();
              } else {
                ProjectUpdateAccessService.isPrerequisiteFulfilled(
                  CurrentProjectService.getCurrentProject(), 'instruments'
                ).catch(redirectToSearchView);

                ctrl.createMode = true;
                AvailableInstrumentNumbersResource.get({
                  id: CurrentProjectService.getCurrentProject().id
                }).$promise.then(
                  function(instrumentNumbers) {
                    if (instrumentNumbers.length === 1) {
                      ctrl.instrument = new InstrumentResource({
                        id: InstrumentIdBuilderService.buildInstrumentId(
                          CurrentProjectService.getCurrentProject().id,
                          instrumentNumbers[0]
                        ),
                        number: instrumentNumbers[0],
                        dataAcquisitionProjectId:
                        CurrentProjectService.getCurrentProject().id,
                        dataPackageId: DataPackageIdBuilderService
                          .buildDataPackageId(CurrentProjectService
                            .getCurrentProject().id
                        ),
                        originalLanguages: []
                      });
                      updateToolbarHeaderAndPageTitle();
                      $scope.registerConfirmOnDirtyHook();
                    } else {
                      $mdDialog.show({
                        controller: 'ChooseInstrumentNumberController',
                        templateUrl: 'scripts/instrumentmanagement/' +
                        'views/choose-instrument-number.html.tmpl',
                        clickOutsideToClose: false,
                        fullscreen: true,
                        locals: {
                          availableInstrumentNumbers: instrumentNumbers
                        }
                      })
                      .then(function(response) {
                        ctrl.instrument = new InstrumentResource({
                          id: InstrumentIdBuilderService.buildInstrumentId(
                            CurrentProjectService.getCurrentProject().id,
                            response.instrumentNumber
                          ),
                          number: response.instrumentNumber,
                          dataAcquisitionProjectId:
                          CurrentProjectService.getCurrentProject().id,
                          dataPackageId: DataPackageIdBuilderService
                            .buildDataPackageId(CurrentProjectService
                              .getCurrentProject().id
                          ),
                        });
                        if (!ctrl.instrument.originalLanguages) {
                          ctrl.instrument.originalLanguages = [];
                        }
                        $scope.responseRateInitializing = true;
                        updateToolbarHeaderAndPageTitle();
                        $scope.registerConfirmOnDirtyHook();
                      });
                    }
                  });
              }
            } else {
              handleReleasedProject();
            }
          }
        } else {
          SimpleMessageToastService.openAlertMessageToast(
          'instrument-management.edit.not-authorized-toast');
        }
      };

      ctrl.saveInstrument = function() {
        if ($scope.instrumentForm.$valid) {
          if (angular.isUndefined(ctrl.instrument.masterId)) {
            ctrl.instrument.masterId = ctrl.instrument.id;
          }
          ctrl.instrument.$save()
          .then(ctrl.updateElasticSearchIndex)
          .then(ctrl.onSavedSuccessfully)
          .catch(function(error) {
              console.log(error);
              SimpleMessageToastService.openAlertMessageToast(
                'instrument-management.edit.error-on-save-toast',
                {instrumentId: ctrl.instrument.id});
            });
        } else {
          // ensure that all validation errors are visible
          angular.forEach($scope.instrumentForm.$error, function(field) {
            angular.forEach(field, function(errorField) {
              errorField.$setTouched();
            });
          });
          SimpleMessageToastService.openAlertMessageToast(
            'instrument-management.edit.instrument-has-validation-errors-toast',
            null);
        }
      };

      ctrl.updateElasticSearchIndex = function() {
        return $q.all([
          ElasticSearchAdminService.processUpdateQueue('instruments'),
          ElasticSearchAdminService.processUpdateQueue('concepts')]);
      };

      ctrl.onSavedSuccessfully = function() {
        $scope.instrumentForm.$setPristine();
        SimpleMessageToastService.openSimpleMessageToast(
          'instrument-management.edit.success-on-save-toast',
          {instrumentId: ctrl.instrument.id});
        if (ctrl.createMode) {
          $state.go('instrumentEdit', {id: ctrl.instrument.id});
        }
      };

      ctrl.openRestorePreviousVersionDialog = function(event) {
        var getVersions = function(id, limit, skip) {
          return InstrumentVersionsResource.get({
            id: id,
            limit: limit,
            skip: skip
          }).$promise;
        };

        var dialogConfig = {
          domainId: ctrl.instrument.id,
          getPreviousVersionsCallback: getVersions,
          labels: {
            title: {
              key: 'instrument-management.edit.choose-previous-version.' +
                  'instrument-description',
              params: {
                instrumentId: ctrl.instrument.id
              }
            },
            text: {
              key: 'instrument-management.edit.choose-previous-version.text'
            },
            cancelTooltip: {
              key: 'instrument-management.edit.choose-previous-version.' +
                  'cancel-tooltip'
            },
            noVersionsFound: {
              key: 'instrument-management.edit.choose-previous-version.' +
                  'no-versions-found',
              params: {
                instrumentId: ctrl.instrument.id
              }
            },
            deleted: {
              key: 'instrument-management.edit.choose-previous-version.' +
                  'instrument-deleted'
            }
          },
          versionLabelAttribute: 'description'
        };

        ChoosePreviousVersionService.showDialog(dialogConfig, event)
            .then(function(wrapper) {
              ctrl.instrument = new InstrumentResource(
                  wrapper.selection);
              if (!ctrl.instrument.originalLanguages) {
                ctrl.instrument.originalLanguages = [];
              }
              ctrl.initSurveyChips();
              ctrl.initConceptChips();
              if (wrapper.isCurrentVersion) {
                $scope.instrumentForm.$setPristine();
                SimpleMessageToastService.openSimpleMessageToast(
                    'instrument-management.edit.current-version-' +
                    'restored-toast',
                    {
                      instrumentId: ctrl.instrument.id
                    });
              } else {
                $scope.instrumentForm.$setDirty();
                SimpleMessageToastService.openSimpleMessageToast(
                    'instrument-management.edit.previous-version-' +
                    'restored-toast',
                    {
                      instrumentId: ctrl.instrument.id
                    });
              }
            });
      };

      $scope.registerConfirmOnDirtyHook = function() {
        var unregisterTransitionHook = $transitions.onBefore({}, function() {
          if ($scope.instrumentForm.$dirty || ctrl.attachmentOrderIsDirty) {
            return CommonDialogsService.showConfirmOnDirtyDialog();
          }
        });

        $scope.$on('$destroy', unregisterTransitionHook);
      };

      ctrl.loadAttachments = function(selectLastAttachment) {
        InstrumentAttachmentResource.findByInstrumentId({
            instrumentId: ctrl.instrument.id
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
              'instrument-management.detail.attachments.' +
                'attachment-deleted-toast',
              {filename: attachment.fileName}
            );
            ctrl.attachments.splice(index, 1);
            delete ctrl.currentAttachmentIndex;
          });
        });
      };

      ctrl.editAttachment = function(attachment, event) {

        var upload = function(file, newAttachmentMetadata) {
          var metadata = _.extend(attachment, newAttachmentMetadata);
          return InstrumentAttachmentUploadService.uploadAttachment(file,
              metadata);
        };

        var labels = getDialogLabels();
        labels.editTitle.params.filename = attachment.fileName;

        var getAttachmentVersions = function(id, filename, limit, skip) {
          return InstrumentAttachmentVersionsResource.get({
            instrumentId: id,
            filename: filename,
            limit: limit,
            skip: skip
          }).$promise;
        };

        var createInstrumentAttachmentResource = function(attachmentWrapper) {
          return new InstrumentAttachmentResource(attachmentWrapper
              .dataPackageAttachment);
        };

        var dialogConfig = {
          dataPackageTitle: ctrl.dataPackageTitle,
          surveySerialNumbers: ctrl.getSurveySerialNumbersFromChips(),
          attachmentMetadata: attachment,
          attachmentTypes: instrumentAttachmentTypes,
          uploadCallback: upload,
          labels: labels,
          exclude: ['title'],
          attachmentDomainIdAttribute: 'instrumentId',
          getAttachmentVersionsCallback: getAttachmentVersions,
          createAttachmentResource: createInstrumentAttachmentResource
        };

        AttachmentDialogService.showDialog(dialogConfig, event)
            .then(ctrl.loadAttachments);
      };

      ctrl.getNextIndexInInstrument = function() {
        if (!ctrl.attachments || ctrl.attachments.length === 0) {
          return 0;
        }
        return _.maxBy(ctrl.attachments, function(attachment) {
          return attachment.indexInInstrument;
        }).indexInInstrument + 1;
      };

      ctrl.addAttachment = function(event) {
        var upload = function(file, attachmentMetadata) {
          var metadata = _.extend({}, attachmentMetadata, {
            instrumentId: ctrl.instrument.id,
            instrumentNumber: ctrl.instrument.number,
            dataAcquisitionProjectId: ctrl.instrument.dataAcquisitionProjectId,
            indexInInstrument: ctrl.getNextIndexInInstrument()
          });
          return InstrumentAttachmentUploadService.uploadAttachment(file,
              metadata);
        };

        var dialogConfig = {
          dataPackageTitle: ctrl.dataPackageTitle,
          surveySerialNumbers: ctrl.getSurveySerialNumbersFromChips(),
          attachmentMetadata: null,
          attachmentTypes: instrumentAttachmentTypes,
          uploadCallback: upload,
          labels: getDialogLabels(),
          exclude: ['title']
        };

        AttachmentDialogService
            .showDialog(dialogConfig, event)
            .then(function() {
              ctrl.loadAttachments(true);
            });
      };

      /**
       * collects all numbers of surveyChips, so that an array of serial numbers can be returned
       * @returns array of all survey serial numbers, e.g. [1, 3]
       */
      ctrl.getSurveySerialNumbersFromChips = function() {
        var surveyIds = [];
        if (ctrl.surveyChips) {
          ctrl.surveyChips.forEach(function(chip) {
            surveyIds.push(chip.number);
          });  
        }
        return surveyIds;
      }

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
          attachment.indexInInstrument = index;
          promises.push(attachment.$save());
        });
        $q.all(promises).then(function() {
          SimpleMessageToastService.openSimpleMessageToast(
          'instrument-management.detail.attachments.' +
          'attachment-order-saved-toast',
          {});
          ctrl.attachmentOrderIsDirty = false;
        });
      };

      ctrl.selectAttachment = function(index) {
        if (Principal.hasAnyAuthority(['ROLE_PUBLISHER',
            'ROLE_DATA_PROVIDER'])) {
          ctrl.currentAttachmentIndex = index;
        }
      };

      ctrl.transformChip = function(chip) {
        return {
          id: chip._source.id,
          number: chip._source.number
        };
      };

      ctrl.transformConceptChip = function(chip) {
        return {
          id: chip._source.id
        };
      };

      ctrl.updateSurveyReferences = function() {
        ctrl.instrument.surveyIds = [];
        ctrl.instrument.surveyNumbers = [];
        if (ctrl.surveyChips) {
          ctrl.surveyChips.forEach(function(chip) {
            ctrl.instrument.surveyIds.push(chip.id);
            ctrl.instrument.surveyNumbers.push(chip.number);
          });
        }
      };

      ctrl.updateConceptReferences = function() {
        ctrl.instrument.conceptIds = [];
        if (ctrl.conceptChips) {
          ctrl.conceptChips.forEach(function(chip) {
            ctrl.instrument.conceptIds.push(chip.id);
          });
        }
      };

      ctrl.searchSurveys = function(query) {
        return SearchDao.search(
          query, 1,
          ctrl.instrument.dataAcquisitionProjectId,
          null, 'surveys', 100,  ctrl.instrument.surveyIds
        ).then(function(result) {
            return result.hits.hits;
          });
      };

      ctrl.searchConcepts = function(query) {
        return SearchDao.search(
          query, 1,
          null,
          null, 'concepts', 100,  ctrl.instrument.conceptIds
        ).then(function(result) {
            return result.hits.hits;
          });
      };

      ctrl.types = [
        'PAPI', 'CAPI', 'CATI', 'CAWI', 'INTERVIEW'
      ];

      init();
    }]);

