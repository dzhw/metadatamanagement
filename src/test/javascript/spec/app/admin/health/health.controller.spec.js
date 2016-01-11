'use strict';
describe('Controllers Tests ', function () {
  beforeEach(mockApiAccountCall);
  beforeEach(mockI18nCalls);
    describe('HealthController', function () {
        var $scope, $q, $modal, createController, MonitoringService, AdminService; // local utility functions

        beforeEach(inject(function ($injector) {
            $scope = $injector.get('$rootScope').$new();
            $q = $injector.get('$q');
            $modal = $injector.get('$modal');
            MonitoringService = {
              checkHealth: function() {
              var deferred = $q.defer();
              deferred.resolve();
              deferred.reject({});
              return deferred.promise;
            }
          };

          AdminService = {
            recreateAllElasticsearchIndices: function() {
            var deferred = $q.defer();
            deferred.resolve();
            deferred.reject({});
            return deferred.promise;
          }
        };
            var locals = {
                '$scope': $scope,
                'MonitoringService': MonitoringService,
                'AdminService': AdminService,
                '$modal': $modal
            };
            createController = function() {
                $injector.get('$controller')('HealthController', locals);
            };
            spyOn(MonitoringService, 'checkHealth').and.callThrough();
            spyOn(AdminService, 'recreateAllElasticsearchIndices').and.callThrough();
            spyOn($modal, 'open').and.callThrough();
            createController();
        }));

        describe('isHealthObject and hasSubSystem', function () {
            it('should verify empty object is not a health property leaf', function () {
                expect($scope.isHealthObject({})).toBe(false);
                expect($scope.hasSubSystem({})).toBe(false);
            });

            it('should verify object with property status and no subsystems is a health property leaf', function () {
                var healthObject = {
                    'status': 'UP'
                };
                expect($scope.isHealthObject(healthObject)).toBe(true);
                expect($scope.hasSubSystem(healthObject)).toBe(false);
            });

            it('should verify that object property status and unrecognized objects is a health property leaf', function () {
                var healthObject = {
                    'status': 'UP',
                    'subsystem': {
                        'hello': 'UP'
                    }
                };
                expect($scope.isHealthObject(healthObject)).toBe(true);
                expect($scope.hasSubSystem(healthObject)).toBe(false);
            });

            it('should verify object with property status but with subsystems is NOT a health property leaf', function () {
                var healthObject = {
                    'status': 'UP',
                    'subsystem': {
                        'status': 'UP'
                    }
                };
                expect($scope.isHealthObject(healthObject)).toBe(true);
                expect($scope.hasSubSystem(healthObject)).toBe(true);
            });

        });

        describe('transformHealthData', function () {
            it('should flatten empty health data', function () {
                var data = {};
                var expected = [];
                expect($scope.transformHealthData(data)).toEqual(expected);
            });

            it('should flatten health data with no subsystems', function () {
                var data = {
                    'status': 'UP',
                    'db': {
                        'status': 'UP',
                        'database': 'H2',
                        'hello': '1'
                    },
                    'mail': {
                        'status': 'UP',
                        'error': 'mail.a.b.c'
                    }
                };
                var expected = [
                    {
                        'name': 'db',
                        'status': 'UP',
                        'details': {
                            'database': 'H2',
                            'hello': '1'
                        }
                    },
                    {
                        'name': 'mail',
                        'status': 'UP',
                        'error': 'mail.a.b.c'
                    }
                ];
                expect($scope.transformHealthData(data)).toEqual(expected);
            });

            it('should flatten health data with subsystems at level 1, main system has no additional information', function () {
                var data = {
                    'status': 'UP',
                    'db': {
                        'status': 'UP',
                        'database': 'H2',
                        'hello': '1'
                    },
                    'mail': {
                        'status': 'UP',
                        'error': 'mail.a.b.c'
                    },
                    'system': {
                        'status': 'DOWN',
                        'subsystem1': {
                            'status': 'UP',
                            'property1': 'system.subsystem1.property1'
                        },
                        'subsystem2': {
                            'status': 'DOWN',
                            'error': 'system.subsystem1.error',
                            'property2': 'system.subsystem2.property2'
                        }
                    }
                };
                var expected = [
                    {
                        'name': 'db',
                        'status': 'UP',
                        'details': {
                            'database': 'H2',
                            'hello': '1'
                        }
                    },
                    {
                        'name': 'mail',
                        'status': 'UP',
                        'error': 'mail.a.b.c'
                    },
                    {
                        'name': 'system.subsystem1',
                        'status': 'UP',
                        'details': {
                            'property1': 'system.subsystem1.property1'
                        }
                    },
                    {
                        'name': 'system.subsystem2',
                        'status': 'DOWN',
                        'error': 'system.subsystem1.error',
                        'details': {
                            'property2': 'system.subsystem2.property2'
                        }
                    }
                ];
                expect($scope.transformHealthData(data)).toEqual(expected);
            });

            it('should flatten health data with subsystems at level 1, main system has additional information', function () {
                var data = {
                    'status': 'UP',
                    'db': {
                        'status': 'UP',
                        'database': 'H2',
                        'hello': '1'
                    },
                    'mail': {
                        'status': 'UP',
                        'error': 'mail.a.b.c'
                    },
                    'system': {
                        'status': 'DOWN',
                        'property1': 'system.property1',
                        'subsystem1': {
                            'status': 'UP',
                            'property1': 'system.subsystem1.property1'
                        },
                        'subsystem2': {
                            'status': 'DOWN',
                            'error': 'system.subsystem1.error',
                            'property2': 'system.subsystem2.property2'
                        }
                    }
                };
                var expected = [
                    {
                        'name': 'db',
                        'status': 'UP',
                        'details': {
                            'database': 'H2',
                            'hello': '1'
                        }
                    },
                    {
                        'name': 'mail',
                        'status': 'UP',
                        'error': 'mail.a.b.c'
                    },
                    {
                        'name': 'system',
                        'status': 'DOWN',
                        'details': {
                            'property1': 'system.property1'
                        }
                    },
                    {
                        'name': 'system.subsystem1',
                        'status': 'UP',
                        'details': {
                            'property1': 'system.subsystem1.property1'
                        }
                    },
                    {
                        'name': 'system.subsystem2',
                        'status': 'DOWN',
                        'error': 'system.subsystem1.error',
                        'details': {
                            'property2': 'system.subsystem2.property2'
                        }
                    }
                ];
                expect($scope.transformHealthData(data)).toEqual(expected);
            });

            it('should flatten health data with subsystems at level 1, main system has additional error', function () {
                var data = {
                    'status': 'UP',
                    'db': {
                        'status': 'UP',
                        'database': 'H2',
                        'hello': '1'
                    },
                    'mail': {
                        'status': 'UP',
                        'error': 'mail.a.b.c'
                    },
                    'system': {
                        'status': 'DOWN',
                        'error': 'show me',
                        'subsystem1': {
                            'status': 'UP',
                            'property1': 'system.subsystem1.property1'
                        },
                        'subsystem2': {
                            'status': 'DOWN',
                            'error': 'system.subsystem1.error',
                            'property2': 'system.subsystem2.property2'
                        }
                    }
                };
                var expected = [
                    {
                        'name': 'db',
                        'status': 'UP',
                        'details': {
                            'database': 'H2',
                            'hello': '1'
                        }
                    },
                    {
                        'name': 'mail',
                        'status': 'UP',
                        'error': 'mail.a.b.c'
                    },
                    {
                        'name': 'system',
                        'status': 'DOWN',
                        'error': 'show me'
                    },
                    {
                        'name': 'system.subsystem1',
                        'status': 'UP',
                        'details': {
                            'property1': 'system.subsystem1.property1'
                        }
                    },
                    {
                        'name': 'system.subsystem2',
                        'status': 'DOWN',
                        'error': 'system.subsystem1.error',
                        'details': {
                            'property2': 'system.subsystem2.property2'
                        }
                    }
                ];
                expect($scope.transformHealthData(data)).toEqual(expected);
            });
        });

        describe('getModuleName(path, name)', function () {
            it('should show both path and name if defined', function () {
                expect($scope.getModuleName('path', 'name')).toEqual('path' + $scope.separator + 'name');
            });

            it('should show only path if name is not defined', function () {
                expect($scope.getModuleName('path')).toEqual('path');
                expect($scope.getModuleName('path', '')).toEqual('path');
                expect($scope.getModuleName('path', null)).toEqual('path');
            });

            it('should show only name if path is not defined', function () {
                expect($scope.getModuleName(null, 'name')).toEqual('name');
                expect($scope.getModuleName('', 'name')).toEqual('name');
            });

            it('should show empty string if neither name nor path is defined', function () {
                expect($scope.getModuleName()).toEqual('');
            });
        });
        describe('MonitoringService, AdminService and scope',function(){
          it('should call get when resolve', function() {
            MonitoringService.checkHealth.and.returnValue($q.resolve());
            $scope.$apply(createController);
            expect(MonitoringService.checkHealth).toHaveBeenCalled();
          });
          it('should call get when reject', function() {
            MonitoringService.checkHealth.and.returnValue($q.reject({}));
            $scope.$apply(createController);
            expect(MonitoringService.checkHealth).toHaveBeenCalled();
          });
          it('updatingHealth should be false', function() {
            MonitoringService.checkHealth.and.returnValue($q.resolve());
            $scope.$apply(createController);
            expect($scope.updatingHealth).toBe(false);
          });
          it('isRecreatingIndices should be false when resolve', function() {
            AdminService.recreateAllElasticsearchIndices.and.returnValue($q.resolve());
            $scope.recreateAllElasticsearchIndices();
            $scope.$apply(createController);
            expect($scope.isRecreatingIndices).toBe(false);
          });
          it('isRecreatingIndices should be false', function() {
            AdminService.recreateAllElasticsearchIndices.and.returnValue($q.reject({}));
            $scope.recreateAllElasticsearchIndices();
            $scope.$apply(createController);
            expect($scope.isRecreatingIndices).toBe(false);
          });
          it('shold set label to success', function() {
            var label =   $scope.getLabelClass('UP');
            expect(label).toBe('label-success');
          });
          it('shold set label to danger', function() {
            var label =   $scope.getLabelClass('');
            expect(label).toBe('label-danger');
          });
          it('shold set modal params', function() {
            $scope.showHealth('');
            expect($modal.open).toHaveBeenCalled();
          });
          it('shold split and return baseName', function() {
            var name = $scope.baseName('baseName.test');
            expect(name).toBe('baseName');
          });
          it('shold split and return subSystemName', function() {
            var name = $scope.subSystemName('subSystemName.name');
            expect(name).toContain('name');
          });
        });
    });
});
