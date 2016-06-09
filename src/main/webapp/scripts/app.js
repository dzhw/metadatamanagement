'use strict';

angular
  .module(
    'metadatamanagementApp', ['LocalStorageModule', 'tmh.dynamicLocale',
      'pascalprecht.translate',
      'nvd3', //for charts
      'ui.bootstrap', // for modal dialogs
      'elasticsearch',
      'ngResource', 'ui.router', 'ngCookies', 'ngAria', 'ngCacheBuster',
      'ngFileUpload', 'infinite-scroll', 'ngMaterial'
    ])

.run(
    function($rootScope, $location, $window, $http, $state, $translate,
      Language, Auth, Principal, ENV, VERSION) {
      $rootScope.ENV = ENV;
      $rootScope.VERSION = VERSION;

      //init the current language
      if ($location.path().indexOf('/en/') > -1) {
        Language.setCurrent('en');
      } else {
        Language.setCurrent('de');
      }

      $rootScope.$on('$stateChangeStart', function(event, toState,
        toStateParams) {
        $rootScope.toState = toState;
        $rootScope.toStateParams = toStateParams;
        if (Principal.isIdentityResolved()) {
          Auth.authorize();
        }
        // Update the language
        Language.setCurrent(toStateParams.lang);
      });
      $rootScope.$on('$stateChangeSuccess', function(event, toState,
        toParams, fromState, fromParams) {
        var titleKey = 'global.title';
        // Remember previous state unless we've been redirected to login or
        // we've just
        // reset the state memory after logout. If we're redirected to
        // login, our
        // previousState is already set in the authExpiredInterceptor. If
        // we're going
        // to login directly, we don't want to be sent to some previous
        // state anyway
        if (toState.name !== 'login' && $rootScope.previousStateName) {
          $rootScope.previousStateName = fromState.name;
          $rootScope.previousStateParams = fromParams;
        }

        // Set the page title key to the one configured in state or use
        // default one
        if (toState.data.pageTitle) {
          titleKey = toState.data.pageTitle;
          $rootScope.pageTitle = toState.data.pageTitle;
        }

        $translate(titleKey).then(function(title) {
          // Change window title with translated one
          $window.document.title = title;
        });
      });

      $rootScope.back = function() {
        // If previous state is 'activate' or do not exist go to 'home'
        if ($rootScope.previousStateName === 'activate' ||
          $state.get($rootScope.previousStateName) === null) {
          $state.go('home');
        } else {
          $state.go($rootScope.previousStateName,
            $rootScope.previousStateParams);
        }
      };
    })
  .config(
    function($windowProvider, $stateProvider, $urlRouterProvider,
      $httpProvider, $locationProvider, $translateProvider,
      tmhDynamicLocaleProvider, httpRequestInterceptorCacheBusterProvider) {

      // Cache everything except rest api requests
      httpRequestInterceptorCacheBusterProvider.setMatchlist([/.*api.*/,
        /.*protected.*/
      ], true);

      $stateProvider.state('site', {
        'abstract': true,
        url: '/{lang:(?:de|en)}',
        views: {
          'navbar@': {
            templateUrl: 'scripts/common/navbar/views/navbar.html.tmpl',
            controller: 'NavbarController'
          }
        },
        resolve: {
          authorize: ['Auth', function(Auth) {
            return Auth.authorize();
          }],
          translatePartialLoader: ['$translate', '$translatePartialLoader',
            function($translate, $translatePartialLoader) {
              $translatePartialLoader.addPart('global');
            }
          ]
        }
      });
      /*
       * var browserLang = $windowProvider.$get().navigator.language ||
       * $windowProvider.$get().navigator.userLanguage;
       * $urlRouterProvider.otherwise('/'+browserLang+'/');
       */
      $urlRouterProvider.when('', '/de/');
      $urlRouterProvider.otherwise('/de/error');
      $httpProvider.interceptors.push('errorHandlerInterceptor');
      $httpProvider.interceptors.push('authExpiredInterceptor');
      $httpProvider.interceptors.push('authInterceptor');
      $httpProvider.interceptors.push('notificationInterceptor');

      // Initialize angular-translate
      $translateProvider.useLoader('$translatePartialLoader', {
        urlTemplate: 'i18n/{lang}/{part}.json'
      });

      $translateProvider.useCookieStorage();
      $translateProvider.useSanitizeValueStrategy('escaped');
      $translateProvider
        .addInterpolation('$translateMessageFormatInterpolation');

      tmhDynamicLocaleProvider
        .localeLocationPattern(
          'bower_components/angular-i18n/angular-locale_{{locale}}.js');
      tmhDynamicLocaleProvider.useCookieStorage();
      tmhDynamicLocaleProvider.storageKey('NG_TRANSLATE_LANG_KEY');
    });
