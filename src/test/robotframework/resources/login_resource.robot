*** Settings ***
Documentation     Resource used by all search and view test cases
Library           ExtendedSelenium2Library
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
    Wait Until Angular Ready    10s
    Wait Until Keyword Succeeds    5s    0.5s    Page Should Contain Element    xpath=//button[@type='submit' and not(contains(@disabled, 'disabled'))]
    Click Element Through Tooltips    xpath=//button[@type='submit'][contains(.,'OK')]
    Wait Until Angular Ready    10s

Delete Robotsproject
    Pass Execution If    '${BROWSER}' == 'ie'    Study Creation not possible in IE
    Get back to german home page
    Input Text    xpath=//input[@placeholder = 'Projekt auswählen']    robotsproject${BROWSER}
    Click Element Through Tooltips    xpath=//md-sidenav/descendant::button[md-icon[text()='']]
    Click Element Through Tooltips    xpath=//button[text()='OK']

Save Changes
    Click Element Through Tooltips    xpath=//ui-view/descendant::button[md-icon[text()='save']]

Publisher Logout
    Click Element Through Tooltips    xpath=//md-icon[contains(.,'account_circle')]

Data Provider Logout
    Click Element Through Tooltips    xpath=//md-icon[contains(.,'account_circle')]
