*** Settings ***
Documentation     Resources for checking url status
Library           RequestsLibrary
Library           SeleniumLibrary

*** Keywords ***
Check URL Status with xpath Locator
    [Arguments]    ${xpathlocator}
    ${uri}  Get Element Attribute   xpath=${xpathlocator}  href
    GET    ${uri}
    Request Should Be Successful
