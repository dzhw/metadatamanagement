*** Settings ***
Documentation     Tests if login is possible
Default Tags      smoketest
Resource          ../../resources/home_page_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/login_resource.robot

*** Test Cases ***
Login as dataprovider
    Login as dataprovider
    Click Element Through Tooltips    xpath=//button[contains(.,'abmelden')]
