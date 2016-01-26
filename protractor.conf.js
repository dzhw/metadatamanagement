// conf.js
'use strict';
exports.config = {
  seleniumServerJar: './node_modules/protractor/selenium/' +
    'selenium-server-standalone-2.48.2.jar',
  capabilities: {
    browserName: 'phantomjs'
  },
  baseUrl: 'https://metadatamanagement.cfapps.io/',
  specs: ['src/test/protractor/i18n/index.js']
};
