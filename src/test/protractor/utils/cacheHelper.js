/* global browser */
/* @Author Daniel Katzberg */
'use strict';

function clearCache() {
    browser.executeScript('window.sessionStorage.clear();'); //clear session
    browser.executeScript('window.localStorage.clear();'); //clear local storage
}

module.exports.clearCache = clearCache;
