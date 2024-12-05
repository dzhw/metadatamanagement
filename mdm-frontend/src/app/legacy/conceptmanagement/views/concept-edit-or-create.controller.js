/* global _*/
'use strict';

angular.module('metadatamanagementApp')
  .controller('ConceptEditOrCreateController', [
  'entity',
  'PageMetadataService',
  '$timeout',
  '$state',
  'BreadcrumbService',
  'Principal',
  'SimpleMessageToastService',
  'ConceptResource',
  'ConceptSearchService',
  '$scope',
  '$q',
  'ElasticSearchAdminService',
  'ElsstSearchService',
  '$transitions',
  'CommonDialogsService',
  'LanguageService',
  'ConceptAttachmentUploadService',
  'ConceptAttachmentResource',
  'AttachmentDialogService',
  'ConceptAttachmentVersionsResource',
  'ChoosePreviousVersionService',
  'ConceptVersionsResource',
  '$mdDialog',
  'CurrentProjectService',
    function(entity, PageMetadataService, $timeout,
      $state, BreadcrumbService, Principal, SimpleMessageToastService,
      ConceptResource, ConceptSearchService, $scope, $q,
      ElasticSearchAdminService, ElsstSearchService, $transitions,
      CommonDialogsService, LanguageService, ConceptAttachmentUploadService,
      ConceptAttachmentResource, AttachmentDialogService,
      ConceptAttachmentVersionsResource, ChoosePreviousVersionService,
      ConceptVersionsResource, $mdDialog, CurrentProjectService) {

      var ctrl = this;

      ctrl.conceptTagSearch = ConceptSearchService.findTags;
      ctrl.findTagsElsst = ElsstSearchService.findTagsElsst;

      var conceptAttachmentTypes = [
        {de: 'Dokumentation', en: 'Documentation'},
        {de: 'Instrument', en: 'Instrument'},
        {de: 'Sonstiges', en: 'Other'}
      ];

      ctrl.translationKeysAuthors = {
        title: 'concept-management.detail.label.authors',
        tooltips: {
          delete: 'concept-management.edit.delete-author-tooltip',
          add: 'concept-management.edit.add-author-tooltip',
          moveUp: 'concept-management.edit.move-author-up-tooltip',
          moveDown: 'concept-management.edit.move-author-down-tooltip'
        },
        hints: {
          firstName: 'concept-management.edit.hints' +
            '.authors.first-name',
          middleName: 'concept-management.edit.hints' +
            '.authors.middle-name',
          lastName: 'concept-management.edit.hints.authors.last-name'
        }
      };

      var updateToolbarHeaderAndPageTitle = function() {
        if (ctrl.createMode) {
          PageMetadataService.setPageTitle(
            'concept-management.edit.create-page-title', {
              conceptId: ctrl.concept.id
            });
        } else {
          PageMetadataService.setPageTitle(
            'concept-management.edit.edit-page-title', {
              conceptId: ctrl.concept.id
            });
        }
        BreadcrumbService.updateToolbarHeader({
          'stateName': $state.current.name,
          'id': ctrl.concept.id,
          'enableLastItem': !ctrl.createMode
        });
      };

      var redirectToSearchView = function() {
        $timeout(function() {
          $state.go('search', {
            lang: LanguageService.getCurrentInstantly(),
            type: 'concepts'
          });
        }, 1000);
      };

      var initEditMode = function(concept) {
        ctrl.createMode = false;
        ctrl.concept = concept;
        ctrl.loadAttachments();
        updateToolbarHeaderAndPageTitle();
        $scope.registerConfirmOnDirtyHook();
      };

      var init = function() {
        if (Principal
            .hasAuthority('ROLE_PUBLISHER')) {
          if (entity) {
            entity.$promise.then(function(concept) {
              initEditMode(concept);
            });
          } else {
            ctrl.createMode = true;
            ctrl.concept = new ConceptResource({
              authors: [{
                firstName: '',
                lastName: ''
              }],
              originalLanguages: []
            });
            updateToolbarHeaderAndPageTitle();
            $scope.registerConfirmOnDirtyHook();
          }
        } else {
          SimpleMessageToastService.openAlertMessageToast(
            'concept-management.edit.not-authorized-toast');
          if (Principal.isAuthenticated()) {
            redirectToSearchView();
          }
        }
      };

      var getDialogLabels = function() {
        return {
          createTitle: {
            key: 'concept-management.detail.attachments.create-title',
            params: {
              conceptId: ctrl.concept.id
            }
          },
          editTitle: {
            key: 'concept-management.detail.attachments.edit-title',
            params: {
              conceptId: ctrl.concept.id
            }
          },
          hints: {
            file: {
              key: 'concept-management.detail.attachments.hints.filename'
            }
          }
        };
      };

      ctrl.saveConcept = function() {
        if ($scope.conceptForm.$valid) {
          ctrl.concept.$save()
            .then(ctrl.updateElasticSearchIndex)
            .then(ctrl.onSavedSuccessfully)
            .catch(function() {
              SimpleMessageToastService.openAlertMessageToast(
                'concept-management.edit.error-on-save-toast', {
                  conceptId: ctrl.concept.id
                });
            });
        } else {
          // ensure that all validation errors are visible
          angular.forEach($scope.conceptForm.$error, function(field) {
            angular.forEach(field, function(errorField) {
              if (errorField.$setTouched) {
                errorField.$setTouched();
              } else if (errorField.$setDirty) {
                errorField.$setDirty();
              }
            });
          });
          SimpleMessageToastService.openAlertMessageToast(
            'concept-management.edit.concept-has-validation-errors-toast',
            null);
        }
      };

      ctrl.updateElasticSearchIndex = function() {
        return ElasticSearchAdminService.processUpdateQueue('concepts');
      };

      ctrl.onSavedSuccessfully = function() {
        $scope.conceptForm.$setPristine();
        SimpleMessageToastService.openSimpleMessageToast(
          'concept-management.edit.success-on-save-toast', {
            conceptId: ctrl.concept.id
          });
        if (ctrl.createMode) {
          $state.go('conceptEdit', {
            id: ctrl.concept.id
          });
        }
      };

      ctrl.openRestorePreviousVersionDialog = function(event) {
        var getVersions = function(id, limit, skip) {
          return ConceptVersionsResource.get({
            id: id,
            limit: limit,
            skip: skip
          }).$promise;
        };

        var dialogConfig = {
          domainId: ctrl.concept.id,
          getPreviousVersionsCallback: getVersions,
          labels: {
            title: {
              key: 'concept-management.edit.choose-previous-version.' +
                  'title',
              params: {
                conceptId: ctrl.concept.id
              }
            },
            text: {
              key: 'concept-management.edit.choose-previous-version.text'
            },
            cancelTooltip: {
              key: 'concept-management.edit.choose-previous-version.' +
                  'cancel-tooltip'
            },
            noVersionsFound: {
              key: 'concept-management.edit.choose-previous-version.' +
                  'no-versions-found',
              params: {
                conceptId: ctrl.concept.id
              }
            },
            deleted: {
              key: 'concept-management.edit.choose-previous-version.' +
                  'concept-deleted'
            }
          },
          versionLabelAttribute: 'title'
        };

        ChoosePreviousVersionService.showDialog(dialogConfig, event)
            .then(function(wrapper) {
              ctrl.concept = new ConceptResource(
                wrapper.selection);
              if (wrapper.isCurrentVersion) {
                $scope.conceptForm.$setPristine();
                SimpleMessageToastService.openSimpleMessageToast(
                  'concept-management.edit.current-version-restored-toast',
                  {
                    conceptId: ctrl.concept.id
                  });
              } else {
                $scope.conceptForm.$setDirty();
                SimpleMessageToastService.openSimpleMessageToast(
                  'concept-management.edit.previous-version-restored-toast',
                  {
                    conceptId: ctrl.concept.id
                  });
              }
            });
      };

      $scope.registerConfirmOnDirtyHook = function() {
        var unregisterTransitionHook = $transitions.onBefore({}, function() {
          if ($scope.conceptForm.$dirty || ctrl.attachmentOrderIsDirty) {
            return CommonDialogsService.showConfirmOnDirtyDialog();
          }
        });

        $scope.$on('$destroy', unregisterTransitionHook);
      };

      ctrl.loadAttachments = function(selectLastAttachment) {
        ConceptAttachmentResource.findByConceptId({
          conceptId: ctrl.concept.id
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
                'concept-management.detail.attachments.' +
                'attachment-deleted-toast',
                {
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
          return ConceptAttachmentUploadService.uploadAttachment(file,
              metadata);
        };

        var labels = getDialogLabels();
        labels.editTitle.params.filename = attachment.fileName;

        var getAttachmentVersions = function(id, filename, limit, skip) {
          return ConceptAttachmentVersionsResource.get({
            conceptId: id,
            filename: filename,
            limit: limit,
            skip: skip
          }).$promise;
        };

        var createConceptAttachmentResource = function(attachmentWrapper) {
          return new ConceptAttachmentResource(attachmentWrapper
              .dataPackageAttachment);
        };

        var dialogConfig = {
          attachmentMetadata: attachment,
          attachmentTypes: conceptAttachmentTypes,
          uploadCallback: upload,
          labels: labels,
          attachmentDomainIdAttribute: 'conceptId',
          getAttachmentVersionsCallback: getAttachmentVersions,
          createAttachmentResource: createConceptAttachmentResource
        };

        AttachmentDialogService.showDialog(dialogConfig, event)
            .then(ctrl.loadAttachments);
      };

      ctrl.getNextIndexInConcept = function() {
        if (!ctrl.attachments || ctrl.attachments.length === 0) {
          return 0;
        }
        return _.maxBy(ctrl.attachments, function(attachment) {
          return attachment.indexInConcept;
        }).indexInConcept + 1;
      };

      ctrl.addAttachment = function(event) {
        var upload = function(file, attachmentMetadata) {
          var metadata = _.extend({}, attachmentMetadata, {
            conceptId: ctrl.concept.id,
            indexInConcept: ctrl.getNextIndexInConcept()
          });
          return ConceptAttachmentUploadService.uploadAttachment(file,
              metadata);
        };

        var dialogConfig = {
          attachmentMetadata: null,
          attachmentTypes: conceptAttachmentTypes,
          uploadCallback: upload,
          labels: getDialogLabels()
        };

        if (CurrentProjectService.getCurrentProject().release.isPreRelease) {
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
          attachment.indexInConcept = index;
          promises.push(attachment.$save());
        });
        $q.all(promises).then(function() {
          SimpleMessageToastService.openSimpleMessageToast(
            'concept-management.detail.attachments.' +
            'attachment-order-saved-toast',
            {});
          ctrl.attachmentOrderIsDirty = false;
        });
      };

      ctrl.selectAttachment = function(index) {
        if (Principal
            .hasAuthority('ROLE_PUBLISHER')) {
          ctrl.currentAttachmentIndex = index;
        }
      };

      /**
       * Displays an info modal.
       * @param {*} $event the click event
       */
      ctrl.infoModal = function( $event) {
        $mdDialog.show({
          controller: 'dataPackageInfoController',
          templateUrl: 'scripts/datapackagemanagement/components/elsst-info.html.tmpl',
          clickOutsideToClose: true,
          escapeToClose: true,
          fullscreen: true,
          targetEvent: $event
        });
      };

      init();
    }]);

