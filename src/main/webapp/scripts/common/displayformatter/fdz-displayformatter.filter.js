'use strict';

angular.module('metadatamanagementApp').filter('fdzDisplayFormatter',
function() {
  return function(toBeDisplayed) {
    if (['Nicht erfasst', 'Undocumented'].indexOf(toBeDisplayed) !== -1) {
      toBeDisplayed = toBeDisplayed + '.';
    }
    return toBeDisplayed;
  };
});
