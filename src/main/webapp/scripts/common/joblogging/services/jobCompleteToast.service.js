/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('JobCompleteToastService',
  function($mdToast, $timeout) {
    var toastParent = angular.element('#toast-container');

    //The Toast for the upload complete
    function openJobCompleteToast(resultMessage, translationParams) {
      //The reason for the time out is the problem with server errors.
      //The simple toast and the job complete / cancel will be stated on the
      //same time. This is just a workaround, until stacked toast are supported
      //by the angular material team.
      $timeout(function() {
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
      }, 10);
    }

    return {
      openJobCompleteToast: openJobCompleteToast
    };

  });
