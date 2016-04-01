/* global jQuery */
'use strict';

angular.module('metadatamanagementApp').service('ParserUtil',
function() {
  var getParsedArray = function(data) {
    var parsedArray;
    if (data) {
      parsedArray = data.replace(/ /g,'').split(',');
    }
    return parsedArray;
  };
  var removeEmptyJsonObjects = function(json) {
    for (var key in json) {
      if (json[key] === null || json[key] === undefined ||
        jQuery.isEmptyObject(json[key])) {
        delete json[key];
        removeEmptyJsonObjects(json);
      }else {
        if (typeof json[key] === 'object') {
          removeEmptyJsonObjects(json[key]);
        }
      }
    }
    return json;
  };
  return {
      getParsedArray: getParsedArray,
      removeEmptyJsonObjects: removeEmptyJsonObjects
    };
});
