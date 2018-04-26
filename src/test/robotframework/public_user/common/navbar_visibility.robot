*** Settings ***
Documentation     Tests if side bar is visible when browser window is maximized and not visible when browser window is small.
...
...               This test has a workflow that is created using keywords in
...               the imported resource file.
Library           ExtendedSelenium2Library

*** Test Cases ***
Navbar is hidden on small devices
        [Setup]   Set small device size
        NavBar should be hidden
        [Teardown]  Maximize Browser Window
Navbar can be opened on small devices
        [Setup]   Set small device size
        Click Menu Button
				NavBar should be open
				[Teardown]  Maximize Browser Window
Navbar is visible on large devices
				NavBar should be always open

*** Keywords ***
NavBar should be hidden
        Page Should Contain Element  xpath=//md-sidenav[contains(@class, 'md-closed')]
NavBar should be open
        Page Should Contain Element  xpath=//md-sidenav[not(contains(@class, 'md-closed'))]
NavBar should be always open
        Page Should Contain Element  xpath=//md-sidenav[contains(@class, 'md-locked-open')]
Click Menu button
				Wait Until Keyword Succeeds  5s  1s  Click Button  xpath=//md-toolbar/descendant::button[md-icon[text()='menu']]
Set small device size
        Set Window Size  800  600
