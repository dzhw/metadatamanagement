/* global protractor */
/* global element */
/* global browser */
/* global by */
'use strict';

function protractorWaitHelper(htmlElementId) {
  var deferred = protractor.promise.defer();
  var expectedCondition = protractor.ExpectedConditions;
  var el = element(by.id(htmlElementId));
  browser.wait(expectedCondition.visibilityOf(el), 4000).then(function() {
    deferred.fulfill(el);
  });
  return deferred.promise;
}

module.exports.protractorWaitHelper = protractorWaitHelper;
