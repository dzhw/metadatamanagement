/* global describe, beforeEach, inject, it, expect, spyOn, _ */
'use strict';

describe('TagEditorController', function() {
  var StudySearchService;
  var $scope;
  var $controller;
  var $q;

  beforeEach(module('metadatamanagementApp'));
  beforeEach(inject(function(_StudySearchService_, _$rootScope_,
                             _$controller_, _$q_) {
    StudySearchService = _StudySearchService_;
    $scope = _$rootScope_.$new();
    $controller = _$controller_;
    $q = _$q_;
  }));

  it('should initialize the tag attribute if it is undefined', function() {
    $controller('TagEditorController', {$scope: $scope});

    expect($scope.tags).toBeDefined();
    expect($scope.tags.de).toBeDefined();
    expect($scope.tags.en).toBeDefined();
  });

  it('should initialize the "de" field on tags if it is undefined', function() {
    var en = [];
    $scope.tags = {en: en};
    $controller('TagEditorController', {$scope: $scope});

    expect($scope.tags.de).toBeDefined();
    expect($scope.tags.en).toBe(en);
  });

  it('should initialize the "en" field on tags if it is undefined', function() {
    var de = [];
    $scope.tags = {de: de};
    $controller('TagEditorController', {$scope: $scope});

    expect($scope.tags.de).toBe(de);
    expect($scope.tags.en).toBeDefined();
  });

  it('should search for existing tags and remove duplicates from search result',
    function() {
      var deferred = $q.defer();
      deferred.resolve(['aa', 'ab', 'ac']);
      spyOn(StudySearchService, 'findTags').and.returnValue(deferred.promise);
      $scope.tagSearch = StudySearchService.findTags;

      _.set($scope, 'tags.de', ['aa', 'ab']);

      $controller('TagEditorController', {$scope: $scope});
      var promise = $scope.searchTags('a', 'de');

      $scope.$apply();
      var result = promise.$$state.value;
      expect(result).toBeDefined();
      expect(result.length).toEqual(1);
      expect(result[0]).toEqual('ac');
    });
});
