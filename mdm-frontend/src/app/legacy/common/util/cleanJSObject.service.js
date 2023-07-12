/* global jQuery */
'use strict';

angular.module('metadatamanagementApp').service('CleanJSObjectService',
  function() {
    var removeWhiteSpace = function(data) {
      var cleanedArray;
      if (data) {
        cleanedArray = data.replace(/ /g, '').split(/,|;/);
        cleanedArray = cleanedArray.filter(function(value) {
          return value !== '';
        });
      }
      return cleanedArray;
    };

    var parseAndTrim = function(data) {
      var cleanedArray;
      if (data) {
        cleanedArray = data.split(/,|;/).map(function(value) {
          return value.trim();
        });
        cleanedArray = cleanedArray.filter(function(value) {
          return value !== '';
        });
      }
      return cleanedArray;
    };

    var isNullOrEmpty = function(object) {
      return (object === null || object === undefined || object.length ===
        0 ||
        (jQuery.isPlainObject(object) && jQuery.isEmptyObject(object)));
    };

    var deleteEmptyStrings = function(object) {
      for (var key in object) {
        if (object.hasOwnProperty(key)) {
          if (object[key] === '') {
            delete object[key];
          } else if (jQuery.isPlainObject(object[key])) {
            deleteEmptyStrings(object[key]);
          } else if (Array.isArray(object[key])) {
            object[key].forEach(deleteEmptyStrings);
          }
        }
      }
    };

    var removeEmptyJsonObjects = function(json) {
      for (var key in json) {
        if (isNullOrEmpty(json[key])) {
          delete json[key];
        } else if (jQuery.isPlainObject(json[key])) {
          removeEmptyJsonObjects(json[key]);
          if (isNullOrEmpty(json[key])) {
            delete json[key];
          }
        } else if (Array.isArray(json[key])) {
          json[key].forEach(removeEmptyJsonObjects);
        }
      }
      return json;
    };
    return {
      removeWhiteSpace: removeWhiteSpace,
      removeEmptyJsonObjects: removeEmptyJsonObjects,
      isNullOrEmpty: isNullOrEmpty,
      deleteEmptyStrings: deleteEmptyStrings,
      parseAndTrim: parseAndTrim
    };
  });
