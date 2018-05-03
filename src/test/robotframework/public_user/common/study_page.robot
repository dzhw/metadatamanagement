*** Settings ***
Documentation     Tests the study page for completeness. Using the
Resource          ../suite_setup_resource.robot
Suite Setup       Open Home Page
Suite Teardown    Finish Test

*** Test Cases ***

#Studienseite deutschsprachig aufrufen
#        Input Text             id=query        Absolventenpanel 2005
#        Wait Until Keyword Succeeds  5s  1s  Click Element          xpath=//study-search-result//span[text()='stu-gra2005$']
#        Page Should Contain    DZHW-Ab­sol­ven­ten­pa­nel 2005
#        [Teardown]  Click Element  xpath=//md-sidenav//md-toolbar//a
Studienseite englischsprachig aufrufen
        [Setup]   Wait Until Keyword Succeeds  5s  1s  Click Button  id=changeLanguageToEn
        Input Text             id=query        Graduate Panel 2005
        Wait Until Keyword Succeeds  5s  1s  Click Element          xpath=//study-search-result//span[text()='stu-gra2005$']
        Page Should Contain    DZHW Graduate Panel 2005
        [Teardown]  Switch back to german and get back to Home Page


*** Keywords ***

Switch back to german and get back to Home Page
           Click Button   id=changeLanguageToDe
           Click Element  xpath=//md-sidenav//md-toolbar//a
