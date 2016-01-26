describe('Unit testing passwordStrengthBar', function() {
  var $compile,
      $scope,
      element;
  beforeEach(mockApiAccountCall);
  beforeEach(mockI18nCalls);
  beforeEach(inject(function(_$compile_, _$rootScope_){
    $compile = _$compile_;
    $scope = _$rootScope_.$new();
    var html =   '<password-strength-bar password-to-check="password"></password-strength-bar>';
    element = $compile(html)($scope);
  }));

  describe('Password strength', function () {
      it("Should display the password strength bar", function() {
          $scope.$digest();
          expect(element.find('ul').length).toEqual(1);
          expect(element.find('li').length).toEqual(5);
      });
      it("Should not change the points of the strength bar", function() {
          $scope.password = "less";
          $scope.$digest();
          var firstpointStyle = element.find('ul').children('li')[0].getAttribute('style');
          expect(firstpointStyle).toContain('background-color: rgb(255, 0, 0)');
      });
      it("Should change the first 2 points of the strength bar", function() {
          $scope.password = "morethan5chars";
          $scope.$digest();

          var firstpointStyle = element.find('ul').children('li')[0].getAttribute('style');
          expect(firstpointStyle).toContain('background-color: rgb(255, 153, 0)');

          var secondpointStyle = element.find('ul').children('li')[1].getAttribute('style');
          expect(secondpointStyle).toContain('background-color: rgb(255, 153, 0)');

          var thirdpointStyle = element.find('ul').children('li')[2].getAttribute('style');
          expect(thirdpointStyle).toContain('background-color: rgb(221, 221, 221)');
      });
      it("Should change the first 4 points of the strength bar", function() {
          $scope.password = "morethan20chars_1234";
          $scope.$digest();

          var firstpointStyle = element.find('ul').children('li')[0].getAttribute('style');
          expect(firstpointStyle).toContain('background-color: rgb(153, 255, 0)');

          var secondpointStyle = element.find('ul').children('li')[1].getAttribute('style');
          expect(secondpointStyle).toContain('background-color: rgb(153, 255, 0)');

          var thirdpointStyle = element.find('ul').children('li')[2].getAttribute('style');
          expect(thirdpointStyle).toContain('background-color: rgb(153, 255, 0)');

          var fourthpointStyle = element.find('ul').children('li')[3].getAttribute('style');
          expect(fourthpointStyle).toContain('background-color: rgb(153, 255, 0)');
      });
      it("Should change the first five points of the strength bar", function() {
          $scope.password = "morethan20chars_1234morethanAzm";
          $scope.$digest();

          var fifthpointStyle = element.find('ul').children('li')[4].getAttribute('style');
          expect(fifthpointStyle).toContain('background-color: rgb(0, 255, 0)');
      });
      it("Should change all points of the strength bar", function() {
          $scope.password = "morethan20chars_1234Amorethan20chars_1234A";
          $scope.$digest();

          var fifthpointStyle = element.find('ul').children('li')[4].getAttribute('style');
          expect(fifthpointStyle).toContain('background-color: rgb(0, 255, 0)');
      });
  });
});
