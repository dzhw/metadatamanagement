/* global _, $, document */
'use strict';

angular.module('metadatamanagementApp')
  .controller('DataSetEditOrCreateController',
    function(entity, PageTitleService, $timeout,
      $state, ToolbarHeaderService, Principal, SimpleMessageToastService,
      CurrentProjectService, DataSetIdBuilderService, DataSetResource,
      $scope, SurveyIdBuilderService, $document,
      ElasticSearchAdminService, $mdDialog, $transitions, StudyResource,
      CommonDialogsService, LanguageService, AvailableDataSetNumbersResource,
      DataSetAttachmentResource, $q, StudyIdBuilderService, SearchDao,
      DataAcquisitionProjectResource, $rootScope, ProjectUpdateAccessService) {
      var ctrl = this;
      ctrl.surveyChips = [];
      var updateToolbarHeaderAndPageTitle = function() {
        if (ctrl.createMode) {
          PageTitleService.setPageTitle(
            'data-set-management.edit.create-page-title', {
            dataSetId: ctrl.dataSet.id
          });
        } else {
          PageTitleService.setPageTitle(
            'data-set-management.edit.edit-page-title', {
            dataSetId: ctrl.dataSet.id
          });
        }
        $rootScope.$broadcast('start-ignoring-404');
        StudyResource.get({id: ctrl.dataSet.studyId}).$promise
          .then(function(study) {
          ToolbarHeaderService.updateToolbarHeader({
            'stateName': $state.current.name,
            'dataSetId': ctrl.dataSet.id,
            'studyId': ctrl.dataSet.studyId,
            'dataSetNumber': ctrl.dataSet.number,
            'studyIsPresent': study != null,
            'projectId': ctrl.dataSet.dataAcquisitionProjectId,
            'enableLastItem': !ctrl.createMode,
            'dataSetIsPresent': !ctrl.createMode
          });
        }).catch(function() {
          ToolbarHeaderService.updateToolbarHeader({
            'stateName': $state.current.name,
            'dataSetId': ctrl.dataSet.id,
            'studyId': ctrl.dataSet.studyId,
            'dataSetNumber': ctrl.dataSet.number,
            'studyIsPresent': false,
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

      var handleUserNotInAssigneeGroup = function() {
        SimpleMessageToastService.openAlertMessageToast(
          'global.error.client-error.not-in-assignee-group');
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
                  .isUpdateAllowed(project, 'data_sets')) {
                  handleUserNotInAssigneeGroup(project);
                } else {
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
                        studyId: StudyIdBuilderService.buildStudyId(
                          CurrentProjectService.getCurrentProject().id
                        ),
                        subDataSets: [{
                          name: ''
                        }]
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
                            studyId: StudyIdBuilderService.buildStudyId(
                              CurrentProjectService.getCurrentProject().id
                            ),
                            subDataSets: [{
                              name: ''
                            }]
                          });
                          $scope.responseRateInitializing = true;
                          updateToolbarHeaderAndPageTitle();
                          $scope.registerConfirmOnDirtyHook();
                        });
                    }
                  });
            } else {
              handleReleasedProject();
            }
          }
        } else {
          SimpleMessageToastService.openAlertMessageToast(
          'data-set-management.edit.not-authorized-toast');
        }
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
        return ElasticSearchAdminService.processUpdateQueue('data_sets');
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
        $mdDialog.show({
            controller: 'ChoosePreviousDataSetVersionController',
            templateUrl: 'scripts/datasetmanagement/' +
              'views/choose-previous-data-set-version.html.tmpl',
            clickOutsideToClose: false,
            fullscreen: true,
            locals: {
              dataSetId: ctrl.dataSet.id
            },
            targetEvent: event
          })
          .then(function(dataSetWrapper) {
            ctrl.dataSet = new DataSetResource(
              dataSetWrapper.dataSet);
            ctrl.initSurveyChips();
            if (dataSetWrapper.isCurrentVersion) {
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
        $mdDialog.show({
            controller: 'DataSetAttachmentEditOrCreateController',
            controllerAs: 'ctrl',
            templateUrl: 'scripts/datasetmanagement/' +
              'views/data-set-attachment-edit-or-create.html.tmpl',
            clickOutsideToClose: false,
            fullscreen: true,
            locals: {
              dataSetAttachmentMetadata: attachment
            },
            targetEvent: event
          }).then(function() {
          ctrl.loadAttachments();
        });
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
        $mdDialog.show({
            controller: 'DataSetAttachmentEditOrCreateController',
            controllerAs: 'ctrl',
            templateUrl: 'scripts/datasetmanagement/' +
              'views/data-set-attachment-edit-or-create.html.tmpl',
            clickOutsideToClose: false,
            fullscreen: true,
            locals: {
              dataSetAttachmentMetadata: {
                indexInDataSet: ctrl.getNextIndexInDataSet(),
                dataSetId: ctrl.dataSet.id,
                dataSetNumber: ctrl.dataSet.number,
                dataAcquisitionProjectId:
                  ctrl.dataSet.dataAcquisitionProjectId
              }
            },
            targetEvent: event
          }).then(function() {
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
    });
