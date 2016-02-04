/* global describe */
/* global it */
/* global browser */
/* @Author Daniel Katzberg */

'use strict';

describe('Metadatamanagement Start page with different languages', function() {
  var loginHelper = require('../utils/loginHelper');
  var utilMissingTranslations = require('../utils/findMissingTranslations');
  utilMissingTranslations.pages = ['/', '/fdzProjects', '/surveys',
    '/variables?page=1', '/settings', '/password', '/user-management',
    '/tracker', '/metrics', '/health', '/configuration', '/audits',
    '/logs', '/disclosure'
  ];

  it('Check german language elements for all pages with a login', function() {
    //Welcome Page
    browser.get(utilMissingTranslations.germanLanguage +
      utilMissingTranslations.pages[0]);

    //'Login'
    loginHelper.login(utilMissingTranslations.germanLanguage +
      utilMissingTranslations.pages[0]);

    //Test pages
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.germanLanguage);

    //'Logout'
    loginHelper.logout(utilMissingTranslations.germanLanguage +
      utilMissingTranslations.pages[0]);
  });

  it('Check english language elements for all pages with a login',
    function() {
      //Welcome Page
      browser.get(utilMissingTranslations.englishLanguage +
        utilMissingTranslations.pages[0]);

      //'Login'
      loginHelper.login(utilMissingTranslations.englishLanguage +
        utilMissingTranslations.pages[0]);

      //Test pages
      utilMissingTranslations.testMissingTranslations(
        utilMissingTranslations.englishLanguage);

      //'Logout'
      loginHelper.logout(utilMissingTranslations.englishLanguage +
        utilMissingTranslations.pages[0]);
    });
});
