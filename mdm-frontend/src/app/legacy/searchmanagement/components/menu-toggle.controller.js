(function() {
  'use strict';

  function MenuToggleController() {
    this.$onInit = function() {
      this.toggleFilterItem = this.parent.toggleFilterItem;
      this.exists = this.parent.exists;
    };
  }

  angular
    .module('metadatamanagementApp')
    .controller('MenuToggleController', MenuToggleController);
})();
