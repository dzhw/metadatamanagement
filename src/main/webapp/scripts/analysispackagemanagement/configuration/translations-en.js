'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'analysis-package-management': {
        'detail': {
          'label': {
            'analysisDataPackages': {
              'description': 'Data Package Description',
              'annotations': 'Annotations',
              'dataSource': 'Data Source',
              'dataSourceUrl': 'Link of data source'
            },
            'accessWay': 'Access way',
            'availabilityType': 'Availability Type',
            'additional-links': 'Additional Links',
            'analysisPackage': 'Analysis Package',
            'analysisPackages': 'Analysis Packages',
            'data-packages': 'Data Packages',
            'annotations': 'Annotations',
            'authors': 'Authors',
            'data-curators': 'Datenkuratierung',
            'description': 'Analysis Package Description',
            'doi': 'DOI',
            'institution': 'Institution',
            'institutions': 'Institution(s)',
            'license': 'License',
            'scripts': 'Scripts',
            'sponsors': 'Sponsored by',
            'tags': 'Tags',
            'title': 'Title',
            'version': 'Version',
            'script-attachments': {
              'file': 'Add File'
            },
            'attachments': {
              'title': 'Title',
              'description': 'Description',
              'language': 'Document Language',
              'file': 'File'
            },
            'script-version': 'Version of Software Package',
            'software-package': 'Software Package'
          },
          'script-attachments': {
            'attachment-deleted-toast': 'Document "{{ filename }}" has been deleted!',
            'add-attachment-tooltip': 'Click to add a new document to this script.',
            'edit-attachment-tooltip': 'Click to edit the metadata for script "{{ filename }}".',
            'length-attachment-tooltip': 'There must be no more than one attachment per script.',
            'table-title': 'Documents related to this script',
            'delete-attachment-tooltip': 'Click to delete this document from this script!',
            'create-title': 'Add new Document to script "{{ scriptTitle }}"',
            'file': 'Add File',
            'save-analysis-package-before-adding-attachment': 'The analysis package has to be saved to enable attaching documents.',
            'hints': {
              'filename': 'Choose a file which you want to attach to the script.'
            }
          },
          'attachments': {
            'table-title': 'Documents related to this Analysis Package',
            'delete-attachment-tooltip': 'Click to delete document "{{ filename }}"!',
            'edit-attachment-tooltip': 'Click to edit the metadata for document "{{ filename }}".',
            'select-attachment-tooltip': 'Click to select document "{{ filename }}" for moving it up or down.',
            'move-attachment-down-tooltip': 'Click to move the selected document down.',
            'move-attachment-up-tooltip': 'Click to move the selected document up.',
            'save-attachment-order-tooltip': 'Click to save the modified order of the documents.',
            'add-attachment-tooltip': 'Click to add a new document to this analysis package.',
            'create-title': 'Add new Document to Analysis Package "{{ analysisPackageId }}"',
            'edit-title': 'Modify Document "{{ filename }}" of Analysis Package "{{ analysisPackageId }}"',
            'attachment-deleted-toast': 'Document "{{ filename }}" has been deleted!',
            'attachment-order-saved-toast': 'The modified order of the documents has been saved.',
            'change-file-tooltip': 'Click to choose a file.',
            'open-choose-previous-version-tooltip': 'Click to restore a previous version of the metadata.',
            'current-version-restored-toast': 'Current version of the metadata for document "{{ filename }}" has been restored.',
            'previous-version-restored-toast': 'Previous version of the metadata for document "{{ filename }}" can be saved now.',
            'language-not-found': 'No valid language found!',
            'save-analysis-package-before-adding-attachment': 'The analysis package has to be saved to enable attaching documents.',
            'hints': {
              'filename': 'Choose a file which you want to attach to the analysis package.'
            }
          },
          'not-found': 'The {{id}} references to an unknown Analysis Package.',
          'not-found-references': 'The id {{id}} has no References to Analysis Packages.',
          'not-yet-released': 'Currently not released',
          'not-released-toast': 'Analysis Package "{{ id }}" is being worked on. Therefore it is not visible to all users at the moment!',
          'beta-release-no-doi': 'This analysis package has no DOI yet.',
          'publications-for-analysis-package': 'Publications related to this Analysis Package'
        },
        'edit': {
          'all-analysis-packages-deleted-toast': 'The analysis package of Data Acquisition Project "{{id}}" has been deleted.',
          'open-choose-previous-version-tooltip': 'Click for restoring a previous version of this analysis package.',
          'save-tooltip': 'Click to save this analysis package.',
          'add-package-author-tooltip': 'Click to add a new author to this analysis package.',
          'move-sponsor-up-tooltip': 'Click to move the selected sponsor up.',
          'move-sponsor-down-tooltip': 'Click to move the selected sponsor down.',
          'add-sponsor-tooltip': 'Click to add another sponsor to this analysis package.',
          'delete-sponsor-tooltip': 'Click to remove the sponsor from this analysis package.',
          'move-script-up-tooltip': 'Click to move the selected script up.',
          'move-script-down-tooltip': 'Click to move the selected script down.',
          'add-script-tooltip': 'Click to add another script to this analysis package.',
          'delete-script-tooltip': 'Click to remove the script from this analysis package.',
          'move-institution-up-tooltip': 'Click to move the selected institution up.',
          'move-institution-down-tooltip': 'Click to move the selected institution down.',
          'add-institution-tooltip': 'Click to add another institution to this analysis package.',
          'add-link-tooltip': 'Click to add another link to this analysis package.',
          'move-link-up-tooltip': 'Click to move the selected link up.',
          'move-link-down-tooltip': 'Click to move the selected link down.',
          'delete-link-tooltip': 'Click to remove the link from this analysis package.',
          'delete-institution-tooltip': 'Click to remove the institution from this analysis package.',
          'delete-package-author-tooltip': 'Click to remove the author from this analysis package.',
          'move-package-author-up-tooltip': 'Click to move the selected author up.',
          'move-package-author-down-tooltip': 'Click to move the selected author down.',
          'add-curator-tooltip': 'Click to add a new data curator to this analysis package.',
          'delete-curator-tooltip': 'Click to remove the data curator from this analysis package.',
          'move-curator-up-tooltip': 'Click to move the selected data curator up.',
          'move-curator-down-tooltip': 'Click to move the selected data curator down.',
          'choose-unreleased-project-toast': 'Analysis Packages may be edited if and only if the project is currently not released!',
          'not-authorized-toast': 'You are not authorized to create or edit analysis packages!',
          'error-on-save-toast': 'An error occurred during saving of Analysis Package {{analysisPackageId}}!',
          'analysis-package-has-validation-errors-toast': 'The Analysis Package has not been saved because there are invalid fields!',
          'success-on-save-toast': 'Analysis Package {{analysisPackageId}} has been saved successfully.',
          'previous-version-restored-toast': 'Previous version of Analysis Package {{ analysisPackageId }} can be saved now.',
          'current-version-restored-toast': 'Current version of Analysis Package {{ analysisPackageId }} has been restored.',
          'choose-previous-version': {
            'title': 'Restore Previous Version of Analysis Package {{ analysisPackageId }}',
            'text': 'Choose a previous version of this analysis package which shall be restored:',
            'cancel-tooltip': 'Click to return without choosing a previous analysis package version.',
            'no-versions-found': 'There are no previous versions of analysis package {{ analysisPackageId }}.',
            'data-package-deleted': 'The analysis package has been deleted!'
          },
          'label': {
            'create-analysis-package': 'Create Analysis Package:',
            'edit-analysis-package': 'Edit Analysis Package:',
            'first-name': 'First Name',
            'middle-name': 'Middle Name',
            'last-name': 'Last Name',
            'tags': 'Tags (Keywords) for the Analysis Package',
            'publication-year': 'Year of Publication',
            'institution': 'Institution',
            'sponsor': 'Sponsor',
            'script': {
              'language': 'Script Language',
              'software-package': 'Software Package',
              'version': 'Version of Software Package'
            },
            'location': 'Location',
            'additional-links': {
              'url': 'URL',
              'display-text': 'Display Text'
            }
          },
          'hints': {
            'data-package': 'Please choose a data package.',
            'additional-links': {
              'url': 'Please enter the URL as in the following example: https://www.dzhw.eu',
              'display-text': {
                'de': 'Optional: Specify a text in German to be used to display the link.',
                'en': 'Optional: Specify a text in English to be used to display the link.'
              }
            },
            'accessWay': {
              'choose': 'Please choose an access way type.'
            },
            'availabilityType': {
              'choose': 'Please choose an availability type.',
              'open': 'No or minor restrictions for access (e.g. acceptance of simple terms of use).',
              'restricted': 'Some kind of major restriction for access (e.g. registration before access is granted; application processes has to be passed before access is granted; very restrictive terms of use).',
              'none': 'Not accessible.',
              'accessible': 'Lorem ipsum...',
              'not-accessible': 'Lorem ipsum...'
            },
            'authors': {
              'first-name': 'Enter the first name of this author.',
              'middle-name': 'If available enter the middle-name of this author.',
              'last-name': 'Enter the last name of this author.'
            },
            'annotations': {
              'de': 'Enter additional annotations for this analysis package in German.',
              'en': 'Enter additional annotations for this analysis package in English.'
            },
            'curators': {
              'first-name': 'Enter the first name of the person involved in data preparation.',
              'middle-name': 'If available enter the middle-name of this person.',
              'last-name': 'Enter the last name of the person involved in data preparation.'
            },
            'description': {
              'de': 'Enter a description of this analysis package in German.',
              'en': 'Enter a description of this analysis package in English.'
            },
            'institution': {
              'de': 'Please enter the German name of the institution which participated in the analysis package.',
              'en': 'Please enter the English name of the institution which participated in the analysis package.'
            },
            'license': 'If no contract is signed we will need a license like cc-by-sa',
            'script': {
              'language': 'Please indicate the language you used for the comments in the script.',
              'software-package': 'Please choose the software package(es) the script is written for.',
              'version': 'Version of Software Package'
            },
            'sponsor': {
              'de': 'Enter the German name of the sponsor of this analysis package.',
              'en': 'Enter the English name of the sponsor of this analysis package.'
            },
            'title': {
              'de': 'Please enter the title of this analysis package in German.',
              'en': 'Please enter the title of this analysis package in English.'
            },
            'analysisDataPackages': {
              'title': {
                'de': 'Please enter the title of this data package in German.',
                'en': 'Please enter the title of this data package in English.'
              },
              'description': {
                'de': 'Enter a description of this data package in German.',
                'en': 'Enter a description of this data package in English.'
              },
              'annotations': {
                'de': 'Enter additional annotations for this data package in German.',
                'en': 'Enter additional annotations for this data package in English.'
              },
              'dataSource': {
                'de': 'The data source where the data is stored must be specified here (e.g. name of the institution/repository, private data storage).',
                'en': 'The data source where the data is stored must be specified here (e.g. name of the institution/repository, private data storage).'
              },
              'dataSourceUrl': 'Enter the data source url.'
            }
          }
        },
        'error': {
          'script-attachment-metadata': {
            'script-uuid': {
              'not-unique': 'A file with this script uuid already exists.',
              '.not-exists': 'This script uuid does not exist.'
            }
          },
          'analysis-package': {
            'annotations': {
              'i18n-string-size': 'The max length of the annotations is 2048 signs.'
            },
            'attachment': {
              'filename': {
                'not-empty': 'A file must be selected.',
                'not-unique': 'A file with this name already exists.',
                'not-valid': 'This file name is invalid',
                'pattern': 'Use only alphanumeric signs, German umlauts, ß and space, underscore and minus for the RDC-ID.'
              },
              'file-not-found': 'The File {{ filename }} was not found and has not been saved.'
            },
            'description': {
              'not-null': 'The description of the analysis package must not be empty!',
              'i18n-string-size': 'The max length of the analysis package description is 2048.',
              'i18n-string-not-empty': 'The description of the analysis package must not be empty for all languages.'
            },
            'institution': {
              'not-null': 'The institution of the analysis package must not be empty!',
              'i18n-string-size': 'The max length of the institution is 512.',
              'i18n-string-entire-not-empty': 'The institution of the analysis package must not be empty for both languages.'
            },
            'language': {
              'not-found': 'No valid language found.',
              'not-null': 'The script language must not be empty.',
              'not-valid': 'Please select one of the provided languages.'
            },
            'software-package': {
              'not-found': 'No valid software package found.',
              'not-null': 'The software package must not be empty.',
              'not-valid': 'Please select one of the provided software package.'

            },
            'software-package-version': {
              'string-size': 'The max length of the software package version is 32.',
              'string-entire-not-empty': 'The software package version must not be empty.'
            },
            'license': {
              'string-size': 'The max length of the license is 1048576 signs.'
            },
            'sponsor': {
              'not-null': 'The sponsor of the analysis package must not be empty!',
              'i18n-string-size': 'The max length of the sponsor of the analysis package is 512.',
              'i18n-string-entire-not-empty': 'The sponsor of the analysis package must not be empty for both languages.'
            },
            'title': {
              'not-null': 'The title of the analysis package must not be empty!',
              'i18n-string-size': 'The max length of the analysis package title is 2048.',
              'i18n-string-entire-not-empty': 'The title of the analysis package must not be empty for all languages.'
            },
            'additional-links': {
              'invalid-url': 'The provided URL is not valid.',
              'url-size': 'The max length for URLs is 2000 chracters.',
              'url-not-empty': 'The URL must not be empty.',
              'display-text-size': 'The max length for display text is 512 chracters.'
            },
            'analysisDataPackages': {
              'title': {
                'not-null': 'The title of the data package must not be empty!',
                'i18n-string-size': 'The max length of the data package title is 2048.',
                'i18n-string-entire-not-empty': 'The title of the data package must not be empty for all languages.'
              },
              'accessWay': {
                'i18n-not-null': 'The access way of the data package must not be empty!'
              },
              'availabilityType': {
                'i18n-not-null': 'The availability type of the data package must not be empty!'
              },
              'analysisDataPackageType': {
                'i18n-not-null': 'The type of the data package must not be empty!'
              },
              'description': {
                'i18n-string-not-empty': 'The description of the data package must not be empty!',
                'i18n-string-size': 'The max length of the data package description is 2048.',
                'i18n-string-entire-not-empty': 'The description of the data package must not be empty for all languages.'
              },
              'annotations': {
                'i18n-string-not-empty': 'The annotations of the data package must not be empty!',
                'i18n-string-size': 'The max length of the data package annotations is 2048.',
                'i18n-string-entire-not-empty': 'The annotations of the data package must not be empty for all languages.'
              },
              'dataSource': {
                'i18n-string-not-empty': 'The data source of the data package must not be empty!',
                'i18n-string-size': 'The max length of the data package data source is 512.',
                'i18n-string-entire-not-empty': 'The data source of the data package must not be empty for all languages.'
              },
              'dataSourceUrl': {
                'invalid-url': 'The provided URL is not valid.',
                'url-size': 'The max length for URLs is 2000 chracters.',
                'url-not-empty': 'The URL must not be empty.',
              }
            }
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
