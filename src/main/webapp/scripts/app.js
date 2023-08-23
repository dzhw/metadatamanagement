/* global window, bowser, event, ClientJS, document */
'use strict';

var app;
try {
  app = angular
    .module(
      'metadatamanagementApp', ['LocalStorageModule', 'tmh.dynamicLocale',
        'pascalprecht.translate',
        'ui.bootstrap', // for modal dialogs
        'elasticsearch', 'hljs', 'ngclipboard',
        'ngResource', 'ui.router', 'ngCookies', 'ngAria',
        'ngFileUpload', 'ngMaterial',
        'blockUI', 'LocalStorageModule', 'jkAngularCarousel',
        'angularMoment', 'ngAnimate', 'vcRecaptcha',
        'ngMessages', 'ngFileSaver', 'ngShortcut', 'angular-uuid',
        'jsonFormatter', 'fdzPaginatorModule', 'ngTextTruncate',
        'ng-showdown', 'swxSessionStorage', 'angulartics', 'angulartics.piwik'
      ])

  .run(
      function($rootScope, $location, $state, LanguageService, Auth, Principal,
        ENV, VERSION, $mdMedia, $transitions, $timeout, $window,
        WebSocketService, $urlRouter, $translate, MigrationService, $browser) {
        // sometimes urlRouter does not load the state automatically on startup
        $urlRouter.sync();
        WebSocketService.connect();
        $rootScope.bowser = bowser;
        // set baseUrl in case someone needs absolute urls
        if (ENV === 'local') {
          $rootScope.baseUrl = $location.protocol() + '://' + $location.host() +
            ':' + $location.port();
        } else {
          $rootScope.baseUrl = $location.protocol() + '://' + $location.host();
        }
        $rootScope.ENV = ENV;
        $rootScope.VERSION = VERSION;
        $rootScope.$mdMedia = $mdMedia;
        $rootScope.$state = $state;
        $rootScope.currentDate = new Date();
        $rootScope.searchQuery = '';
        $rootScope.sidebarContent = {
          'search': false,
          'filter': false,
          'detailSearch': true,
          'configurator': true,
          'account': false,
          'projectCockpit': false,
          'welcomeDialog': false,
          'projectOverview': false,
          'admin': false
        };
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
              $rootScope.toState.name === 'register' ||
              $rootScope.toState.name === 'start')) {
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
          $rootScope.sidebarContent = {
            'search': false,
            'filter': false,
            'detailSearch': false,
            'configurator': false,
            'account': false,
            'projectCockpit': false,
            'welcomeDialog': false,
            'projectOverview': false,
            'admin': false
          };
          // configure sidenav according to auth status and route
          // not authenticated & on detail page
          if (!Principal.isAuthenticated() &&
            (trans.$to().name).indexOf('Detail') !== -1) {
            $rootScope.sidebarContent = {
              'search': false,
              'filter': false,
              'detailSearch': true,
              'configurator': true,
              'account': false,
              'projectCockpit': false,
              'welcomeDialog': false,
              'projectOverview': false,
              'admin': false
            };
            //not authenticated & on search page
          } else if (!Principal.isAuthenticated() &&
            (trans.$to().name).indexOf('search') !== -1) {
            $rootScope.sidebarContent = {
              'search': true,
              'filter': true,
              'detailSearch': false,
              'configurator': false,
              'account': false,
              'projectCockpit': false,
              'welcomeDialog': false,
              'projectOverview': false,
              'admin': false
            };
          }

          // authenticated & on search page (provider view)
          if (Principal.isAuthenticated() && Principal.isProviderActive() &&
          (trans.$to().name).indexOf('search') !== -1) {
            $rootScope.sidebarContent = {
              'search': false,
              'filter': false,
              'detailSearch': false,
              'configurator': false,
              'account': true,
              'projectCockpit': Principal.showProjectCockpitInSidenav(),
              'welcomeDialog': Principal.displayWelcomeDialog(),
              'projectOverview': Principal.showProjectOverviewInSidenav(),
              'admin': Principal.showAdminMenuInSidenav()
            };
          //autheticated & on search page for released datasets (order view)
          } else if (Principal.isAuthenticated() &&
            !Principal.isProviderActive() &&
            (trans.$to().name).indexOf('search') !== -1) {
            $rootScope.sidebarContent = {
              'search': true,
              'filter': true,
              'detailSearch': false,
              'configurator': false,
              'account': true,
              'projectCockpit': false,
              'welcomeDialog': false,
              'projectOverview': false,
              'admin': false
            };
          // authenticated & on detail page when in order view
          } else if (Principal.isAuthenticated() &&
            ((trans.$to().name).indexOf('Detail') !== -1 ||
            (trans.$to().name).indexOf('Edit') !== -1) &&
            !Principal.isProviderActive()) {
            $rootScope.sidebarContent = {
              'search': false,
              'filter': false,
              'detailSearch': true,
              'configurator': true,
              'account': true,
              'projectCockpit': false,
              'welcomeDialog': false,
              'projectOverview': false,
              'admin': false
            };
          // authenticated & on detail page when in provider view
          } else if (Principal.isAuthenticated() &&
            ((trans.$to().name).indexOf('Detail') !== -1 ||
            (trans.$to().name).indexOf('Edit') !== -1) &&
            Principal.isProviderActive()) {
            $rootScope.sidebarContent = {
              'search': false,
              'filter': false,
              'detailSearch': false,
              'configurator': false,
              'account': true,
              'projectCockpit': Principal.showProjectCockpitInSidenav(),
              'welcomeDialog': Principal.displayWelcomeDialog(),
              'projectOverview': Principal.showProjectOverviewInSidenav(),
              'admin': Principal.showAdminMenuInSidenav()
            };
          } else if (Principal.isAuthenticated() &&
          ((trans.$to().name).indexOf('project-cockpit') !== -1 ||
          (trans.$to().name).indexOf('project-overview') !== -1 ||
          (trans.$to().name).indexOf('user-management') !== -1 ||
          (trans.$to().name).indexOf('logs') !== -1 ||
          (trans.$to().name).indexOf('health') !== -1 ||
          (trans.$to().name).indexOf('settings') !== -1 ||
          (trans.$to().name).indexOf('password') !== -1)
          ) {
            $rootScope.sidebarContent = {
              'search': false,
              'filter': false,
              'detailSearch': false,
              'configurator': false,
              'account': true,
              'projectCockpit': Principal.showProjectCockpitInSidenav(),
              'welcomeDialog': Principal.displayWelcomeDialog(),
              'projectOverview': Principal.showProjectOverviewInSidenav(),
              'admin': Principal.showAdminMenuInSidenav()
            };
          }
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
            if (Principal.isDataprovider() && !Principal.isPublisher() &&
              !Principal.isAdmin()) {
              $state.go('searchReleased');
              Principal.deactivateProviderView();
            } else {
              $state.go('search', {
                lang: LanguageService.getCurrentInstantly()
              });
              Principal.activateProviderView();
            }
          } else {
            $state.go($rootScope.previousStateName,
              $rootScope.previousStateParams);
          }
        };

        // ignore ui-routers transition superseded errors
        var standardDefaultErrorHandler = $state.defaultErrorHandler();
        $state.defaultErrorHandler(function(error) {
          // transition superseded
          if (error.type === 2) {
            return;
          }
          standardDefaultErrorHandler(error);
        });

        MigrationService.migrate();

        // let seo4ajax know that we are ready to be captured
        $timeout(function() {
          $browser.notifyWhenNoOutstandingRequests(function() {
            if (window.onCaptureReady) {
              window.onCaptureReady();
            }
          });
        }, 1000);
      })
    .config(
      function($stateProvider, $urlRouterProvider,
        $httpProvider, $locationProvider, $translateProvider,
        tmhDynamicLocaleProvider, blockUIConfig, $mdThemingProvider,
        localStorageServiceProvider, $qProvider, $provide, $showdownProvider,
        $analyticsProvider, ENV) {
        localStorageServiceProvider
          .setPrefix('metadatamanagementApp')
          .setStorageType('localStorage')
          .setNotify(true, true);
        // enable urls without #
        $locationProvider.html5Mode(true);
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
        $urlRouterProvider.when('/de', '/de/start');
        $urlRouterProvider.when('/de/', '/de/start');
        $urlRouterProvider.when('/en', '/en/start');
        $urlRouterProvider.when('/en/', '/en/start');
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
              config.url.indexOf('_msearch') !== -1 ||
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

        $mdThemingProvider.disableTheming(true);

        $qProvider.errorOnUnhandledRejections(false);

        $showdownProvider.setOption('simplifiedAutoLink', true);
        $showdownProvider.setOption('strikethrough', true);
        $showdownProvider.setOption('simpleLineBreaks', true);
        $showdownProvider.setOption('headerLevelStart', 6);
        $showdownProvider.setOption('requireSpaceBeforeHeadingText', true);

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

        if (ENV === 'local') {
          $analyticsProvider.developerMode(true);
        }
        // disable automatic page tracking
        $analyticsProvider.virtualPageviews(false);
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
