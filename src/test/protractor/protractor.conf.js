/* global browser */

// conf.js
'use strict';

exports.config = {
  seleniumServerJar: './../../../node_modules/protractor/selenium/' +
    'selenium-server-standalone-2.51.0.jar',
  capabilities: {
    browserName: 'phantomjs',
    'phantomjs.binary.path': './node_modules/phantomjs/bin/phantomjs',
  },
  onPrepare: function() {
    browser.driver.manage().window().maximize();
    require('./utils/locators.js');
  },

  baseUrl: 'https://metadatamanagement.cfapps.io/',
  specs: [
    //'login/login.spec.js',
    //'i18n/publicPagesGerman.spec.js',
    //'i18n/publicPagesEnglish.spec.js',
    //'i18n/pagesWithLoginGerman.spec.js',
    //'i18n/pagesWithLoginEnglish.spec.js',
    'brokenLinks/publicBrokenLinksGerman.spec.js'
  ],
  jasmineNodeOpts: {
    defaultTimeoutInterval: 180000
  },
  allScriptsTimeout: 120000
};
