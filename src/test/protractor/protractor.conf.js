/* global browser */

'use strict';

exports.config = {
  onPrepare: function() {
      require('./utils/locators.js');
      browser.driver.manage().window().maximize();
    },

  baseUrl: 'http://localhost:8080/',
  capabilities: {
    'browserName': 'chrome'
  },
  ignoreSynchronization: true,
  directConnect: true,
  specs: ['home/home.spec.js', 'disclosure/disclosure.spec.js'],

  // Options to be passed to Jasmine-node.
  jasmineNodeOpts: {
    showColors: true, // Use colors in the command line report.
  }
};
