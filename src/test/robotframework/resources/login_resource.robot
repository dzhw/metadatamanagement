*** Settings ***
Documentation     Resource used by all search and view test cases
Library           ExtendedSelenium2Library
Resource          click_element_resource.robot
Resource          home_page_resource.robot
Variables         ../common_variables.yaml

*** Keywords ***
Login as dataprovider
    ${visible}=  Run Keyword And Return Status    Element Should Be Visible   xpath=//*[@id = 'login']
    Run Keyword If    not ${visible}    Click Element Through Tooltips    xpath=//*[@id = 'account-menu-toggle']
    Click Element Through Tooltips    xpath=//*[@id = 'login']
    Input Text    id=username    dataprovider
    Input Password    id=password    dataprovider
    Click Element Through Tooltips    xpath=//button[@type='submit']

Login as publisher
    ${visible}=  Run Keyword And Return Status    Element Should Be Visible   xpath=//*[@id = 'login']
    Run Keyword If    not ${visible}    Click Element Through Tooltips    xpath=//*[@id = 'account-menu-toggle']
    Click Element Through Tooltips    xpath=//*[@id = 'login']
    Input Text    id=username    publisher
    Input Password    id=password    publisher
    Click Element Through Tooltips    xpath=//button[@type='submit']

Create Project
    [Arguments]    ${projectname}
    Click Element Through Tooltips    xpath=//md-sidenav//button[md-icon[text()='add']]
    Input Text    name=id    ${projectname}
    Run Keyword If    '${BROWSER}' == 'safari'  Sleep  1s
    Wait Until Keyword Succeeds    5s    0.5s    Page Should Contain Element    xpath=//button[@type='submit' and not(contains(@disabled, 'disabled'))]
    Click Element Through Tooltips    xpath=//button[@type='submit'][contains(.,'OK')]
    Sleep  3s

Delete Robotsproject
    Pass Execution If    '${BROWSER}' == 'ie'    Study Creation not possible in IE
    Get back to german home page
    Input Text    xpath=//input[@placeholder = 'Projekt auswählen']    robotsproject${BROWSER}
    Click Element Through Tooltips    xpath=//md-sidenav/descendant::button[md-icon[text()='']]
    Click Element Through Tooltips    xpath=//button[text()='OK']

Save Changes
    Click Element Through Tooltips    xpath=//ui-view/descendant::button[md-icon[text()='save']]

Publisher Logout
    ${url} =    Get Location
    Run Keyword If    '/de/' in '${url}'    Click Element Through Tooltips    xpath=//button[contains(.,'abmelden')]
    Run Keyword If    '/en/' in '${url}'    Click Element Through Tooltips    xpath=//button[contains(.,'logout')]

Data Provider Logout
    ${url} =    Get Location
    Run Keyword If    '/de/' in '${url}'    Click Element Through Tooltips    xpath=//button[contains(.,'abmelden')]
    Run Keyword If    '/en/' in '${url}'    Click Element Through Tooltips    xpath=//button[contains(.,'logout')]
