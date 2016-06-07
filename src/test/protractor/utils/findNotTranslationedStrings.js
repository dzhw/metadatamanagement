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
  var expForDrowpDown = /\s*dropdown pointer\s*/g;
  var expForSideNav = /\s*md-closed\s*/g;
  var deferred = protractor.promise.defer();
  htmlContent.getOuterHtml().then(function(content) {
    var openedSideNav = content.replace(expForSideNav, ' ');
    var openedDropdowns = openedSideNav
    .replace(expForDrowpDown, 'dropdown pointer open ');
    content = openedDropdowns;
    deferred.fulfill(htmlContent);
  });
  return deferred.promise;
}

module.exports.checkHTMLContent = checkHTMLContent;
module.exports.openAllDropdownMenues = openAllDropdownMenues;
