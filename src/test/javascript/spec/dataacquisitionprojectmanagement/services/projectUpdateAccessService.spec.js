/* global describe */
/* global beforeEach */
/* global it */
/* global expect */
/* global spyOn */
/* global inject */
'use strict';

describe('ProjectUpdateAccessService', function() {
  var ProjectUpdateAccessService;
  var Principal;
  var CurrentProjectService;

  beforeEach(module('metadatamanagementApp'));
  beforeEach(inject(function(_ProjectUpdateAccessService_, _Principal_,
                             _CurrentProjectService_) {
    ProjectUpdateAccessService = _ProjectUpdateAccessService_;
    Principal = _Principal_;
    CurrentProjectService = _CurrentProjectService_;
  }));

  it('should return false if no project is selected', function() {
    spyOn(CurrentProjectService, 'getCurrentProject')
      .and.returnValue(undefined);

    expect(ProjectUpdateAccessService.isUpdateAllowed()).toBe(false);
  });

  it('should return true if an updateable project is selected', function() {
    spyOn(CurrentProjectService, 'getCurrentProject').and.returnValue({
      assigneeGroup: 'PUBLISHER'
    });
    spyOn(Principal, 'hasAuthority').and.returnValue(true);

    expect(ProjectUpdateAccessService.isUpdateAllowed()).toBe(true);
  });

  it('should return false if a project has been released', function() {
    spyOn(CurrentProjectService, 'getCurrentProject').and.returnValue({
      release: {}
    });

    expect(ProjectUpdateAccessService.isUpdateAllowed()).toBe(false);
  });

  it('should return false if the assigneeGroup doesn\'t match the current' +
    'user\'s role', function() {
    spyOn(CurrentProjectService, 'getCurrentProject').and.returnValue({
      assigneeGroup: 'DATA_PROVIDER'
    });
    spyOn(Principal, 'hasAuthority').and.returnValue(false);

    expect(ProjectUpdateAccessService.isUpdateAllowed()).toBe(false);
  });

  it('should return false if assigneeGroup is unknown', function() {
    spyOn(CurrentProjectService, 'getCurrentProject').and.returnValue({
      assigneeGroup: ''
    });
    expect(ProjectUpdateAccessService.isUpdateAllowed()).toBe(false);
  });
});
