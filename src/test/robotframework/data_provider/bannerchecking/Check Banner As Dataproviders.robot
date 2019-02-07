*** Settings ***
Documentation     Check for Publishers no Welcome Banner but for Dataproviders appears and assert the Welcome text and dont show again checkbox
Resource          ../../resources/login_resource.robot
Resource          ../../resources/click_element_resource.robot

*** Test Cases ***
Check for Pubslihers Banner Does not Appears
   Data Provider Logout
   Login as publisher
   Assert No Welcome Text After Login
   Publisher Logout
Check for Dataproviders Welcome Banner Appears
   Login as dataprovidertest
   Assert Welcome Text After Login
   Assert Checkbox Dont Show Again is Available  #prerequisite to maintain the test, never mark the checkbox for dataprovidertest
   Close The Banner
   Click Element Through Tooltips  xpath=//span[contains(.,'Hinweise für Datengeber')]
   Close The Banner
   Reload Page
   Sleep  1s
   Assert Welcome Text After Login
   Close The Banner
   Dataprovidertest Provider Logout
   Login as dataprovider

*** Keywords ***
Assert No Welcome Text After Login
   Page Should Not Contain Element   xpath=//md-dialog[@arial-label='Herzlich Willkommen']

Assert Welcome Text After Login
   Page Should Contain Element   xpath=//h2[contains(., 'Herzlich Willkommen')]

Assert Checkbox Dont Show Again is Available
   Page Should Contain Element   xpath=//md-checkbox//div[contains(. , ' Nicht mehr anzeigen')]


Login as dataprovidertest
   ${visible}=  Run Keyword And Return Status    Element Should Be Visible   xpath=//*[@id = 'login']
   Run Keyword If    not ${visible}    Click Element Through Tooltips    xpath=//*[@id = 'account-menu-toggle']
   Click Element Through Tooltips    xpath=//*[@id = 'login']
   Input Text    id=username    dataprovidertest
   Input Password    id=password    dataprovidertest
   Click Element Through Tooltips    xpath=//button[@type='submit']

Close The Banner
   Click Element Through Tooltips  xpath=//button[contains(.,' Schließen')]


Dataprovidertest Provider Logout
    ${url} =    Get Location
    Run Keyword If    '/de/' in '${url}'    Click Element Through Tooltips    xpath=//button[contains(.,'abmelden')]
    Run Keyword If    '/en/' in '${url}'    Click Element Through Tooltips    xpath=//button[contains(.,'logout')]
