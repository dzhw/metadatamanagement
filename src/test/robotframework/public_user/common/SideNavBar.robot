*** Settings ***
Resource          ../resource.robot
Documentation     Tests if side bar is visible when browser window is maximized and not visible when browser window is small.
...
...               This test has a workflow that is created using keywords in
...               the imported resource file.
Suite Setup       Open Browser To Home Page
Suite Teardown    Close Browser


*** Test Cases ***
SideNavBar Visibility
        Set Window Size  800  600
        Wait Until Element Is Visible  xpath=//md-toolbar/descendant::button[md-icon[text()='menu']]
        NavBar should be hidden
				Click Button  xpath=//md-toolbar/descendant::button[md-icon[text()='menu']]
				NavBar should be open
				Maximize Browser Window
				NavBar should be always open


*** Keywords ***

NavBar should be hidden
        Page Should Contain Element  xpath=//md-sidenav[contains(@class, 'md-closed')]

NavBar should be open
        Page Should Not Contain Element  xpath=//md-sidenav[contains(@class, 'md-closed')]

NavBar should be always open
        Page Should Contain Element  xpath=//md-sidenav[contains(@class, 'md-locked-open')]
