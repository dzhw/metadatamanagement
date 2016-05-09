/* global jQuery */
'use strict';

angular.module('metadatamanagementApp').service('CleanJSObjectService',
  function() {
    var removeWhiteSpace = function(data) {
      var cleanedArray;
      if (data) {
        cleanedArray = data.replace(/ /g, '').split(',');
      }
      return cleanedArray;
    };

    var isNullOrEmpty = function(object) {
      return (object === null || object === undefined || object.length ===
        0 ||
        (jQuery.isPlainObject(object) && jQuery.isEmptyObject(object)));
    };

    var removeEmptyJsonObjects = function(json) {
      if (jQuery.isPlainObject(json)) {
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
      removeWhiteSpace: removeWhiteSpace,
      removeEmptyJsonObjects: removeEmptyJsonObjects,
      isNullOrEmpty: isNullOrEmpty
    };
  });
