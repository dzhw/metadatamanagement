/* global browser */

'use strict';

exports.config = {
  onPrepare: function() {
    require('./utils/locators.js');
    browser.driver.manage().window().maximize();
  },
  //disable these if you want to run tests locally
  sauceUser: process.env.SAUCE_USERNAME,
  sauceKey: process.env.SAUCE_ACCESS_KEY,
  sauceBuild: process.env.TRAVIS_BUILD_NUMBER,

  baseUrl: 'https://metadatamanagement-dev.cfapps.io/',
  specs: [
    'home/home.spec.js',
    'disclosure/disclosure.spec.js',
    'common/navbar.spec.js',
    'common/toolbar.spec.js',
  ],
  multiCapabilities: [{
    'name': 'Win8.1/Chrome',
    'platform': 'Windows 8.1',
    'browserName': 'chrome',
    'version': 'latest',
    'screenResolution': '1280x1024'
  }, {
    'name': 'Win8.1/Firefox',
    'platform': 'Windows 8.1',
    'browserName': 'firefox',
    'screenResolution': '1280x1024',
    'version': 'latest'
  }, {
    'name': 'Win8.1/IE11',
    'platform': 'Windows 8.1',
    'browserName': 'internet explorer',
    'version': 'latest',
    'screenResolution': '1280x1024'
  }],
  //enable this for local tests without selenium
  //directConnect: true,
  jasmineNodeOpts: {
    defaultTimeoutInterval: 120000
  },
  allScriptsTimeout: 120000
};
