/* global browser */

// conf.js
'use strict';

exports.config = {
  onPrepare: function() {
    require('./utils/locators.js');
    browser.driver.manage().window().maximize(); // tobe remvoved
  },

  //disable these if you want to run tests locally
  sauceUser: process.env.SAUCE_USERNAME,
  sauceKey: process.env.SAUCE_ACCESS_KEY,
  sauceBuild: process.env.TRAVIS_BUILD_NUMBER,

  baseUrl: 'https://metadatamanagement-dev.cfapps.io/',
  specs: [
    'disclosure/disclosure.spec.js',
    'administration/configuration/configuration.spec.js',
    'administration/health/health.spec.js',
    'administration/logs/logs.spec.js',
    'administration/metrics/metrics.spec.js',
    'administration/usermanagement/usermanagement.spec.js',
    'usermanagement/account/login/login.spec.js',
    'usermanagement/account/activate/activate.spec.js',
    'usermanagement/account/password/password.spec.js',
    'usermanagement/account/register/register.spec.js',
    'usermanagement/account/reset/request/reset.spec.js',
    'usermanagement/account/settings/settings.spec.js',
    'common/navbar.spec.js',
  ],

  multiCapabilities: [{
    'name': 'Win8.1/Chrome',
    'platform': 'Windows 8.1',
    'browserName': 'chrome',
    'screenResolution': '1280x1024'
  }, {
    'name': 'Win8.1/Firefox',
    'platform': 'Windows 8.1',
    'browserName': 'firefox',
    'screenResolution': '1280x1024'
  }, {
    'name': 'Win8.1/Firefox',
    'platform': 'Windows 8.1',
    'browserName': 'firefox',
    'screenResolution': '1280x1024'
  }],

  //enable this for local tests without selenium
  //directConnect: true,

  jasmineNodeOpts: {
    defaultTimeoutInterval: 120000
  },
  allScriptsTimeout: 120000
};
