/* global element */
/* global by */
/* global browser */
/* global expect */
/* @Author Daniel Katzberg */
'use strict';

//var loginHelper = require('../utils/loginHelper');

function checkCurrentUrl(checkLinkUrl) {
  browser.getCurrentUrl().then(function(urlCurrent) {
    expect(checkLinkUrl).toBe(urlCurrent);
  });
  browser.ignoreSynchronization=false;
}

//This function checks links of a page.
//If the link is broken, this method return a error
function checkBrokenLinks(links) {

  for (var linksIndex = 0; linksIndex < links.length; linksIndex++) {

    //ignore empty hrefs
    if (!links[linksIndex] ||
      links[linksIndex].indexOf('mailto') !== -1) {
      continue;
    }

    //Check expected link is the new curent link. if not, link is broken and
    //will be directed to the error page
    var checkLinkUrl = links[linksIndex];
    if (!checkLinkUrl.startsWith(browser.baseUrl)) {
      browser.ignoreSynchronization=true;
    }
    //switch off waiting for angular in case this is not our domain
    browser.navigate().to(links[linksIndex])
      .then(checkCurrentUrl(checkLinkUrl));
  }
}

//Loads a page and read all links.
function checkLinks(language, pages, checkAllLinksOnPage) {

  for (var pageIndex = 0; pageIndex < pages.length; pageIndex++) {

    //load page and wait for it
    browser.get(language + pages[pageIndex]);

    if (checkAllLinksOnPage) {
      element.all(by.tagName('a')).getAttribute('href').then(checkBrokenLinks);
    } else {
      //check for broken links only in content view
      element(by.uiView('content')).all(by.tagName('a')).getAttribute('href').then(checkBrokenLinks);
    }
  }
}

module.exports.checkLinks = checkLinks;
