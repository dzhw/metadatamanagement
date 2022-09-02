/* global describe, beforeEach, inject, it, expect */
'use strict';

describe('fdz-require-one-validator', function() {

  var $scope;
  var testForm;

  var dom = '<form name="testForm">' +
    '<input type="text" fdz-required="!source.b" name="a"' +
    ' ng-model="source.a">' +
    '<input type="text" fdz-required="!source.a" name="b"' +
    ' ng-model="source.b">' +
    '</form>';

  beforeEach(module('metadatamanagementApp'));
  beforeEach(mockSso);
  beforeEach(inject(function(_$rootScope_, _$compile_) {
    $scope = _$rootScope_.$new();
    $scope.source = {a: '', b: ''};

    _$compile_(dom)($scope);
    testForm = $scope.testForm;
  }));

  it('should should validate to true if a view value exists', function() {
    testForm.a.$setViewValue('test');
    $scope.$digest();
    expect(testForm.$valid).toBe(true);
    expect(testForm.a.$valid).toBe(true);
    expect($scope.source.a).toEqual('test');
  });

  it('should validate to false if siblings don\'t have a value', function() {
    $scope.$digest();
    expect(testForm.$invalid).toBe(true);
    expect(testForm.a.$invalid).toBe(true);
    expect(testForm.b.$invalid).toBe(true);
  });

  it('should validate to true if sibling has a value', function() {
    testForm.b.$setViewValue('test');
    $scope.$digest();
    expect(testForm.$valid).toBe(true);
    expect(testForm.a.$valid).toBe(true);
    expect(testForm.b.$valid).toBe(true);
    expect($scope.source.a).toBeFalsy();
    expect($scope.source.b).toEqual('test');
  });
});
