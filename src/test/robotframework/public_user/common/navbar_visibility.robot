*** Settings ***
Documentation     Tests if side bar is visible when browser window is maximized and not visible when browser window is small.
Resource    ../resources/search_resource.robot
Resource    ../resources/home_page_resource.robot
Force Tags  smoketest

*** Test Cases ***
Navbar is hidden on small devices
  [Setup]   Set small device size
  Navbar should be hidden
  [Teardown]  Maximize Browser Window

Navbar can be opened on small devices
  [Setup]   Set small device size
  Click menu button
  Navbar should be open
  [Teardown]  Maximize Browser Window

Navbar is visible on large devices
  Navbar should be always open

*** Keywords ***
Navbar should be hidden
  Page Should Contain Element  xpath=//md-sidenav[contains(@class, "md-closed")]

Navbar should be open
  Page Should Contain Element  xpath=//md-sidenav[not(contains(@class, "md-closed"))]

Navbar should be always open
  Page Should Contain Element  xpath=//md-sidenav[contains(@class, "md-locked-open")]

Click menu button
  Wait Until Keyword Succeeds  5s  1s  Click Button  xpath=//md-toolbar/descendant::button[md-icon[text()="menu"]]

Set small device size
  Set Window Size  800  600
