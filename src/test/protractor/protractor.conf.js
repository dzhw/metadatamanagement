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
    //rootElement: 'html',
    specs: [
      'home/home.spec.js',
      //'disclosure/disclosure.spec.js',
      //'common/navbar.spec.js',
      //'common/toolbar.spec.js'
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
      'version': '54.0'
    }, {
      'name': 'Win10/Edge',
      'platform': 'Windows 10',
      'browserName': 'MicrosoftEdge',
      'version': 'latest',
      'screenResolution': '1280x1024'
    } ,{
      'name': 'Win10/IE11',
      'platform': 'Windows 10',
      'browserName': 'internet explorer',
      'version': 'latest',
      'screenResolution': '1280x1024'
    }],
    //enable this for local tests without selenium
    //directConnect: true,
    jasmineNodeOpts: {
      defaultTimeoutInterval: 120000
    },
    allScriptsTimeout: 120000,
    getPageTimeout: 120000
  };
