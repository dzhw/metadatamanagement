'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'global': {
        'title': 'metadatamanagement',
        'browsehappy': 'You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/?locale=en">upgrade your browser</a> to improve your experience.',
        'rdc-alt-text': 'Research Data Center, German Center for Research on Higher Education and Science Studies',
        'dzhw-alt-text': 'The German Centre for Research on Higher Education and Science Studies',
        'bmbf-alt-text': 'Sponsored by the FMER',
        'search': 'Search',
        'toolbar': {
          'buttons': {
            'fdz-staff-area-tooltip': 'Click to open submenu "Access for RDC Employees"',
            'logout': 'Sign out {{username}}',
            'logout-tooltip': 'Click to sign out "{{username}}"',
            'login': 'Sign in',
            'login-tooltip': 'Click to sign in',
            'change-language': 'Click to view the english version',
            'register': 'Sign up',
            'register-tooltip': 'Click to register',
            'open-menu': 'Open Navigation Menu',
            'open-menu-tooltip': 'Click to open Navigation Menu',
            'disclosure-tooltip': 'Click to open the disclosure',
            'administration-tooltip': 'Click to open submenu "Administration"',
            'user-management-tooltip': 'Click to open user management',
            'metrics-tooltip': 'Click to open metrics',
            'health-tooltip': 'Click to open health',
            'configuration-tooltip': 'Click to open configuration',
            'logs-tooltip': 'Click to open logs',
            'settings-tooltip': 'Click to open settings',
            'password-tooltip': 'Click to open password'
          }
        },
        'dialog': {
            'tooltip': {
              'close': 'Click to close the dialog'
            }
          },
          'toast': {
            'tooltip': {
              'close': 'Click to close the message'
            }
          },
        'toolbarHeader': {
            'search': 'Click to navigate to the last {{type}}',
            'default': 'Click to navigate to {{type}} "{{param}}"'
          },
        'cards': {
          'details': 'Details',
          'related-objects': 'Related objects'
        },
        'buttons': {
          'close': 'Close',
          'ok': 'OK',
          'cancel': 'Cancel'
        },
        'tooltips': {
          'create-project': 'Click to create a new Data Acquisition Project.',
          'delete-project': 'Click to delete the selected Data Acquisition Project with all related data.',
          'release-project': 'Click to release the chosen project for all users.',
          'unrelease-project': 'Click to unrelease the chosen project.',
          'post-validation': 'Click to validate a chosen Data Acquisition Project.',
          'surveys': {
            'no': 'No related survey',
            'one': 'Click to show survey',
            'many': 'Click to show all related surveys'
          },
          'data-sets': {
            'no': 'No related data set',
            'one': 'Click to show data set',
            'many': 'Click to show all related data sets',
            'same-data-sets': 'Click to show same data sets'
          },
          'publications': {
            'no': 'No related publication',
            'one': 'Click to show publication',
            'many': 'Click to show all related publications'
          },
          'questions': {
            'no': 'No related question',
            'one': 'Click to show question',
            'many': 'Click to show all related questions'
          },
          'instruments': {
            'no': 'No related instrument',
            'one': 'Click to show instrument',
            'many': 'Click to show all related instruments'
          },
          'variables': {
            'no': 'No related variables',
            'one': 'Click to show variable',
            'many': 'Click to show all related variables',
            'same-in-panel': 'Click to show same variables in panel'
          },
          'studies':{
            'no': 'no related studies',
            'one': 'Click to show study',
            'many': 'Click to show all related studies'
          },
          'files': {
            'download': 'Click to download file "{{filename}}"'
          },
          'images': 'Click to open image in new Tab',
          'pager': {
            'previous':'Click to load previous search results',
            'next': 'Click to load next search results',
            'current': 'Click to load search page "{{number}}"'
          },
          'toolbarHeader': {
            'search': 'Click to load last search results',
            'data-set': 'Click to show data set "{{param}}"',
            'survey': 'Click to show survey "{{param}}"',
            'surveys': 'Click to show surveys',
            'question': 'Click to show question "{{param}}"',
            'variable': 'Click to show variable "{{param}}"',
            'study': 'Click to show study "{{param}}"',
            'instrument': 'Click to show instrument "{{param}}"',
            'publication': 'Click to show publication "{{param}}"'
          }
        },
        'menu': {
          'entities': {
            'main': 'Entities',
            'rdcProject': 'Data Acquisition Projects',
            'current-project': 'Current Data Acquisition Project:',
            'select-project': 'Select Project',
            'unknown-project': 'The Data Acquisition Project {{projectId}} is unknown.'
          },
          'search': {
            'title': 'Metadata Search'
          },
          'account': {
            'main': 'Access for RDC Employees',
            'settings': 'Settings',
            'password': 'Password',
            'sessions': 'Sessions'
          },
          'admin': {
            'main': 'Administration',
            'user-management': 'User management',
            'metrics': 'Metrics',
            'health': 'Health',
            'configuration': 'Configuration',
            'logs': 'Logs',
            'apidocs': 'API',
            'database': 'Database'
          },
          'skip-navigation': 'Skip to Content',
          'skip-navigation-tooltip': 'Click to skip elements from navigation bar',
          'back-to-search': 'Click for navigating to search page',
          'language': 'Language',
          'disclosure': 'Disclosure',
          'notepad': 'Notepad'
        },
        'form': {
          'username': 'Username',
          'username-placeholder': 'Your username',
          'newpassword': 'New password',
          'newpassword-placeholder': 'New password',
          'confirmpassword': 'New password confirmation',
          'confirmpassword-placeholder': 'Confirm the new password',
          'email': 'E-mail',
          'email-placeholder': 'Your e-mail'
        },
        'messages': {
          'info': {
            'register': 'You don\'t have an account yet? '
          },
          'error': {
            'dontmatch': 'The password and its confirmation do not match!',
            'unavailable': 'Not available!'
          },
          'validate': {
            'newpassword': {
              'required': 'Your password is required.',
              'minlength': 'Your password is required to be at least 5 characters.',
              'maxlength': 'Your password cannot be longer than 50 characters.',
              'strength': 'Password strength:'
            },
            'confirmpassword': {
              'required': 'Your confirmation password is required.',
              'minlength': 'Your confirmation password is required to be at least 5 characters.',
              'maxlength': 'Your confirmation password cannot be longer than 50 characters.'
            },
            'email': {
              'required': 'Your e-mail is required.',
              'invalid': 'Your e-mail is invalid.',
              'minlength': 'Your e-mail is required to be at least 5 characters.',
              'maxlength': 'Your e-mail cannot be longer than 50 characters.'
            }
          }
        },
        'log-messages': {
          'intro': 'Following errors occurred during the last upload:',
          'internal-server-error': 'An internal server error occurred.',
          'unsupported-excel-file': 'Excel File could not be read',
          'unsupported-tex-file': 'Tex File could not be read',
          'unable-to-parse-json-file': 'The JSON file "{{file}}" does not contain valid JSON!',
          'unable-to-read-file': 'The file "{{file}}" could not be read!',
          'unable-to-read-excel-sheet': 'Unable to read Excel sheet "{{sheet}}"!',
          'unable-to-read-excel-sheets': 'Unable to read at least one of the excel sheets "{{sheets}}"!',
          'post-validation-terminated': 'The Post-Validation terminated with {{errors}} Errors.'
        },
        'field': {
          'rdc-id': 'RDC-ID',
          'id': 'ID'
        },
        'entity': {
          'action': {
            'addblob': 'Add blob',
            'addimage': 'Add image',
            'back': 'Back',
            'cancel': 'Cancel',
            'delete': 'Delete',
            'edit': 'Edit',
            'save': 'Save',
            'view': 'View',
            'ok': 'OK'
          },
          'detail': {
            'field': 'Field',
            'value': 'Value'
          },
          'delete': {
            'title': 'Confirm delete operation'
          },
          'validation': {
            'required': 'This field is required.',
            'minlength': 'This field is required to be at least {{min}} characters.',
            'maxlength': 'This field cannot be longer than {{max}} characters.',
            'min': 'This field should be more than {{min}}.',
            'max': 'This field cannot be more than {{max}}.',
            'minbytes': 'This field should be more than {{min}} bytes.',
            'maxbytes': 'This field cannot be more than {{max}} bytes.',
            'pattern': 'This field should follow pattern {{pattern}}.',
            'number': 'This field should be a number.',
            'datetimelocal': 'This field should be a date and time.',
            'rdc-id': 'The RDC ID must contain only letters, digits and underscore.',
            'variable': {
              'name': 'The name of a variable must contain only letters, digits and underscore.'
            },
            'survey': {
              'period': 'The begin of the field period must be before the end.'
            },
            'data-acquisition-project': {
              'id': 'The name of the project must contain only letters and digits.',
              'release': {
                'version': 'The version must contain only digits and dots.'
              }
            }
          }
        },
        'error': {
          'title': 'Error page!',
          '403': 'You are not authorized to access the page.',
          'server-not-reachable': 'Server not reachable',
          'not-null': 'Field {{fieldName}} cannot be empty!',
          'entity': {
            'exists': 'There is already a {{params[0]}} with RDC-ID "{{params[1]}}"!',
            'compoundexists': 'There is already a "{{params[0]}}" with the field combination "{{params[1]}}"!',
            'notfound': '{{params[0]}} with RDC-ID "{{params[1]}}" not found!'
          },
          'period': {
            'valid-period': 'The start and end date have to be set and the dates have to be in the correct order!'
          },
          'import': {
            'json-parsing-error': 'The import of the object "{{entity}}" failed, because the field "{{property}}" has an invalid value: {{invalidValue}}',
            'no-json-mapping': 'A server side error happened during the import of an object.',
            'file-size-limit-exceeded': 'The file "{{ entity }}" exceeds the limit of 10MB!'
          },
          'server-error': {
            'internal-server-error': 'Sorry, something went wrong :-( ({{ status }}).'
          },
          'client-error': {
            'unauthorized-error': 'You are not authorized to do this (Status {{ status }}).',
            'forbidden-error': 'You are not authorized to do this (Status {{ status }}).',
            'not-found-error': 'Sorry, something went wrong :-( ({{ status }}).'
          }
        },
        'logos': {
          'rdc': 'Research Data Center, German Center for Research on Higher Education and Science Studies',
          'bmbf-tooltip': 'Click to open the website of the federal ministry of education and research',
          'bmbf-alt-text': 'Logo of the federal ministry of education and research',
          'dzhw-tooltip': 'Click to open the website of the german center for research on higher education and science studies',
          'dzhw-alt-text': 'Logo of german center for research on higher education and science studies'
        },
        'main': {
          'title': 'Welcome to the RDC of the DZHW. You are looking for ...'
        },
        'pagination': {
          'next': 'Next',
          'previous': 'Previous'
        },
        'joblogging': {
          'protocol-dialog': {
            'title': 'Protocol',
            'success': 'Success',
            'warning': 'Warnings',
            'error': 'Error'
          },
          'job-complete-toast': {
            'title': 'Protocol',
            'show-log': 'Click to open the protocol.'
          },
          'block-ui-message': '{{warnings}} Warnings and {{ errors }} Errors on {{ total }} Objects'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
