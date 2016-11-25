/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('StudySearchDialogController',
  function($mdDialog, $scope, blockUI, paramObject, StudySearchService) {
    var ctrl = this;
    ctrl.paramObject = paramObject;
    var blockArea = blockUI.instances.get('blockRelatedStudyContainer');
    ctrl.infiniteItems = {
      numLoaded_: 0,
      toLoad_: 0,
      pageToLoad: 0,
      from: 0,
      size: 5,
      items: [],
      getItemAtIndex: function(index) {
                if (index > this.numLoaded_) {
                  this.fetchMoreItems_(index);
                  return null;
                }
                return this.items[index];
              },
      getLength: function() {
        if (this.numLoaded_ >= ctrl.count) {
          return this.items.length;
        }
        return this.items.length + this.size;
      },
      fetchMoreItems_: function(index) {
                if (this.toLoad_ < index) {
                  this.toLoad_ += this.size;
                  if (_.isArray(ctrl.paramObject.methodParams)) {
                    ctrl.count = ctrl.paramObject.methodParams.length;
                    var searchTerms = _.chunk(ctrl.paramObject
                      .methodParams, this.size);
                    blockArea.start();
                    StudySearchService[ctrl.paramObject.methodName]
                    (searchTerms[this.pageToLoad])
                    .then(angular.bind(this, function(studies) {
                          _.pullAllBy(studies.docs, [{'found': false}],
                          'found');
                          this.items = _.concat(this.items, studies.docs);
                          this.numLoaded_ = this.items.length;
                          this.pageToLoad += 1;
                        })).finally(function() {
                          blockArea.stop();
                        });
                  } else {
                    blockArea.start();
                    StudySearchService[ctrl.paramObject.methodName](
                      ctrl.paramObject.methodParams, this.from, this.size,
                      ctrl.paramObject.studyId)
                      .then(angular.bind(this, function(studies) {
                        ctrl.count = studies.hits.total;
                        this.items = _.concat(this.items, studies.hits.hits);
                        this.numLoaded_ = this.items.length;
                        this.from += this.size;
                      })).finally(function() {
                        blockArea.stop();
                      });
                  }
                }
              }
    };
    ctrl.closeDialog = $mdDialog.hide;
    $scope.$on('$stateChangeStart', function() {
      ctrl.closeDialog();
    });
  });
