/* global _ */
'use strict';
angular.module('metadatamanagementApp').service('DeleteMetadataService',
  function($rootScope, ProjectUpdateAccessService, CommonDialogsService,
    ElasticSearchAdminService, SimpleMessageToastService, $injector,
    DataPackageIdBuilderService) {

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
              {id: DataPackageIdBuilderService.buildDataPackageId(project.id)})
              .$promise;
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
          if (type === 'dataPackages') {
            SimpleMessageToastService.openSimpleMessageToast(
              'data-package-management.edit.all-data-packages-deleted-toast',
              {id: project.id});
          } else if (type === 'publications') {
            SimpleMessageToastService.openSimpleMessageToast(
              'related-publication-management.assign.' +
              'all-publications-removed-toast',
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
