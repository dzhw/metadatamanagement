/* Author: Daniel Katzberg */
'use strict';

/* The Controller for the search. It differs between tabs and a tab represent
a result of a type like variable or dataSet and so on. */
angular.module('metadatamanagementApp').controller('SearchController',
    function($scope, Principal, ElasticSearchProperties, $location,
        AlertService, SearchDao, $translate, CustomModalService,
        VariableUploadService, CurrentProjectService, $mdDialog,
        CleanJSObjectService, SearchToastService) {

        //Check the login status
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
          });

        $scope.totalHits = 0;
        $scope.currentPageNumber = 1;
        $scope.searchResult = {};

        //Need for interpretation of the query element in the url.
        $scope.query = $location.search().query;

        //The current index of the active tab
        $scope.selectedTabIndex = 0;

        //Information for the different tabs
        $scope.tabs = [{
            title: 'global.menu.search.all',
            elasticSearchType: '',
            count: null
          }, {
            title: 'global.menu.search.variables',
            icon: 'assets/images/icons/variable.svg',
            elasticSearchType: 'variables',
            count: null
          }, {
            title: 'global.menu.search.questions',
            icon: 'assets/images/icons/question.svg',
            elasticSearchType: 'atomic_questions',
            count: null
          }, {
            title: 'global.menu.search.surveys',
            icon: 'assets/images/icons/survey.svg',
            elasticSearchType: 'surveys',
            count: null
          }, {
            title: 'global.menu.search.data-sets',
            icon: 'assets/images/icons/data-set.svg',
            elasticSearchType: 'data_sets',
            count: null
          }];

        //Search function
        $scope.search = function() {
            //Iterate the tabs. The search results have to be
            //different between the tabs
            var selectedTab = $scope.tabs[$scope.selectedTabIndex];

            $scope.tabs.forEach(function(tab) {
                tab.count = null;
              });

            //Search with different types, binded on every tab
            $location.search('query', $scope.query);
            $location.search('page', $scope.currentPageNumber);
            SearchDao.search($scope.query, $scope.currentPageNumber,
                    selectedTab.elasticSearchType)
                .then(function(data) {
                    $scope.searchResult = data.hits.hits;
                    $scope.totalHits = data.hits.total;
                    selectedTab.count = data.hits.total;

                    //Count information by aggregations
                    if (selectedTab.elasticSearchType === '') {
                      $scope.tabs.forEach(function(tab) {
                          if (tab.elasticSearchType !== '') {
                            tab.count = 0;
                          }
                          data.aggregations.countByType.buckets.forEach(
                            function(bucket) {
                              if (bucket.key === tab.elasticSearchType) {
                                // jscs:disable
                                  tab.count = bucket.doc_count;
                                  // jscs:enable
                              }
                            });
                        });
                    }

                    //If something going wrong: send an alert
                  }, function(error) {
                    AlertService.error(error.message);
                    console.trace(error);
                  });
          };

        $scope.onTabSelected = function() {
            $scope.currentPageNumber = 1;
            $scope.search();
          };

        $scope.uploadVariables = function(file) {
            var dataAcquisitionProject = CurrentProjectService
              .getCurrentProject();
            if (file !== null &&
                !CleanJSObjectService.isNullOrEmpty(dataAcquisitionProject)) {
              // Appending dialog to document.body to cover sidenav in docs app
              var confirm = $mdDialog.confirm()
                  .title()
                  .textContent($translate.instant(
                      'search.deleteMessages.deleteVariables', {
                          id: dataAcquisitionProject.id
                        }))
                  .ariaLabel($translate.instant(
                      'search.deleteMessages.deleteVariables', {
                          id: dataAcquisitionProject.id
                        }))
                  .ok($translate.instant(
                      'search.buttons.ok', {
                          id: dataAcquisitionProject.id
                        }))
                  .cancel($translate.instant(
                      'search.buttons.cancel', {
                          id: dataAcquisitionProject.id
                        }));
              $mdDialog.show(confirm).then(function() {
                  //start upload and open log toast
                  VariableUploadService
                      .uploadVariables(file, dataAcquisitionProject.id);

                  //Cancel. Nothing happens
                }, function() {});
            } else {
              SearchToastService.openNoProjectToast();
            }
          };

        //Refresh function for the refresh button
        $scope.refresh = function() {
            $scope.search();
          };
      });
