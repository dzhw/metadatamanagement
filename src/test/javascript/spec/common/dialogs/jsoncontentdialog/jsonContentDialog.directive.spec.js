/* global describe */
/* global beforeEach */
/* global it */
/* global expect */
/* global inject */
/* global spyOn */
/* global mockSso */
'use strict';

describe('jsonContentDialog.directive', function() {
  var $compile;
  var $rootScope;
  var $mdDialog;

  var capturedConfig = null;

  beforeEach(module('metadatamanagementApp'));
  beforeEach(module('templates'));
  beforeEach(mockSso);
  beforeEach(inject(function(_$compile_, _$rootScope_, _$mdDialog_) {
    $compile = _$compile_;
    $rootScope = _$rootScope_;
    $mdDialog = _$mdDialog_;

    spyOn($mdDialog, 'show').and.callFake(function(config) {
      capturedConfig = config;
    });
  }));

  it('should call $mdDialog.show with the correct settings', function() {
    var directive = '<json-content-dialog content="content"' +
      'header-title="headerTitle"></json-content-dialog>';
    var scope = $rootScope.$new();
    scope.content = {'foo': 'bar'};
    scope.headerTitle = 'Test';

    var element = $compile(directive)(scope);
    $rootScope.$digest();
    var buttons = element.find('button');

    expect(buttons.length).toBe(1);
    buttons[0].click();

    expect($mdDialog.show).toHaveBeenCalled();
    expect(capturedConfig.locals.headerTitle).toEqual('Test');
    expect(capturedConfig.locals.content).toEqual(scope.content);

    var controller = capturedConfig.controller;

    expect(controller).toBeDefined();
    var dialogControllerScope = {};

    controller(dialogControllerScope, $mdDialog, scope.content,
      scope.headerTitle);

    expect(dialogControllerScope.cancel).toBe($mdDialog.hide);
    expect(dialogControllerScope.content).toBe(scope.content);
    expect(dialogControllerScope.headerTitle).toBe(scope.headerTitle);
  });

  it('should exclude fields if an exclude list is provided', function() {
    var directive = '<json-content-dialog content="content"' +
      'header-title="headerTitle" exclude-fields="excludeFields">' +
      '</json-content-dialog>';
    var content = {a: '1', b: '2'};
    var expectedContent = {a: '1'};
    var scope = $rootScope.$new();
    scope.content = content;
    scope.headerTitle = 'Test';
    scope.excludeFields = ['b'];

    var element = $compile(directive)(scope);
    $rootScope.$digest();
    var buttons = element.find('button');

    expect(buttons.length).toBe(1);
    buttons[0].click();

    expect($mdDialog.show).toHaveBeenCalled();
    expect(capturedConfig.locals.content).toEqual(expectedContent);
  });
});
