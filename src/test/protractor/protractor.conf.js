/* global browser */

// conf.js
'use strict';

exports.config = {
  onPrepare: function() {
    browser.driver.manage().window().maximize();
    require('./utils/locators.js');
  },

  //disable these if you want to run tests locally
  sauceUser: process.env.SAUCE_USERNAME,
  sauceKey: process.env.SAUCE_ACCESS_KEY,

  baseUrl: 'https://metadatamanagement-dev.cfapps.io/',
  specs: [
    'login/login.spec.js',
    'i18n/publicPagesGerman.spec.js',
    'i18n/publicPagesEnglish.spec.js',
    'i18n/pagesWithLoginGerman.spec.js',
    'i18n/pagesWithLoginEnglish.spec.js',
    'brokenLinks/publicBrokenLinksGerman.spec.js',
    'brokenLinks/publicBrokenLinksEnglish.spec.js',
    'brokenLinks/withLoginBrokenLinksGerman.spec.js',
    'brokenLinks/withLoginBrokenLinksEnglish.spec.js'
  ],

  multiCapabilities: [{
    'name': 'Win8.1/Chrome',
    'platform': 'Windows 8.1',
    'browserName': 'chrome',
    'build': 1234
  }, {
    'name': 'Win8.1/Firefox',
    'platform': 'Windows 8.1',
    'browserName': 'firefox',
    'build': 1234
  }, {
    'name': 'Win8.1/IE11',
    'platform': 'Windows 8.1',
    'browserName': 'internet explorer',
    'version': '11.0',
    'build': 1234
  }],

  //enable this for local tests without selenium
  //directConnect: true,

  jasmineNodeOpts: {
    defaultTimeoutInterval: 120000
  },
  allScriptsTimeout: 120000
};
