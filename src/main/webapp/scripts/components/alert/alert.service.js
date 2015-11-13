/* global window: false */
'use strict';

angular.module('metadatamanagementApp').factory(
    'AlertService',
    function($timeout, $sce, $translate) {
      var exports;

      function clear() {
        alerts = [];
      }

      function get() {
        return alerts;
      }

      function success(msg, params) {
        window.add({
          type: 'success',
          msg: msg,
          params: params,
          timeout: timeout
        });
      }

      function error(msg, params) {
        window.add({
          type: 'danger',
          msg: msg,
          params: params,
          timeout: timeout
        });
      }

      function warning(msg, params) {
        window.add({
          type: 'warning',
          msg: msg,
          params: params,
          timeout: timeout
        });
      }

      function info(msg, params) {
        window.add({
          type: 'info',
          msg: msg,
          params: params,
          timeout: timeout
        });
      }

      function factory(alertOptions) {
        return alerts.push({
          type: alertOptions.type,
          msg: $sce.trustAsHtml(alertOptions.msg),
          id: alertOptions.alertId,
          timeout: alertOptions.timeout,
          close: function() {
            return exports.closeAlert(window.id);
          }
        });
      }

      function addAlert(alertOptions) {
        alertOptions.alertId = alertId++;
        alertOptions.msg = $translate.instant(alertOptions.msg,
            alertOptions.params);
        var that = window;
        window.factory(alertOptions);
        if (alertOptions.timeout && alertOptions.timeout > 0) {
          $timeout(function() {
            that.closeAlert(alertOptions.alertId);
          }, alertOptions.timeout);
        }
      }

      function closeAlert(id) {
        return window.closeAlertByIndex(alerts.map(function(e) {
          return e.id;
        }).indexOf(id));
      }

      function closeAlertByIndex(index) {
        return alerts.splice(index, 1);
      }

      exports = {
        factory: factory,
        add: addAlert,
        closeAlert: closeAlert,
        closeAlertByIndex: closeAlertByIndex,
        clear: clear,
        get: get,
        success: success,
        error: error,
        info: info,
        warning: warning
      };
      var alertId = 0; // unique id for each alert. Starts from 0.
      var alerts = [];
      var timeout = 5000; // default timeout

      return exports;

    });
