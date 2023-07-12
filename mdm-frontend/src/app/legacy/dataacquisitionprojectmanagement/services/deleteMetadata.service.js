/* global _ */
'use strict';
angular.module('metadatamanagementApp').service('DeleteMetadataService',
  function($rootScope, ProjectUpdateAccessService, CommonDialogsService,
    ElasticSearchAdminService, SimpleMessageToastService, $injector) {

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
          return $injector.get('DeleteAll' +
            _.upperFirst(_.camelCase(type)) + 'Resource').deleteAll(
            {id: project.id}).$promise;
        }).then(function() {
          return ElasticSearchAdminService.
            processUpdateQueue(_.snakeCase(type));
        }).then(function() {
          $rootScope.$broadcast('deletion-completed');
          if (type === 'dataPackages') {
            SimpleMessageToastService.openSimpleMessageToast(
              'data-package-management.edit.all-data-packages-deleted-toast',
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
