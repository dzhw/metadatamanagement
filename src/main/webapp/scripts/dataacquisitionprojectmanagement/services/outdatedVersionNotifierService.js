/* globals _ */
'use strict';

angular.module('metadatamanagementApp').service('OutdatedVersionNotifier',
  function($state, SimpleMessageToastService) {

    var createHref = function(item) {
      return $state.href($state.current.name, {
          id: item.masterId,
          version: _.get(item, 'release.version')
        },
        {
          absolute: false
        });
    };

    var showMessage = function(href, oldVersion, newVersion) {
      var messageParams = {
        href: href,
        oldVersion: oldVersion,
        newVersion: newVersion
      };
      SimpleMessageToastService.openAlertMessageToast('data-acquisition' +
        '-project-management.outdated-version-alert', messageParams);

    };

    var checkVersionAndNotify = function(item, fetchFn) {
      if (angular.isDefined(item.successorId)) {
        fetchFn().promise.then(function(result) {
          var oldVersion = _.get(item, 'release.version');
          var newVersion = _.get(result, 'release.version');
          var href = createHref(result);
          showMessage(href, oldVersion, newVersion);
        });
      }
    };

    return {
      checkVersionAndNotify: checkVersionAndNotify
    };
  });
