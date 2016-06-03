/* global protractor */

'use strict';
var http = require('http');

function checkHREFs(toBeCheckedURL, previousPage) {
  var deferred = protractor.promise.defer();
  var result = {
    isValidUrl: true
  };
  http.get(toBeCheckedURL, function() {
    deferred.fulfill(result);
  }).on('error', function() {
    result.isValidUrl = false;
    result.message = 'On page [' + previousPage +
    '], the following URL is incorrect: [' + toBeCheckedURL + ']';
    deferred.fulfill(result);
  });
  return deferred.promise;
}

module.exports.checkHREFs = checkHREFs;
