*** Settings ***
Documentation     Publisher Create a new Project and Assign role and check the project as dataprovider
Resource          ../../resources/login_resource.robot
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/project_management_resource.robot

*** Variables ***
${PROJECT_NAME}  hossainrobot

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
   Select project by name  ${PROJECT_NAME}${BROWSER}
   Click on Cockpit Button
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
   Add Another Author
   Focus    xpath=//input[@name = 'authorsFirstName_1']
   Input Text    name=authorsFirstName_1    R2
   Input Text    name=authorsMiddleName_1    D2
   Input Text    name=authorsLastName_1    noLastName
   Save Changes
   Delect project by name  ${PROJECT_NAME}${BROWSER}

*** Keywords ***

Choose Panel As Survey Design
    Click Element Through Tooltips    xpath=//md-select[@name = 'surveyDesign']
    Click Element Through Tooltips    xpath=//md-select-menu//md-option[contains(., 'Panel')]

Choose In Aufbereitung as Data Availibility
    Click Element Through Tooltips    xpath=//md-select[@name = 'dataAvailability']
    Click Element Through Tooltips    xpath=//md-select-menu//md-option[contains(., 'In Aufbereitung')]

Add Another Author
    Click Element Through Tooltips    xpath=//md-card/descendant::button[md-icon[text()='add']]


