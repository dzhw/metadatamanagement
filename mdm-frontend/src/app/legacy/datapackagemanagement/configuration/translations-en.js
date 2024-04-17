'use strict';

angular.module('metadatamanagementApp').config([
  '$translateProvider',

  function($translateProvider) {
    var translations = {
      //jscs:disable
      'data-package-management': {
        'common': {
          'approvedUsage': {
            'scientificUse': 'scientific use',
            'teachingPurposes':'teaching purposes',
            'nonCommercialUse':'non-commercial use',
            'commercialUse':'commercial use'
          }
        },
        'detail': {
          'label': {
            'studySeries': 'Study Series',
            'dataPackage': 'Data Package',
            'dataPackages': 'Data Packages',
            'institution': 'Institution',
            'institutions': 'Institution(s)',
            'projectContributors': 'Project Contributors',
            'data-curators': 'Data Curation',
            'sponsors': 'Sponsored by',
            'fundingRef': 'Funding reference',
            'fundingProgram': 'Associated funding program',
            'version': 'Version',
            'surveyDesign': 'Survey Design',
            'annotations': 'Annotations',
            'transmission-via-VerbundFdb': 'Data transmission via the VerbundFDB',
            'remarksUserService': 'Remarks for the User Service',
            'externalDataPackage': 'DZHW external data package',
            'wave': 'Waves',
            'survey-data-type': 'Survey Data Type',
            'survey-period': 'Survey Period',
            'title': 'Title',
            'dataLanguages': 'Data available in',
            'tags': 'Tags',
            'approvedUsageList': 'Allowed use cases',
            'approvedUsage': 'Special restrictions for the data use',
            'additional-links': 'Additional Links',
            'attachments': {
              'type': 'Type',
              'title': 'Title',
              'description': 'Description',
              'language': 'Document Language',
              'file': 'File',
              'authors': 'Authors',
              'citation-details': 'Additional Citation Details'
            },
            'data-set': {
              'accessWays': 'Access Ways',
              'description': 'Description',
              'description-tooltip': 'Click to show data set "{{id}}"',
              'maxNumberOfObservations': 'Observations',
              'maxNumberOfEpisodes': 'Episodes',
              'surveyed-in': 'Contains data from these surveys'
            },
            'doi': 'DOI',
            'published-at': 'published at',
            'published': 'Published at'
          },
          'attachments': {
            'table-title': 'Documents related to this Data Package',
            'attachment-deleted-toast': 'Document "{{ filename }}" has been deleted!',
            'delete-attachment-tooltip': 'Click to delete document "{{ filename }}"!',
            'edit-attachment-tooltip': 'Click to edit the metadata for document "{{ filename }}".',
            'select-attachment-tooltip': 'Click to select document "{{ filename }}" for moving it up or down.',
            'move-attachment-down-tooltip': 'Click to move the selected document down.',
            'move-attachment-up-tooltip': 'Click to move the selected document up.',
            'save-attachment-order-tooltip': 'Click to save the modified order of the documents.',
            'attachment-order-saved-toast': 'The modified order of the documents has been saved.',
            'add-attachment-tooltip': 'Click to add a new document to this data package.',
            'edit-title': 'Modify Document "{{ filename }}" of Data Package "{{ dataPackageId }}"',
            'create-title': 'Add new Document to Data Package "{{ dataPackageId }}"',
            'change-file-tooltip': 'Click to choose a file.',
            'open-choose-previous-version-tooltip': 'Click to restore a previous version of the metadata.',
            'current-version-restored-toast': 'Current version of the metadata for document "{{ filename }}" has been restored.',
            'previous-version-restored-toast': 'Previous version of the metadata for document "{{ filename }}" can be saved now.',
            'language-not-found': 'No valid language found!',
            'save-data-package-before-adding-attachment': 'The data package has to be saved to enable attaching documents.',
            'hints': {
              'filename': 'Choose a file which you want to attach to the data package.'
              }
          },
          'data-set': {
            'card-title': 'Available Data Sets'
          },
          'title': 'Data Package (Dataset): {{ title }}',
          'page-description': '{{ description }}',
          'description': 'Data Package Description',
          'basic-data-of-surveys': 'Basic Data of Surveys',
          'not-found': 'The {{id}} references to an unknown Data Package.',
          'not-found-references': 'The id {{id}} has no References to Data Packages.',
          'not-yet-released': 'Currently not released',
          'not-released-toast': 'Data Package "{{ id }}" is being worked on. Therefore it is not visible to all users at the moment!',
          'beta-release-no-doi': 'This data package has no DOI yet.',
          'publications-for-series': 'Publications related to Series "{{studySeries}}"',
          'publications-for-data-package': 'Publications related to this Data Package',
          'tooltips': {
            'surveys': {
              'one': 'Click to show the survey of this data package',
              'many': 'Click to show all surveys of this data package'
            },
            'data-sets': {
              'one': 'Click to show the data set of this data package',
              'many': 'Click to show all data sets of this data package'
            },
            'series-publications': 'Click to show all publications related to this study series',
            'publications': {
              'one': 'Click to show the publication related to this data package',
              'many': 'Click to show all publications related to this data package'
            },
            'variables': {
              'one': 'Click to show the variable of this data package',
              'many': 'Click to show all variables of this data package'
            },
            'questions': {
              'one': 'Click to show the question of this data package',
              'many': 'Click to show all questions of this data package'
            },
            'instruments': {
              'one': 'Click to show the instruments of this data package',
              'many': 'Click to show all instruments of this data package'
            },
            'data-packages': {
              'study-series': 'Click to show all data packages of the study series.'
            },
            'concepts': {
              'one': 'Click to show the concept which has been measured in this data package',
              'many': 'Click to show all concepts which have been measured in this data package'
            }
          },
          'doi-tooltip': 'Click to open the DOI in a new tab',
          'link-tooltip': 'Click to open the link in a new tab.',
          'tag-tooltip': 'Click to search for data packages with this tag',
          'generate-datapackage-overview-tooltip': 'Click to generate an overview of this data package as a PDF.',
          'overview-generation-started-toast': 'The data package overview is now being generated. You will be notified by e-mail as soon as the process is completed.',
          'order-datapackage-tooltip': 'Click to order this data package.'
        },
        'log-messages': {
          'data-package': {
            'saved': 'Data Package with RDC-ID {{ id }} was saved successfully!',
            'not-saved': 'Data Package with RDC-ID {{ id }} has not been saved!',
            'data-package-file-not-found': 'The selected directory does not contain the following file: data-package.xlsx!',
            'releases-file-not-found': 'The selected directory does not contain the following file: releases.xlsx!',
            'unable-to-delete': 'The data package could not be deleted!',
            'upload-terminated': 'Finished upload of {{ total }} Data Package and {{ attachments }} Attachments with {{ warnings }} warnings and {{ errors }} errors.',
            'cancelled': 'Data Package upload cancelled!'
          }
        },
        'error': {
          'data-package': {
            'id': {
              'not-empty': 'The RDC-ID of the Data Package must not be empty!',
              'size': 'The max length of the RDC-ID is 512 signs.',
              'pattern': 'Use only alphanumeric signs, German umlauts, ß and space, underscore and minus for the RDC-ID.',
              'not-valid-id': 'The data package id must be equal to the id scheme "stu-" + {ProjectId} + "$" .'
            },
            'title': {
              'not-null': 'The title of the data package must not be empty!',
              'i18n-string-size': 'The max length of the data package title is 2048.',
              'i18n-string-entire-not-empty': 'The title of the data package must not be empty for all languages.'
            },
            'description': {
              'not-null': 'The description of the data package must not be empty!',
              'i18n-string-size': 'The max length of the data package description is 2048.',
              'i18n-string-not-empty': 'The description of the data package must not be empty for all languages.'
            },
            'institution': {
              'not-null': 'The institution of the data package must not be empty!',
              'i18n-string-size': 'The max length of the institution is 512.',
              'i18n-string-entire-not-empty': 'The institution of the data package must not be empty for both languages.'
            },
            'sponsor': {
              'not-empty': 'The list of sponsors of a data package needs min. one element and must not be empty.',
              'i18n-string-size': 'The max length of the sponsor name of the data package is 512.',
              'i18n-string-entire-not-empty': 'The sponsor name of the data package must not be empty for both languages.'
            },
            'study-series': {
              'i18n-string-size': 'The max length of the study series is 512 signs.',
              'i18n-string-entire-not-empty-optional': 'If the study series is given in one language, it has to be set in all languages.',
              'i18n-string-must-not-contain-comma': 'The study series must not contain comma.'
            },
            'survey-design': {
              'not-null': 'The survey design of the data package must not be empty!',
              'valid-survey-design': 'The allowed values for the survey design of the data package are: Cross-Section, Panel.'
            },
            'project-contributors': {
              'not-empty': 'The list of project contributors of a data package needs min. one element and must not be empty!'
            },
            'annotations': {
              'i18n-string-size': 'The max length of the annotations is 2048 signs.'
            },
            'data-acquisition-project': {
              'id': {
                'not-empty': 'The RDC-ID of the Data Acquisition Project for the Data Package must not be empty!'
              }
            },
            'additional-links': {
              'invalid-url': 'The provided URL is not valid.',
              'url-size': 'The max length for URLs is 2000 chracters.',
              'url-not-empty': 'The URL must not be empty.',
              'display-text-size': 'The max length for display text is 512 chracters.'
            }
          },
          'data-package-attachment-metadata': {
            'publication-year': {
              'not-null': 'The year of publication must not be empty!',
              'invalid-number': 'The year must be a valid number!',
              'min': 'The earliest year is 1990!',
              'max': 'The latest year is next year!'
            },
            'location': {
              'not-empty': 'The location must not be empty!',
              'string-size': 'The max length of the location is 512 characters.'
            },
            'institution': {
              'not-empty': 'The institution must not be empty!',
              'string-size': 'The max length of the institution is 512 characters.'
            }
          }
        },
        'edit': {
          'edit-page-title': 'Edit Data Package {{dataPackageId}}',
          'create-page-title': 'Create Data Package {{dataPackageId}}',
          'success-on-save-toast': 'Data Package {{dataPackageId}} has been saved successfully.',
          'error-on-save-toast': 'An error occurred during saving of Data Package {{dataPackageId}}!',
          'data-package-has-validation-errors-toast': 'The Data Package has not been saved because there are invalid fields!',
          'previous-version-restored-toast': 'Previous version of Data Package {{ dataPackageId }} can be saved now.',
          'current-version-restored-toast': 'Current version of Data Package {{ dataPackageId }} has been restored.',
          'not-authorized-toast': 'You are not authorized to create or edit data packages!',
          'choose-unreleased-project-toast': 'Data Packages may be edited if and only if the project is currently not released!',
          'label': {
            'edit-data-package': 'Edit Data Package:',
            'create-data-package': 'Create Data Package:',
            'first-name': 'First Name',
            'middle-name': 'Middle Name',
            'last-name': 'Last Name',
            'tags': 'Tags (Keywords) for the Data Package',
            'publication-year': 'Year of Publication',
            'institution': 'Institution',
            'sponsor': 'Sponsor',
            'fundingRef': 'Funding reference',
            'fundingProgram': 'Associated funding program',
            'location': 'Location',
            'additional-links': {
              'url': 'URL',
              'display-text': 'Display Text'
            }
          },
          'open-choose-previous-version-tooltip': 'Click for restoring a previous version of this data package.',
          'save-tooltip': 'Click to save this data package.',
          'move-contributor-up-tooltip': 'Click to move the selected contributor up.',
          'move-contributor-down-tooltip': 'Click to move the selected contributor down.',
          'add-contributor-tooltip': 'Click to add a new contributor to this data package.',
          'delete-contributor-tooltip': 'Click to remove the contributor from this data package.',
          'move-curator-up-tooltip': 'Click to move the selected data curator up.',
          'move-curator-down-tooltip': 'Click to move the selected data curator down.',
          'add-curator-tooltip': 'Click to add a new data curator to this data package.',
          'delete-author-tooltip': 'Click to remove the author from this report.',
          'move-author-up-tooltip': 'Click to move the selected author up.',
          'move-author-down-tooltip': 'Click to move the selected author down.',
          'add-author-tooltip': 'Click to add a new author to this report.',
          'delete-curator-tooltip': 'Click to remove the data curator from this data package.',
          'move-institution-up-tooltip': 'Click to move the selected institution up.',
          'move-institution-down-tooltip': 'Click to move the selected institution down.',
          'add-institution-tooltip': 'Click to add another institution to this data package.',
          'delete-institution-tooltip': 'Click to remove the institution from this data package.',
          'move-sponsor-up-tooltip': 'Click to move the selected sponsor up.',
          'move-sponsor-down-tooltip': 'Click to move the selected sponsor down.',
          'add-sponsor-tooltip': 'Click to add another sponsor to this data package.',
          'delete-sponsor-tooltip': 'Click to remove the sponsor from this data package.',
          'move-link-up-tooltip': 'Click to move the selected link up.',
          'move-link-down-tooltip': 'Click to move the selected link down.',
          'add-link-tooltip': 'Click to add another link to this data package.',
          'delete-link-tooltip': 'Click to remove the link from this data package.',
          'choose-previous-version': {
            'title': 'Restore Previous Version of Data Package {{ dataPackageId }}',
            'text': 'Choose a previous version of this data package which shall be restored:',
            'cancel-tooltip': 'Click to return without choosing a previous data package version.',
            'no-versions-found': 'There are no previous versions of data package {{ dataPackageId }}.',
            'data-package-deleted': 'The data package has been deleted!'
          },
          'hints': {
            'title': {
              'de': 'Please enter the title of this data package in German.',
              'en': 'Please enter the title of this data package in English.'
            },
            'study-series': {
              'de': 'If available enter the name of the study series in German.',
              'en': 'If available enter the name of the study series in English.'
            },
            'institution': {
              'de': 'Please enter the German name of the institution which has conducted the surveys.',
              'en': 'Please enter the English name of the institution which has conducted the surveys.'
            },
            'sponsor': {
              'de': 'Enter the German name of the sponsor of this data package.',
              'en': 'Enter the English name of the sponsor of this data package.',
              'funding-ref': 'Enter the funding reference of the sponsor of this data package.',
              'funding-prg': 'Enter the associated funding program.'
            },
            'survey-design': 'Choose the survey design of this data package.',
            'annotations': {
              'de': 'Enter additional annotations for this data package in German.',
              'en': 'Enter additional annotations for this data package in English.'
            },
            'approved-usage-list': 'If available enter the allowed use cases.',
            'approved-usage': 'If available enter the special restrictions for the data use.',
            'description': {
              'de': 'Enter a description of this data package in German.',
              'en': 'Enter a description of this data package in English.'
            },
            'consent': {
              'part1': 'Please note! The texts entered in this field are published under a cc0 1.0 license via the da|ra service (',
              'link1': 'https://www.da-ra.de/',
              'part2': '). By entering and saving, you agree to the corresponding license terms. For more information on the license terms, please visit ',
              'link2': 'https://creativecommons.org/publicdomain/zero/1.0/'
            },
            'project-contributors': {
              'first-name': 'Enter the first name of this project member.',
              'middle-name': 'If available enter the middle-name of this project member.',
              'last-name': 'Enter the last name of this project member.'
            },
            'authors': {
              'first-name': 'Enter the first name of this author.',
              'middle-name': 'If available enter the middle-name of this author.',
              'last-name': 'Enter the last name of this author.'
            },
            'curators': {
              'first-name': 'Enter the first name of the person involved in data preparation.',
              'middle-name': 'If available enter the middle-name of this person.',
              'last-name': 'Enter the last name of the person involved in data preparation.'
            },
            'citation-details': {
              'publication-year': 'Enter the year in which this report has been published.',
              'institution': 'Enter the name of the institution which has published this report.',
              'location': 'Enter the location of the institution which has published this report.'
            },
            'additional-links': {
              'url': 'Please enter the URL as in the following example: https://www.dzhw.eu',
              'display-text': {
                'de': 'Optional: Specify a text in German to be used to display the link.',
                'en': 'Optional: Specify a text in English to be used to display the link.'
              }
            }
          },
          'all-data-packages-deleted-toast': 'The data package of Data Acquisition Project "{{id}}" has been deleted.'
        },
        'tag-editor': {
          'label': {
            'german-tags': 'German Tags',
            'english-tags': 'English Tags'
          },
          'placeholder': 'Enter a new tag',
          'error': {
            'required': 'At least one tag must be provided'
          }
        },
        'create-overview': {
          'title': 'Generate Data Package Overview',
          'version': 'Version of the overview',
          'languages': {
            'in-german': 'German',
            'in-english': 'English'
          },
          'error': {
            'version': {
              'not-empty': 'The version must not be empty.',
              'pattern': 'The version must match the pattern "major.minor.patch" (e.g. "1.0.0").',
              'size': 'The version must not contain more than 32 characters.'
            }
          },
          'hints': {
            'version': 'Specify the version number which will be used to display the DOI of the data package.',
            'languages': 'Specify at least one language in which the overview shall be generated.'
          },
          'tooltip': {
            'cancel': 'Click to cancel the generation of the overview',
            'ok': 'Click to start the generation of the overview'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  }]);
