*** Settings ***
Documentation     Resources for checking url status
Library           HttpLibrary.HTTP
Library           ExtendedSelenium2Library

*** Keywords ***
Check URL Status with xpath Locator
    [Arguments]    ${xpathlocator}
    ${uri}  Get Element Attribute   xpath=${xpathlocator}@href
    GET    ${uri}
    Response Status Code Should Equal   200 OK
