/* globals _ */
'use strict';

angular.module('metadatamanagementApp').service('OutdatedVersionNotifier',
  function($state, SimpleMessageToastService, $document, $mdToast, Principal) {

    var createHref = function(item) {
      return $state.href($state.current.name, {
          id: item.masterId,
          version: _.get(item, 'release.version')
        },
        {
          absolute: false
        });
    };

    var createMasterRef = function(item) {
      return $state.href($state.current.name, {
          id: item.masterId,
          version: null
        },
        {
          absolute: false
        });
    };

    var showPublicUserMessage = function(href, oldVersion, newVersion) {
      var messageParams = {
        href: href,
        oldVersion: oldVersion,
        newVersion: newVersion
      };
      SimpleMessageToastService.openAlertMessageToast('data-acquisition' +
        '-project-management.outdated-version-alert', messageParams);
    };

    var showLoggedInUserMessage = function(href, version) {
      var messageParams = {
        href: href,
        version: version
      };
      SimpleMessageToastService.openAlertMessageToast('data-acquisition' +
        '-project-management.not-master-alert', messageParams);
    };

    var checkVersionAndNotify = function(item, fetchFn) {
      if (Principal.loginName() && item.shadow) {
        var version = _.get(item, 'release.version');
        var href = createMasterRef(item);
        showLoggedInUserMessage(href, version);
      } else if (item.shadow && angular.isDefined(item.successorId)) {
        fetchFn().promise.then(function(result) {
          var oldVersion = _.get(item, 'release.version');
          var newVersion = _.get(result, 'release.version');
          var href = createHref(result);
          showPublicUserMessage(href, oldVersion, newVersion);
        });
      } else {
        if ($document.find('[data-translate="data-acquisition' +
          '-project-management.outdated-version-alert"]').length > 0 ||
          $document.find('[data-translate="data-acquisition' +
            '-project-management.not-master-alert"]').length > 0) {
          $mdToast.hide();
        }
      }
    };

    return {
      checkVersionAndNotify: checkVersionAndNotify
    };
  });
