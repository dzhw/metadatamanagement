'use strict';

angular.module('metadatamanagementApp')
  .controller('StudyEditOrCreateController',
    function(entity, PageTitleService, LanguageService, $document, $timeout,
      $state, ToolbarHeaderService, Principal, SimpleMessageToastService,
      CurrentProjectService, StudyIdBuilderService, StudyResource) {
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
          'studyIsPresent': !ctrl.createMode,
          'projectId': ctrl.study.dataAcquisitionProjectId
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
              ctrl.study = {
                id: StudyIdBuilderService.buildStudyId(
                  CurrentProjectService.getCurrentProject().id),
                dataAcquisitionProjectId:
                  CurrentProjectService.getCurrentProject().id,
                authors: [{firstName: '', lastName:''}]
              };
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
        {de: 'Verfügbar', en:'Available'},
        {de: 'In Aufbereitung', en:'In preparation'},
        {de: 'Nicht Verfügbar', en:'Not available'}
      ];

      ctrl.surveyDesigns = [
        {de: 'Panel', en:'Panel'},
        {de: 'Querschnitt', en:'Cross-Section'}
      ];

      ctrl.deleteAuthor = function(index) {
        ctrl.study.authors.splice(index, 1);
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
        $document.find('input[name="'+ ctrl.currentAuthorInputName + '"]')
          .focus();
      };

      ctrl.moveCurrentAuthorDown = function() {
        var a = ctrl.study.authors[ctrl.currentAuthor + 1];
        ctrl.study.authors[ctrl.currentAuthor + 1] =
          ctrl.study.authors[ctrl.currentAuthor];
        ctrl.study.authors[ctrl.currentAuthor] = a;
        ctrl.currentAuthorInputName = ctrl.currentAuthorInputName
          .replace('_' + ctrl.currentAuthor, '_' + (ctrl.currentAuthor + 1));
        $document.find('input[name="'+ ctrl.currentAuthorInputName + '"]')
          .focus();
      };

      init();
    });
