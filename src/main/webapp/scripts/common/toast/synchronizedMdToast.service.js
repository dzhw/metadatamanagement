/* global _ */
'use strict';

angular.module('metadatamanagementApp').service('SynchronizedMdToast',
  function($mdToast) {
    // debounce frequent calls to $mdToast.show cause otherwise
    // toasts might overlap and cannot be closed anymore
    var debouncedShow = _.debounce($mdToast.show, 100);

    return {
      show: debouncedShow,
      hide: $mdToast.hide
    };
  });
