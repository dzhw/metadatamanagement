*** Settings ***
Documentation     A resource file with reusable keywords and variables.
...
...               The system specific keywords created here form our own
...               domain specific language. They utilize keywords provided
...               by the imported Selenium2Library.
Library           ExtendedSelenium2Library

*** Variables ***
${WEBSITE}           https://metadatamanagement-test.cfapps.io/#!/de
${VALID USER}        robot
${VALID PASSWORD}    robot
${LOGIN URL}         ${WEBSITE}/login
${WELCOME URL}       ${WEBSITE}/search?page=1
${STUDY URL}         ${WEBSITE}/search?page=1&type=studies

*** Keywords ***
Open Browser to Home Page
    Open Browser    ${WEBSITE}    ${BROWSER}
    Maximize Browser Window

Open Browser To Login Page
    Open Browser    ${LOGIN URL}    ${BROWSER}
    Maximize Browser Window
	  Login Page Should Be Open

Open Browser To Search Page As Robot
    Open Browser To Login Page
    Input Username              ${VALID USER}
    Input Password              ${VALID PASSWORD}
    Submit Credentials
    Welcome Page Should Be Open

Go To Login Page
    Go To    ${LOGIN URL}
    Login Page Should Be Open

Input Username
    [Arguments]    ${username}
    Input Text    username    ${username}

Input Password
    [Arguments]    ${password}
    Input Text    password    ${password}

Submit Credentials
    Click Button    xpath=//button[@type='submit']
