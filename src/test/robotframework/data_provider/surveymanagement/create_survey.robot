*** Settings ***
Documentation     Data driven test of survey creation.
Metadata          Info on data   This test suite uses the project with the name "robotproject" which needs to be available in the testing environment
Suite Setup       Go To Survey Create Page
Suite Teardown    Close Survey Editor
Force Tags        noslowpoke
Test Template     Survey Page With Empty Or Invalid Options Should Fail
Resource          ../../resources/home_page_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/login_resource.robot

*** Variables ***
${TOAST_MSSG}  Die Erhebung wurde nicht gespeichert

*** Test Cases ***    GerTitle       EngTitle     SerialNumber    FielPeriodStart    FieldPeriodEnd    GerSurvMeth                                     GerSurvMeth                                 GerPopDesc       EngPopDesc                    NetSampleSize

Empty German Title    ${Empty}       Something    1               01.05.2018         31.05.2018        Standardisierte postalische Befragung            Standardised self-administered survey       Na eben alle    Of course everyone             1

Empty English Title
                      Irgendetwas    ${Empty}     1               01.05.2018         31.05.2018        Standardisierte postalische Befragung            Standardised self-administered survey       Na eben alle    Of course everyone             1

Empty Serial Number   Irgendetwas    Something    ${Empty}        01.05.2018         31.05.2018        Standardisierte postalische Befragung            Standardised self-administered survey       Na eben alle    Of course everyone             1

Empty German SurveyMethod
                      Irgendetwas    Something    1               01.05.2018         01.06.2018        ${Empty}                                         Standardised self-administered survey       Na eben alle    Of course everyone             1

Empty English SurveyMethod
                      Irgendetwas    Something    1               01.05.2018         01.06.2018        Standardisierte postalische Befragung            ${Empty}                                    Na eben alle    Of course everyone             1

Empty German PopDescription
                      Irgendetwas    Something    1               01.05.2018         01.06.2018        Standardisierte postalische Befragung            Standardised self-administered survey       ${Empty}        Of course everyone             1

Empty English PopDescription
                      Irgendetwas    Something    1               01.05.2018         01.06.2018        Standardisierte postalische Befragung            Standardised self-administered survey       Na eben alle    ${Empty}                       1

Empty NetSampleSize   Irgendetwas    Something    1               01.05.2018         01.06.2018        Standardisierte postalische Befragung            Standardised self-administered survey       Na eben alle    Of course everyone             ${Empty}

Invalid NetSampleSize First
                      Irgendetwas    Something    1           01.05.2018         01.06.2018        Standardisierte postalische Befragung            Standardised self-administered survey       Na eben alle    Of course everyone             -5

Invalid NetSampleSize Second
                      Irgendetwas    Something    1           01.05.2018         01.06.2018        Standardisierte postalische Befragung            Standardised self-administered survey       Na eben alle    Of course everyone             Testsamplesize



*** Keywords ***
Survey Page With Empty Or Invalid Options Should Fail
    [Arguments]    ${GTitle}    ${ETitle}    ${SerialNumber}    ${FieldPeriodStart}    ${FieldPeriodEnd}    ${GSurveyMethod}    ${ESurveyMethod}
    ...    ${GPopDesc}    ${EPopDesc}     ${NetSampleSize}
    Pass Execution If    '${BROWSER}' == 'ie'    Survey Creation not possible in IE
    Clear Element Text    name=titleDe
    Input Text    name=titleDe    ${GTitle}
    Clear Element Text    name=titleEn
    Input Text    name=titleEn    ${ETitle}
    Clear Element Text    name=serialNumber
    Input Text    name=serialNumber    ${SerialNumber}
    Clear Element Text    xpath=//md-datepicker[@name='fieldPeriodStart']//input
    Input Text    xpath=//md-datepicker[@name='fieldPeriodStart']//input    ${FieldPeriodStart}
    Clear Element Text    xpath=//md-datepicker[@name='fieldPeriodEnd']//input
    Input Text    xpath=//md-datepicker[@name='fieldPeriodEnd']//input    ${FieldPeriodEnd}
    Clear Element Text    name=surveyMethodDe
    Input Text    name=surveyMethodDe    ${GSurveyMethod}
    Clear Element Text    name=surveyMethodEn
    Input Text    name=surveyMethodEn    ${ESurveyMethod}
    Choose Quantitative Daten As Data Type
    Clear Element Text    name=populationDescriptionDe
    Input Text    name=populationDescriptionDe    ${GPopDesc}
    Clear Element Text    name=populationDescriptionEn
    Input Text    name=populationDescriptionEn    ${EPopDesc}
    Choose Sample Type
    Input Gross Sample Size
    Clear Element Text    name=sampleSize
    Input Text    name=sampleSize    ${NetSampleSize}
    Select a survey unit
    Select a country
    Save Changes
    Page Should Contain    Die Erhebung wurde nicht gespeichert, weil es noch ungültige Felder gibt!
    Close The Toast Message   ${TOAST_MSSG}

Go To Survey Create Page
    Pass Execution If    '${BROWSER}' == 'ie'    Survey Creation not possible in IE
    Select project by name    robotproject
    Wait for Angular    2s
    Click on surveys tab
    Click Element Through Tooltips    xpath=//ui-view/descendant::button[md-icon[text()='add']]
    Click add button

Close Survey Editor
    Pass Execution If    '${BROWSER}' == 'ie'    Survey Creation not possible in IE
    Get back to german home page
    Click Element Through Tooltips    xpath=//button[text()='Ja']
    #Probleme mit allen anderen Optionen, merkwürdiges Resultat wenn zuschnell ausgeloggt wird.
    Sleep    1s

Choose Quantitative Daten As Data Type
    Click Element Through Tooltips    xpath=//md-select[@name='dataType']
    Click Element Through Tooltips    xpath=//md-select-menu//md-option[contains(., 'Quantitative Daten')]

Choose Sample Type
    Clear Element Text    xpath=//sample-type-picker//input[@name='sampleType']
    Input Text    xpath=//sample-type-picker//input[@name='sampleType']   Kombination aus Wahrscheinlichkeits- und Nicht-Wahrscheinlichkeitsauswahl

Input Gross Sample Size
    Clear Element Text    name=grossSampleSize
    Input Text    name=grossSampleSize    5

Select a survey unit
    Clear Element Text   name=unit
    Input Text    xpath=//md-input-container//input[@name='unit']  Eltern

Click add button
    Click Element Through Tooltips    xpath=//button[@type='button']//md-icon[text()='add']

Select a country
    Clear Element Text   name=countryInput
    Input Text    xpath=//md-input-container//input[@name='countryInput']   Deutschland

Close The Toast Message
    [Arguments]  ${TOAST_MSSG}
    Click Element Through Tooltips  xpath=//md-toast//span[contains(.,"Die Erhebung wurde nicht gespeichert")]
    Element Should Contain  xpath=//md-toast//span[contains(.,"Die Erhebung wurde nicht gespeichert")]  ${TOAST_MSSG}
    Click Element Through Tooltips  xpath=//button//following::md-icon[contains(.,"close")]
