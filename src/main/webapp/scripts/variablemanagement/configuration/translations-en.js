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
            'unable-to-read-file': 'The file {{file}} in {{dataSet}} could not be opened!',
            'json-parse-error': 'The file {{file}} in {{dataSet}} could not be parsed!',
            'missing-id': 'Variable {{ index }} does not contain a RDC-ID and has not been saved:',
            'upload-terminated': 'Finished upload of {{ total }} Variables with {{warnings}} warnings and {{ errors }} errors.',
            'unable-to-delete': 'The variables could not be deleted!',
            'cancelled': 'Variables upload cancelled!',
            'missing-json-file': 'There is no JSON file for Variable {{name}} in Data Set {{dataSet}}!',
            'missing-excel-file': 'There is no Excel file in Data Set {{dataSet}}!',
            'missing-name': 'The {{variableIndex}}. Variable in Datensatz {{dataSet}} doesn\'t have a Name!',
            'generation-details-rule-success-copy-to-clipboard': 'The Generation Rule was successfully copied into the clipboard.',
            'filter-details-success-copy-to-clipboard': 'The Filter Expression was successfully copied into the clipboard.',
            'duplicate-name': 'The name ({{ name }}) of Variable {{ index }} of Data Set {{ dataSetNumber }} has already been used.',
            'distribution': {
              'histogram': {
                'incomplete-histogram-information': 'The Variable {{ variableName }} of Data Set {{ dataSetName }} has the scalelevel "ratio" but there is not enough information for displaying a histogram.'
              }
            }
          }
        },
        'detail': {
          'title': '{{ label }} ({{ variableId }})',
          'variable': 'Variable',
          'variables': 'Variables',
          'variable-informations': 'Informations',
          'variables-in-panel': 'Panel Variables',
          'derived-variables': 'Derived Variables',
          'related-objects': 'Related Objects',
          'frequencies': 'Frequencies',
          'generation-details': 'Generation Details',
          'name': 'Name',
          'data-type': 'Data Type',
          'scale-level': 'Level of Measurement',
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
          'copy-complete-input-filter-tooltip': 'Click to copy the input filter to the clipboard',
          'previous-variable-in-data-set':'Previous Variable in Data Set',
          'no-previous-variable':'No previous Variable in Data Set',
          'next-variable-in-data-set':'Next Variable in Data Set',
          'no-next-variable':'No next Variable in Data Set.',
          'show-complete-input-filter-tooltip': {
            'true': 'Click to show the whole input filter',
            'false': 'Click to minimize the content area of the input filter'
          },
          'copy-complete-rule-tooltip': 'Click to copy the generation rule to the clipboard',
          'show-complete-rule-tooltip': {
            'true': 'Click to show the whole generation rule',
            'false': 'Click to minimize the content area of the generation rule'
          },
          'label': 'Label',
          'not-found': 'The {{id}} references to an unknown Variable.',
          'not-found-references': 'The id {{id}} has no References to Variables.',
          'same-in-panel': 'Same Variables',
          'show-complete-distribution-tooltip': {
            'true': 'Show complete distribution',
            'false': 'Minimize table of distribution'
          },
          'statistics': {
            'graphics': 'Figure Frequencies/Distribution (valid responses)',
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
            'deviance': 'Deviance',
            'mean-deviation': 'Mean absolute Deviation'
          },
          'no-related-variables': 'No related Variables.',
          'related-variables': 'Related Variables',
          'central-tendency': 'Central Tendency',
          'dispersion': 'Dispersion',
          'distribution': 'Distribution',
          'not-released-toast': 'Variable "{{ id }}" has not yet been released to all users!',
          'tooltips': {
            'surveys': {
              'one': 'Click to show the survey from which this variable resulted',
              'many': 'Click to show all surveys from which this variable resulted'
            },
            'data-sets': {
              'one': 'Click to show the data set containing this variable'
            },
            'publications': {
              'one': 'Click to show the publication related to this variable',
              'many': 'Click to show all publications related to this variable'
            },
            'questions': {
              'one': 'Click to show the question from which this variable resulted',
              'many': 'Click to show all questions from which this variable resulted'
            },
            'variables': {
              'same-in-panel': 'Click to show all panel variables related to this variable',
              'derived-variables': 'Click to show all derived variables related to this variable',
            },
            'studies': {
              'one': 'Click to show the study from which this variable resulted'
            }
          }
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
              'size': 'The max length of the filter expression is 2048 signs.'
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
              'size': 'The max length of the value of a valid response is 256 signs.',
              'not-null': 'The value of a valid response must not be empty!'
            },
            'valid-relative-frequency': {
              'not-null': 'The valid relative frequency of a valid response must not be empty!'
            }
          },
          'statistics': {
            'minimum': {
              'size': 'The max length of the minimum is 32 signs.'
            },
            'maximum': {
              'size': 'The max length of the maximum is 32 signs.'
            },
            'median': {
              'size': 'The max length of the median is 32 signs.'
            },
            'first-quartile': {
              'size': 'The max length of the first quartile is 32 signs.'
            },
            'third-quartile': {
              'size': 'The max length of the third quartile is 32 signs.'
            }
          },
          'variable': {
            'valid-variable-name': 'The RDC-ID of the Variable is not valid for the Pattern: "var-" + {RDCID}+ "-ds" + {DataSetNumber} + "-" + {VariableName} + "$".',
            'unique-variable-name-in-data-set': 'The Name of the Variable is already used within this Data Set.',
            'data-set-number-not-null': 'The Number of the Data Set of the Variable must not be empty!',
            'data-set-id-not-empty': 'The RDC-ID of the Data Set of the Variable must not be empty!',
            'data-set-index-not-null': 'The Index of the Data Set of the Variable must not be empty!',
            'survey-numbers-not-empty': 'The List of Survey Numbers of a variable needs min. one element and must not be empty!',
            'related-question-number-size': 'The max Length of Question Number is 32 signs.',
            'related-question-number-not-empty': 'A Related Question doesn\'t have a Number!',
            'related-question-instrument-number-not-empty': 'An Instrument doesn\'t have a Number!',
            'valid-panel-identifier': 'The Panel Identifier of the Variable is not valid for the Pattern: DataAcquisitionProjectId + "-" + "ds" + "dataSetNumber" + "-" + string.',
            'valid-derived-variables-identifier': 'The Identifier of the derived Variable is not valid for the Pattern: DataAcquisitionProjectId + "-" + "ds" + "dataSetNumber" + "-" + string.',
            'panel-identifier-size': 'The max length of Panel Identifier of the Variable is 128 signs.',
            'panel-identifier-pattern': 'Use only alphanumeric signs, german umlauts, ß and minus for the Panel Identifier of the Variable.',
            'derived-variables-identifier-size': 'The max length of derived Variables Identifier of the Variable is 128 signs.',
            'derived-variables-identifier-pattern': 'Use only alphanumeric signs, german umlauts, ß and minus for the derived Variables Identifier of the Variable.',
            'only-ordinal-scale-level-for-date-data-type': 'The scale level of a date variable must be ordinal.',
            'valid-response-value-must-be-a-number-on-numeric-data-type': 'If the variable has a numeric data type, the values of valid responses have to be numeric!',
            'statistics-minimum-must-be-a-number-on-numeric-data-type': 'If the variable has a numeric data type, the minimum of statistics have to be numeric!',
            'statistics-maximum-must-be-a-number-on-numeric-data-type': 'If the variable has a numeric data type, the maximum of statistics have to be numeric!',
            'statistics-median-must-be-a-number-on-numeric-data-type': 'If the variable has a numeric data type, the median of statistics have to be numeric!',
            'statistics-first-quartile-must-be-a-number-on-numeric-data-type': 'If the variable has a numeric data type, the first quartile of statistics have to be numeric!',
            'statistics-third-quartile-must-be-a-number-on-numeric-data-type': 'If the variable has a numeric data type, the third quartile of statistics have to be numeric!',
            'valid-response-value-must-be-an-iso-date-on-date-data-type': 'If the variable has a date data type, the values of valid responses have to be matchable by the pattern yyyy-MM-dd.',
            'statistics-minimum-must-be-an-iso-date-on-date-data-type': 'If the variable has a date data type, the minimum of statistics have to be matchable by the pattern yyyy-MM-dd.',
            'statistics-maximum-must-be-an-iso-date-on-date-data-type': 'If the variable has a date data type, the maximum of statistics have to be matchable by the pattern yyyy-MM-dd.',
            'statistics-median-must-be-an-iso-date-on-date-data-type': 'If the variable has a date data type, the median of statistics have to be matchable by the pattern yyyy-MM-dd.',
            'statistics-first-quartile-must-be-an-iso-date-on-date-data-type': 'If the variable has a date data type, the first quartile of statistics have to be matchable by the pattern yyyy-MM-dd.',
            'statistics-third-quartile-must-be-an-iso-date-on-date-data-type': 'If the variable has a date data type, the third quartile of statistics have to be matchable by the pattern yyyy-MM-dd.',
            'id': {
              'not-empty': 'The RDC-ID of the Variable must not be empty!',
              'size': 'The max length of the RDC-ID is 128 signs.',
              'pattern': 'Use only alphanumeric signs, german umlauts, ß and space, underscore, exclamation sign and minus for the RDC-ID.'
            },
            'data-type': {
              'not-null': 'The data type of the Variable must not be empty!',
              'valid-data-type': 'The value for data type is not valid. Allowed values in english: string, numerisch, datum. Allowed values in english: string, numeric, date.'
            },
            'scaleLevel': {
              'valid-scale-level': 'The values for Scale Level are not valid. Allowed values in german: ordinal, nominal, intervall, verhältnis. Allowed values in english: ordinal, nominal, interval, ratio.',
              'not-null': 'The scale level of a variable must not be empty!'
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
            'variable-has-invalid-data-set-id': 'The Variable {{id}} references to an unknown Data Set ({{toBereferenzedId}}).',
            'variable-has-invalid-question-id': 'The Variable {{id}} references to an unknown Question ({{toBereferenzedId}}).',
            'variable-survey-ids-are-not-consistent-with-data-set': 'The Variable {{id}} references different surveys than its Data Set {{toBereferenzedId}}.',
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });
