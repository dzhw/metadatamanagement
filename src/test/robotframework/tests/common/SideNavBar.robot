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
        Page Should Contain Element  dom="ng-isolate-scope _md md-whiteframe-4dp layout-column md-closed"
				Click Button  xpath=//*[@id="toolbar"]/div[2]/button
				Page Should Contain Element  dom="ng-isolate-scope _md md-whitefrane-4dp layout-column"
				Maximize Browser Window
				Page Should Contain Element  dom="ng-isolate-scope _md md-whiteframe-4dp layout-column md-locked-open"
