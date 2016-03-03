/* global describe */
/* global it */
/* global browser */
/* global afterEach */
/* global beforeEach */
/* global xit */
/* @Author Daniel Katzberg */

'use strict';

describe('Check ENGLISH language with a login for ', function() {
  var brokenLinks = require('../utils/brokenLinks');
  var utilMissingTranslations = require('../utils/findMissingTranslations');
  var cacheHelper = require('../utils/cacheHelper');
  var loginHelper = require('../utils/loginHelper');

  //Login only once
  beforeEach(function() {
    browser.get(utilMissingTranslations.englishLanguage + '/');
    loginHelper.login();
    browser.ignoreSynchronization = true;
  });

  //Logout only once
  afterEach(function() {
    browser.ignoreSynchronization = false;
    browser.get(utilMissingTranslations.englishLanguage + '/');
    loginHelper.logout();
    cacheHelper.clearCache();
  });

  it('... welcome Page ', function() {
    var pages = ['/'];

    //Check broken links
    brokenLinks.checkLinks(
      utilMissingTranslations.englishLanguage, pages);
    browser.get(utilMissingTranslations.germanLanguage + pages[0]);
  });

  it('... dataAcquisitionProjects Page ', function() {
    var pages = ['/dataAcquisitionProjects'];

    //Check broken links
    brokenLinks.checkLinks(
      utilMissingTranslations.englishLanguage, pages);
    browser.get(utilMissingTranslations.germanLanguage + pages[0]);
  });

  it('... surveys Page ', function() {
    var pages = ['/surveys'];

    //Check broken links
    brokenLinks.checkLinks(
      utilMissingTranslations.englishLanguage, pages);
    browser.get(utilMissingTranslations.germanLanguage + pages[0]);
  });

  xit('... variables Page ', function() {
    var pages = ['/variables'];

    //Check broken links
    brokenLinks.checkLinks(
      utilMissingTranslations.englishLanguage, pages);
    browser.get(utilMissingTranslations.germanLanguage + pages[0]);
  });

  it('... settings Page ', function() {
    var pages = ['/settings'];

    //Check broken links
    brokenLinks.checkLinks(
      utilMissingTranslations.englishLanguage, pages);
    browser.get(utilMissingTranslations.germanLanguage + pages[0]);
  });

  it('... password Page ', function() {
    var pages = ['/password'];

    //Check broken links
    brokenLinks.checkLinks(
      utilMissingTranslations.englishLanguage, pages);
    browser.get(utilMissingTranslations.germanLanguage + pages[0]);
  });

  it('... user-management Page ', function() {
    var pages = ['/user-management'];

    //Check broken links
    brokenLinks.checkLinks(
      utilMissingTranslations.englishLanguage, pages);
    browser.get(utilMissingTranslations.germanLanguage + pages[0]);
  });

  it('... metrics Page ', function() {
    var pages = ['/metrics'];

    //Check broken links
    brokenLinks.checkLinks(
      utilMissingTranslations.englishLanguage, pages);
    browser.get(utilMissingTranslations.germanLanguage + pages[0]);
  });

  it('... health Page ', function() {
    var pages = ['/health'];

    //Check broken links
    brokenLinks.checkLinks(
      utilMissingTranslations.englishLanguage, pages);
    browser.get(utilMissingTranslations.germanLanguage + pages[0]);
  });

  it('... configuration Page ', function() {
    var pages = ['/configuration'];

    //Check broken links
    brokenLinks.checkLinks(
      utilMissingTranslations.englishLanguage, pages);
    browser.get(utilMissingTranslations.germanLanguage + pages[0]);
  });

  it('... logs Page ', function() {
    var pages = ['/logs'];

    //Check broken links
    brokenLinks.checkLinks(
      utilMissingTranslations.englishLanguage, pages);
    browser.get(utilMissingTranslations.germanLanguage + pages[0]);
  });

  it('... disclosure Page ', function() {
    var pages = ['/disclosure'];

    //Check broken links
    brokenLinks.checkLinks(
      utilMissingTranslations.englishLanguage, pages);
    browser.get(utilMissingTranslations.germanLanguage + pages[0]);
  });
});
