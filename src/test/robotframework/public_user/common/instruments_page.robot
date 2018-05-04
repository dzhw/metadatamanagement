*** Settings ***
Documentation     Tests the user experience of searching & finding the Graduate Panel 2005 and opening the questionnaire
Resource          ../suite_setup_resource.robot
Suite Setup       Open Home Page
Suite Teardown    Finish Test


*** Test Cases ***
Looking for Absolventenpanel 2005s Fragebogen Erste Welle in german
        Click on Instrumente/Instruments
        Search for Absolventenpanel 2005 Fragebogen Erste Welle
        Click on the search result
        Look whether german instrument page opens correctly
        [Teardown]  Get back to home page

Looking for Graduate Panel 2005s questionnaire first wave in english
        [Setup]   Change language to english
        Click on Instrumente/Instruments
        Search for DZHW Graduate Panel 2005 questionnaire first wave
        Click on the search result
        Look whether english instrument page opens correctly
        [Teardown]  Get back to german home page

*** Keywords ***
Click on Instrumente/Instruments
#           Run Keyword If    '${USE_SAUCELABS}' == '${EMPTY}'  Mouse Over  xpath=//md-pagination-wrapper/md-tab-item[3]
           Wait Until Keyword Succeeds  5s  1s  Click Element  xpath=//md-pagination-wrapper/md-tab-item[3]
Search for Absolventenpanel 2005 Fragebogen Erste Welle
           Input Text             id=query        Absolventenpanel 2005 Fragebogen Erste Welle
Search for DZHW Graduate Panel 2005 questionnaire first wave
           Input Text             id=query        Graduate Panel 2005 Questionnaire First Wave
Click on the search result
           Wait Until Keyword Succeeds  5s  1s  Click Element          xpath=//instrument-search-result//span[text()='ins-gra2005-ins1$']
Look whether german instrument page opens correctly
           Page Should Contain  gra2005_W1_Questionnaire_de.pdf
Look whether english instrument page opens correctly
           Page Should Contain  gra2005_W1_Questionnaire_en.pdf
Get back to home page
           Click Element  xpath=//md-sidenav//md-toolbar//a
Change language to english
           Wait Until Keyword Succeeds  5s  1s  Click Button  id=changeLanguageToEn
Get back to german home page
           Run Keyword If    '${USE_SAUCELABS}' == '${EMPTY}'  Mouse Over  id=changeLanguageToDe
           Click Button   id=changeLanguageToDe
           Run Keyword If    '${USE_SAUCELABS}' == '${EMPTY}'  Mouse Over  xpath=//md-sidenav//md-toolbar//a
           Click Element  xpath=//md-sidenav//md-toolbar//a
