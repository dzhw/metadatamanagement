/* global describe */
/* global beforeEach */
/* global it */
/* global inject */
/* global expect */

'use strict';

describe('userList.directive', function() {
  var $scope;
  var UserResource;
  var directiveElement;
  var compile;
  var userEndpoint = "/api/users/localuser/public";
  var $httpBackend;
  var element;

	beforeEach(module('metadatamanagementApp'));
	beforeEach(module('templates'));  
  beforeEach(inject(function(_$compile_, _$rootScope_, $injector) {
    compile= _$compile_;
    $scope = _$rootScope_.$new();
    $httpBackend = $injector.get('$httpBackend');

    // test project 
    $scope.project  = {
      version : 0,
      createdDate : "2022-09-29T11:28:00.99",
      createdBy : "localuser",
      lastModifiedDate : "2022-09-29T11:28:00.99",
      lastModifiedBy : "localuser",
      shadow : false,
      hidden : false,
      id : "test",
      masterId : "test",
      hasBeenReleasedBefore : false,
      configuration : {
        publishers : [ "localuser" ],
        dataProviders : [ ],
        requirements : {
          analysisPackagesRequired : false,
          dataPackagesRequired : true,
          publicationsRequired : false,
          surveysRequired : false,
          dataSetsRequired : false,
          instrumentsRequired : false,
          questionsRequired : false,
          variablesRequired : false,
          conceptsRequired : false
        }
      },
      assigneeGroup : "PUBLISHER"
    };
    $scope.group = "publishers";
    $scope.isPublisher = true;

    UserResource = {
      search: function(callback) {
        return callback([]);
      },
    };

    // return two users: one is activated, one is deactivated
    spyOn(UserResource, 'search').and.returnValue([
      {
        "login": "active",
        "firstName": "Active",
        "lastName": "User",
        "email": "active@localhost",
        "activated": true,
        "langKey": "en",
        "authorities": [
          "ROLE_DATA_PROVIDER",
          "ROLE_USER",
          "ROLE_ADMIN",
          "ROLE_PUBLISHER",
          "ROLE_RELEASE_MANAGER"
        ],
        "welcomeDialogDeactivated": false
      },{
        "login": "deactivated",
        "firstName": "Deactivated",
        "lastName": "User",
        "email": "deactivated@localhost",
        "activated": false,
        "langKey": "en",
        "authorities": [
          "ROLE_DATA_PROVIDER",
          "ROLE_USER",
          "ROLE_ADMIN",
          "ROLE_PUBLISHER",
          "ROLE_RELEASE_MANAGER"
        ],
        "welcomeDialogDeactivated": false
      }
    ]);

    directiveElement = getCompiledElement();
  }));

  it('should have applied template', function () {
    expect(directiveElement.html()).not.toEqual('');
  });

	it('should include active users only', function() {
    var controller = directiveElement.controller('userList');
    controller.searchUsers("").then(function( data ) {
      var res = data;
      expect(res.length).toEqual(1);
      expect(res).toContain({
        "login": "active",
        "firstName": "Active",
        "lastName": "User",
        "email": "active@localhost",
        "activated": true,
        "langKey": "en",
        "authorities": [
          "ROLE_DATA_PROVIDER",
          "ROLE_USER",
          "ROLE_ADMIN",
          "ROLE_PUBLISHER",
          "ROLE_RELEASE_MANAGER"
        ],
        "welcomeDialogDeactivated": false
      });
      expect(res).not.toContain({
        "login": "deactivated",
        "firstName": "Deactivated",
        "lastName": "User",
        "email": "deactivated@localhost",
        "activated": false,
        "langKey": "en",
        "authorities": [
          "ROLE_DATA_PROVIDER",
          "ROLE_USER",
          "ROLE_ADMIN",
          "ROLE_PUBLISHER",
          "ROLE_RELEASE_MANAGER"
        ],
        "welcomeDialogDeactivated": false
      })
  });

	});

  function getCompiledElement(){
    $httpBackend.expectGET(userEndpoint).respond(200, {});
    element = angular.element("<user-list project='project' is-publisher='true' group='publishers'></user-list>")
    var compiledDirective = compile(element)($scope);
    $scope.$digest();
    return compiledDirective;
  }

});
