*** Settings ***
Documentation     Tests the user experience of searching & finding the Graduate Panel 2005 and opening the survey page of the first wave for bachelor graduates
Resource          ../resources/suite_setup_resource.robot
Resource          ../resources/search_resource.robot
Resource          ../resources/home_page_resource.robot
Suite Setup       Open Home Page
Suite Teardown    Finish Test


*** Test Cases ***
Looking for Absolventenpanel 2005s first wave survey in german
        Click on surveys tab
        Search for  Absolventenpanel 2005 Erste Welle
        Click on search result by id  sur-gra2005-sy3$
        Page Should Contain  n = 1.622
        [Teardown]  Get back to home page

Looking for Graduate Panel 2005s first wave survey in english
        [Setup]   Change language to english
        Click on surveys tab
        Search for  DZHW Graduate Panel 2005 First Wave
        Click on search result by id  sur-gra2005-sy3$
        Page Should Contain  n = 1,622
        [Teardown]  Get back to german home page

*** Keywords ***
Click on surveys tab
           Wait Until Keyword Succeeds  5s  1s  Click Element Through Tooltips  xpath=//md-pagination-wrapper/md-tab-item[2]

           #Wait Until Keyword Succeeds  5s  1s  Click Element  xpath=//md-pagination-wrapper/md-tab-item[2]
#Search for  Absolventenpanel 2005 Erste Welle
#           Input Text             id=query        Absolventenpanel 2005 Erste Welle
#Search for  DZHW Graduate Panel 2005 First Wave
#           Input Text             id=query        Graduate Panel 2005 First Wave
#Click on the search result
#           Wait Until Keyword Succeeds  5s  1s  Click Element Through Tooltips   xpath=//a//span[text()='sur-gra2005-sy3$']

#TODO global keywords

#Search for  Absolventenpanel 2005 Erste Welle
#           Input Text             id=query        Absolventenpanel 2005 Erste Welle

#Click on search result by id  sur-gra2005-sy3$



#Get back to home page
#           Click Element Through Tooltips  xpath=//*[@id="SideNavBar"]/md-toolbar/a
#Change language to english
#           Wait Until Keyword Succeeds  5s  1s  Click Button  id=changeLanguageToEn
#Get back to german home page
#           Click Element Through Tooltips  xpath=//*[@id = 'changeLanguageToDe']
#           Get back to home page
