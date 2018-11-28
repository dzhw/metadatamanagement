*** Setting ***
Documentation     Common setup and teardown for all tests
Suite Setup       Login as publisher after logout
Suite Teardown    Publisher Logout
Library           ExtendedSelenium2Library
Resource          ../resources/login_resource.robot
Resource          ../resources/click_element_resource.robot

*** Keywords ***
Publisher Logout
    ${url} =    Get Location
    Run Keyword If    '/de/' in '${url}'    Click Element Through Tooltips    xpath=//button[contains(.,'abmelden')]
    Run Keyword If    '/en/' in '${url}'    Click Element Through Tooltips    xpath=//button[contains(.,'logout')]
