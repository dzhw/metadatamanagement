/* global protractor */
/* global element */
/* global browser */
/* global by */
'use strict';

function waitFor(htmlElementId) {
  var deferred = protractor.promise.defer();
  var expectedCondition = protractor.ExpectedConditions;
  var el = element(by.id(htmlElementId));
  browser.wait(expectedCondition.visibilityOf(el), 60000).then(function() {
    deferred.fulfill(el);
  });
  return deferred.promise;
}

module.exports.waitFor = waitFor;
