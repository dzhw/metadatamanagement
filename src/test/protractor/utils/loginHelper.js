/* global element */
/* global by */
/* global expect */
/* @Author Daniel Katzberg */
'use strict';

/* Checks if a test has a valid login in the MDM */
function isLogin() {
  element.all(by.dataTranslate('global.menu.entities.main')).then(function(
    entities) {
    expect(entities.length).toBe(1); //four drop downs
  });
  expect(element(by.uiSref('login')).isPresent()).toBe(false);
  expect(element(by.ngClick('logout()')).isPresent()).toBe(true);
}

/* Log out */
function logout() {

  //Click at 'Account'
  element(by.dataTranslate('global.menu.account.main')).click();

  //Click at Logout
  element(by.ngClick('logout()')).click();

  element.all(by.dataTranslate('global.menu.entities.main')).then(function(
    entities) {
    expect(entities.length).toBe(0); //four drop downs
  });
}

/* Log in */
function login() {

  //deactivate old login session. a fresh session should be started.
  //first of all: logout
  element(by.uiSref('login')).isPresent().then(function(value) {
    if (value === false) {
      logout();
    }
  });

  //Expect no entities dropdown (only visible with a login)
  expect(element(by.dataTranslate('global.menu.entities.main')).isPresent())
    .toBe(false);

  //Click at 'Account'
  element(by.dataTranslate('global.menu.account.main')).click();

  //Click at 'Login'
  expect(element(by.uiSref('login')).isPresent()).toBe(true);
  element(by.uiSref('login')).click();

  //add account information and login
  element(by.id('username')).sendKeys('protractortestuser');
  element(by.id('password')).sendKeys('testUser');
  element(by.dataTranslate('login.form.button')).click();

  //check if Logged in
  //isLogin();
}

module.exports.login = login;
module.exports.logout = logout;
module.exports.isLogin = isLogin;
