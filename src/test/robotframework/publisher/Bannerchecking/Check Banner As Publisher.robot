*** Settings ***
Documentation     Check for Publishers no welcome banner appears
Library           SeleniumLibrary

*** Test Cases ***
Check for Publisher Banner Does not Appear
   Assert No Welcome Text After Login

*** Keywords ***
Assert No Welcome Text After Login
   Page Should Not Contain Element   xpath=//md-dialog[@arial-label='Herzlich Willkommen']
