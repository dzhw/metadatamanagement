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
    'name': 'Win10/Chrome',
    'platform': 'Windows 10',
    'browserName': 'chrome',
    'version': 'latest',
    'screenResolution': '1280x1024'
  }, {
    'name': 'Win10/Firefox',
    'platform': 'Windows 10',
    'browserName': 'firefox',
    'screenResolution': '1280x1024',
    'version': 'latest'
  }, {
    'name': 'Win10/Edge',
    'platform': 'Windows 10',
    'browserName': 'MicrosoftEdge',
    'version': 'latest',
    'screenResolution': '1280x1024'
  }, {
    'name': 'Win7/IE10',
    'platform': 'Windows 7',
    'browserName': 'internet explorer',
    'version': '10.0',
    'screenResolution': '1280x1024'
  }, {
    'name': 'Win7/IE11',
    'platform': 'Windows 7',
    'browserName': 'internet explorer',
    'version': '11.0',
    'screenResolution': '1280x1024'
  }],
  //enable this for local tests without selenium
  //directConnect: true,
  jasmineNodeOpts: {
    defaultTimeoutInterval: 120000
  },
  allScriptsTimeout: 120000
};
