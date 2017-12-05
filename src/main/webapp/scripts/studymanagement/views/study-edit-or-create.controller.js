'use strict';

angular.module('metadatamanagementApp')
  .controller('StudyEditOrCreateController',
    function(entity, PageTitleService, $document, $timeout,
      $state, ToolbarHeaderService, Principal, SimpleMessageToastService,
      CurrentProjectService, StudyIdBuilderService, StudyResource, $scope,
      ElasticSearchAdminService, $mdDialog) {
      var ctrl = this;

      var updateToolbarHeaderAndPageTitle = function() {
        if (ctrl.createMode) {
          PageTitleService.setPageTitle(
            'study-management.edit.create-page-title', {
            studyId: ctrl.study.id
          });
        } else {
          PageTitleService.setPageTitle(
            'study-management.edit.edit-page-title', {
            studyId: ctrl.study.id
          });
        }
        ToolbarHeaderService.updateToolbarHeader({
          'stateName': $state.current.name,
          'id': ctrl.study.id,
          'studyId': ctrl.study.id,
          'studyIsPresent': !ctrl.createMode,
          'projectId': ctrl.study.dataAcquisitionProjectId,
          'enableLastItem': true
        });
      };

      var init = function() {
        if (Principal.hasAuthority('ROLE_PUBLISHER')) {
          if (entity) {
            entity.$promise.then(function(study) {
              ctrl.study = study;
              updateToolbarHeaderAndPageTitle();
            });
          } else {
            StudyResource.get({
              id: StudyIdBuilderService.buildStudyId(
                CurrentProjectService.getCurrentProject().id)
            }).$promise.then(function(study) {
              ctrl.study = study;
              updateToolbarHeaderAndPageTitle();
            }).catch(function() {
              ctrl.createMode = true;
              ctrl.study = new StudyResource({
                id: StudyIdBuilderService.buildStudyId(
                  CurrentProjectService.getCurrentProject().id),
                dataAcquisitionProjectId:
                  CurrentProjectService.getCurrentProject().id,
                authors: [{firstName: '', lastName: ''}],
                doi: StudyIdBuilderService.buildDoi(
                  CurrentProjectService.getCurrentProject().id)
              });
              updateToolbarHeaderAndPageTitle();
            });
          }
        } else {
          // TODO show not authorized message
          /*SimpleMessageToastService.openSimpleMessageToast(
          'study-management.detail.not-released-toast', {id: study.id}
        );*/
        }
      };

      ctrl.dataAvailabilities = [
        {de: 'Verfügbar', en: 'Available'},
        {de: 'In Aufbereitung', en: 'In preparation'},
        {de: 'Nicht verfügbar', en: 'Not available'}
      ];

      ctrl.surveyDesigns = [
        {de: 'Panel', en: 'Panel'},
        {de: 'Querschnitt', en: 'Cross-Section'}
      ];

      ctrl.deleteAuthor = function(index) {
        ctrl.study.authors.splice(index, 1);
        $scope.studyForm.$setDirty();
      };

      ctrl.addAuthor = function() {
        ctrl.study.authors.push({firstName: '', lastName: ''});
        $timeout(function() {
          $document.find('input[name="authorsFirstName_' +
            (ctrl.study.authors.length - 1) + '"]')
            .focus();
        });
      };

      ctrl.setCurrentAuthor = function(index, event) {
        ctrl.currentAuthorInputName = event.target.name;
        ctrl.currentAuthor = index;
      };

      ctrl.deleteCurrentAuthor = function(event) {
        if (event.relatedTarget && (
          event.relatedTarget.id === 'move-author-up-button' ||
          event.relatedTarget.id === 'move-author-down-button')) {
          return;
        }
        delete ctrl.currentAuthor;
      };

      ctrl.moveCurrentAuthorUp = function() {
        var a = ctrl.study.authors[ctrl.currentAuthor - 1];
        ctrl.study.authors[ctrl.currentAuthor - 1] =
          ctrl.study.authors[ctrl.currentAuthor];
        ctrl.study.authors[ctrl.currentAuthor] = a;
        ctrl.currentAuthorInputName = ctrl.currentAuthorInputName
          .replace('_' + ctrl.currentAuthor, '_' + (ctrl.currentAuthor - 1));
        $document.find('input[name="' + ctrl.currentAuthorInputName + '"]')
          .focus();
        $scope.studyForm.$setDirty();
      };

      ctrl.moveCurrentAuthorDown = function() {
        var a = ctrl.study.authors[ctrl.currentAuthor + 1];
        ctrl.study.authors[ctrl.currentAuthor + 1] =
          ctrl.study.authors[ctrl.currentAuthor];
        ctrl.study.authors[ctrl.currentAuthor] = a;
        ctrl.currentAuthorInputName = ctrl.currentAuthorInputName
          .replace('_' + ctrl.currentAuthor, '_' + (ctrl.currentAuthor + 1));
        $document.find('input[name="' + ctrl.currentAuthorInputName + '"]')
          .focus();
        $scope.studyForm.$setDirty();
      };

      ctrl.saveStudy = function() {
        ctrl.study.$save()
        .then(ctrl.updateElasticSearchIndex)
        .then(ctrl.onSavedSuccessfully)
        .catch(function(error) {
          console.log(error);
          SimpleMessageToastService.openSimpleMessageToast(
            'study-management.edit.error-on-save-toast',
            {studyId: ctrl.study.id});
        });
      };

      ctrl.updateElasticSearchIndex = function() {
        return ElasticSearchAdminService.processUpdateQueue('studies');
      };

      ctrl.onSavedSuccessfully = function() {
        $scope.studyForm.$setPristine();
        SimpleMessageToastService.openSimpleMessageToast(
          'study-management.edit.success-on-save-toast',
          {studyId: ctrl.study.id}, true);
        if (ctrl.createMode) {
          $state.go('studyEdit', {id: ctrl.study.id});
        }
      };

      ctrl.openRestorePreviousVersionDialog = function(event) {
        $mdDialog.show({
            controller: 'ChoosePreviousStudyVersionController',
            templateUrl: 'scripts/studymanagement/' +
              'views/choose-previous-study-version.html.tmpl',
            clickOutsideToClose: false,
            fullscreen: true,
            locals: {
              studyId: ctrl.study.id
            },
            targetEvent: event
          })
          .then(function(study) {
            ctrl.study = new StudyResource(study);
            $scope.studyForm.$setDirty();
            SimpleMessageToastService.openSimpleMessageToast(
              'study-management.edit.previous-version-restored-toast',
              {
                studyId: ctrl.study.id
              }, true);
          });
      };

      init();
    });
