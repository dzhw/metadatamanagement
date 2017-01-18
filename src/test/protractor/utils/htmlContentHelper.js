/* global protractor */
'use strict';

function findNotTranslationedStrings(htmlContent, pageUrl) {
  var exp = /\{\{\s*\S*\s*\}\}/g;
  var deferred = protractor.promise.defer();
  htmlContent.getAttribute('innerHTML').then(function(content) {
    var results = content.match(exp);
    var length = results ? results.length : 0;
    var result = {
      length: length,
      message: 'On [' + pageUrl + '] is/are ' + length +
        ' not translated string(s)\n' + results
    };
    deferred.fulfill(result);
  });
  return deferred.promise;
}

module.exports.findNotTranslationedStrings = findNotTranslationedStrings;
