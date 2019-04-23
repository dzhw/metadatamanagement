/* global _ */
'use strict';
angular.module('metadatamanagementApp').service('DeleteMetadataService',
  function($rootScope, ProjectUpdateAccessService, CommonDialogsService,
    ElasticSearchAdminService, SimpleMessageToastService, $injector,
    StudyIdBuilderService) {

    var deleteAllOfType = function(project, type) {
      if (ProjectUpdateAccessService.isUpdateAllowed(
        project,
        _.snakeCase(type),
        true
      )) {
        CommonDialogsService.showConfirmDeletionDialog({
          type: 'all-' + _.kebabCase(type),
          id: project.id
        }).then(function() {
          if (type === 'publications') {
            return $injector.get('DeleteAllPublicationsResource').deleteAll(
              {id: StudyIdBuilderService.buildStudyId(project.id)}).$promise;
          }
          return $injector.get('DeleteAll' +
            _.upperFirst(_.camelCase(type)) + 'Resource').deleteAll(
            {id: project.id}).$promise;
        }).then(function() {
          if (type === 'publications') {
            return ElasticSearchAdminService.
              processUpdateQueue('related_publications');
          }
          return ElasticSearchAdminService.
            processUpdateQueue(_.snakeCase(type));
        }).then(function() {
          $rootScope.$broadcast('deletion-completed');
          if (type === 'studies') {
            SimpleMessageToastService.openSimpleMessageToast(
              'study-management.edit.all-studies-deleted-toast',
              {id: project.id});
          } else {
            SimpleMessageToastService.openSimpleMessageToast(
              _.kebabCase(type).slice(0, -1) + '-management.edit.all-' +
              _.kebabCase(type) + '-deleted-toast',
              {id: project.id});
          }
        });
      }
    };

    return {
      deleteAllOfType: deleteAllOfType
    };
  });
