*** Settings ***
Documentation     Tests the user experience of searching & finding the Graduate Panel 2005 and opening its study page
Resource          ../suite_setup_resource.robot
Suite Setup       Open Home Page
Suite Teardown    Finish Test

*** Test Cases ***

Looking for Absolventenpanel 2005 in german
        Search for Absolventepanel 2005
        Click on the search result
        Look whether german study page opens right
        [Teardown]  Get back to home page
Looking for Graduate Panel 2005 in english
        [Setup]   Change language to english
        Search for DZHW Graduate Panel 2005
        Click on the search result
        Look whether english study page opens right
        [Teardown]  Get back to german home page


*** Keywords ***
Change language to english
           Wait Until Keyword Succeeds  5s  1s  Click Button  id=changeLanguageToEn

Search for Absolventepanel 2005
           Input Text             id=query        Absolventenpanel 2005

Search for DZHW Graduate Panel 2005
           Input Text             id=query        Graduate Panel 2005

Look whether german study page opens right
            Page Should Contain  Wirt­schafts- und Fi­nanz­kri­se

Look whether english study page opens right
            Page Should Contain  eco­nomic and fi­nan­cial cri­sis

Click on the search result
           Wait Until Keyword Succeeds  5s  1s  Click Element          xpath=//study-search-result//span[text()='stu-gra2005$']

Get back to home page
           Click Element  xpath=//md-sidenav//md-toolbar//a

Get back to german home page
           Click Button   id=changeLanguageToDe
           Click Element  xpath=//md-sidenav//md-toolbar//a
