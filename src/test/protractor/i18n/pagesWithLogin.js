/* global describe */
/* global it */
/* global browser */
/* global beforeEach */
/* global afterEach */
/* @Author Daniel Katzberg */

'use strict';

xdescribe('Metadatamanagement Start page with different languages', function() {
  var loginHelper = require('../utils/loginHelper');
  var utilMissingTranslations = require('../utils/findMissingTranslations');
  var pages = ['/', '/fdzProjects', '/surveys',
    '/variables?page=1', '/settings', '/password', '/user-management',
    '/tracker', '/metrics', '/health', '/configuration', '/audits',
    '/logs', '/disclosure'
  ];

  beforeEach(function() {

  });

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
  });

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
    });
});
