*** Settings ***
Documentation     Tests the user experience of searching & finding a specific question of the Graduate Panel 2005
Resource          ../suite_setup_resource.robot
Suite Setup       Open Home Page
Suite Teardown    Finish Test


*** Test Cases ***
Looking for Absolventenpanel 2005s Fragebogen Erste Welle in german
        Click on Fragen/Questions
        Search for a specific questions of the Absolventenpanel 2005
        Click on the search result
        Look whether german question page opens correctly
        [Teardown]  Get back to home page

Looking for Graduate Panel 2005s questionnaire first wave in english
        [Setup]   Change language to english
        Click on Fragen/Questions
        Search for a specific question of the DZHW Graduate Panel 2005
        Click on the search result
        Look whether english question page opens correctly
        [Teardown]  Get back to german home page

*** Keywords ***
Click on Fragen/Questions
           Run Keyword If    '${USE_SAUCELABS}' == '${EMPTY}'  Mouse Over  xpath=//md-pagination-wrapper/md-tab-item[4]
           Wait Until Keyword Succeeds  5s  1s  Click Element  xpath=//md-pagination-wrapper/md-tab-item[4]
Search for a specific questions of the Absolventenpanel 2005
           Input Text             id=query        What experiences have you had (so far) during your training/internship?
Search for a specific question of the DZHW Graduate Panel 2005
           Input Text             id=query        Wel­che Er­fah­run­gen haben Sie (bis­her) in Ihrer Aus­bil­dungs- bzw. Prak­ti­kums­pha­se ge­macht?
Click on the search result
           Wait Until Keyword Succeeds  5s  1s  Click Element          xpath=//question-search-result//span[text()='que-gra2005-ins1-3.3$']
Look whether german question page opens correctly
           Page Should Contain  Bilder zur Frage
Look whether english question page opens correctly
           Page Should Contain  Images of this Question
Get back to home page
           Click Element  xpath=//md-sidenav//md-toolbar//a
Change language to english
           Wait Until Keyword Succeeds  5s  1s  Click Button  id=changeLanguageToEn
Get back to german home page
           Run Keyword If    '${USE_SAUCELABS}' == '${EMPTY}'  Mouse Over  id=changeLanguageToDe
           Click Button   id=changeLanguageToDe
           Run Keyword If    '${USE_SAUCELABS}' == '${EMPTY}'  Mouse Over  xpath=//md-sidenav//md-toolbar//a
           Click Element  xpath=//md-sidenav//md-toolbar//a
