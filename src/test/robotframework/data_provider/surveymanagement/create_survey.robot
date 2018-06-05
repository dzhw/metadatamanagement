*** Settings ***
Documentation     Data driven test of survey creation.
Resource    ../resources/home_page_resource.robot
Resource    ../resources/search_resource.robot
Resource    ../resources/login_resource.robot
Test Template  Survey Page With Empty Or Invalid Options Should Fail
Test Setup  Go To Survey Create Page
Test Teardown  Close Survey Editor And Log Out
Default Tags  Long

*** Test Cases ***             GerTitle     EngTitle   Wave      FielPeriodStart  FieldPeriodEnd  GerSurvMeth  EngSurvMeth  GerPopTitle  EngPopTitle  GerPopDesc    EngPopDesc          GerSample  EngSample  NetSampleSize   ResponseRate
Empty German Title             ${Empty}     Something  1         01.05.2018       31.05.2018      Versuch      Trial        Alle         All          Na eben alle  Of course everyone  Alle       All        1400            10
Empty English Title            Irgendetwas  ${Empty}   1         01.05.2018       31.05.2018      Versuch      Trial        Alle         All          Na eben alle  Of course everyone  Alle       All        1400            10
Empty Wave                     Irgendetwas  Something  ${Empty}  01.05.2018       31.05.2018      Versuch      Trial        Alle         All          Na eben alle  Of course everyone  Alle       All        1400            10
Empty FieldPeriodStart         Irgendetwas  Something  1         ${Empty}         31.05.2018      Versuch      Trial        Alle         All          Na eben alle  Of course everyone  Alle       All        1400            10
Empty FieldPeriodEnd           Irgendetwas  Something  1         01.05.2018       ${Empty}        Versuch      Trial        Alle         All          Na eben alle  Of course everyone  Alle       All        1400            10
Invalid FieldPeriodEnd First   Irgendetwas  Something  1         01.05.2018       01.01.2000      Versuch      Trial        Alle         All          Na eben alle  Of course everyone  Alle       All        1400            10
Invalid FieldPeriodStart       Irgendetwas  Something  1         Startzeitpunkt   01.01.2000      Versuch      Trial        Alle         All          Na eben alle  Of course everyone  Alle       All        1400            10
Invalid FieldPeriodEnd Second  Irgendetwas  Something  1         01.01.2000       Endzeitpunkt    Versuch      Trial        Alle         All          Na eben alle  Of course everyone  Alle       All        1400            10
Empty German SurveyMethod      Irgendetwas  Something  1         01.05.2018       01.06.2018      ${Empty}     Trial        Alle         All          Na eben alle  Of course everyone  Alle       All        1400            10
Empty English SurveyMethod     Irgendetwas  Something  1         01.05.2018       01.06.2018      Versuch      ${Empty}     Alle         All          Na eben alle  Of course everyone  Alle       All        1400            10
Empty PopTitle                 Irgendetwas  Something  1         01.05.2018       01.06.2018      Versuch      Trial        ${Empty}     ${Empty}     Na eben alle  Of course everyone  Alle       All        1400            10
Empty PopDescription           Irgendetwas  Something  1         01.05.2018       01.06.2018      Versuch      Trial        Alle         All          ${Empty}      ${Empty}            Alle       All        1400            10
Empty Sample                   Irgendetwas  Something  1         01.05.2018       01.06.2018      Versuch      Trial        Alle         All          Na eben alle  Of course everyone  ${Empty}   ${Empty}   1400            10
Empty SampleSize               Irgendetwas  Something  1         01.05.2018       01.06.2018      Versuch      Trial        Alle         All          Na eben alle  Of course everyone  Alle       All        ${Empty}        10
Invalid SampleSize First       Irgendetwas  Something  1         01.05.2018       01.06.2018      Versuch      Trial        Alle         All          Na eben alle  Of course everyone  Alle       All        -5              10
Invalid SampleSize First       Irgendetwas  Something  1         01.05.2018       01.06.2018      Versuch      Trial        Alle         All          Na eben alle  Of course everyone  Alle       All        Testsamplesize  10
Invalid ResponseRate           Irgendetwas  Something  1         01.05.2018       01.06.2018      Versuch      Trial        Alle         All          Na eben alle  Of course everyone  Alle       All        Testsamplesize  E


*** Keywords ***
Survey Page With Empty Or Invalid Options Should Fail
  Pass Execution If    '${BROWSER}' == 'ie'  Survey Creation not possible in IE
  [Arguments]  ${GTitle}  ${ETitle}  ${Wave}  ${FieldPeriodStart}  ${FieldPeriodEnd}  ${GSurveyMethod}  ${ESurveyMethod}  ${GPopTitle}  ${EPopTitle}  ${GPopDesc}  ${EPopDesc}  ${GSample}  ${ESample}  ${NetSampleSize}  ${ResponseRate}
  Input Text  name=titleDe                  ${GTitle}
  Input Text  name=titleEn                  ${ETitle}
  Input Text  name=wave                     ${Wave}
  Input Text  xpath=//md-datepicker[@name='fieldPeriodStart']//input  ${FieldPeriodStart}
  Input Text  xpath=//md-datepicker[@name='fieldPeriodEnd']//input  ${FieldPeriodEnd}
  Input Text  name=surveyMethodDe           ${GSurveyMethod}
  Input Text  name=surveyMethodEn           ${ESurveyMethod}
  Click Element Through Tooltips            xpath=//md-select[@name = 'dataType']
  Click Element Through Tooltips            xpath=//md-select-menu//md-option[contains(., 'Quantitative Daten')]
  Input Text  name=populationTitleDe        ${GPopTitle}
  Input Text  name=populationTitleEn        ${EPopTitle}
  Input Text  name=populationDescriptionDe  ${GPopDesc}
  Input Text  name=populationDescriptionEn  ${EPopDesc}
  Input Text  name=sampleDe                 ${GSample}
  Input Text  name=sampleEn                 ${ESample}
  Input Text  name=sampleSize               ${NetSampleSize}
  Input Text  name=responseRate             ${ResponseRate}
  Save Changes
  Page Should Contain  Die Erhebung wurde nicht gespeichert, weil es noch ungültige Felder gibt!

Go To Survey Create Page
  Pass Execution If    '${BROWSER}' == 'ie'  Survey Creation not possible in IE
  Login as dataprovider
  Select project by name    robotsproject
  Wait Until Angular Ready	6s
  Click on surveys tab
  Click Element Through Tooltips          xpath=//ui-view/descendant::button[md-icon[text()='add']]
  Wait Until Element Is Visible   xpath=//ui-view/descendant::a[md-icon[text()='add']]
  Click Element Through Tooltips          xpath=//ui-view/descendant::a[md-icon[text()='add']]


Close Survey Editor And Log Out
  Pass Execution If    '${BROWSER}' == 'ie'  Survey Creation not possible in IE
  Click Element Through Tooltips  xpath=//md-icon[text()='close']
  Get back to home page
  Click Element Through Tooltips  xpath=//button[text()='Ja']
  Sleep  1s
  Click Element Through Tooltips  xpath=//button[@id='logout']
