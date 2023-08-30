/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('SimpleMessageToastService', ['SynchronizedMdToast', 
  function(SynchronizedMdToast) {
    var toastParent = angular.element('#toast-container');

    function openSimpleMessageToasts(messageArray, preventAutomaticalHiding) {
      SynchronizedMdToast.show({
        controller: 'SimpleMessageToastController',
        templateUrl: 'scripts/common/toast/simple-message-toast.html.tmpl',
        hideDelay: !preventAutomaticalHiding ? 5000 : 0,
        position: 'top right',
        parent: toastParent,
        locals: {
          messages: messageArray,
          alert: false
        }
      });
    }

    function openSimpleMessageToast(messageId, messageParams,
      preventAutomaticalHiding) {
      SynchronizedMdToast.show({
        controller: 'SimpleMessageToastController',
        templateUrl: 'scripts/common/toast/simple-message-toast.html.tmpl',
        hideDelay: !preventAutomaticalHiding ? 5000 : 0,
        position: 'top right',
        parent: toastParent,
        locals: {
          messages: [{messageId: messageId, messageParams: messageParams}],
          alert: false
        }
      });
    }

    function openAlertMessageToasts(messageArray) {
      SynchronizedMdToast.show({
        controller: 'SimpleMessageToastController',
        templateUrl: 'scripts/common/toast/simple-message-toast.html.tmpl',
        hideDelay: 0,
        position: 'top right',
        parent: toastParent,
        locals: {
          messages: messageArray,
          alert: true
        }
      });
    }

    function openAlertMessageToast(messageId, messageParams) {
      SynchronizedMdToast.show({
        controller: 'SimpleMessageToastController',
        templateUrl: 'scripts/common/toast/version-alert-toast.html.tmpl',
        hideDelay: 0,
        position: 'top right',
        parent: toastParent,
        locals: {
          messages: [{messageId: messageId, messageParams: messageParams}],
          alert: true
        }
      });
    }

    return {
      openSimpleMessageToast: openSimpleMessageToast,
      openSimpleMessageToasts: openSimpleMessageToasts,
      openAlertMessageToast: openAlertMessageToast,
      openAlertMessageToasts: openAlertMessageToasts
    };

  }]);

