*** Settings ***
Documentation     Check for Publishers no welcome banner appears
Library           ExtendedSelenium2Library

*** Test Cases ***
Check for Pubslihers Banner Does not Appears
   Assert No Welcome Text After Login

*** Keywords ***
Assert No Welcome Text After Login
   Page Should Not Contain Element   xpath=//md-dialog[@arial-label='Herzlich Willkommen']
