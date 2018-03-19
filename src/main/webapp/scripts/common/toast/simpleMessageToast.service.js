/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('SimpleMessageToastService',
  function(SynchronizedMdToast) {
    var toastParent = angular.element('#toast-container');

    function openSimpleMessageToast(messageId, messageParams,
      useDefaultHideDelay) {
      SynchronizedMdToast.show({
        controller: 'SimpleMessageToastController',
        templateUrl: 'scripts/common/toast/simple-message-toast.html.tmpl',
        hideDelay: useDefaultHideDelay ? 5000 : 0,
        position: 'top right',
        parent: toastParent,
        locals: {
          messageId: messageId,
          messageParams: messageParams,
          alert: false
        }
      });
    }

    function openAlertMessageToast(messageId, messageParams) {
      SynchronizedMdToast.show({
        controller: 'SimpleMessageToastController',
        templateUrl: 'scripts/common/toast/simple-message-toast.html.tmpl',
        hideDelay: 0,
        position: 'top right',
        parent: toastParent,
        locals: {
          messageId: messageId,
          messageParams: messageParams,
          alert: true
        }
      });
    }

    return {
      openSimpleMessageToast: openSimpleMessageToast,
      openAlertMessageToast: openAlertMessageToast
    };

  });
