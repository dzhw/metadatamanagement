*** Settings ***
Resource          ../resource.robot
Documentation     Tests if side bar is visible when browser window is maximized and not visible when browser window is small.
...
...               This test has a workflow that is created using keywords in
...               the imported resource file.
Suite Setup       Open Browser To Home Page
Suite Teardown    Close Browser


*** Test Cases ***
SideNavBar Visibility with small frame
        Set Window Size  800  600
        NavBar should be hidden
        Sleep         0.5s
				Click Button  xpath=//md-toolbar/descendant::button[md-icon[text()='menu']]
				NavBar should be open
				Maximize Browser Window
				NavBar should be forced open


*** Keywords ***

NavBar should be hidden
        Page Should Contain Element  dom="ng-isolate-scope _md md-whiteframe-4dp layout-column md-closed"

NavBar should be open
        Page Should Contain Element  dom="ng-isolate-scope _md md-whitefrane-4dp layout-column"

NavBar should be forced open
        Page Should Contain Element  dom="ng-isolate-scope _md md-whiteframe-4dp layout-column md-locked-open"
