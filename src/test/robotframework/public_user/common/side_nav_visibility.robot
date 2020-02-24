*** Settings ***
Documentation     Tests if side bar is visible when browser window is maximized and not visible when browser window is small.
Resource          ../../resources/search_resource.robot
Resource          ../../resources/home_page_resource.robot

*** Test Cases ***
Left side nav is hidden on small devices
    [Setup]    Set small device size
    Navigate to search
    Navbar should be hidden
    [Teardown]    Maximize Browser Window

Left side nav can be opened on small devices
    [Setup]    Set small device size
    Click menu button
    Navbar should be open
    [Teardown]    Maximize Browser Window

Left side nav is visible on large devices
    Navbar should be always open

Left side nav is not visible on start page
    Click fdz logo
    Navbar should be hidden

*** Keywords ***
Navbar should be hidden
    Page Should Contain Element    xpath=//md-sidenav[contains(@class, "md-closed")]

Navbar should be open
    Page Should Contain Element    xpath=//md-sidenav[not(contains(@class, "md-closed"))]

Navbar should be always open
    Page Should Contain Element    xpath=//md-sidenav[contains(@class, "md-locked-open")]

Click menu button
    Wait Until Keyword Succeeds    5s    1s    Click Button    xpath=//md-toolbar/descendant::button[md-icon[text()="menu"]]

Set small device size
    Set Window Size    800    600
