/* global describe */
/* global it */
/* global browser */
/* global afterAll */
/* @Author Daniel Katzberg */

'use strict';

describe('Metadatamanagement Start page with different languages', function() {
  var loginHelper = require('../utils/loginHelper');
  var cacheHelper = require('../utils/cacheHelper');
  var utilMissingTranslations = require('../utils/findMissingTranslations');

  var pages = ['/', '/fdzProjects', '/surveys',
    '/variables?page=1', '/settings', '/password', '/user-management',
    '/tracker', '/metrics', '/health', '/configuration', '/audits',
    '/logs', '/disclosure'
  ];

  afterAll(cacheHelper.clearCache);

  it('Check german language elements for all pages with a login', function() {

    //Welcome Page
    browser.get(utilMissingTranslations.germanLanguage +
      pages[0]);

    //'Login'
    loginHelper.login(utilMissingTranslations.germanLanguage +
      pages[0]);

    //Test pages
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.germanLanguage, pages);

    //'Logout'
    loginHelper.logout(utilMissingTranslations.germanLanguage +
      pages[0]);
  }, 60000);

  it('Check english language elements for all pages with a login',
    function() {
      //Welcome Page
      browser.get(utilMissingTranslations.englishLanguage +
        pages[0]);

      //'Login'
      loginHelper.login(utilMissingTranslations.englishLanguage +
        pages[0]);

      //Test pages
      utilMissingTranslations.testMissingTranslations(
        utilMissingTranslations.englishLanguage, pages);

      //'Logout'
      loginHelper.logout(utilMissingTranslations.englishLanguage +
        pages[0]);
    }, 60000);
});
