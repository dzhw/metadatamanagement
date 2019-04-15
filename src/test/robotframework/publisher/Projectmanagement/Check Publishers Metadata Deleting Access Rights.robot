*** Settings ***
Documentation     Publisher Create a new Project and Studies, then checks the metadata deleting access rights
Resource          ../../resources/login_resource.robot
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/project_management_resource.robot

*** Variables ***
${PROJECT_NAME}  hossainrobot
${TOAST_MSSG}  Die Aktion ist nicht möglich

*** Test Cases ***
Publisher Create But Can Not Delete Study When Publisher is Ready
   Create Project  ${PROJECT_NAME}${BROWSER}
   Assign a dataprovider  dataprovider
   Select Survey Checkbox
   Select Instruments Checkbox
   Select Questions Checkbox
   Select Datasets Checkbox
   Select Variable Checkbox
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
   Input Text    xpath=//md-autocomplete[@md-search-text="tagSearchTextDe"]//input[@type="search"]   Deutsche Tags Schlüsselwörter
   Input Text    xpath=//md-autocomplete[@md-search-text="tagSearchTextEn"]//input[@type="search"]   English Days Keyword
   Save Changes
   Click on Cockpit Button
   Click Publisher Ready Checkbox for Studies
   Click on Delete Button for Metadata
   Close The Toast Message  ${TOAST_MSSG}

Publisher Create and Can Delete Study When Both are Ready
   Click Dataprovider Ready Checkbox for Studies
   Click on Delete Button for Metadata
   Close The Toast Message  ${TOAST_MSSG}

Publisher Create and Can Delete Study When Dataprovider is Ready
   Click Publisher Ready Checkbox for Studies    #deselect the checkbox this time
   Click on Delete Button for Metadata
   Discard Changes No

Publisher Create and Can Delete Study When Both are Not Ready
    Click Dataprovider Ready Checkbox for Studies   #deselect the checkbox this time
    Click on Delete Button for Metadata
    Discard Changes Yes
    Delete project by name  ${PROJECT_NAME}${BROWSER}

*** Keywords ***
Choose Panel As Survey Design
    Click Element Through Tooltips    xpath=//md-select[@name = 'surveyDesign']
    Click Element Through Tooltips    xpath=//md-select-menu//md-option[contains(., 'Panel')]

Choose In Aufbereitung as Data Availibility
    Click Element Through Tooltips    xpath=//md-select[@name = 'dataAvailability']
    Click Element Through Tooltips    xpath=//md-select-menu//md-option[contains(., 'In Aufbereitung')]

Click on Delete Button for Metadata
    Click Element Through Tooltips   xpath=//md-card-actions//span[contains(., "Löschen")]
