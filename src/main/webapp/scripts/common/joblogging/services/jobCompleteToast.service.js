/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('JobCompleteToastService',
  function(SynchronizedMdToast) {
    var toastParent = angular.element('#toast-container');

    //The Toast for the upload complete
    function openJobCompleteToast(resultMessage, translationParams) {
      SynchronizedMdToast.show({
        controller: 'JobCompleteToastController',
        templateUrl: 'scripts/common/joblogging/views/' +
          'job-complete-toast.html.tmpl',
        hideDelay: 0,
        autoWrap: false,
        position: 'top right',
        parent: toastParent,
        locals: {
          resultMessage: resultMessage,
          translationParams: translationParams
        }
      });
    }

    return {
      openJobCompleteToast: openJobCompleteToast
    };

  });
