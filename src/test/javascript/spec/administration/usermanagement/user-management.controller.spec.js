/* global describe */
/* global beforeEach */
/* global it */
/* global inject */
/* global expect */
/* global mockApis */
/* global spyOn */

'use strict';

describe('UserManagementController ', function() {

  beforeEach(module('metadatamanagementApp'));

  var $controller;

  beforeEach(inject(function(_$controller_){
    $controller = _$controller_;
  }));

  describe('$scope.grade', function() {
      var $scope, controller, UserResourceMock, CommonDialogsServiceMock;

      // define UserResourceMock
      beforeEach(function() {
          var user = 'TestUser';
          var header = function(par) {
              return 'part1;part11,part2;part22';
          };
          UserResourceMock = {
            query: function(par, callback) {
              callback(user, header);
              return {
                then: function(callback) {
                  return callback(['de', 'fr']);
                }
              };
            },
            update: function(user, callback) {
              /*ToDO remove try and catch*/
              try {
                callback();
              } catch (e) {}
              return {
                then: function(callback) {
                  return callback();
                }
              };
            },
            get: function(par, callback) {
              callback();
              return {
                then: function(callback) {
                  return callback();
                }
              };
            },
            save: function() {
              return {
                then: function(callback) {
                  return callback();
                }
              };
            },
            refresh: function() {
              return {
                then: function(callback) {
                  return callback();
                }
              };
            }
          };
      });

      // Define CommonDialogsServiceMock
      beforeEach(function() {
        CommonDialogsServiceMock = {
          showConfirmDialog: function(titleKey, titleParams, contentKey, contentParams, targetEvent) {
            return {
              then: function(callback) {
                return callback();
              }
            };
          }
        }
      });

      beforeEach(function() {
        $scope = {};
        controller = $controller('UserManagementController', { $scope: $scope,
          UserResource: UserResourceMock,
          CommonDialogsService: CommonDialogsServiceMock });
      });

      it('should show confirm dialog if user gets activated', function() {
        spyOn(CommonDialogsServiceMock, 'showConfirmDialog').and.callThrough();
        $scope.setActive({}, true);
        expect(CommonDialogsServiceMock.showConfirmDialog).toHaveBeenCalled();
      });
      it('should show confirm dialog if user gets deactivated', function() {
        spyOn(CommonDialogsServiceMock, 'showConfirmDialog').and.callThrough();
        $scope.setActive({}, false);
        expect(CommonDialogsServiceMock.showConfirmDialog).toHaveBeenCalled();
      });
      it('should call User.get', function() {
        spyOn(UserResourceMock, 'get').and.callThrough();
        $scope.showUpdate('login');
        expect(UserResourceMock.get).toHaveBeenCalled();
      });
      it('should set page to 10', function() {
        $scope.loadPage(10);
        expect($scope.page).toEqual(10);
      });
    });
  });
