'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'user-management': {
        'home': {
          'title': 'User Management',
          'createLabel': 'Create a new user',
          'createOrEditLabel': 'Create or edit a user'
        },
        'delete': {
          'question': 'Are you sure you want to delete user {{ login }}?'
        },
        'detail': {
          'title': 'User'
        },
        'first-name': 'First name',
        'last-name': 'Last name',
        'email': 'Email',
        'activated': 'Activated',
        'deactivated': 'Deactivated',
        'profiles': 'Profiles',
        'langKey': 'Language',
        'createdBy': 'Created by',
        'createdDate': 'Created date',
        'lastModifiedBy': 'Modified by',
        'lastModifiedDate': 'Modified date',
        'error': {
          'authority': {
            'name': {
              'not-null': 'The Name of Authority must not be empty!'
            }
          },
          'user': {
            'login': {
              'not-null': 'The Login must not be empty!'
            },
            'password': {
              'not-null': 'The Password must not be empty!'
            },
            'must-not-be-deactivated': 'The user must not be deactivated, because he is assigned to the following projects: {{ projectIds }}',
            'must-not-loose-publisher-role': 'The user must not loose the role "Publisher", because he is assigned to the following projects as publisher: {{ projectIds }}',
            'must-not-loose-data-provider-role': 'The user must not loose the role "Data Provider", because he is assigned to the following projects as data provider: {{ projectIds }}'
          }
        },
        'login': {
          'login': 'Login',
          'title': 'Sign in for Data Providers',
          'form': {
            'password': 'Password',
            'password-placeholder': 'Your password',
            'rememberme': 'Remember me',
            'button': 'Sign in'
          },
          'messages': {
            'error': {
              'authentication': '<strong>Failed to sign in!</strong> Please check your credentials and try again.'
            }
          },
          'password': {
            'forgot': 'Did you forget your password?'
          },
          'registration': {
            'register': 'Register a new account'
          }
        },
        'settings': {
          'title': 'Account Details for <b>{{username}}</b>',
          'form': {
            'firstname': 'First Name',
            'firstname.placeholder': 'Your first name',
            'lastname': 'Last Name',
            'lastname.placeholder': 'Your last name',
            'language': 'Language',
            'button': 'Save'
          },
          'messages': {
            'error': {
              'fail': '<strong>An error has occurred!</strong> Account Details could not be saved.',
              'emailexists': '<strong>E-mail is already in use!</strong> Please choose another one.'
            },
            'success': '<strong>Account Details saved!</strong>',
            'validate': {
              'firstname': {
                'minlength': 'Your first name is required to be at least 1 character',
                'maxlength': 'Your first name cannot be longer than 50 characters'
              },
              'lastname': {
                'minlength': 'Your last name is required to be at least 1 character',
                'maxlength': 'Your last name cannot be longer than 50 characters'
              }
            }
          }
        },
        'user-messages': {
          'create-title': 'Create Message to all Users (currently online)',
          'new-message-title': 'New Message from {{sender}}',
          'message-de-label': 'Notification (in German)',
          'message-en-label': 'Notification (in English)',
          'dialog-tooltip': {
            'close': 'Click to cancel sending a message',
            'send': 'Click to send the message to all users (currently online)',
            'open-create-dialog': 'Click to create a message to all users (currently online)'
          },
          'buttons': {
            'send': 'Send'
          }
        },
        'welcome-dialog': {
          'title': 'Welcome',
          'text-body': '<p>Dear {{username}},</p><p>we are pleased that you have decided to make your research data available to other researchers via our RDC. This system collects and publishes all information related to your research data (so-called metadata).</p><p>In the navigation menu on the left side you will find a list of all data acquisition projects to which you have been assigned as a data provider.</p><img src="/assets/images/welcome-dialog-project-chooser-en.png" class="fdz-welcome-dialog-image"/><p style="margin:10px 0px 0px 0px;">Simply select the project for which you want to provide metadata and click on the orange button "Project-Cockpit" <img src="/assets/images/welcome-dialog-project-cockpit-button.png"/> below the selected project.</p><p>The project can be assigned either to the publishers (RDC employees) or to you as a data provider. As soon as the project is <img src="/assets/images/welcome-dialog-assigned-en.png" alt="Assigned to Data Providers"/>, you will be notified and you can start to enter Metadata.</p><p>A detailed user documentation can be found here: <a href="https://metadatamanagement.readthedocs.io/de/stable/" target="_blank">Documentation</a>.</p>',
          'do-not-show-again': 'Don\'t show this dialog again'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
