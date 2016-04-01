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

  var isNullOrEmpty = function(object) {
    return (object === null || object === undefined ||
      jQuery.isEmptyObject(object));
  };

  var removeEmptyJsonObjects = function(json) {
    if (typeof json === 'object') {
      for (var key in json) {
        if (isNullOrEmpty(json[key])) {
          delete json[key];
        } else {
          removeEmptyJsonObjects(json[key]);
          if (isNullOrEmpty(json[key])) {
            delete json[key];
          }
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
