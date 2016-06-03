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
  sauceBuild: process.env.TRAVIS_BUILD_NUMBER,

  baseUrl: 'https://metadatamanagement-dev.cfapps.io/',
  specs: [
    'disclosure/disclosure.spec.js',
  ],

  multiCapabilities: [{
    'name': 'Win8.1/Chrome',
    'platform': 'Windows 8.1',
    'browserName': 'chrome'
  }, {
    'name': 'Win8.1/Firefox',
    'platform': 'Windows 8.1',
    'browserName': 'firefox'
  }, {
    'name': 'Win8.1/IE11',
    'platform': 'Windows 8.1',
    'browserName': 'internet explorer',
    'version': '11.0'
  }],

  //enable this for local tests without selenium
  //directConnect: true,

  jasmineNodeOpts: {
    defaultTimeoutInterval: 120000
  },
  allScriptsTimeout: 120000
};
