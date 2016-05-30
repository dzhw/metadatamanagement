'use strict';

angular.module('metadatamanagementApp')
  .service('NavbarService', [
    '$location',
    '$rootScope',
    function($location) {

      console.log($location);

      //START PAGE
      var sections = [{
        name: 'global.menu.home',
        state: 'home',
        type: 'link',
        icon: 'glyphicon glyphicon-home',
        authentication: false,
        method: 'focusSection()'
      }];

      //ENTITIES
      sections.push({
        name: 'global.menu.entities.main',
        type: 'toggle',
        icon: 'glyphicon glyphicon-th-list',
        authentication: true,
        pages: [{
          name: 'global.menu.entities.rdcProject',
          type: 'link',
          state: 'dataAcquisitionProject',
          icon: 'glyphicon glyphicon-asterisk',
          method: 'focusSection()'
        }, {
          name: 'global.menu.entities.dataSet',
          state: 'dataSet',
          type: 'link',
          icon: 'glyphicon glyphicon-asterisk',
          method: 'focusSection()'
        }, {
          name: 'global.menu.entities.survey',
          state: 'survey',
          type: 'link',
          icon: 'glyphicon glyphicon-asterisk',
          method: 'focusSection()'
        }, {
          name: 'global.menu.entities.variable',
          state: 'variable({page: 1})',
          type: 'link',
          icon: 'glyphicon glyphicon-asterisk',
          method: 'focusSection()'
        }]
      });

      // ACCOUNT
      sections.push({
        name: 'global.menu.account.main',
        type: 'toggle',
        icon: 'glyphicon glyphicon-user',
        pages: [{
          name: 'global.menu.account.settings',
          type: 'link',
          state: 'settings',
          icon: 'glyphicon glyphicon-wrench',
          authentication: true,
          method: 'focusSection()'
        }, {
          name: 'global.menu.account.password',
          state: 'password',
          type: 'link',
          icon: 'glyphicon glyphicon-lock',
          authentication: true,
          method: 'focusSection()'
        }, {
          name: 'global.menu.account.logout',
          state: 'home',
          type: 'link',
          icon: 'glyphicon glyphicon-log-out',
          authentication: true,
          method: 'logout()'
        }, {
          name: 'global.menu.account.login',
          state: 'login',
          type: 'link',
          icon: 'glyphicon glyphicon-log-in',
          authentication: false,
          method: 'focusSection()'
        }, {
          name: 'global.menu.account.register',
          state: 'register',
          type: 'link',
          icon: 'glyphicon glyphicon-plus-sign',
          authentication: false,
          method: 'focusSection()'
        }]
      });

      // ADMINISTRATION
      sections.push({
        name: 'global.menu.admin.main',
        type: 'toggle',
        icon: 'glyphicon glyphicon-tower',
        authentication: true,
        pages: [{
          name: 'global.menu.admin.user-management',
          state: 'user-management',
          type: 'link',
          icon: 'glyphicon glyphicon-user',
          method: 'focusSection()'
        }, {
          name: 'global.menu.admin.metrics',
          state: 'metrics',
          type: 'link',
          icon: 'glyphicon glyphicon-dashboard',
          method: 'focusSection()'
        }, {
          name: 'global.menu.admin.health',
          state: 'health',
          type: 'link',
          icon: 'glyphicon glyphicon-heart',
          method: 'focusSection()'
        }, {
          name: 'global.menu.admin.configuration',
          state: 'configuration',
          type: 'link',
          icon: 'glyphicon glyphicon-list-alt',
          method: 'focusSection()'
        }, {
          name: 'global.menu.admin.logs',
          state: 'logs',
          type: 'link',
          icon: 'glyphicon glyphicon-tasks',
          method: 'focusSection()'
        }]
      });

      //DISCLOSURE
      sections.push({
        name: 'global.menu.disclosure',
        state: 'disclosure',
        type: 'link',
        icon: 'glyphicon glyphicon-blackboard',
        method: 'focusSection()'
      });

      //LANGUAGES
      sections.push({
        name: 'global.menu.language',
        state: 'home',
        type: 'toggle',
        icon: 'glyphicon glyphicon-flag',
        pages: [{
          name: 'de',
          state: 'home',
          type: 'link'
        }, {
          name: 'en',
          state: 'home',
          type: 'link'
        }]
      });

      var self;

      self = {
        sections: sections,

        toggleSelectSection: function(section) {
          self.openedSection = (self.openedSection === section ? null :
            section);
        },
        isSectionSelected: function(section) {
          return self.openedSection === section;
        },

        selectPage: function(section, page) {
          //page && page.url && $location.path(page.url);
          self.currentSection = section;
          self.currentPage = page;
        }
      };

      return self;
    }
  ]);
