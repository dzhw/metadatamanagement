/* global describe */
/* global it */
/* global browser */
/* global afterEach */
/* global beforeEach */
/* global xit */
/* @Author Daniel Katzberg */

'use strict';

describe('Check ENGLISH language with a login for ', function() {
  var loginHelper = require('../utils/loginHelper');
  var cacheHelper = require('../utils/cacheHelper');
  var utilMissingTranslations = require('../utils/findMissingTranslations');

  //Login only once
  beforeEach(function() {
    browser.get(utilMissingTranslations.englishLanguage + '/');
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
      utilMissingTranslations.englishLanguage, pages);
  });

  it('... dataAcquisitionProjects Page ', function() {
    var pages = ['/dataAcquisitionProjects'];

    //Test pages
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.englishLanguage, pages);
  });

  it('... surveys Page ', function() {
    var pages = ['/surveys'];

    //Test pages
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.englishLanguage, pages);
  });

  it('... variables Page ', function() {
    var pages = ['/variables?page=1'];

    //Test pages
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.englishLanguage, pages);
  });

  it('... settings Page ', function() {
    var pages = ['/settings'];

    //Test pages
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.englishLanguage, pages);
  });

  it('... password Page ', function() {
    var pages = ['/password'];

    //Test pages
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.englishLanguage, pages);
  });

  it('... user-management Page ', function() {
    var pages = ['/user-management'];

    //Test pages
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.englishLanguage, pages);
  });

  it('... tracker Page ', function() {
    var pages = ['/tracker'];

    //Test pages
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.englishLanguage, pages);
  });

  it('... metrics Page ', function() {
    var pages = ['/metrics'];

    //Test pages
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.englishLanguage, pages);
  });

  it('... health Page ', function() {
    var pages = ['/health'];

    //Test pages
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.englishLanguage, pages);
  });

  it('... configuration Page ', function() {
    var pages = ['/configuration'];

    //Test pages
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.englishLanguage, pages);
  });

  xit('... audits Page ', function() {
    var pages = ['/audits'];

    //Test pages
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.englishLanguage, pages);
  });

  it('... logs Page ', function() {
    var pages = ['/logs'];

    //Test pages
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.englishLanguage, pages);
  });

  it('... disclosure Page ', function() {
    var pages = ['/disclosure'];

    //Test pages
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.englishLanguage, pages);
  });
});
