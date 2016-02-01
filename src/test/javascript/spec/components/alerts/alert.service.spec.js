'use strict';

describe('Factory Tests ', function () {
  var AlertService, $q, $scope, $timeout;
    describe('AlertService', function() {
    beforeEach(mockApiAccountCall);
    beforeEach(mockI18nCalls);
        beforeEach(inject(function($injector) {
            AlertService = $injector.get('AlertService');
            $timeout =   $injector.get('$timeout');
            $q = $injector.get('$q');
            $scope = $injector.get('$rootScope').$new();
        }));
        it(' should remove all alerts', function() {
          AlertService.clear();
          expect(AlertService.get()).toEqual([]);
        });
        it(' should set tape of message to warning', function() {
          AlertService.warning();
          expect(AlertService.get()[0].type).toEqual('warning');
        });
        it(' should set tape of message to danger', function() {
          AlertService.error();
          expect(AlertService.get()[0].type).toEqual('danger');
        });
        it(' should set tape of message to info', function() {
          AlertService.info();
          expect(AlertService.get()[0].type).toEqual('info');
        });
        it(' should set tape of message to success', function() {
          AlertService.success();
          expect(AlertService.get()[0].type).toEqual('success');
        });
        it(' should remove alert', function() {
          AlertService.success('success',{});
          $timeout.flush();
          AlertService.closeAlert('1');
          expect(AlertService.get()).toEqual([]);
        });
        xit(' should make a GET request', function() {
          $timeout.flush();
          var temp = AlertService.factory({
            'alertId': '1',
            'type' : 'success',
            'msg': 'alert',
            'timeout': 5000,
            'params': {}
          });
          alert(temp);
        });
      });
    });
