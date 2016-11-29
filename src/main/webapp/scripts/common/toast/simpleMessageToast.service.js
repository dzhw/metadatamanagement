/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('SimpleMessageToastService',
  function($mdToast, $timeout) {
    var toastParent = angular.element('#toast-container');

    //The Toast for the upload complete
    function openSimpleMessageToast(messageId, messageParams) {
      console.log(toastParent);
      console.log('simple');
      console.log($mdToast);

      $timeout(function() {
        $mdToast.show({
          controller: 'SimpleMessageToastController',
          templateUrl: 'scripts/common/toast/simple-message-toast.html.tmpl',
          hideDelay: 0,
          position: 'top right',
          parent: toastParent,
          locals: {
            messageId: messageId,
            messageParams: messageParams
          }
        });
      }, 1000);
    }

    return {
      openSimpleMessageToast: openSimpleMessageToast
    };

  });
