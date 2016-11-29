/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('JobCompleteToastService',
  function($mdToast) {
    var toastParent = angular.element('#toast-container');

    //The Toast for the upload complete
    function openJobCompleteToast(resultMessage, translationParams) {
      console.log('job complete');
      $mdToast.hide();
      console.log($mdToast);
      $mdToast.show({
        controller: 'SimpleMessageToastController',
        templateUrl: 'scripts/common/toast/simple-message-toast.html.tmpl',
        hideDelay: 0,
        position: 'top right',
        parent: toastParent,
        locals: {
          messageId: resultMessage,
          messageParams: translationParams
        }
      });
    }

    return {
      openJobCompleteToast: openJobCompleteToast
    };

  });
