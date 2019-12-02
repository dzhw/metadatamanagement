(function() {
  'use strict';

  var FooterComponent = {
    templateUrl: 'scripts/common/footer/footer.html.tmpl',
    controller: 'footerController',
    bindings: {
      lang: '<'
    }
  };

  angular
    .module('metadatamanagementApp')
    .component('fdzFooter', FooterComponent);
})();
