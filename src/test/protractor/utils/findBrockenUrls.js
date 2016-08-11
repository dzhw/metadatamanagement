/* global protractor */
/* global browser */

'use strict';
var https = require('https');

function checkHREFs(toBeCheckedURL, pageUrl) {
  var deferred = protractor.promise.defer();
  var result = {
    isValidUrl: true
  };
  https.get(toBeCheckedURL, function() {
    deferred.fulfill(result);
  }).on('error', function() {
    result.isValidUrl = false;
    result.message = 'On page [' + pageUrl +
      '], the following URL is incorrect: [' + toBeCheckedURL + ']';
    deferred.fulfill(result);
  });
  return deferred.promise;
}

function checkStates(toBeCheckedURL, pageUrl, stateName) {
  var deferred = protractor.promise.defer();
  var result = {
    isValidUrl: true
  };
  browser.getCurrentUrl().then(function(currentUrlurl) {
    if (currentUrlurl !== toBeCheckedURL) {
      result.isValidUrl = false;
      result.message = 'On page [' + pageUrl +
        '], the following State is incorrect: [' + stateName + ' : ' +
        toBeCheckedURL + ']';
    }
    deferred.fulfill(result);
  });
  return deferred.promise;
}

module.exports.checkHREFs = checkHREFs;
module.exports.checkStates = checkStates;
