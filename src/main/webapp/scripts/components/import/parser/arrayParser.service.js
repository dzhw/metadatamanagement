'use strict';

angular.module('metadatamanagementApp').service('ArrayParser',
function() {
  var getParsedArray = function(data) {
    var parsedArray;
    if (data) {
      parsedArray = data.replace(/ /g,'').split(',');
    }
    return parsedArray;
  };
  return {
      getParsedArray: getParsedArray
    };
});
