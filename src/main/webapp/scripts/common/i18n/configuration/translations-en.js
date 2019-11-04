'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'global': {
        'title': 'Metadatamanagement (MDM)',
        'browsehappy': 'You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/?locale=en">upgrade your browser</a> to improve your experience.',
        'rdc-alt-text': 'Logo of the Research Data Center of the German Center for Research on Higher Education and Science Studies',
        'dzhw-alt-text': 'The German Centre for Research on Higher Education and Science Studies',
        'bmbf-alt-text': 'Sponsored by the FMER',
        'search': 'Data Search',
        'in-german': 'in German',
        'in': 'in',
        'in-english': 'in English',
        'toolbar': {
          'buttons': {
            'fdz-staff-area-tooltip': {
              'false': 'Click to open menu "Access for Data Providers"',
              'true': 'Click to close menu "Access for Data Providers"'
            },
            'logout': 'Sign out {{username}}',
            'logout-tooltip': 'Click to sign out "{{username}}"',
            'login': 'Sign in',
            'login-tooltip': 'Click to sign in',
            'change-language': 'Klicken, um die Seiten auf Deutsch anzuzeigen',
            'register': 'Sign up',
            'register-tooltip': 'Click to register',
            'open-menu-tooltip': 'Click to open the menu',
            'disclosure-tooltip': 'Click to show the disclosure',
            'dataprotection-tooltip': 'Click to show the data protection statement',
            'administration-tooltip': {
              'false': 'Click to open menu "Administration"',
              'true': 'Click to close menu "Administration"'
            },
            'user-management-tooltip': 'Click to open the User Management',
            'health-tooltip': 'Click to check the availability of the external services',
            'configuration-tooltip': 'Click to show the Spring Configuration',
            'logs-tooltip': 'Click to change loglevel',
            'settings-tooltip': 'Click to change your account details',
            'password-tooltip': 'Click to change your password'
          },
          'released': 'Released',
          'not-released': 'Not released'
        },
        'dialog': {
          'tooltip': {
            'close': 'Click to close the dialog',
            'save': 'Click to save the protocol'
          }
        },
        'toast': {
          'tooltip': {
            'close': 'Click to close the notification'
          }
        },
        'cards': {
          'metadata': 'Metadata for the File',
          'file': 'File',
          'details': 'Details',
          'related-objects': 'Related objects'
        },
        'buttons': {
          'close': 'Close',
          'ok': 'OK',
          'save': 'Save',
          'cancel': 'Cancel',
          'closeDialogTemporarily': 'Not yet!'
        },
        'tooltips': {
          'create-project': 'Click to create a new Data Acquisition Project.',
          'delete-project': 'Click to delete the selected Data Acquisition Project with all related data.',
          'release-project': 'Click to release the selected project for all users.',
          'unrelease-project': 'Click to unrelease the selected project.',
          'cockpit-project': 'Click to go to the Project Cockpit.',
          'post-validation': 'Click to validate the selected Data Acquisition Project.',
          'files': {
            'download': 'Click to download file "{{filename}}"'
          },
          'images': 'Click to open image in a new Tab',
          'pager': {
            'previous': 'Click to show previous search results',
            'next': 'Click to show next search results',
            'current': 'Click to show search results on page {{number}}'
          },
          'toolbarHeader': {
            'search': 'Click to show last search results',
            'data-set': 'Click to show data set {{param}}',
            'survey': 'Click to show survey {{param}}',
            'surveys': 'Click to show the surveys',
            'question': 'Click to show question {{param}}',
            'variable': 'Click to show variable {{param}}',
            'study': 'Click to show study {{param}}',
            'instrument': 'Click to show instrument {{param}}',
            'publication': 'Click to show publication {{param}}',
            'concept': 'Click to show concept {{param}}'
          },
          'feedback-dialog': {
            'github': 'Click to give feedback or report a Bug by Github',
            'email': 'Click to give feedback or report a Bug by an E-Mail.'
          },
          'navbar-feedback': 'Click to give feedback or report a Bug',
          'navbar-documentation': 'Click to show the user documentation',
          'navbar-usage-info': 'Click to show usage information for data providers',
          'navbar-project-overview': 'Click to show the project overview'
        },
        'feedback-dialog': {
          'toolbar-head': 'Give Feedback or report Bugs',
          'content-body-feedback': 'You can submit bugs and request enhancements ...',
          'content-body-via': '... via',
          'content-body-thanks': 'Thank you for taking the time to send us feedback!',
          'mail-subject': 'Metadata Management System Feedback'
        },
        'navbar-feedback': {
          'title': 'Give Feedback',
          'source': 'Source',
          'category': 'Category'
        },
        'menu': {
          'show-english-pages': 'Show              Pages in English',
          'show-german-pages': 'Seiten             auf Deutsch anzeigen',
          'entities': {
            'main': 'Entities',
            'rdcProject': 'Data Acquisition Projects',
            'current-project': 'Current Data Acquisition Project',
            'select-project': 'Select Project',
            'unknown-project': 'No project found!'
          },
          'search': {
            'title': 'Metadatamanagement (MDM) - Data Search for Higher Education Research and Science Studies'
          },
          'account': {
            'main': 'Account Details ({{username}})',
            'settings': 'Edit Account',
            'password': 'Change Password',
            'sessions': 'Sessions'
          },
          'admin': {
            'main': 'Administration',
            'user-management': 'User Management',
            'health': 'External Services',
            'configuration': 'Spring Configuration',
            'logs': 'Loglevel',
            'apidocs': 'API',
            'database': 'Database'
          },
          'skip-navigation': 'Skip to Content',
          'skip-navigation-tooltip': 'Click to skip the menu',
          'back-to-search': 'Click to show the search page',
          'language': 'Language',
          'data-access': 'Data Access',
          'disclosure': 'Disclosure',
          'dataprotection': 'Data Protection',
          'notepad': 'Notepad',
          'documentation': 'Documentation',
          'usage-info': 'Information for Data Providers',
          'project-overview': 'Project Overview'
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
            'unavailable': 'Not available!',
            'undocumented': 'Undocumented.'
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
            'rdc-id': 'The RDC ID must contain only letters, digits and underscore.'
          }
        },
        'error': {
          'title': 'Error page!',
          '403': 'You are not authorized to access the page.',
          'server-not-reachable': 'Server not reachable',
          'not-null': 'Field {{fieldName}} cannot be empty!',
          'browser-not-supported': 'This action isn\'t supported in the currently used browser',
          'entity': {
            'exists': 'There is already a {{params[0]}} with RDC-ID "{{params[1]}}"!',
            'compoundexists': 'There is already a "{{params[0]}}" with the field combination "{{params[1]}}"!',
            'notfound': '{{params[0]}} with RDC-ID "{{params[1]}}" not found!'
          },
          'period': {
            'valid-period': 'The start and end date have to be set and the dates have to be in the correct order!'
          },
          'import': {
            'json-not-readable': 'The Json has an error in the field "{{invalidValue}}".',
            'json-parsing-error': 'The import of the object "{{entity}}" from an json file failed, because the field "{{property}}" has an invalid value: {{invalidValue}}',
            'excel-parsing-error': 'The import of the object "{{entity}}" from an excel file from line {{index}} failed, because the field "{{property}}" has an invalid value: {{invalidValue}}',
            'no-json-mapping': 'A server side error happened during the import of an object.',
            'file-size-limit-exceeded': 'The file "{{ entity }}" exceeds the limit of 15MB!',
            'file-already-exists': 'Saving failed because there is already a file with the name {{ filename }}!'
          },
          'server-error': {
            'internal-server-error': 'Sorry, something went wrong :( ({{ status }}).',
            'freemarker': {
              'parsing-error': 'There is a parsing problem by Freemarker in file "{{entity}}" (Line, Column): {{invalidValue}}',
              'invalid-reference-error': 'There is a invalid reference in the Freemarker script in file "{{entity}}" (Line, Column): {{invalidValue}}'
            }
          },
          'client-error': {
            'unauthorized-error': 'You are not logged in. Therefore you are not authorized to do this (Status {{ status }}).',
            'forbidden-error': 'You do not have the permission (role) to do this (Status {{ status }}).',
            'not-found-error': 'The requested page has not been found ({{ status }}).',
            'not-in-assignee-group': 'Editing is currently impossible, because the publisher or data provider group is assigned to the project.'
          },
          'person': {
            'first-name': {
              'not-empty': 'The first name of a person has to be given.'
            },
            'last-name': {
              'not-empty': 'The last name of a person has to be given.'
            }
          },
          'resolution': {
            'width-x': {
              'not-null': 'The width of an image (maximum value at the x-axis) has to be given.'
            },
            'height-y': {
              'not-null': 'The height of an image (maximum value at the y-axis) has to be given.'
            }
          }
        },
        'logos': {
          'rdc': 'Research Data Center, German Center for Research on Higher Education and Science Studies',
          'bmbf-tooltip': 'Click to open the website of the federal ministry of education and research',
          'bmbf-alt-text': 'Logo of the federal ministry of education and research',
          'dzhw-tooltip': 'Click to open the website of the german center for research on higher education and science studies',
          'dzhw-alt-text': 'Logo of the german center for research on higher education and science studies'
        },
        'main': {
          'title': 'Welcome to the RDC of the DZHW. You are looking for ...'
        },
        'pagination': {
          'next': 'Next',
          'previous': 'Previous',
          'of': 'of',
          'sort': 'Sort by',
          'items': 'Items per page'
        },
        'sort':  {
          'relevance': 'Relevance'
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
          'type': {
            'error': 'Error',
            'warning': 'Warning',
            'info': 'Information'
          },
          'protocol': {
            'created-by': 'Created by the MDM, at'
          },
          'block-ui-message': '{{warnings}} Warnings and {{ errors }} Errors on {{ total }} Objects'
        },
        'common-dialogs': {
          'yes': 'Yes',
          'no': 'No',
          'confirm-dirty': {
            'title': 'Discard Changes?',
            'content': 'There are unsaved changes. Do you want to discard these changes?'
          },
          'confirm-file-delete': {
            'title': 'Delete File "{{ filename }}"?',
            'content': 'Do you really want to delete File "{{ filename }}"?'
          },
          'confirm-filename-change': {
            'title': 'Change filename?',
            'content': 'Do you really want to change the filename from "{{ oldFilename }}" to "{{ newFilename }}"?\n\nIf you do so, you will loose the history of the metadata of the document!'
          },
          'confirm-delete-survey': {
            'title': 'Delete Survey "{{ id }}"?',
            'content': 'Do you really want to delete Survey "{{ id }}"?'
          },
          'confirm-delete-concept': {
            'title': 'Delete Concept "{{ id }}"?',
            'content': 'Do you really want to delete Concept "{{ id }}"?'
          },
          'confirm-delete-instrument': {
            'title': 'Delete Instrument "{{ id }}"?',
            'content': 'Do you really want to delete Instrument "{{ id }}"?'
          },
          'confirm-delete-data-set': {
            'title': 'Delete Data Set "{{ id }}"?',
            'content': 'Do you really want to delete Data Set "{{ id }}"?'
          },
          'confirm-delete-all-questions': {
            'title': 'Delete all questions of project "{{ id }}"?',
            'content': 'Do you really want to delete all questions of Data Acquisition Project "{{ id }}"?'
          },
          'confirm-delete-all-studies': {
            'title': 'Delete the study of project "{{ id }}"?',
            'content': 'Do you really want to delete the study of Data Acquisition Project "{{ id }}"?'
          },
          'confirm-delete-all-variables': {
            'title': 'Delete all variables of project "{{ id }}"?',
            'content': 'Do you really want to delete all variables of Data Acquisition Project "{{ id }}"?'
          },
          'confirm-delete-all-instruments': {
            'title': 'Delete all instruments of project "{{ id }}"?',
            'content': 'Do you really want to delete all instruments of Data Acquisition Project "{{ id }}"?'
          },
          'confirm-delete-all-surveys': {
            'title': 'Delete all surveys of project "{{ id }}"?',
            'content': 'Do you really want to delete all surveys of Data Acquisition Project "{{ id }}"?'
          },
          'confirm-delete-all-data-sets': {
            'title': 'Delete all data sets of project "{{ id }}"?',
            'content': 'Do you really want to delete all data sets of Data Acquisition Project "{{ id }}"?'
          },
          'confirm-delete-all-publications': {
            'title': 'Remove all publications from the study of project "{{ id }}"?',
            'content': 'Do you really want to remove all publications from the Study of Data Acquisition Project "{{ id }}"?'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
