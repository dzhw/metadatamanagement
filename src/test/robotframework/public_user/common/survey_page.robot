*** Settings ***
Documentation     Tests the user experience of searching & finding the Graduate Panel 2005 and opening the survey page of the first wave for bachelor graduates
Resource          ../suite_setup_resource.robot
Suite Setup       Open Home Page
Suite Teardown    Finish Test


*** Test Cases ***
Looking for Absolventenpanel 2005s first wave survey in german
        Click on Erhebungen/survey
        Search for Absolventenpanel 2005 Erste Welle
        Click on the search result
        Look whether german survey page opens correctly
        [Teardown]  Get back to home page

Looking for Graduate Panel 2005s first wave survey in english
        [Setup]   Change language to english
        Click on Erhebungen/survey
        Search for DZHW Graduate Panel 2005 First Wave
        Click on the search result
        Look whether english survey page opens correctly
        [Teardown]  Get back to german home page

*** Keywords ***
Click on Erhebungen/survey
           Mouse Over  xpath=//md-pagination-wrapper/md-tab-item[2]
           Wait Until Keyword Succeeds  5s  1s  Click Element  xpath=//md-pagination-wrapper/md-tab-item[2]
Search for Absolventenpanel 2005 Erste Welle
           Input Text             id=query        Absolventenpanel 2005 Erste Welle
Search for DZHW Graduate Panel 2005 First Wave
           Input Text             id=query        Graduate Panel 2005 First Wave
Click on the search result
           Wait Until Keyword Succeeds  5s  1s  Click Element          xpath=//survey-search-result//span[text()='sur-gra2005-sy3$']
Look whether german survey page opens correctly
           Page Should Contain  n = 1.622
Look whether english survey page opens correctly
           Page Should Contain  n = 1,622
Get back to home page
           Click Element  xpath=//md-sidenav//md-toolbar//a
Change language to english
           Wait Until Keyword Succeeds  5s  1s  Click Button  id=changeLanguageToEn
Get back to german home page
           Click Button   id=changeLanguageToDe
           Mouse over  xpath=//md-sidenav//md-toolbar//a
           Click Element  xpath=//md-sidenav//md-toolbar//a
