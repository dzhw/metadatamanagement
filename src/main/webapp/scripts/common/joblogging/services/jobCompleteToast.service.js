/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('JobCompleteToastService',
  function($mdToast) {
    var toastParent = angular.element('#toast-container');

    //The Toast for the upload complete
    function openJobCompleteToast(resultMessage) {
      $mdToast.show({
        controller: 'JobCompleteToastController',
        templateUrl: 'scripts/common/joblogging/job-complete-toast.html.tmpl',
        hideDelay: 0,
        position: 'top right',
        parent: toastParent,
        locals: {
          resultMessage: resultMessage
        }
      });
    }

    return {
      openJobCompleteToast: openJobCompleteToast
    };

  });
