(function() {
  'use strict';

  var Component = {
    controller: 'DataPackageConfiguratorController',
    templateUrl:
      'scripts/ordermanagement/components/data-package-configurator.html.tmpl'
  };

  angular
    .module('metadatamanagementApp')
    .component('fdzDataPackageConfigurator', Component);

})();
