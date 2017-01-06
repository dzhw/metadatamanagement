'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'variable-management': {
        'log-messages': {
          'variable': {
            'saved': 'Variable with RDC-ID {{ id }} was saved successfully!',
            'not-saved': 'Variable with RDC-ID {{ id }} has not been saved!',
            'missing-id': 'Variable {{ index }} does not contain a RDC-ID and has not been saved:',
            'upload-terminated': 'Finished upload of {{ total }} Variables with {{ errors }} errors.',
            'unable-to-delete': 'The variables could not be deleted!',
            'cancelled': 'Variables upload cancelled!',
            'missing-json-file': 'There is no JSON file for Variable {{ id }}!',
            'generation-details-rule-success-copy-to-clipboard': 'The Generation Rule was successfully copied into the clipboard.',
            'filter-details-success-copy-to-clipboard': 'The Filter Expression was successfully copied into the clipboard.'
          }
        },
        'detail': {
          'title': '{{ label }} ({{ variableId }})',
          'variable': 'Variable',
          'variables': 'Variables',
          'variable-informations': 'Informations',
          'same-variables-in-panel': 'Panel Variables',
          'related-objects': 'Related Objects',
          'frequencies': 'Frequencies',
          'generation-details': 'Generation Details',
          'name': 'Name',
          'data-type': 'Datatype',
          'scale-level': 'Scalelevel',
          'question-text': 'Question text',
          'access-ways': 'Access Ways',
          'annotations': 'Annotations',
          'filter-description': 'Filter Description',
          'filter-details-language': 'Filtering Language',
          'filter-details-expression': 'Filter Expression',
          'input-filter': 'Input Filter',
          'generation-details-description': 'Generation Details Description',
          'generation-details-expression-language': 'Generation Expression Language',
          'generation-details-rule': 'Generation Rule',
          'show-complete-rule': {
            'true': 'Show complete Generation Rule',
            'false': 'Minimize Generation Rule '
          },
          'show-complete-filter-details': {
            'true': 'Show complete Input Filter',
            'false': 'Minimize Input Filter'
          },
          'button': {
            'aria-label': {
              'copy-to-clipboard': 'Copy to Clipboard'
            }
          },
          'label': 'Label',
          'not-found': 'The {{id}} references to an unknown Variable.',
          'not-found-references': 'The id {{id}} has no References to Variables.',
          'same-in-panel': 'Same Variables',
          'show-complete-distribution': {
            'true': 'Show complete distribution',
            'false': 'Minimize table of distribution'
          },
          'statistics': {
            'graphics': 'Figure Frequencies/Distribution',
            'graphic-is-loading': 'is loading...',
            'graphic-is-not-available': 'No plotting available',
            'statistics': 'Descriptive Metrics',
            'all': 'All',
            'page': 'Page',
            'rowsPerPage': 'Rows per Page',
            'of': 'of',
            'value': 'Value',
            'label': 'Label',
            'frequency': 'Frequency',
            'valid-percent': 'Percent (valid)',
            'percent': 'Percent',
            'firstQuartile': 'First Quartile',
            'highWhisker': 'High Whisker',
            'lowWhisker': 'Low Whisker',
            'maximum': 'Maximum',
            'median': 'Median',
            'minimum': 'Minimum',
            'thirdQuartile': 'Third Quartile',
            'validResponses': 'Number of different Observations',
            'total-absolute-frequency': 'Number of different Observations',
            'totalValidAbsoluteFrequency': 'Total Valid Absolute Frequency',
            'totalValidRelativeFrequency': 'Total Valid Relative Frequency',
            'mean-value': 'Mean Value',
            'skewness': 'Skewness',
            'kurtosis': 'Kurtosis',
            'standardDeviation': 'Standard Deviation',
            'mode': 'Mode',
            'deviance': 'Deviation',
            'mean-deviation': 'Mean absolute Deviation'
          },
          'no-related-variables': 'No related Variables.',
          'related-variables': 'Related Variables',
          'central-tendency': 'Central Tendency',
          'dispersion': 'Dispersion',
          'distribution': 'Distribution'
        },
        'labels': {
          'part-of-data-set': 'Part of Data Set:',
          'surveyed-in': 'Surveyed in:'
        },
        'error': {
          'distribution': {
            'valid-responses': {
              'unique-value': 'The value has to be unique within the valid responses.'
            },
            'total-absolute-frequency': {
              'not-null': 'The total absolute frequency must not be empty!'
            },
            'total-valid-absolute-frequency': {
              'not-null': 'The total valid absolute frequency must not be empty!'
            },
            'total-valid-relative-frequency': {
              'not-null': 'The total valid relative frequency must not be empty!'
            },
            'missings': {
              'uniqueCode': 'The code has to be unique within the missings.'
            }
          },
          'filter-details': {
            'expression': {
              'not-empty': 'The filter expression must not be empty!',
              'size': 'The max length of the filter expression is 128 signs.'
            },
            'description': {
              'i18n-string-size': 'The max length of the filter description is 2048 signs.'
            },
            'expression-language': {
              'not-empty': 'The filter expression language  must not be empty!',
              'valid-filter-expression-language': 'The filter expression language is not valid. Valid is: SpEL and Stata.'
            }
          },
          'generation-details': {
            'not-empty-generation-details-description-or-rule': 'The rule or the description of a generation rule must not be empty! Both can be set, too.',
            'rule-expression-language-and-rule-filled-or-empty': 'Both, the rule and the rule expression language, have to be filled or empty.',
            'description': {
              'i18n-string-size': 'The max length of the generation rule description of the variable is 2048 signs.'
            },
            'rule': {
              'size': 'The max length of the rule is 1048576 signs.'
            },
            'rule-expression-language': {
              'valid-rule-expression-language': 'The rule expression language is not valid. Valid is: R and Stata.'
            }
          },
          'missing': {
            'code': {
              'not-null': 'The code of a missing must be not empty!'
            },
            'label': {
              'i18n-string-size': 'The max length of the label of a missing is 128 signs.'
            },
            'absolute-frequency': {
              'not-null': 'The absolute frequency of a missing must not be empty!'
            },
            'relative-frequency': {
              'not-null': 'The relative frequency of a missing must not be empty!'
            }
          },
          'valid-response': {
            'label': {
              'i18n-string-size': 'The max length of the label of a valid response is 2048 signs.'
            },
            'absolute-frequency': {
              'not-null': 'The absolute frequency of a valid response must not be empty!'
            },
            'relative-frequency': {
              'not-null': 'The relative frequency of a valid response must not be empty!'
            },
            'value': {
              'size': 'The max length of the value of a valid response is 32 signs.',
              'not-null': 'The value of a valid response must not be empty!'
            },
            'valid-relative-frequency': {
              'not-null': 'The valid relative frequency of a valid response must not be empty!'
            }
          },
          'variable': {
            'valid-variable-name': 'The RDC-ID of the Variable is not valid for the Pattern: RDCID-VariableName.',
            'unique-variable-name-in-project': 'The name of the Variable is already used within this Project.',
            'mandatory-scale-level-for-numeric-data-type': 'The Scale Level of a numeric Variable must not be empty or is must be empty on a string data type!',
            'valid-response-value-must-be-a-number-on-numeric-data-type': 'If the variable has a numeric data type, the values of valid responses have to be numeric!',
            'valid-response-value-must-be-an-iso-date-on-date-data-type': 'If the variable has a date data type, the values of valid responses have to be matchable by the ISO Standard 8601.',
            'id': {
              'not-empty': 'The RDC-ID of the Variable must not be empty!',
              'size': 'The max length of the RDC-ID is 128 signs.',
              'pattern': 'Use only alphanumeric signs, german umlauts, ß and space, underscore and minus for the RDC-ID.'
            },
            'data-type': {
              'not-null': 'The data type of the Variable must not be empty!',
              'valid-data-type': 'The value for data type is not valid. Allowed values in english: string, numerisch. Allowed values in english: string, numeric.'
            },
            'scaleLevel': {
              'valid-scale-level': 'The values for Scale Level are not valid. Allowed values in german: ordinal, nominal, kontinuierlich. Allowed values in english: ordinal, nominal, continous.'
            },
            'name': {
              'not-empty': 'The Name of the Variable must not be empty!',
              'size': 'The max length of the name is 32 signs.',
              'pattern': 'Use only alphanumeric signs and underscore for the name.'
            },
            'label': {
              'not-null': 'The label of the Variable must not be empty!',
              'i18n-string-size': 'The max length of the label is 128 signs.',
              'i18n-string-not-empty': 'At least an english or a german label must be given!'
            },
            'annotations': {
              'i18n-string-size': 'The max length of the annotations is 2048 signs.'
            },
            'access-ways': {
              'not-empty': 'The list of access ways of a variable needs min. one element and must not be empty!',
              'valid-access-ways': 'The values of access ways are invalid. Valid values are: download-cuf, download-suf, remote-desktop-suf, onsite-suf, not-accessible.'
            },
            'survey': {
              'ids': {
                'not-empty': 'The Variable must be assigned to at least one Survey!'
              }
            },
            'related-question-strings': {
              'i18n-string-size': 'The max length of the related question strings is 2048 signs.'
            },
            'data-acquisition-project': {
              'id': {
                'not-empty': 'The RDC-ID of the Data Acquisition Project for the Variable must not be empty!'
              }
            }
          },
          'post-validation': {
            'variable-has-invalid-survey-id': 'The Variable {{id}} references to an unknown Survey ({{toBereferenzedId}}).',
            'variable-id-is-not-in-invalid-variables-panel': 'The Variable {{id}} references to an unknown Panel Variable ({{toBereferenzedId}}).',
            'variable-id-is-not-valid-in-related-variables': 'The Variable {{id}} references to an unknown related Variable ({{toBereferenzedId}}).',
            'variable-has-invalid-data-set-id': 'The Variable {{id}} references to an unknown Data Set ({{toBereferenzedId}}).'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
