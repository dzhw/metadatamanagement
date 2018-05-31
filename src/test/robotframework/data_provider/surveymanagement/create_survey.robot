*** Settings ***
Documentation     Data driven test of survey creation.
Resource    ../resources/home_page_resource.robot
Resource    ../resources/search_resource.robot
Resource    ../resources/login_resource.robot
Test Template  Survey Page With Empty Options Should Fail
Test Setup  Go To Survey Create Page
Test Teardown  Close Survey Editor And Log Out
Default Tags  Long

*** Test Cases ***
Empty German Title  ${Empty}  Something  1  01.05.2018  31.05.2018  Versuch  Trial  Alle  All  Na eben alle  Of course everyone  Alle  All  1400  10
Empty English Title  Irgendetwas  ${Empty}  1  01.05.2018  31.05.2018  Versuch  Trial  Alle  All  Na eben alle  Of course everyone  Alle  All  1400  10
Empty Wave  Irgendetwas  Something  ${Empty}  01.05.2018  31.05.2018  Versuch  Trial  Alle  All  Na eben alle  Of course everyone  Alle  All  1400  10
Empty FieldPeriodStart  Irgendetwas  Something  1  ${Empty}  31.05.2018  Versuch  Trial  Alle  All  Na eben alle  Of course everyone  Alle  All  1400  10
Empty FieldPeriodEnd  Irgendetwas  Something  1  01.05.2018  ${Empty}  Versuch  Trial  Alle  All  Na eben alle  Of course everyone  Alle  All  1400  10
Invalid FieldPeriodEnd First  Irgendetwas  Something  1  01.05.2018  01.01.2000  Versuch  Trial  Alle  All  Na eben alle  Of course everyone  Alle  All  1400  10
Invalid FieldPeriodStart  Irgendetwas  Something  1  Startzeitpunkt  01.01.2000  Versuch  Trial  Alle  All  Na eben alle  Of course everyone  Alle  All  1400  10
Invalid FieldPeriodEnd Second  Irgendetwas  Something  1  01.01.2000  Endzeitpunkt  Versuch  Trial  Alle  All  Na eben alle  Of course everyone  Alle  All  1400  10
Empty German SurveyMethod  Irgendetwas  Something  1  01.05.2018  01.06.2018  ${Empty}  Triel  Alle  All  Na eben alle  Of course everyone  Alle  All  1400  10
Empty English SurveyMethod  Irgendetwas  Something  1  01.05.2018  01.06.2018  Versuch  ${Empty}  Alle  All  Na eben alle  Of course everyone  Alle  All  1400  10
Empty PopTitle  Irgendetwas  Something  1  01.05.2018  01.06.2018  Versuch  Trial  ${Empty}  ${Empty}  Na eben alle  Of course everyone  Alle  All  1400  10
Empty PopDescription  Irgendetwas  Something  1  01.05.2018  01.06.2018  Versuch  Trial  Alle  All  ${Empty}  ${Empty}  Alle  All  1400  10
Empty Sample  Irgendetwas  Something  1  01.05.2018  01.06.2018  Versuch  Trial  Alle  All  Na eben alle  Of course everyone  ${Empty}  ${Empty}  1400  10
Empty SampleSize  Irgendetwas  Something  1  01.05.2018  01.06.2018  Versuch  Trial  Alle  All  Na eben alle  Of course everyone  Alle  All  ${Empty}  10
Invalid SampleSize First  Irgendetwas  Something  1  01.05.2018  01.06.2018  Versuch  Trial  Alle  All  Na eben alle  Of course everyone  Alle  All  -5  10
Invalid SampleSize First  Irgendetwas  Something  1  01.05.2018  01.06.2018  Versuch  Trial  Alle  All  Na eben alle  Of course everyone  Alle  All  Testsamplesize  10
Invalid ResponseRate  Irgendetwas  Something  1  01.05.2018  01.06.2018  Versuch  Trial  Alle  All  Na eben alle  Of course everyone  Alle  All  Testsamplesize  E


*** Keywords ***
Go To Survey Create Page
  Pass Execution If    '${BROWSER}' == 'ie'  Survey Creation not possible in IE
  Login as dataprovider
  Input Text  xpath=//md-sidenav//input[@type="search"]  robotsproject
  Wait Until Angular Ready	6s
  Click on surveys tab
  Click Element Through Tooltips          xpath=//ui-view/descendant::button[md-icon[text()='add']]
  Wait Until Element Is Visible   xpath=//ui-view/descendant::a[md-icon[text()='add']]
  Click Element Through Tooltips          xpath=//ui-view/descendant::a[md-icon[text()='add']]

Survey Page With Empty Options Should Fail
  Pass Execution If    '${BROWSER}' == 'ie'  Survey Creation not possible in IE
  [Arguments]  ${GTitle}  ${ETitle}  ${Wave}  ${FieldPeriodStart}  ${FieldPeriodEnd}  ${GSurveyMethod}  ${ESurveyMethod}  ${GPopTitle}  ${EPopTitle}  ${GPopDesc}  ${EPopDesc}  ${GSample}  ${ESample}  ${NetSampleSize}  ${ResponseRate}
  Input Text  name=titleDe                  ${GTitle}
  Input Text  name=titleEn                  ${ETitle}
  Input Text  name=wave                     ${Wave}
  Input Text  xpath=//md-datepicker[@name="fieldPeriodStart"]//input  ${FieldPeriodStart}
#  Click Button    xpath=//md-input-container[1]//md-datepicker//button
#  Click Element Through Tooltips  xpath=//td[contains(.,"${Month}")]//span[text()="15"]
#  Click Element   css=#md-0-month-2018-5-9 > span:nth-child(1)
  Input Text  xpath=//md-datepicker[@name="fieldPeriodEnd"]//input  ${FieldPeriodEnd}
#  Click Button    xpath=//md-input-container[2]//md-datepicker//button
#  Click Element Through Tooltips  xpath=//td[contains(.,"${Month}")]//span[text()="22"]
#  Click Element   css=#md-1-month-2018-5-16 > span:nth-child(1)
  Input Text  name=surveyMethodDe           ${GSurveyMethod}
  Input Text  name=surveyMethodEn           ${ESurveyMethod}
  Click Element Through Tooltips            xpath=//md-select[@name = "dataType"]
  Click Element Through Tooltips            xpath=//md-select-menu//md-option[contains(., "Quantitative Daten")]
  Input Text  name=populationTitleDe        ${GPopTitle}
  Input Text  name=populationTitleEn        ${EPopTitle}
  Input Text  name=populationDescriptionDe  ${GPopDesc}
  Input Text  name=populationDescriptionEn  ${EPopDesc}
  Input Text  name=sampleDe                 ${GSample}
  Input Text  name=sampleEn                 ${ESample}
  Input Text  name=sampleSize               ${NetSampleSize}
  Input Text  name=responseRate             ${ResponseRate}
  Click Element Through Tooltips  xpath=//ui-view/descendant::button[md-icon[text()='save']]
  Page Should Contain  Die Erhebung wurde nicht gespeichert, weil es noch ungültige Felder gibt!

Close Survey Editor And Log Out
  Pass Execution If    '${BROWSER}' == 'ie'  Survey Creation not possible in IE
  Click Element Through Tooltips  xpath=//md-icon[text()="close"]
  Get back to home page
#  ${present}=  Run Keyword And Return Status    Page Should Contain  Sie haben ungespeicherte Änderungen.
#  Run Keyword If    ${present} == "True"
  Click Element Through Tooltips  xpath=//button[text()="Ja"]
  Sleep  1s
#  Wait Until Keyword Succeeds  2s  1s  Page Should Contain Element  xpath=//button[@id="logout" and not(contains(.,"disabled"))]
#  Click Element Through Tooltips  xpath=//button[@id="logout"]
#  Click Element Through Tooltips  xpath=//md-sidenav//span[text()="dataprovider abmelden"]
  Click Element Through Tooltips  xpath=//button[@id="logout"]
