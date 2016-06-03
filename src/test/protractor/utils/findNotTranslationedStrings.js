/* global protractor */
'use strict';

function checkHTMLContent(htmlContent, pageUrl) {
  var exp = /\{\{\s*\S*\s*\}\}/g;
  var deferred = protractor.promise.defer();
  htmlContent.getInnerHtml().then(function(content) {
    var results = content.match(exp);
    var length = results ? results.length : 0;
    var result = {
      length: length,
      message: 'On [' + pageUrl + '] are ' + length +
      ' not translated string(s)\n' + results
    };
    deferred.fulfill(result);
  });
  return deferred.promise;
}

module.exports.checkHTMLContent = checkHTMLContent;
