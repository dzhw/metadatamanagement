
/* global browser */
// conf.js
'use strict';

exports.config = {
  onPrepare: function() {
    require('./utils/locators.js');
    browser.driver.manage().window().maximize();
  },
  baseUrl: 'http://localhost:8080/',
  specs: [
    'home/home.spec.js',
    'disclosure/disclosure.spec.js',
    'common/navbar.spec.js',
    'common/toolbar.spec.js',
    ],
  jasmineNodeOpts: {
      defaultTimeoutInterval: 120000
    },
  allScriptsTimeout: 120000
};
