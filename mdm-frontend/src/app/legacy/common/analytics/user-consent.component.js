(function() {
  'use strict';

  var Component = {
    controller: 'UserConsentController',
    templateUrl: 'scripts/common/analytics/' +
      'user-consent.html.tmpl',
  };

  angular
    .module('metadatamanagementApp')
    .component('userConsentComponent', Component);
})();
