/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('NoOpenProjectToastService',
  function($mdToast) {
    var toastParent = angular.element('#toast-container');

    //The Toast for no project is choosen
    var openNoProjectToast = function() {
      $mdToast.show({
        controller: 'NoOpenProjectToastController',
        templateUrl: 'scripts/searchmanagement/' +
          'views/no-open-project-toast.html.tmpl',
        hideDelay: 10000,
        position: 'top right',
        parent: toastParent,
        locals: {
          resultMessage: {}
        }
      });
    };

    return {
      openNoProjectToast: openNoProjectToast
    };

  });
