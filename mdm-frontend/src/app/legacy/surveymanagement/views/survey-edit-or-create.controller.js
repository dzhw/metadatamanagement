/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('SurveyEditOrCreateController', [
  'entity',
  'PageMetadataService',
  '$timeout',
  '$state',
  'BreadcrumbService',
  'Principal',
  'SimpleMessageToastService',
  'CurrentProjectService',
  'SurveyIdBuilderService',
  'SurveyResource',
  '$scope',
  'ElasticSearchAdminService',
  '$mdDialog',
  '$transitions',
  'DataPackageResource',
  'CommonDialogsService',
  'LanguageService',
  'AvailableSurveyNumbersResource',
  'SurveyAttachmentResource',
  '$q',
  'DataPackageIdBuilderService',
  'moment',
  'SurveyResponseRateImageUploadService',
  'SurveySearchService',
  '$log',
  'DataAcquisitionProjectResource',
  '$rootScope',
  'ProjectUpdateAccessService',
  'AttachmentDialogService',
  'SurveyAttachmentUploadService',
  'SurveyAttachmentVersionsResource',
  'SurveyVersionsResource',
  'ChoosePreviousVersionService',
    function(entity, PageMetadataService, $timeout,
      $state, BreadcrumbService, Principal, SimpleMessageToastService,
      CurrentProjectService, SurveyIdBuilderService, SurveyResource, $scope,
      ElasticSearchAdminService, $mdDialog, $transitions, DataPackageResource,
      CommonDialogsService, LanguageService, AvailableSurveyNumbersResource,
      SurveyAttachmentResource, $q, DataPackageIdBuilderService, moment,
      SurveyResponseRateImageUploadService, SurveySearchService, $log,
      DataAcquisitionProjectResource, $rootScope, ProjectUpdateAccessService,
      AttachmentDialogService, SurveyAttachmentUploadService,
      SurveyAttachmentVersionsResource, SurveyVersionsResource,
      ChoosePreviousVersionService) {
      var ctrl = this;
      var surveyMethodCache = {};
      var updateToolbarHeaderAndPageTitle = function() {
        if (ctrl.createMode) {
          PageMetadataService.setPageTitle(
            'survey-management.edit.create-page-title', {
            surveyId: ctrl.survey.id
          });
        } else {
          PageMetadataService.setPageTitle(
            'survey-management.edit.edit-page-title', {
            surveyId: ctrl.survey.id
          });
        }
        $rootScope.$broadcast('start-ignoring-404');
        DataPackageResource.get({id: ctrl.survey.dataPackageId}).$promise
          .then(function(dataPackage) {
          BreadcrumbService.updateToolbarHeader({
            'stateName': $state.current.name,
            'id': ctrl.survey.id,
            'dataPackageId': ctrl.survey.dataPackageId,
            'number': ctrl.survey.number,
            'dataPackageIsPresent': dataPackage != null,
            'projectId': ctrl.survey.dataAcquisitionProjectId,
            'enableLastItem': !ctrl.createMode
          });
        }).catch(function() {
          BreadcrumbService.updateToolbarHeader({
            'stateName': $state.current.name,
            'id': ctrl.survey.id,
            'dataPackageId': ctrl.survey.dataPackageId,
            'number': ctrl.survey.number,
            'dataPackageIsPresent': false,
            'projectId': ctrl.survey.dataAcquisitionProjectId,
            'enableLastItem': !ctrl.createMode
          });
        }).finally(function() {
          $rootScope.$broadcast('stop-ignoring-404');
        });
      };

      var displayError = function(error) {
        var msg = _.get(error, 'errors[0].message');
        if (msg) {
          SimpleMessageToastService.openAlertMessageToast(msg);
        }
      };

      var redirectToSearchView = function() {
        $timeout(function() {
          $state.go('search', {
            lang: LanguageService.getCurrentInstantly(),
            type: 'surveys'
          });
        }, 1000);
      };

      var handleReleasedProject = function() {
        SimpleMessageToastService.openAlertMessageToast(
          'survey-management.edit.choose-unreleased-project-toast');
        redirectToSearchView();
      };

      var init = function() {
        if (Principal.hasAnyAuthority(['ROLE_PUBLISHER',
            'ROLE_DATA_PROVIDER'])) {
          if (entity) {
            entity.$promise.then(function(survey) {
              ctrl.createMode = false;
              DataAcquisitionProjectResource.get({
                id: survey.dataAcquisitionProjectId
              }).$promise.then(function(project) {
                if (project.release != null && !project.release.isPreRelease) {
                  handleReleasedProject();
                } else if (!ProjectUpdateAccessService
                  .isUpdateAllowed(project, 'surveys', true)) {
                  redirectToSearchView();
                } else {
                  CurrentProjectService.setCurrentProject(project);
                  ctrl.survey = survey;
                  ctrl.currentSurveyMethod = survey.surveyMethod;
                  $scope.responseRateInitializing = true;
                  ctrl.loadAttachments();
                  updateToolbarHeaderAndPageTitle();
                  $scope.registerConfirmOnDirtyHook();
                  SurveyResponseRateImageUploadService.getImage(
                    ctrl.survey.id, ctrl.survey.number, 'en')
                    .then(function(image) {
                      ctrl.responseRateImageEn = image;
                    });
                  SurveyResponseRateImageUploadService.getImage(
                    ctrl.survey.id, ctrl.survey.number, 'de')
                    .then(function(image) {
                      ctrl.responseRateImageDe = image;
                    });
                }
              });
            });
          } else {
            if (CurrentProjectService.getCurrentProject() &&
            (!CurrentProjectService.getCurrentProject().release 
              || CurrentProjectService.getCurrentProject().release.isPreRelease)) {
              if (!ProjectUpdateAccessService
                .isUpdateAllowed(CurrentProjectService.getCurrentProject(),
                   'surveys', true)) {
                redirectToSearchView();
              } else {
                ctrl.createMode = true;
                AvailableSurveyNumbersResource.get({
                  id: CurrentProjectService.getCurrentProject().id
                }).$promise.then(
                  function(surveyNumbers) {
                    if (surveyNumbers.length === 1) {
                      ctrl.survey = new SurveyResource({
                        id: SurveyIdBuilderService.buildSurveyId(
                          CurrentProjectService.getCurrentProject().id,
                          surveyNumbers[0]
                        ),
                        number: surveyNumbers[0],
                        dataAcquisitionProjectId:
                        CurrentProjectService.getCurrentProject().id,
                        dataPackageId: DataPackageIdBuilderService
                          .buildDataPackageId(CurrentProjectService
                            .getCurrentProject().id
                        ),
                        serialNumber: 1
                      });
                      $scope.responseRateInitializing = true;
                      updateToolbarHeaderAndPageTitle();
                      $scope.registerConfirmOnDirtyHook();
                    } else {
                      $mdDialog.show({
                        controller: 'ChooseSurveyNumberController',
                        templateUrl: 'scripts/surveymanagement/' +
                        'views/choose-survey-number.html.tmpl',
                        clickOutsideToClose: false,
                        fullscreen: true,
                        locals: {
                          availableSurveyNumbers: surveyNumbers
                        }
                      })
                      .then(function(response) {
                        ctrl.survey = new SurveyResource({
                          id: SurveyIdBuilderService.buildSurveyId(
                            CurrentProjectService.getCurrentProject().id,
                            response.surveyNumber
                          ),
                          number: response.surveyNumber,
                          dataAcquisitionProjectId:
                          CurrentProjectService.getCurrentProject().id,
                          dataPackageId: DataPackageIdBuilderService
                            .buildDataPackageId(CurrentProjectService
                              .getCurrentProject().id
                          ),
                          serialNumber: 1
                        });
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
          'survey-management.edit.not-authorized-toast');
        }
      };

      var getDialogLabels = function() {
        return {
          createTitle: {
            key: 'survey-management.detail.attachments.create-title',
            params: {
              surveyId: ctrl.survey.id
            }
          },
          editTitle: {
            key: 'survey-management.detail.attachments.edit-title',
            params: {
              surveyId: ctrl.survey.id
            }
          },
          hints: {
            file: {
              key: 'survey-management.detail.attachments.hints.filename'
            }
          }
        };
      };

      ctrl.saveSurvey = function() {
        if ($scope.surveyForm.$valid) {
          if (CurrentProjectService.getCurrentProject() &&
              CurrentProjectService.getCurrentProject().release &&
              CurrentProjectService.getCurrentProject().release.isPreRelease
          ) {
            CommonDialogsService.showConfirmEditPreReleaseDialog(
              'global.common-dialogs' +
              '.confirm-edit-pre-released-project.title',
              {},
              'global.common-dialogs' +
              '.confirm-edit-pre-released-project.content',
              {},
              null
            ).then(function success() {
              if (angular.isUndefined(ctrl.survey.masterId)) {
                ctrl.survey.masterId = ctrl.survey.id;
              }
              ctrl.survey.$save()
              .then(ctrl.updateElasticSearchIndex)
              .then(ctrl.onSavedSuccessfully)
              .catch(function(error) {
                  $log.error(error);
                  SimpleMessageToastService.openAlertMessageToast(
                    'survey-management.edit.error-on-save-toast',
                    {surveyId: ctrl.survey.id});
                });
            });
          } else {
            if (angular.isUndefined(ctrl.survey.masterId)) {
              ctrl.survey.masterId = ctrl.survey.id;
            }
            ctrl.survey.$save()
            .then(ctrl.updateElasticSearchIndex)
            .then(ctrl.onSavedSuccessfully)
            .catch(function(error) {
                $log.error(error);
                SimpleMessageToastService.openAlertMessageToast(
                  'survey-management.edit.error-on-save-toast',
                  {surveyId: ctrl.survey.id});
              });
          }
        } else {
          // ensure that all validation errors are visible
          angular.forEach($scope.surveyForm.$error, function(field) {
            angular.forEach(field, function(errorField) {
              if (errorField.$setTouched) {
                errorField.$setTouched();
              } else if (errorField.$setDirty) {
                // could be a ngForm which doesn't have $setTouched
                errorField.$setDirty();
              }
            });
          });
          SimpleMessageToastService.openAlertMessageToast(
            'survey-management.edit.survey-has-validation-errors-toast',
            null);
        }
      };

      ctrl.updateElasticSearchIndex = function() {
        return $q.all([
          ElasticSearchAdminService.
            processUpdateQueue('surveys'),
          ElasticSearchAdminService.
            processUpdateQueue('data_packages')
        ]);
      };

      ctrl.onSavedSuccessfully = function() {
        $scope.surveyForm.$setPristine();
        SimpleMessageToastService.openSimpleMessageToast(
          'survey-management.edit.success-on-save-toast',
          {surveyId: ctrl.survey.id});
        if (ctrl.createMode) {
          $state.go('surveyEdit', {id: ctrl.survey.id});
        }
      };

      ctrl.openRestorePreviousVersionDialog = function(event) {
        var getVersions = function(id, limit, skip) {
          return SurveyVersionsResource.get({
            id: id,
            limit: limit,
            skip: skip
          }).$promise;
        };

        var dialogConfig = {
          domainId: ctrl.survey.id,
          getPreviousVersionsCallback: getVersions,
          labels: {
            title: {
              key: 'survey-management.edit.choose-previous-version.title',
              params: {
                surveyId: ctrl.survey.id
              }
            },
            text: {
              key: 'survey-management.edit.choose-previous-version.text'
            },
            cancelTooltip: {
              key: 'survey-management.edit.choose-previous-version.' +
                  'cancel-tooltip'
            },
            noVersionsFound: {
              key: 'survey-management.edit.choose-previous-version.' +
                  'no-versions-found',
              params: {
                surveyId: ctrl.survey.id
              }
            },
            deleted: {
              key: 'survey-management.edit.choose-previous-version.' +
                  'survey-deleted'
            }
          }
        };

        ChoosePreviousVersionService.showDialog(dialogConfig, event)
            .then(function(wrapper) {
              ctrl.survey = new SurveyResource(wrapper.selection);
              if (!ctrl.survey.serialNumber) {
                ctrl.survey.serialNumber = 1;
              }
              $scope.responseRateInitializing = true;
              if (wrapper.isCurrentVersion) {
                $scope.surveyForm.$setPristine();
                SimpleMessageToastService.openSimpleMessageToast(
                    'survey-management.edit.current-version-restored-toast',
                    {
                      surveyId: ctrl.survey.id
                    });
              } else {
                $scope.surveyForm.$setDirty();
                SimpleMessageToastService.openSimpleMessageToast(
                    'survey-management.edit.previous-version-restored-toast',
                    {
                      surveyId: ctrl.survey.id
                    });
              }
            });
      };

      $scope.registerConfirmOnDirtyHook = function() {
        var unregisterTransitionHook = $transitions.onBefore({}, function() {
          if ($scope.surveyForm.$dirty || ctrl.attachmentOrderIsDirty ||
            ctrl.responseRateImageDeDirty || ctrl.responseRateImageEnDirty) {
            return CommonDialogsService.showConfirmOnDirtyDialog();
          }
        });

        $scope.$on('$destroy', unregisterTransitionHook);
      };

      ctrl.loadAttachments = function(selectLastAttachment) {
        SurveyAttachmentResource.findBySurveyId({
            surveyId: ctrl.survey.id
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
              'survey-management.detail.attachments.attachment-deleted-toast',
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
          return SurveyAttachmentUploadService.uploadAttachment(file, metadata);
        };

        var labels = getDialogLabels();
        labels.editTitle.params.filename = attachment.fileName;

        var getAttachmentVersions = function(id, filename, limit, skip) {
          return SurveyAttachmentVersionsResource.get({
            surveyId: id,
            filename: filename,
            limit: limit,
            skip: skip
          }).$promise;
        };

        var createSurveyAttachmentResource = function(attachmentWrapper) {
          return new SurveyAttachmentResource(attachmentWrapper
              .dataPackageAttachment);
        };

        var dialogConfig = {
          attachmentMetadata: attachment,
          attachmentDomainIdAttribute: 'surveyId',
          getAttachmentVersionsCallback: getAttachmentVersions,
          uploadCallback: upload,
          createAttachmentResource: createSurveyAttachmentResource,
          labels: labels
        };

        AttachmentDialogService.showDialog(dialogConfig, event)
          .then(ctrl.loadAttachments);
      };

      ctrl.getNextIndexInSurvey = function() {
        if (!ctrl.attachments || ctrl.attachments.length === 0) {
          return 0;
        }
        return _.maxBy(ctrl.attachments, function(attachment) {
          return attachment.indexInSurvey;
        }).indexInSurvey + 1;
      };

      ctrl.addAttachment = function(event) {
        var upload = function(file, attachmentMetadata) {
          var metadata = _.extend({}, attachmentMetadata, {
            surveyId: ctrl.survey.id,
            surveyNumber: ctrl.survey.number,
            dataAcquisitionProjectId: ctrl.survey.dataAcquisitionProjectId,
            indexInSurvey: ctrl.getNextIndexInSurvey()
          });
          return SurveyAttachmentUploadService.uploadAttachment(file, metadata);
        };

        var dialogConfig = {
          attachmentMetadata: null,
          uploadCallback: upload,
          labels: getDialogLabels()
        };

        if (CurrentProjectService.getCurrentProject() &&
            CurrentProjectService.getCurrentProject().release &&
            CurrentProjectService.getCurrentProject().release.isPreRelease
        ) {
          CommonDialogsService.showConfirmAddAttachmentPreReleaseDialog(
            'global.common-dialogs' +
            '.confirm-edit-pre-released-project.attachment-title',
            {},
            'global.common-dialogs' +
            '.confirm-edit-pre-released-project.attachment-content',
            {},
            null
          ).then(function success() {
            AttachmentDialogService
              .showDialog(dialogConfig, event)
              .then(function() {
                ctrl.loadAttachments(true);
              });
          });
        } else {
          AttachmentDialogService
          .showDialog(dialogConfig, event)
          .then(function() {
            ctrl.loadAttachments(true);
          });
        }
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
          attachment.indexInSurvey = index;
          promises.push(attachment.$save());
        });
        $q.all(promises).then(function() {
          SimpleMessageToastService.openSimpleMessageToast(
          'survey-management.detail.attachments.attachment-order-saved-toast',
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

      ctrl.dataTypes = [
        {de: 'Quantitative Daten', en: 'Quantitative Data'},
        {de: 'Qualitative Daten', en: 'Qualitative Data'}
      ];

      ctrl.validatePeriod = function() {
        $timeout(function() {
          if (!moment(ctrl.survey.fieldPeriod.start).isSameOrBefore(
            moment(ctrl.survey.fieldPeriod.end))) {
            $scope.surveyForm.fieldPeriodEnd.$setValidity('mindate', false);
            $scope.surveyForm.fieldPeriodEnd.$setTouched();
          } else {
            $scope.surveyForm.fieldPeriodEnd.$setValidity('mindate', true);
            $scope.surveyForm.fieldPeriodEnd.$setTouched();
          }});
      };

      ctrl.saveResponseRateImageDe = function(file) {
        ctrl.responseRateImageDe = file;
        ctrl.responseRateImageDeDirty = true;
      };

      ctrl.deleteResponseRateImageDe = function() {
        delete ctrl.responseRateImageDe;
        ctrl.responseRateImageDeDirty = true;
      };

      ctrl.uploadOrDeleteResponseRateImageDe = function() {
        var metadata = {
          dataAcquisitionProjectId: ctrl.survey
            .dataAcquisitionProjectId,
          surveyId: ctrl.survey.id,
          surveyNumber: ctrl.survey.number
        };
        if (!ctrl.responseRateImageDe) {
          SurveyResponseRateImageUploadService.deleteImage(
            ctrl.survey.id, ctrl.survey.number, 'de'
          ).then(function() {
            SimpleMessageToastService.openSimpleMessageToast(
              'survey-management.edit.survey-image-deleted-toast',
              null);
            ctrl.responseRateImageDeDirty = false;
          }, displayError);
        } else {
          SurveyResponseRateImageUploadService.uploadImage(
            ctrl.responseRateImageDe, metadata, ctrl.survey.number, 'de')
            .then(function() {
              SimpleMessageToastService.openSimpleMessageToast(
                'survey-management.edit.survey-image-saved-toast',
                null);
              ctrl.responseRateImageDeDirty = false;
            }, displayError);
        }
      };

      ctrl.saveResponseRateImageEn = function(file) {
        ctrl.responseRateImageEn = file;
        ctrl.responseRateImageEnDirty = true;
      };

      ctrl.deleteResponseRateImageEn = function() {
        delete ctrl.responseRateImageEn;
        ctrl.responseRateImageEnDirty = true;
      };

      ctrl.uploadOrDeleteResponseRateImageEn = function() {
        var metadata = {
          dataAcquisitionProjectId: ctrl.survey
            .dataAcquisitionProjectId,
          surveyId: ctrl.survey.id,
          surveyNumber: ctrl.survey.number
        };
        if (!ctrl.responseRateImageEn) {
          SurveyResponseRateImageUploadService.deleteImage(
            ctrl.survey.id, ctrl.survey.number, 'en'
          ).then(function() {
            SimpleMessageToastService.openSimpleMessageToast(
              'survey-management.edit.survey-image-deleted-toast',
              null);
            ctrl.responseRateImageEnDirty = false;
          }, displayError);
        } else {
          SurveyResponseRateImageUploadService.uploadImage(
            ctrl.responseRateImageEn, metadata, ctrl.survey.number, 'en')
            .then(function() {
              SimpleMessageToastService.openSimpleMessageToast(
                'survey-management.edit.survey-image-saved-toast',
                null);
              ctrl.responseRateImageEnDirty = false;
            }, displayError);
        }
      };

      ctrl.searchSurveyMethods = function(searchText, language) {
        if (searchText === surveyMethodCache.searchText &&
          language === surveyMethodCache.language) {
          return surveyMethodCache.searchResult;
        }

        //Search Call to Elasticsearch
        return SurveySearchService.findSurveyMethods(searchText, {},
            language, true)
          .then(function(surveyMethods) {
            surveyMethodCache.searchText = searchText;
            surveyMethodCache.language = language;
            surveyMethodCache.searchResult = surveyMethods;
            return surveyMethods;
          });
      };

      $scope.$watchGroup(['ctrl.survey.sampleSize',
        'ctrl.survey.grossSampleSize'], function() {
            if (!$scope.responseRateInitializing && ctrl.survey) {
              if (ctrl.survey.sampleSize != null &&
                ctrl.survey.grossSampleSize) {
                ctrl.survey.responseRate = Number((
                  ctrl.survey.sampleSize / ctrl.survey.grossSampleSize * 100
                ).toFixed(2));
                $scope.surveyForm.responseRate.$setTouched();
              }
            }
            $scope.responseRateInitializing = false;
          });

      init();
    }]);

