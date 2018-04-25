*** Settings ***
Documentation     Tests if side bar is visible when browser window is maximized and not visible when browser window is small.
...
...               This test has a workflow that is created using keywords in
...               the imported resource file.
Library           ExtendedSelenium2Library

*** Test Cases ***
SideNavBar Visibility
        Set Window Size  800  600
        Sleep  0.5s  Wait for navbar animation
        NavBar should be hidden
				Click Button  xpath=//md-toolbar/descendant::button[md-icon[text()='menu']]
				NavBar should be open
				Maximize Browser Window
				NavBar should be always open

*** Keywords ***
NavBar should be hidden
        Page Should Contain Element  xpath=//md-sidenav[contains(@class, 'md-closed')]
NavBar should be open
        Page Should Contain Element  xpath=//md-sidenav[not(contains(@class, 'md-closed'))]
NavBar should be always open
        Page Should Contain Element  xpath=//md-sidenav[contains(@class, 'md-locked-open')]
