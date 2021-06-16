*** Settings ***
Documentation     Resource used by all search and view test cases
Library           SeleniumLibrary
Library           AngularJSLibrary  root_selector=[data-ng-app]
Resource          click_element_resource.robot
Resource          home_page_resource.robot
Variables         ../common_variables.yaml

*** Keywords ***
Login as dataprovider
    Click Element Through Tooltips    xpath=//a[contains(@aria-label,'anzumelden')]
    Input Text    id=username    dataprovider
    Input Password    id=password    dataprovider
    Click Element Through Tooltips    xpath=//button[@type='submit']

Login as publisher
    Click Element Through Tooltips    xpath=//a[contains(@aria-label,'anzumelden')]
    Input Text    id=username    publisher
    Input Password    id=password    publisher
    Click Element Through Tooltips    xpath=//button[@type='submit']

Create Project
    [Arguments]    ${projectname}
    Click Element Through Tooltips    xpath=//md-sidenav//button[md-icon[text()='add']]
    Input Text    name=id    ${projectname}
    Run Keyword If    '${BROWSER}' == 'safari'  Sleep  10s
    Wait for Angular    2s
    Wait Until Keyword Succeeds    5s    0.5s    Page Should Contain Element    xpath=//button[@type='submit' and not(contains(@disabled, 'disabled'))]
    Click Element Through Tooltips    xpath=//button[@type='submit'][contains(.,'OK')]
    Wait for Angular    2s

Delete Robotsproject
    Pass Execution If    '${BROWSER}' == 'ie'    Data Package Creation not possible in IE
    Get back to german home page
    Input Text    xpath=//input[@placeholder = 'Projekt auswählen']    robotsproject${BROWSER}
    Wait For Angular    2s
    Click Element Through Tooltips    xpath=//md-sidenav/descendant::button[@ng-click='ctrl.deleteProject()']
    Click Element Through Tooltips    xpath=//button[text()='OK']
    Run Keyword If    '${BROWSER}' == 'safari'    Sleep  10s
    Wait for Angular    2s

Save Changes
    Click Element Through Tooltips    xpath=//ui-view/descendant::button[md-icon[text()='save']]

Publisher Logout
    Click Element Through Tooltips    xpath=//md-icon[contains(.,'account_circle')]

Data Provider Logout
    Click Element Through Tooltips    xpath=//md-icon[contains(.,'account_circle')]
