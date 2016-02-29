/* global describe */
/* global it */
/* global browser */
/* global afterAll */
/* global beforeEach */
/* global afterEach */
/* @Author Daniel Katzberg */

'use strict';
var brokenLinks = require('../utils/brokenLinks');
var cacheHelper = require('../utils/cacheHelper');

describe('Check broken links in GERMAN language for ... ', function() {

  beforeEach(function() {
    return browser.ignoreSynchronization = true;
  });

  afterEach(function() {
    return browser.ignoreSynchronization = false;
  });

  afterAll(function() {
    cacheHelper.clearCache();
  });

  it('... welcome page', function() {
    var pages = ['/'];
    brokenLinks.checkLinks('#/de', pages);
  });
});
