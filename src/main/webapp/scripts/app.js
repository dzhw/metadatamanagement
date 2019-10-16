/* global bowser, event, ClientJS, document */
'use strict';

var app;
try {
  app = angular
    .module(
      'metadatamanagementApp', ['LocalStorageModule', 'tmh.dynamicLocale',
        'pascalprecht.translate', 'fdzPaginatorModule',
        'ui.bootstrap', // for modal dialogs
        'elasticsearch', 'hljs', 'ngclipboard',
        'ngResource', 'ui.router', 'ngCookies', 'ngAria',
        'ngFileUpload', 'ngMaterial',
        'blockUI', 'LocalStorageModule', 'jkAngularCarousel',
        'angularMoment', 'ngAnimate', 'vcRecaptcha',
        'ngMessages', 'katex', 'ngFileSaver', 'duScroll', 'ngShortcut',
        'jsonFormatter'
      ])

  .run(
      function($rootScope, $location, $state, LanguageService, Auth, Principal,
        ENV, VERSION, $mdMedia, $transitions, $timeout, $window,
        WebSocketService, $urlRouter, $translate, MigrationService) {
        // sometimes urlRouter does not load the state automatically on startup
        $urlRouter.sync();
        WebSocketService.connect();
        $rootScope.bowser = bowser;
        $rootScope.ENV = ENV;
        $rootScope.VERSION = VERSION;
        $rootScope.$mdMedia = $mdMedia;
        $rootScope.$state = $state;
        $rootScope.currentDate = new Date();
        //prevent default browser actions for drag and drop
        $window.addEventListener('dragover', function(e) {
          e = e || event;
          e.preventDefault();
        }, false);
        $window.addEventListener('drop', function(e) {
          e = e || event;
          e.preventDefault();
        }, false);
        if (typeof String.prototype.endsWith !== 'function') {
          String.prototype.endsWith = function(suffix) {
            return this.indexOf(suffix, this.length - suffix.length) !== -1;
          };
        }
        //init the current language
        if ($location.path().indexOf('/en/') > -1) {
          LanguageService.setCurrent('en');
        } else if ($location.path().indexOf('/de/') > -1) {
          LanguageService.setCurrent('de');
        } else {
          LanguageService.setCurrent($translate.preferredLanguage());
        }

        $transitions.onStart({}, function(trans) {
          $rootScope.toState = trans.$to();
          $rootScope.toStateParams = trans.params();
          if (Principal.isIdentityResolved()) {
            Auth.authorize();
          }
          // Update the language
          LanguageService.setCurrent($rootScope.toStateParams.lang);
          // an authenticated user can't access to login and
          // register pages
          if (Principal.isAuthenticated() &&
            $rootScope.toState.parent.name === 'account' &&
            ($rootScope.toState.name === 'login' ||
              $rootScope.toState.name === 'register')) {
            return trans.router.stateService.target('search',
            {
              lang: LanguageService.getCurrentInstantly()
            });
          }
          if ($rootScope.toState.data.authorities &&
            $rootScope.toState.data.authorities.length > 0) {
            // wait for initialization of Principal Service
            $timeout(function() {
              if ($rootScope.toState.data.authorities &&
                $rootScope.toState.data.authorities.length > 0 &&
                (!Principal.hasAnyAuthority(
                  $rootScope.toState.data.authorities) ||
                  !Principal.isAuthenticated())) {
                $rootScope.$broadcast('userNotAuthorized');
              }
            }, 1000);
          }
        });

        $transitions.onSuccess({}, function(trans) {
          $rootScope.toStateName = trans.$to().name;
          // Remember previous state unless we've been redirected to login or
          // we've just
          // reset the state memory after logout. If we're redirected to
          // login, our
          // previousState is already set in the authExpiredInterceptor. If
          // we're going
          // to login directly, we don't want to be sent to some previous
          // state anyway
          if ($rootScope.toStateName !== 'login' &&
          $rootScope.previousStateName) {
            $rootScope.previousStateName = trans.$from().name;
            $rootScope.previousStateParams = trans.$from().params;
          }
        });

        $rootScope.back = function() {
          // If previous state is 'activate' or do not exist go to 'search'
          if ($rootScope.previousStateName === 'activate' ||
            $state.get($rootScope.previousStateName) === null) {
            $state.go('search', {
              lang: LanguageService.getCurrentInstantly()
            });
          } else {
            $state.go($rootScope.previousStateName,
              $rootScope.previousStateParams);
          }
        };

        MigrationService.migrate();
      })
    .config(
      function($stateProvider, $urlRouterProvider,
        $httpProvider, $locationProvider, $translateProvider,
        tmhDynamicLocaleProvider, blockUIConfig, $mdThemingProvider,
        localStorageServiceProvider, $qProvider, $provide) {
        localStorageServiceProvider
          .setPrefix('metadatamanagementApp')
          .setStorageType('localStorage')
          .setNotify(true, true);
        // enable urls without #
        $locationProvider.html5Mode(false);
        $locationProvider.hashPrefix('!');
        $stateProvider.state('site', {
          'abstract': true,
          url: '/{lang:(?:de|en)}',
          resolve: {
            authorize: ['Auth', function(Auth) {
              return Auth.authorize();
            }]
          }
        });

        // Initialize angular-translate
        $translateProvider.registerAvailableLanguageKeys(['de','en'], {
          'en_*': 'en',
          'de_*': 'de',
          '*': 'en'
        });
        $translateProvider.determinePreferredLanguage();
        $translateProvider.fallbackLanguage('en');
        $translateProvider.useCookieStorage();
        $translateProvider.useSanitizeValueStrategy(null);
        $translateProvider
          .addInterpolation('$translateMessageFormatInterpolation');
        $translateProvider
          .useMissingTranslationHandler('translationErrorHandler');
        $translateProvider
          .addInterpolation('$translateMessageFormatInterpolation');

        tmhDynamicLocaleProvider
          .localeLocationPattern(
            '../../node_modules/angular-i18n/angular-locale_{{locale}}.js');
        tmhDynamicLocaleProvider.useStorage('$cookies');
        tmhDynamicLocaleProvider.storageKey('NG_TRANSLATE_LANG_KEY');

        $urlRouterProvider.when('', '/');
        $urlRouterProvider.when('/', '/');
        $urlRouterProvider.when('/de', '/de/search');
        $urlRouterProvider.when('/de/', '/de/search');
        $urlRouterProvider.when('/en', '/en/search');
        $urlRouterProvider.when('/en/', '/en/search');
        $urlRouterProvider.otherwise('/en/error');

        $httpProvider.interceptors.push('errorHandlerInterceptor');
        $httpProvider.interceptors.push('authInterceptor');
        blockUIConfig.templateUrl = 'scripts/common/blockui/blockUI.html.tmpl';
        blockUIConfig.autoInjectBodyBlock = false;
        blockUIConfig.blockBrowserNavigation = true;
        blockUIConfig.delay = 50;
        // Tell the blockUI service to ignore certain requests
        blockUIConfig.requestFilter = function(config) {
          // If the request contains '/api/search' ...
          if (config.url.indexOf('_search') !== -1 ||
             (config.url.indexOf('/api/data-acquisition-projects/') !== -1 &&
                config.method === 'GET' &&
                config.url.indexOf('/shadows') === -1 &&
                config.url.indexOf('/releases') === -1) ||
              config.url.indexOf('/api/users/findUserWithRole') !== -1 ||
              (config.url.indexOf('/api/concepts/') !== -1 &&
                    config.method === 'GET')) {
            return false; // ... don't block it.
          }
        };

        $mdThemingProvider.definePalette('dzhwPrimaryPalette', {
          '50': 'F2F7F8',
          '100': 'E5F0F7',
          '200': 'CCE1F0',
          '300': 'B2D2E8',
          '400': '99C3E1',
          '500': '7FB4D8',
          '600': '66A6D1',
          '700': '4C96C9',
          '800': '3388C1',
          '900': '1979BA',
          'A100': '006AB2',
          'A200': '006AB2',
          'A400': '006AB2',
          'A700': '006AB2',
          'contrastDefaultColor': 'light',
          'contrastDarkColors': ['50', '100', '200', '300']
        });

        $mdThemingProvider.definePalette('dzhwAccentPalette', {
          '50': 'FFFAF4',
          '100': 'FEF5E9',
          '200': 'FDECD3',
          '300': 'FCE2BD',
          '400': 'FBD9A7',
          '500': 'FACF91',
          '600': 'FAC57C',
          '700': 'F9BC65',
          '800': 'F8B250',
          '900': 'F7A839',
          'A100': 'F69F24',
          'A200': 'FF7F24',
          'A400': 'FF7F24',
          'A700': 'FF7F24',
          'contrastDefaultColor': 'dark'
        });

        $mdThemingProvider.definePalette('dzhwWarnPalette', {
          '50': 'FAE7E7',
          '100': 'FAE7E7',
          '200': 'F5D0D0',
          '300': 'F0B9B8',
          '400': 'EBA2A1',
          '500': 'E58A89',
          '600': 'E07372',
          '700': 'DB5B5A',
          '800': 'D64543',
          '900': 'D12D2B',
          'A100': 'C91630',
          'A200': 'C91630',
          'A400': 'C91630',
          'A700': 'C91630',
          'contrastDefaultColor': 'light',
          'contrastDarkColors': ['50', '100', '200', '300']
        });

        $mdThemingProvider.theme('default')
          .primaryPalette('dzhwPrimaryPalette', {
            'default': 'A100',
            'hue-1': '800',
            'hue-2': '600',
            'hue-3': '400'
          })
          .accentPalette('dzhwAccentPalette', {
            'default': '900',
            'hue-1': '700',
            'hue-2': '500',
            'hue-3': '300'
          })
          .warnPalette('dzhwWarnPalette', {
            'default': 'A100',
            'hue-1': '800',
            'hue-2': '600',
            'hue-3': '400'
          });

        $qProvider.errorOnUnhandledRejections(false);

        $provide.decorator('$state', function($delegate, $stateParams) {
          $delegate.forceReload = function() {
              return $delegate.go($delegate.current, $stateParams, {
                  reload: true,
                  inherit: false,
                  notify: true
                });
            };
          return $delegate;
        });
      })
      .value('duScrollDuration', 500)
      .value('duScrollEasing', function easeInCubic(t) {
          return t * t * t;
        })
      //use a fake sessionId for consistent shard routing
      .constant('clientId', new ClientJS().getFingerprint())
      .constant('ClientJS', new ClientJS());
} finally {
  if (!app) {
    document.getElementsByClassName('fdz-bootstrap-error')[0].style.display =
    'table-header-group';
  }
}
