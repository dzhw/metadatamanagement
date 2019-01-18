*** Settings ***
Documentation     Publisher Create a new Project and Assign role and check the project as dataprovider
Resource          ../../resources/login_resource.robot
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/project_management_resource.robot

*** Variables ***
${PROJECT_NAME}  hossainrobot
${TOAST_MSSG}  Die Aktion ist nicht möglich

*** Test Cases ***
Create and Delete Study
   Create Project  ${PROJECT_NAME}${BROWSER}
   Assign a dataprovider  dataprovider  1
   Select Survey Checkbox
   Select Instruments Checkbox
   Select Questions Checkbox
   Select Datasets Checkbox
   Select Variable Checkbox
   Save Changes
   Switch To Status Tab
   Ensure Study Creation is Possible
   Input Text    name=titleDe    Test Studie
   Input Text    name=titleEn    Test Study
   Input Text    name=studySeriesDe    Roboter Studien ${BROWSER}
   Input Text    name=studySeriesEn    Robot Studies ${BROWSER}
   Choose Panel As Survey Design
   Input Text    name=institutionDe    DZHW ${BROWSER}
   Input Text    name=institutionEn    DZHW ${BROWSER}
   Input Text    name=sponsorDe    BMBF ${BROWSER}
   Input Text    name=sponsorEn    BMBF ${BROWSER}
   Input Text    name=annotationsDe    Diese Studie wurde von Robot automatisch erstellt.
   Input Text    name=annotationsEn    This study was created automatically by Robot.x
   Choose In Aufbereitung as Data Availibility
   Focus    xpath=//textarea[@name = 'descriptionDe']
   Input Text    name=descriptionDe    Diese Studie wurde automatisch erstellt und überprüft die Eingabe valider Eigenschaften.
   Input Text    name=descriptionEn    This study was created automatically and test the input of valid attributes.
   Focus    xpath=//input[@name = 'authorsFirstName_0']
   Input Text    name=authorsFirstName_0    Anne
   Input Text    name=authorsMiddleName_0    noMiddleName
   Input Text    name=authorsLastName_0    Droid
   Save Changes
   Click on Cockpit Button
   Click Publisher Ready Checkbox for Studies
   Click on Delete Button
   Save Changes
   Delete project by name  ${PROJECT_NAME}${BROWSER}

*** Keywords ***
Choose Panel As Survey Design
    Click Element Through Tooltips    xpath=//md-select[@name = 'surveyDesign']
    Click Element Through Tooltips    xpath=//md-select-menu//md-option[contains(., 'Panel')]

Choose In Aufbereitung as Data Availibility
    Click Element Through Tooltips    xpath=//md-select[@name = 'dataAvailability']
    Click Element Through Tooltips    xpath=//md-select-menu//md-option[contains(., 'In Aufbereitung')]

Click on Delete Button
    Click Element Through Tooltips   xpath=//md-card-actions//span[contains(., "Löschen")]
    Click Element Through Tooltips  xpath=//md-toast//span[contains(.,"Die Aktion ist nicht möglich")]
    Element Should Contain  xpath=//md-toast//span[contains(.,"Die Aktion ist nicht möglich")]  ${TOAST_MSSG}
    Click Element Through Tooltips  xpath=//button//following::md-icon[contains(.,"close")]

