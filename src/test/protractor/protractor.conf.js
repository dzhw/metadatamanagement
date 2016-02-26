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
  onPrepare: function () {
    browser.driver.manage().window().maximize();
    require('./utils/locators.js');
  },

  baseUrl: 'https://metadatamanagement.cfapps.io/',
  specs: [//'i18n/publicPages.spec.js',
    //'i18n/pagesWithLogin.spec.js',
    'login/login.spec.js',
  ],
  jasmineNodeOpts: {defaultTimeoutInterval: 180000},
  allScriptsTimeout: 120000
};
