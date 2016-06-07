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

function openAllDropdownMenues(htmlContent) {
  var expForDrowpDown = /dropdown pointer/g;
  var expForSideNav = /md-closed/g;
  var deferred = protractor.promise.defer();
  htmlContent.getInnerHtml().then(function(content) {
    content.replace(expForDrowpDown, 'dropdown pointer open');
    content.replace(expForSideNav, ' ');
    deferred.fulfill(htmlContent);
  });
  return deferred.promise;
}

module.exports.checkHTMLContent = checkHTMLContent;
module.exports.openAllDropdownMenues = openAllDropdownMenues;
