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
  
  it('should return true if an updateable project is selected', function() {
    spyOn(CurrentProjectService, 'getCurrentProject').and.returnValue({
      assigneeGroup: 'DATA_PROVIDER',
      configuration : {
        surveysState : {
          publisherReady : false,
          dataProviderReady : false
        }
      }
    });
    spyOn(Principal, 'hasAuthority').and.returnValue(true);

    expect(ProjectUpdateAccessService.isUpdateAllowed(undefined, 'surveys')).toBe(true);
  });

  it('should return false if an project is marked as dataProviderReady', function() {
    spyOn(CurrentProjectService, 'getCurrentProject').and.returnValue({
      assigneeGroup: 'DATA_PROVIDER',
      configuration : {
        surveysState : {
          publisherReady : false,
          dataProviderReady : true
        }
      }
    });
    spyOn(Principal, 'hasAuthority').and.returnValue(true);

    expect(ProjectUpdateAccessService.isUpdateAllowed(undefined, 'surveys')).toBe(false);
  });
  it('should return true if an updateable project is selected', function() {
    spyOn(CurrentProjectService, 'getCurrentProject').and.returnValue({
      assigneeGroup: 'DATA_PROVIDER',
      configuration : {
        studiesState : {
          publisherReady : false,
          dataProviderReady : false
        }
      }
    });
    spyOn(Principal, 'hasAuthority').and.returnValue(true);

    expect(ProjectUpdateAccessService.isUpdateAllowed(undefined, 'studies')).toBe(true);
  });

  it('should return false if an project is marked as dataProviderReady', function() {
    spyOn(CurrentProjectService, 'getCurrentProject').and.returnValue({
      assigneeGroup: 'DATA_PROVIDER',
      configuration : {
        studiesState : {
          publisherReady : false,
          dataProviderReady : true
        }
      }
    });
    spyOn(Principal, 'hasAuthority').and.returnValue(true);

    expect(ProjectUpdateAccessService.isUpdateAllowed(undefined, 'studies')).toBe(false);
  });

  it('should return true if an updateable project is selected', function() {
    spyOn(CurrentProjectService, 'getCurrentProject').and.returnValue({
      assigneeGroup: 'DATA_PROVIDER',
      configuration : {
        instrumentsState : {
          publisherReady : false,
          dataProviderReady : false
        }
      }
    });
    spyOn(Principal, 'hasAuthority').and.returnValue(true);

    expect(ProjectUpdateAccessService.isUpdateAllowed(undefined, 'instruments')).toBe(true);
  });

  it('should return false if an project is marked as dataProviderReady', function() {
    spyOn(CurrentProjectService, 'getCurrentProject').and.returnValue({
      assigneeGroup: 'DATA_PROVIDER',
      configuration : {
        instrumentsState : {
          publisherReady : false,
          dataProviderReady : true
        }
      }
    });
    spyOn(Principal, 'hasAuthority').and.returnValue(true);

    expect(ProjectUpdateAccessService.isUpdateAllowed(undefined, 'instruments')).toBe(false);
  });

  it('should return true if an updateable project is selected', function() {
    spyOn(CurrentProjectService, 'getCurrentProject').and.returnValue({
      assigneeGroup: 'DATA_PROVIDER',
      configuration : {
        variablesState : {
          publisherReady : false,
          dataProviderReady : false
        }
      }
    });
    spyOn(Principal, 'hasAuthority').and.returnValue(true);

    expect(ProjectUpdateAccessService.isUpdateAllowed(undefined, 'variables')).toBe(true);
  });

  it('should return false if an project is marked as dataProviderReady', function() {
    spyOn(CurrentProjectService, 'getCurrentProject').and.returnValue({
      assigneeGroup: 'DATA_PROVIDER',
      configuration : {
        variablesState : {
          publisherReady : true,
          dataProviderReady : true
        }
      }
    });
    spyOn(Principal, 'hasAuthority').and.returnValue(true);

    expect(ProjectUpdateAccessService.isUpdateAllowed(undefined, 'variables')).toBe(false);
  });

  it('should return true if an updateable project is selected', function() {
    spyOn(CurrentProjectService, 'getCurrentProject').and.returnValue({
      assigneeGroup: 'DATA_PROVIDER',
      configuration : {
        dataSetsState : {
          publisherReady : false,
          dataProviderReady : false
        }
      }
    });
    spyOn(Principal, 'hasAuthority').and.returnValue(true);

    expect(ProjectUpdateAccessService.isUpdateAllowed(undefined, 'data_sets')).toBe(true);
  });

  it('should return false if an project is marked as dataProviderReady', function() {
    spyOn(CurrentProjectService, 'getCurrentProject').and.returnValue({
      assigneeGroup: 'DATA_PROVIDER',
      configuration : {
        dataSetsState : {
          publisherReady : true,
          dataProviderReady : false
        }
      }
    });
    spyOn(Principal, 'hasAuthority').and.returnValue(true);

    expect(ProjectUpdateAccessService.isUpdateAllowed(undefined, 'data_sets')).toBe(false);
  });

  it('should return true if an updateable project is selected', function() {
    spyOn(CurrentProjectService, 'getCurrentProject').and.returnValue({
      assigneeGroup: 'DATA_PROVIDER',
      configuration : {
        questionsState : {
          publisherReady : false,
          dataProviderReady : false
        }
      }
    });
    spyOn(Principal, 'hasAuthority').and.returnValue(true);

    expect(ProjectUpdateAccessService.isUpdateAllowed(undefined, 'questions')).toBe(true);
  });

  it('should return false if an project is marked as dataProviderReady', function() {
    spyOn(CurrentProjectService, 'getCurrentProject').and.returnValue({
      assigneeGroup: 'DATA_PROVIDER',
      configuration : {
        questionsState : {
          publisherReady : true,
          dataProviderReady : false
        }
      }
    });
    spyOn(Principal, 'hasAuthority').and.returnValue(true);

    expect(ProjectUpdateAccessService.isUpdateAllowed(undefined, 'questions')).toBe(false);
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

  it('should not call CurrentProjectService if the project was given as a ' +
    'parameter', function() {
    var spy = spyOn(CurrentProjectService, 'getCurrentProject').and
      .callThrough();
    ProjectUpdateAccessService.isUpdateAllowed({});
    expect(spy).not.toHaveBeenCalled();
  });
});
