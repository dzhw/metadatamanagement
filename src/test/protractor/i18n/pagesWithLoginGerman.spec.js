/* global describe */
/* global it */
/* global browser */
/* global afterEach */
/* global beforeEach */
/* global xit */
/* @Author Daniel Katzberg */

'use strict';

describe('Check GERMAN language with a login for ', function() {
  var loginHelper = require('../utils/loginHelper');
  var cacheHelper = require('../utils/cacheHelper');
  var utilMissingTranslations = require('../utils/findMissingTranslations');

  //Login only once
  beforeEach(function() {
    browser.get(utilMissingTranslations.germanLanguage + '/');
    loginHelper.login();
  });

  //Logout only once
  afterEach(function() {
    loginHelper.logout();
    cacheHelper.clearCache();
  });

  it('... welcome Page ', function() {
    var pages = ['/'];

    //Test pages
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.germanLanguage, pages);
  });

  it('... dataAcquisitionProjects Page ', function() {
    var pages = ['/dataAcquisitionProjects'];

    //Test pages
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.germanLanguage, pages);
  });

  it('... surveys Page ', function() {
    var pages = ['/surveys'];

    //Test pages
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.germanLanguage, pages);
  });

  it('... variables Page ', function() {
    var pages = ['/variables?page=1'];

    //Test pages
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.germanLanguage, pages);
  });

  it('... settings Page ', function() {
    var pages = ['/settings'];

    //Test pages
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.germanLanguage, pages);
  });

  it('... password Page ', function() {
    var pages = ['/password'];

    //Test pages
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.germanLanguage, pages);
  });

  it('... user-management Page ', function() {
    var pages = ['/user-management'];

    //Test pages
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.germanLanguage, pages);
  });

  it('... tracker Page ', function() {
    var pages = ['/tracker'];

    //Test pages
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.germanLanguage, pages);
  });

  it('... metrics Page ', function() {
    var pages = ['/metrics'];

    //Test pages
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.germanLanguage, pages);
  });

  it('... health Page ', function() {
    var pages = ['/health'];

    //Test pages
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.germanLanguage, pages);
  });

  it('... configuration Page ', function() {
    var pages = ['/configuration'];

    //Test pages
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.germanLanguage, pages);
  });

  it('... logs Page ', function() {
    var pages = ['/logs'];

    //Test pages
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.germanLanguage, pages);
  });

  it('... disclosure Page ', function() {
    var pages = ['/disclosure'];

    //Test pages
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.germanLanguage, pages);
  });
});
