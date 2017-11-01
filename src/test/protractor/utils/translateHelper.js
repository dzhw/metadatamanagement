/* global protractor */
'use strict';
var protractorWaitHelper = require('../utils/protractorWaitHelper');

function changeLanguage(currentUrl, targetLanguage) {
  var currentLanguage;
  var deferred = protractor.promise.defer();
  if (currentUrl.indexOf('/de/') !== -1) {
    currentLanguage = 'de';
    protractorWaitHelper.waitFor('changeLanguageToEn').then(
      function(changeLanguageButton) {
        if (currentLanguage !== targetLanguage) {
          changeLanguageButton.click();
        }
        deferred.fulfill();
      });
  } else {
    currentLanguage = 'en';
    protractorWaitHelper.waitFor('changeLanguageToDe').then(
      function(changeLanguageButton) {
        if (currentLanguage !== targetLanguage) {
          changeLanguageButton.click();
        }
        deferred.fulfill();
      });
  }
  return deferred.promise;
}

module.exports.changeLanguage = changeLanguage;
