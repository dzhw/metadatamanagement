/* global _, $, document */
'use strict';

angular.module('metadatamanagementApp')
  .controller('DataSetEditOrCreateController', [
  'entity',
  'PageMetadataService',
  '$timeout',
  '$state',
  'BreadcrumbService',
  'Principal',
  'SimpleMessageToastService',
  'CurrentProjectService',
  'DataSetIdBuilderService',
  'DataSetResource',
  '$scope',
  'SurveyIdBuilderService',
  '$document',
  'ElasticSearchAdminService',
  '$mdDialog',
  '$transitions',
  'DataPackageResource',
  'CommonDialogsService',
  'LanguageService',
  'AvailableDataSetNumbersResource',
  'DataSetAttachmentResource',
  '$q',
  'DataPackageIdBuilderService',
  'SearchDao',
  'DataAcquisitionProjectResource',
  '$rootScope',
  'ProjectUpdateAccessService',
  'AttachmentDialogService',
  'DataSetAttachmentUploadService',
  'DataSetAttachmentVersionsResource',
  'ChoosePreviousVersionService',
  'DataSetVersionsResource',
  'DataFormatsResource',
    function(entity, PageMetadataService, $timeout,
      $state, BreadcrumbService, Principal, SimpleMessageToastService,
      CurrentProjectService, DataSetIdBuilderService, DataSetResource,
      $scope, SurveyIdBuilderService, $document,
      ElasticSearchAdminService, $mdDialog, $transitions, DataPackageResource,
      CommonDialogsService, LanguageService, AvailableDataSetNumbersResource,
      DataSetAttachmentResource, $q, DataPackageIdBuilderService, SearchDao,
      DataAcquisitionProjectResource, $rootScope, ProjectUpdateAccessService,
      AttachmentDialogService, DataSetAttachmentUploadService,
      DataSetAttachmentVersionsResource, ChoosePreviousVersionService,
      DataSetVersionsResource, DataFormatsResource) {
      var ctrl = this;
      ctrl.surveyChips = [];
      ctrl.availableDataFormats = DataFormatsResource.query();
      var updateToolbarHeaderAndPageTitle = function() {
        if (ctrl.createMode) {
          PageMetadataService.setPageTitle(
            'data-set-management.edit.create-page-title', {
            dataSetId: ctrl.dataSet.id
          });
        } else {
          PageMetadataService.setPageTitle(
            'data-set-management.edit.edit-page-title', {
            dataSetId: ctrl.dataSet.id
          });
        }
        $rootScope.$broadcast('start-ignoring-404');
        DataPackageResource.get({id: ctrl.dataSet.dataPackageId}).$promise
          .then(function(dataPackage) {
          BreadcrumbService.updateToolbarHeader({
            'stateName': $state.current.name,
            'dataSetId': ctrl.dataSet.id,
            'dataPackageId': ctrl.dataSet.dataPackageId,
            'dataSetNumber': ctrl.dataSet.number,
            'dataPackageIsPresent': dataPackage != null,
            'projectId': ctrl.dataSet.dataAcquisitionProjectId,
            'enableLastItem': !ctrl.createMode,
            'dataSetIsPresent': !ctrl.createMode
          });
        }).catch(function() {
          BreadcrumbService.updateToolbarHeader({
            'stateName': $state.current.name,
            'dataSetId': ctrl.dataSet.id,
            'dataPackageId': ctrl.dataSet.dataPackageId,
            'dataSetNumber': ctrl.dataSet.number,
            'dataPackageIsPresent': false,
            'projectId': ctrl.dataSet.dataAcquisitionProjectId,
            'enableLastItem': !ctrl.createMode,
            'dataSetIsPresent': !ctrl.createMode
          });
        }).finally(function() {
          $rootScope.$broadcast('stop-ignoring-404');
        });
      };

      var redirectToSearchView = function() {
        $timeout(function() {
          $state.go('search', {
            lang: LanguageService.getCurrentInstantly(),
            type: 'data_sets'
          });
        }, 1000);
      };

      var handleReleasedProject = function() {
        SimpleMessageToastService.openAlertMessageToast(
          'data-set-management.edit.choose-unreleased-project-toast');
        redirectToSearchView();
      };

      var handlePrerequisitesMissing = function() {
        redirectToSearchView();
      };

      ctrl.initSurveyChips = function() {
        ctrl.surveyChips = [];
        ctrl.dataSet.surveyNumbers.forEach(
          function(surveyNumber) {
            ctrl.surveyChips.push({
              id: SurveyIdBuilderService.buildSurveyId(
                ctrl.dataSet.dataAcquisitionProjectId,
                surveyNumber
              ),
              number: surveyNumber
            });
          });
      };

      var init = function() {
        if (Principal.hasAnyAuthority(['ROLE_PUBLISHER',
            'ROLE_DATA_PROVIDER'])) {
          if (entity) {
            entity.$promise.then(function(dataSet) {
              ctrl.createMode = false;
              DataAcquisitionProjectResource.get({
                id: dataSet.dataAcquisitionProjectId
              }).$promise.then(function(project) {
                if (project.release != null) {
                  handleReleasedProject();
                } else if (!ProjectUpdateAccessService
                  .isUpdateAllowed(project, 'data_sets', true)) {
                  redirectToSearchView();
                } else {
                  ProjectUpdateAccessService.isPrerequisiteFulfilled(
                    project, 'data_sets'
                  ).catch(handlePrerequisitesMissing);
                  CurrentProjectService.setCurrentProject(project);
                  ctrl.dataSet = dataSet;
                  ctrl.initSurveyChips();
                  ctrl.loadAttachments();
                  updateToolbarHeaderAndPageTitle();
                  $scope.registerConfirmOnDirtyHook();
                }
              });
            });
          } else {
            if (CurrentProjectService.getCurrentProject() &&
            !CurrentProjectService.getCurrentProject().release) {
              if (!ProjectUpdateAccessService
                .isUpdateAllowed(CurrentProjectService.getCurrentProject(),
                 'data_sets', true)) {
                redirectToSearchView();
              } else {
                ProjectUpdateAccessService.isPrerequisiteFulfilled(
                  CurrentProjectService.getCurrentProject(), 'data_sets'
                ).catch(handlePrerequisitesMissing);

                ctrl.createMode = true;
                AvailableDataSetNumbersResource.get({
                  id: CurrentProjectService.getCurrentProject().id
                }).$promise.then(
                  function(dataSetNumbers) {
                    if (dataSetNumbers.length === 1) {
                      ctrl.dataSet = new DataSetResource({
                        id: DataSetIdBuilderService.buildDataSetId(
                          CurrentProjectService.getCurrentProject().id,
                          dataSetNumbers[0]
                        ),
                        number: dataSetNumbers[0],
                        dataAcquisitionProjectId:
                        CurrentProjectService.getCurrentProject().id,
                        dataPackageId: DataPackageIdBuilderService
                          .buildDataPackageId(CurrentProjectService
                            .getCurrentProject().id
                        ),
                        subDataSets: [{
                          name: ''
                        }],
                        languages: []
                      });
                      updateToolbarHeaderAndPageTitle();
                      $scope.registerConfirmOnDirtyHook();
                    } else {
                      $mdDialog.show({
                        controller: 'ChooseDataSetNumberController',
                        templateUrl: 'scripts/datasetmanagement/' +
                        'views/choose-data-set-number.html.tmpl',
                        clickOutsideToClose: false,
                        fullscreen: true,
                        locals: {
                          availableDataSetNumbers: dataSetNumbers
                        }
                      })
                      .then(function(response) {
                        ctrl.dataSet = new DataSetResource({
                          id: DataSetIdBuilderService.buildDataSetId(
                            CurrentProjectService.getCurrentProject().id,
                            response.dataSetNumber
                          ),
                          number: response.dataSetNumber,
                          dataAcquisitionProjectId:
                          CurrentProjectService.getCurrentProject().id,
                          dataPackageId: DataPackageIdBuilderService
                            .buildDataPackageId(CurrentProjectService
                              .getCurrentProject().id
                          ),
                          subDataSets: [{
                            name: ''
                          }]
                        });
                        if (!ctrl.dataSet.languages) {
                          ctrl.dataSet.languages = [];
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
          'data-set-management.edit.not-authorized-toast');
        }
      };

      var getDialogLabels = function() {
        return {
          createTitle: {
            key: 'data-set-management.detail.attachments.create-title',
            params: {
              dataSetId: ctrl.dataSet.id
            }
          },
          editTitle: {
            key: 'data-set-management.detail.attachments.edit-title',
            params: {
              dataSetId: ctrl.dataSet.id
            }
          },
          hints: {
            file: {
              key: 'data-set-management.detail.attachments.hints.filename'
            }
          }
        };
      };

      ctrl.allAccessWays = ['download-cuf', 'download-suf',
        'remote-desktop-suf', 'onsite-suf'];
      $scope.$watch('ctrl.dataSet.subDataSets', function() {
          if (ctrl.dataSet) {
            ctrl.availableAccessWays = ctrl.allAccessWays.slice();
            ctrl.dataSet.subDataSets.forEach(function(subDataSet) {
              _.remove(ctrl.availableAccessWays, function(availableAccessWay) {
                return availableAccessWay === subDataSet.accessWay;
              });
            });
          }
        }, true);

      ctrl.deleteSubDataSet = function(index) {
        ctrl.dataSet.subDataSets.splice(index, 1);
        $scope.dataSetForm.$setDirty();
      };

      ctrl.addSubDataSet = function() {
        ctrl.dataSet.subDataSets.push({
          name: ''
        });
        $timeout(function() {
          $document.find('input[name="subDataSetsName_' +
              (ctrl.dataSet.subDataSets.length - 1) + '"]')
            .focus();
        });
      };

      ctrl.setCurrentSubDataSet = function(index, event) {
        ctrl.currentSubDataSetInputName = event.target.name;
        ctrl.currentSubDataSetIndex = index;
      };
      var timeoutActive = null;
      ctrl.deleteCurrentSubDataSet = function(event) {
        if (timeoutActive) {
          $timeout.cancel(timeoutActive);
        }
        timeoutActive = $timeout(function() {
          timeoutActive = false;
          // msie workaround: inputs unfocus on button mousedown
          if (document.activeElement &&
            $(document.activeElement).parents('#move-container').length) {
            return;
          }
          if (event.relatedTarget && (
              event.relatedTarget.id === 'move-sub-data-set-up-button' ||
              event.relatedTarget.id === 'move-sub-data-set-down-button')) {
            return;
          }
          delete ctrl.currentSubDataSetIndex;
          timeoutActive = null;
        }, 1500);
      };

      ctrl.moveCurrentSubDataSetUp = function() {
        var a = ctrl.dataSet.subDataSets[ctrl.currentSubDataSetIndex - 1];
        ctrl.dataSet.subDataSets[ctrl.currentSubDataSetIndex - 1] =
          ctrl.dataSet.subDataSets[ctrl.currentSubDataSetIndex];
        ctrl.dataSet.subDataSets[ctrl.currentSubDataSetIndex] = a;
        ctrl.currentSubDataSetInputName = ctrl.currentSubDataSetInputName
          .replace('_' + ctrl.currentSubDataSetIndex,
            '_' + (ctrl.currentSubDataSetIndex - 1));
        $document.find('*[name="' + ctrl.currentSubDataSetInputName + '"]')
          .focus();
        $scope.dataSetForm.$setDirty();
      };

      ctrl.moveCurrentSubDataSetDown = function() {
        var a = ctrl.dataSet.subDataSets[ctrl.currentSubDataSetIndex + 1];
        ctrl.dataSet.subDataSets[ctrl.currentSubDataSetIndex + 1] =
          ctrl.dataSet.subDataSets[ctrl.currentSubDataSetIndex];
        ctrl.dataSet.subDataSets[ctrl.currentSubDataSetIndex] = a;
        ctrl.currentSubDataSetInputName = ctrl.currentSubDataSetInputName
          .replace('_' + ctrl.currentSubDataSetIndex,
            '_' + (ctrl.currentSubDataSetIndex + 1));
        $document.find('*[name="' + ctrl.currentSubDataSetInputName + '"]')
          .focus();
        $scope.dataSetForm.$setDirty();
      };

      ctrl.saveDataSet = function() {
        if ($scope.dataSetForm.$valid) {
          if (angular.isUndefined(ctrl.dataSet.masterId)) {
            ctrl.dataSet.masterId = ctrl.dataSet.id;
          }
          ctrl.dataSet.$save()
          .then(ctrl.updateElasticSearchIndex)
          .then(ctrl.onSavedSuccessfully)
          .catch(function(error) {
              console.log(error);
              SimpleMessageToastService.openAlertMessageToast(
                'data-set-management.edit.error-on-save-toast',
                {dataSetId: ctrl.dataSet.id});
            });
        } else {
          // ensure that all validation errors are visible
          angular.forEach($scope.dataSetForm.$error, function(field) {
            angular.forEach(field, function(errorField) {
              errorField.$setTouched();
            });
          });
          SimpleMessageToastService.openAlertMessageToast(
            'data-set-management.edit.data-set-has-validation-errors-toast',
            null);
        }
      };

      ctrl.updateElasticSearchIndex = function() {
        return $q.all([
          ElasticSearchAdminService.
            processUpdateQueue('data_sets'),
          ElasticSearchAdminService.
            processUpdateQueue('data_packages'),
        ]);
      };

      ctrl.onSavedSuccessfully = function() {
        $scope.dataSetForm.$setPristine();
        SimpleMessageToastService.openSimpleMessageToast(
          'data-set-management.edit.success-on-save-toast',
          {dataSetId: ctrl.dataSet.id});
        if (ctrl.createMode) {
          $state.go('dataSetEdit', {id: ctrl.dataSet.id});
        }
      };

      ctrl.openRestorePreviousVersionDialog = function(event) {
        var getVersions = function(id, limit, skip) {
          return DataSetVersionsResource.get({
            id: id,
            limit: limit,
            skip: skip
          }).$promise;
        };

        var dialogConfig = {
          domainId: ctrl.dataSet.id,
          getPreviousVersionsCallback: getVersions,
          labels: {
            title: {
              key: 'data-set-management.edit.choose-previous-version.' +
                  'title',
              params: {
                dataSetId: ctrl.dataSet.id
              }
            },
            text: {
              key: 'data-set-management.edit.choose-previous-version.text'
            },
            cancelTooltip: {
              key: 'data-set-management.edit.choose-previous-version.' +
                  'cancel-tooltip'
            },
            noVersionsFound: {
              key: 'data-set-management.edit.choose-previous-version.' +
                  'no-versions-found',
              params: {
                dataSetId: ctrl.dataSet.id
              }
            },
            deleted: {
              key: 'data-set-management.edit.choose-previous-version.' +
                  'data-set-deleted'
            }
          },
          versionLabelAttribute: 'description'
        };

        ChoosePreviousVersionService.showDialog(dialogConfig, event)
            .then(function(wrapper) {
              ctrl.dataSet = new DataSetResource(
                wrapper.selection);
              if (!ctrl.dataSet.languages) {
                ctrl.dataSet.languages = [];
              }
              ctrl.initSurveyChips();
              if (wrapper.isCurrentVersion) {
                $scope.dataSetForm.$setPristine();
                SimpleMessageToastService.openSimpleMessageToast(
                  'data-set-management.edit.current-version-restored-toast',
                  {
                    dataSetId: ctrl.dataSet.id
                  });
              } else {
                $scope.dataSetForm.$setDirty();
                SimpleMessageToastService.openSimpleMessageToast(
                  'data-set-management.edit.previous-version-restored-toast',
                  {
                    dataSetId: ctrl.dataSet.id
                  });
              }
            });
      };

      $scope.registerConfirmOnDirtyHook = function() {
        var unregisterTransitionHook = $transitions.onBefore({}, function() {
          if ($scope.dataSetForm.$dirty || ctrl.attachmentOrderIsDirty) {
            return CommonDialogsService.showConfirmOnDirtyDialog();
          }
        });

        $scope.$on('$destroy', unregisterTransitionHook);
      };

      ctrl.loadAttachments = function(selectLastAttachment) {
        DataSetAttachmentResource.findByDataSetId({
            dataSetId: ctrl.dataSet.id
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
              'data-set-management.detail.attachments.' +
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
          return DataSetAttachmentUploadService.uploadAttachment(file,
              metadata);
        };

        var labels = getDialogLabels();
        labels.editTitle.params.filename = attachment.fileName;

        var getAttachmentVersions = function(id, filename, limit, skip) {
          return DataSetAttachmentVersionsResource.get({
            dataSetId: id,
            filename: filename,
            limit: limit,
            skip: skip
          }).$promise;
        };

        var createDataSetAttachmentResource = function(attachmentWrapper) {
          return new DataSetAttachmentResource(attachmentWrapper
              .dataPackageAttachment);
        };

        var dialogConfig = {
          attachmentMetadata: attachment,
          uploadCallback: upload,
          labels: labels,
          attachmentDomainIdAttribute: 'dataSetId',
          getAttachmentVersionsCallback: getAttachmentVersions,
          createAttachmentResource: createDataSetAttachmentResource
        };

        AttachmentDialogService.showDialog(dialogConfig, event)
            .then(ctrl.loadAttachments);
      };

      ctrl.getNextIndexInDataSet = function() {
        if (!ctrl.attachments || ctrl.attachments.length === 0) {
          return 0;
        }
        return _.maxBy(ctrl.attachments, function(attachment) {
          return attachment.indexInDataSet;
        }).indexInDataSet + 1;
      };

      ctrl.addAttachment = function(event) {
        var upload = function(file, attachmentMetadata) {
          var metadata = _.extend({}, attachmentMetadata, {
            dataSetId: ctrl.dataSet.id,
            dataSetNumber: ctrl.dataSet.number,
            dataAcquisitionProjectId: ctrl.dataSet.dataAcquisitionProjectId,
            indexInDataSet: ctrl.getNextIndexInDataSet()
          });
          return DataSetAttachmentUploadService.uploadAttachment(file,
              metadata);
        };

        var dialogConfig = {
          attachmentMetadata: null,
          uploadCallback: upload,
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
          attachment.indexInDataSet = index;
          promises.push(attachment.$save());
        });
        $q.all(promises).then(function() {
          SimpleMessageToastService.openSimpleMessageToast(
          'data-set-management.detail.attachments.' +
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

      ctrl.updateSurveyReferences = function() {
        ctrl.dataSet.surveyIds = [];
        ctrl.dataSet.surveyNumbers = [];
        if (ctrl.surveyChips) {
          ctrl.surveyChips.forEach(function(chip) {
            ctrl.dataSet.surveyIds.push(chip.id);
            ctrl.dataSet.surveyNumbers.push(chip.number);
          });
        }
      };

      ctrl.searchSurveys = function(query) {
        return SearchDao.search(
          query, 1,
          ctrl.dataSet.dataAcquisitionProjectId,
          null, 'surveys', 100,  ctrl.dataSet.surveyIds
        ).then(function(result) {
            return result.hits.hits;
          });
      };

      ctrl.types = [{de: 'Personendatensatz', en: 'Individual Data'},
        {de: 'Episodendatensatz', en: 'Spell Data'}
      ];

      ctrl.formats = [{de: 'breit', en: 'wide'},
        {de: 'lang', en: 'long'}
      ];

      init();
    }]);

