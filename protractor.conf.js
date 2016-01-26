// conf.js
'use strict';
exports.config = {
  seleniumServerJar: './node_modules/protractor/selenium/selenium-server-standalone-2.48.2.jar',
  chromeDriver: './node_modules/protractor/selenium/chromedriver',
  specs: ['src/test/protractor/title.js']
    /*capabilities: {
      browserName: 'chrome',
      version: '',
      platform: 'ANY',
      'webdriver.chrome.driver': require('chromedriver').path
    }*/
};
