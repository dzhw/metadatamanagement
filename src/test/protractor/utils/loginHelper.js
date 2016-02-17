/* global element */
/* global by */
/* global expect */
/* @Author Daniel Katzberg */
'use strict';

function logout() {
  //expect(element(by.dataTranslate('global.menu.entities.main'))).toBe(true);

  //Click at 'Account'
  element(by.dataTranslate('global.menu.account.main')).click();

  //Click at Logout
  element(by.ngClick('logout()')).click();

  element.all(by.dataTranslate('global.menu.entities.main')).then(function (entities) {
    expect(entities.length).toBe(0); //four drop downs
  });
}

function login() {

  //Expect no entities dropdown (only visible with a login)
  expect(element(by.dataTranslate('global.menu.entities.main')).isPresent()).toBe(false);

  //Click at 'Account'
  element(by.dataTranslate('global.menu.account.main')).click();

  //Check for login button
  if (!(element(by.uiSref('login')).isPresent())) {
    logout();
  }

  //Click at 'Login'
  expect(element(by.uiSref('login')).isPresent()).toBe(true);
  element(by.uiSref('login')).click();

  //add account information and login
  element(by.id('username')).sendKeys('admin');
  element(by.id('password')).sendKeys('admin');
  element(by.dataTranslate('login.form.button')).click();

  element.all(by.dataTranslate('global.menu.entities.main')).then(function (entities) {
    expect(entities.length).toBe(1); //four drop downs
  });
}

module.exports.login = login;
module.exports.logout = logout;
