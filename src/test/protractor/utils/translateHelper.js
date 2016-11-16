/* global protractor */
/* global element */
/* global by */

'use strict';
var protractorHelper = require('../utils/protractorWaitHelper');

function changeLanguage(htmlElementId, currentUrl, targetLanguage) {
  var deferred = protractor.promise.defer();
  var currentLanguage;
  var changeLanguageButton;
  if (currentUrl.indexOf('/de/') !== -1) {
    changeLanguageButton = element(by.id('changeLanguageToEn'));
    currentLanguage = 'de';
  } else {
    changeLanguageButton = element(by.id('changeLanguageToDe'));
    currentLanguage = 'en';
  }
  if (currentLanguage !== targetLanguage) {
    changeLanguageButton.click();
  }
  protractorHelper.protractorWaitHelper(htmlElementId).then(function(el) {
    deferred.fulfill(el);
  });
  return deferred.promise;
}

module.exports.changeLanguage = changeLanguage;
