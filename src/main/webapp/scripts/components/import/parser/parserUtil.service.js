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
  var isJsonObjectwithValues = function(json) {
    var returnValue = false;
    for (var key in json) {
      /*if (typeof json[key] === 'object' && json[key] !== null) {
        isJsonObjectwithValues(json[key]);
      } else {*/
      if (json[key] !== undefined) {
        returnValue = true;
      }
      //}
    }
    return returnValue;
  };
  return {
      getParsedArray: getParsedArray,
      isJsonObjectwithValues: isJsonObjectwithValues
    };
});
