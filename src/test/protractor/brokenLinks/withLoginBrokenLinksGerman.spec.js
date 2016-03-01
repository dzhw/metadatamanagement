/* global describe */
/* global it */
/* global browser */
/* global afterAll */
/* global beforeAll */
/* global afterEach */
/* global beforeEach */
/* global xit */
/* @Author Daniel Katzberg */

'use strict';

describe('Check GERMAN language with a login for ', function() {
  var brokenLinks = require('../utils/brokenLinks');
  var utilMissingTranslations = require('../utils/findMissingTranslations');
  var cacheHelper = require('../utils/cacheHelper');
  var loginHelper = require('../utils/loginHelper');

  beforeEach(function() {
    browser.ignoreSynchronization = true;
  });

  afterEach(function() {
    browser.ignoreSynchronization = false;
  });

  //Login only once
  beforeAll(function() {
    browser.get(utilMissingTranslations.germanLanguage + '/');
    loginHelper.login();
  });

  //Logout only once
  afterAll(function() {
    browser.get(utilMissingTranslations.germanLanguage + '/');
    loginHelper.logout();
    cacheHelper.clearCache();
  });

  it('... welcome Page ', function() {
    var pages = ['/'];

    //Check broken links
    brokenLinks.checkLinks(
      utilMissingTranslations.germanLanguage, pages);
  });

  it('... dataAcquisitionProjects Page ', function() {
    var pages = ['/dataAcquisitionProjects'];

    //Check broken links
    brokenLinks.checkLinks(
      utilMissingTranslations.germanLanguage, pages);
  });

  it('... surveys Page ', function() {
    var pages = ['/surveys'];

    //Check broken links
    brokenLinks.checkLinks(
      utilMissingTranslations.germanLanguage, pages);
  });

  it('... variables Page ', function() {
    var pages = ['/variables'];

    //Check broken links
    brokenLinks.checkLinks(
      utilMissingTranslations.germanLanguage, pages);
  });

  it('... settings Page ', function() {
    var pages = ['/settings'];

    //Check broken links
    brokenLinks.checkLinks(
      utilMissingTranslations.germanLanguage, pages);
  });

  it('... password Page ', function() {
    var pages = ['/password'];

    //Check broken links
    brokenLinks.checkLinks(
      utilMissingTranslations.germanLanguage, pages);
  });

  it('... user-management Page ', function() {
    var pages = ['/user-management'];

    //Check broken links
    brokenLinks.checkLinks(
      utilMissingTranslations.germanLanguage, pages);
  });

  it('... tracker Page ', function() {
    var pages = ['/tracker'];

    //Check broken links
    brokenLinks.checkLinks(
      utilMissingTranslations.germanLanguage, pages);
  });

  it('... metrics Page ', function() {
    var pages = ['/metrics'];

    //Check broken links
    brokenLinks.checkLinks(
      utilMissingTranslations.germanLanguage, pages);
  });

  it('... health Page ', function() {
    var pages = ['/health'];

    //Check broken links
    brokenLinks.checkLinks(
      utilMissingTranslations.germanLanguage, pages);
  });

  it('... configuration Page ', function() {
    var pages = ['/configuration'];

    //Check broken links
    brokenLinks.checkLinks(
      utilMissingTranslations.germanLanguage, pages);
  });

  xit('... audits Page ', function() {
    var pages = ['/audits'];

    //Check broken links
    brokenLinks.checkLinks(
      utilMissingTranslations.germanLanguage, pages);
  });

  it('... logs Page ', function() {
    var pages = ['/logs'];

    //Check broken links
    brokenLinks.checkLinks(
      utilMissingTranslations.germanLanguage, pages);
  });

  it('... disclosure Page ', function() {
    var pages = ['/disclosure'];

    //Check broken links
    brokenLinks.checkLinks(
      utilMissingTranslations.germanLanguage, pages);
  });
});
