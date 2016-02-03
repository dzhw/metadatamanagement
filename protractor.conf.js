/* global browser */

// conf.js
'use strict';
exports.config = {
  seleniumServerJar: './node_modules/protractor/selenium/' +
    'selenium-server-standalone-2.48.2.jar',
  capabilities: {
    browserName: 'phantomjs',
    'phantomjs.binary.path': './node_modules/phantomjs/bin/phantomjs'
  },
  onPrepare: function() {
    browser.driver.manage().window().maximize();
    require('./src/test/protractor/utils/locators.js');
  },
  //baseUrl: 'https://metadatamanagement.cfapps.io/',
  baseUrl: 'https://metadatamanagement.cfapps.io/',
  specs: ['src/test/protractor/i18n/publicPages.js',
    'src/test/protractor/login/login.js'
  ]
};
