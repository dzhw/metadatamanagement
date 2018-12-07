/* global describe, beforeEach, it, expect, spyOn, inject, _ */
'use strict';

describe('ProjectUpdateAccessService', function() {
  var ProjectUpdateAccessService;
  var Principal;
  var CurrentProjectService;
  var publisherName = 'publisher';
  var dataProviderName = 'dataProvider';
  var defaultProject;

  var trueOnDataProvider = function(role) {
    return role === 'ROLE_DATA_PROVIDER';
  };

  var trueOnPublisher = function(role) {
    return role === 'PUBLISHER';
  };

  beforeEach(module('metadatamanagementApp'));
  beforeEach(inject(function(_ProjectUpdateAccessService_, _Principal_,
                             _CurrentProjectService_) {
    ProjectUpdateAccessService = _ProjectUpdateAccessService_;
    Principal = _Principal_;
    CurrentProjectService = _CurrentProjectService_;

    defaultProject = {
      assigneeGroup: 'DATA_PROVIDER',
      configuration: {
        publishers: ['publisher'],
        dataProviders: ['dataProvider'],
        surveysState: {
          publisherReady: false,
          dataProviderReady: false
        }
      }
    };
  }));

  it('should return false if no project is selected', function() {
    spyOn(CurrentProjectService, 'getCurrentProject')
      .and.returnValue(undefined);

    expect(ProjectUpdateAccessService.isUpdateAllowed(undefined,'')).toBe(false);
  });

  it('should return true if an updateable project is selected', function() {
    spyOn(Principal, 'loginName').and.returnValue(publisherName);
    spyOn(Principal, 'hasAuthority').and.returnValue(true);
    expect(ProjectUpdateAccessService.isUpdateAllowed(defaultProject)).toBe(true);
  });

  it('should return true if the survey state is still false', function() {
    spyOn(Principal, 'loginName').and.returnValue(dataProviderName);
    spyOn(Principal, 'hasAuthority').and.callFake(trueOnDataProvider);
    _.set(defaultProject, 'configuration.surveysState.publisherReady', false);
    _.set(defaultProject, 'configuration.surveysState.dataProviderReady', false);

    expect(ProjectUpdateAccessService.isUpdateAllowed(defaultProject, 'surveys')).toBe(true);
  });

  it('should return false if survey state is marked as dataProviderReady', function() {
    spyOn(Principal, 'loginName').and.returnValue(dataProviderName);
    spyOn(Principal, 'hasAuthority').and.callFake(trueOnDataProvider);
    _.set(defaultProject, 'configuration.surveysState.publisherReady', false);
    _.set(defaultProject, 'configuration.surveysState.dataProviderReady', true);

    expect(ProjectUpdateAccessService.isUpdateAllowed(defaultProject, 'surveys')).toBe(false);
  });

  it('should return true if studies state is still false', function() {
    spyOn(Principal, 'loginName').and.returnValue(dataProviderName);
    spyOn(Principal, 'hasAuthority').and.callFake(trueOnDataProvider);
    _.set(defaultProject, 'configuration.studiesState.publisherReady', false);
    _.set(defaultProject, 'configuration.studiesState.dataProviderReady', false);

    expect(ProjectUpdateAccessService.isUpdateAllowed(defaultProject, 'studies')).toBe(true);
  });

  it('should return false if studies state is marked as dataProviderReady', function() {
    spyOn(Principal, 'loginName').and.returnValue(dataProviderName);
    spyOn(Principal, 'hasAuthority').and.callFake(trueOnDataProvider);
    _.set(defaultProject, 'configuration.studiesState.publisherReady', false);
    _.set(defaultProject, 'configuration.studiesState.dataProviderReady', true);

    expect(ProjectUpdateAccessService.isUpdateAllowed(defaultProject, 'studies')).toBe(false);
  });

  it('should return true if instruments state is still false', function() {
    spyOn(Principal, 'loginName').and.returnValue(dataProviderName);
    spyOn(Principal, 'hasAuthority').and.callFake(trueOnDataProvider);
    _.set(defaultProject, 'configuration.instrumentsState.publisherReady', false);
    _.set(defaultProject, 'configuration.instrumentsState.dataProviderReady', false);

    expect(ProjectUpdateAccessService.isUpdateAllowed(defaultProject, 'instruments')).toBe(true);
  });

  it('should return false if instruments state is marked as dataProviderReady', function() {
    spyOn(Principal, 'loginName').and.returnValue(dataProviderName);
    spyOn(Principal, 'hasAuthority').and.callFake(trueOnDataProvider);
    _.set(defaultProject, 'configuration.instrumentsState.publisherReady', false);
    _.set(defaultProject, 'configuration.instrumentsState.dataProviderReady', true);

    expect(ProjectUpdateAccessService.isUpdateAllowed(defaultProject, 'instruments')).toBe(false);
  });

  it('should return true if variables state is still false', function() {
    spyOn(Principal, 'loginName').and.returnValue(dataProviderName);
    spyOn(Principal, 'hasAuthority').and.callFake(trueOnDataProvider);
    _.set(defaultProject, 'configuration.variablesState.publisherReady', false);
    _.set(defaultProject, 'configuration.variablesState.dataProviderReady', false);

    expect(ProjectUpdateAccessService.isUpdateAllowed(defaultProject, 'variables')).toBe(true);
  });

  it('should return false if variables state is marked as dataProviderReady', function() {
    spyOn(Principal, 'loginName').and.returnValue(dataProviderName);
    spyOn(Principal, 'hasAuthority').and.callFake(trueOnDataProvider);
    _.set(defaultProject, 'configuration.variablesState.publisherReady', false);
    _.set(defaultProject, 'configuration.variablesState.dataProviderReady', true);

    expect(ProjectUpdateAccessService.isUpdateAllowed(defaultProject, 'variables')).toBe(false);
  });

  it('should return true if data_sets status is still false', function() {
    spyOn(Principal, 'loginName').and.returnValue(dataProviderName);
    spyOn(Principal, 'hasAuthority').and.callFake(trueOnDataProvider);
    _.set(defaultProject, 'configuration.dataSetsState.publisherReady', false);
    _.set(defaultProject, 'configuration.dataSetsState.dataProviderReady', false);

    expect(ProjectUpdateAccessService.isUpdateAllowed(defaultProject, 'data_sets')).toBe(true);
  });

  it('should return false if data_sets state is marked as publisherReady', function() {
    spyOn(Principal, 'loginName').and.returnValue(publisherName);
    spyOn(Principal, 'hasAuthority').and.callFake(trueOnPublisher);
    _.set(defaultProject, 'configuration.dataSetsState.publisherReady', true);
    _.set(defaultProject, 'configuration.dataSetsState.dataProviderReady', false);

    expect(ProjectUpdateAccessService.isUpdateAllowed(defaultProject, 'data_sets')).toBe(false);
  });

  it('should return true if questions state is still false', function() {
    spyOn(Principal, 'loginName').and.returnValue(dataProviderName);
    spyOn(Principal, 'hasAuthority').and.callFake(trueOnDataProvider);
    _.set(defaultProject, 'configuration.questionsState.publisherReady', false);
    _.set(defaultProject, 'configuration.questionsState.dataProviderReady', false);

    expect(ProjectUpdateAccessService.isUpdateAllowed(defaultProject, 'questions')).toBe(true);
  });

  it('should return false if questions state is marked as publisherReady', function() {
    spyOn(Principal, 'loginName').and.returnValue(dataProviderName);
    spyOn(Principal, 'hasAuthority').and.callFake(trueOnDataProvider);
    _.set(defaultProject, 'configuration.questionsState.publisherReady', true);
    _.set(defaultProject, 'configuration.questionsState.dataProviderReady', false);

    expect(ProjectUpdateAccessService.isUpdateAllowed(undefined, 'questions')).toBe(false);
  });

  it('should return false if a project has been released', function() {
    spyOn(Principal, 'loginName').and.returnValue(dataProviderName);
    spyOn(Principal, 'hasAuthority').and.callFake(trueOnDataProvider);
    defaultProject.release = {};

    expect(ProjectUpdateAccessService.isUpdateAllowed(defaultProject)).toBe(false);
  });

  it('should return false if the assigneeGroup doesn\'t match the current' +
    'user\'s role', function() {
    defaultProject.assigneeGroup = 'PUBLISHER';
    spyOn(Principal, 'loginName').and.returnValue(dataProviderName);
    spyOn(Principal, 'hasAuthority').and.callFake(trueOnDataProvider);

    expect(ProjectUpdateAccessService.isUpdateAllowed(defaultProject)).toBe(false);
  });

  it('should return false if assigneeGroup is unknown', function() {
    spyOn(Principal, 'loginName').and.returnValue(dataProviderName);
    spyOn(Principal, 'hasAuthority').and.callFake(trueOnDataProvider);
    defaultProject.assigneeGroup = '';
    expect(ProjectUpdateAccessService.isUpdateAllowed(defaultProject)).toBe(false);
  });

  it('should not call CurrentProjectService if the project was given as a ' +
    'parameter', function() {
    var spy = spyOn(CurrentProjectService, 'getCurrentProject').and
      .callThrough();
    ProjectUpdateAccessService.isUpdateAllowed({});
    expect(spy).not.toHaveBeenCalled();
  });
});
