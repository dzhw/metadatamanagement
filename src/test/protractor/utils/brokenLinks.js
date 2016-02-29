/* global element */
/* global by */
/* global browser */
/* global expect */
/* @Author Daniel Katzberg */
'use strict';

//This function checks links of a page.
//If the link is broken, this method return a error
function checkBrokenLinks(links) {
  for (var linksIndex = 0; linksIndex < links.length; linksIndex++) {

    //ignore empty hrefs
    if (links[linksIndex].length === 0) {
      continue;
    }

    //Check expected link is the new curent link. if not, link is broken and
    //will be directed to the error page
    var checkLinkUrl = links[linksIndex];
    browser.navigate().to(links[linksIndex])
      .then(checkCurrentUrl(checkLinkUrl));
  }
}

//Loads a page and read all links.
function checkLinks(language, pages) {

  for (var pageIndex = 0; pageIndex < pages.length; pageIndex++) {
    //load page
    browser.get(language + pages[pageIndex]);

    //check for broken links
    element.all(by.tagName('a')).getAttribute('href').then(checkBrokenLinks);
  }
}

function checkCurrentUrl(checkLinkUrl) {
  browser.getCurrentUrl().then(function(urlCurrent) {
    expect(checkLinkUrl).toBe(urlCurrent);
  });
}

module.exports.checkLinks = checkLinks;
