*** Settings ***
Documentation     Publisher Create a new Project and Data Package, then checks the metadata deleting access rights
Metadata          Info on data    This test suite creates a temporary test project with the name "${PROJECT_NAME}${BROWSER}" which is automatically deleted afterwards
Resource          ../../resources/login_resource.robot
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/project_management_resource.robot

*** Variables ***
${PROJECT_NAME}  temprobotcheckaccess
${TOAST_MSSG}  Die Aktion ist nicht möglich

*** Test Cases ***
Publisher Create But Can Not Delete Data Package When Publisher is Ready
   ${created}  Run Keyword and return status  Create Project  ${PROJECT_NAME}${BROWSER}
   Run Keyword If  ${created}==False  Fail  Could not create new project '${PROJECT_NAME}${BROWSER}'
   Assign a dataprovider  dataprovider
   Select Survey Checkbox
   Select Instruments Checkbox
   Select Questions Checkbox
   Select Datasets Checkbox
   Select Variable Checkbox
   Switch To Status Tab
   Ensure Data Package Creation is Possible
   Input Text    name=titleDe    Test Datenpaket
   Input Text    name=titleEn    Test Data Package
   Input Text    name=studySeriesDe    Roboter Datenpakete ${BROWSER}
   Input Text    name=studySeriesEn    Robot Data Packages ${BROWSER}
   Choose Panel As Survey Design
   Input Text    name=institutionDe_0    DZHW ${BROWSER}
   Input Text    name=institutionEn_0    DZHW ${BROWSER}
   Input Text    name=sponsorDe_0    BMBF ${BROWSER}
   Input Text    name=sponsorEn_0    BMBF ${BROWSER}
   Input Text    name=annotationsDe    Dieses Datenpaket wurde von Robot automatisch erstellt.
   Input Text    name=annotationsEn    This data package was created automatically by Robot.x
   Set Focus To Element    xpath=//textarea[@name = 'descriptionDe']
   Input Text    name=descriptionDe    Dieses Datenpaket wurde automatisch erstellt und überprüft die Eingabe valider Eigenschaften.
   Input Text    name=descriptionEn    This data package was created automatically and test the input of valid attributes.
   Set Focus To Element    xpath=//input[@name = 'projectContributorsFirstName_0']
   Input Text    name=projectContributorsFirstName_0    Anne
   Input Text    name=projectContributorsMiddleName_0    noMiddleName
   Input Text    name=projectContributorsLastName_0    Droid
   Set Focus To Element    xpath=//input[@name = 'dataCuratorsFirstName_0']
   Input Text    name=dataCuratorsFirstName_0    Anne
   Input Text    name=dataCuratorsMiddleName_0    noMiddleName
   Input Text    name=dataCuratorsLastName_0    Droid
   Input Text    xpath=//md-autocomplete[@md-search-text="tagSearchTextDe"]//input   Deutsche Tags Schlüsselwörter
   Run Keyword And Ignore Error  Click Element Through Tooltips    xpath=//md-virtual-repeat-container//span[text()='Deutsche Tags Schlüsselwörter']
   Input Text    xpath=//md-autocomplete[@md-search-text="tagSearchTextEn"]//input   English Days Keyword
   Run Keyword And Ignore Error  Click Element Through Tooltips    xpath=//md-virtual-repeat-container//span[text()='English Days Keyword']
   Save Changes
   Click on Cockpit Button
   Click Publisher Ready Checkbox  dataPackages
   Click on Delete Button for Metadata
   Close The Toast Message  ${TOAST_MSSG}

Publisher Create and Can Delete Data Package When Both are Ready
   Click Dataprovider Ready Checkbox  dataPackages
   Run Keyword If    '${BROWSER}' == 'safari'   Sleep  10s
   Click on Delete Button for Metadata
   Close The Toast Message  ${TOAST_MSSG}

Publisher Create and Can Delete Data Package When Dataprovider is Ready
   Click Publisher Ready Checkbox  dataPackages   #deselect the checkbox this time
   Click on Delete Button for Metadata
   Discard Changes No

Publisher Create and Can Delete Data Package When Both are Not Ready
    Click Dataprovider Ready Checkbox  dataPackages   #deselect the checkbox this time
    Click on Delete Button for Metadata
    Discard Changes Yes
    Delete project by name  ${PROJECT_NAME}${BROWSER}

*** Keywords ***
Choose Panel As Survey Design
    Click Element Through Tooltips    xpath=//md-select[@name = 'surveyDesign']
    Click Element Through Tooltips    xpath=//md-select-menu//md-option[contains(., 'Panel')]

Click on Delete Button for Metadata
    Click Element Through Tooltips   xpath=//md-card-actions//span[contains(., "Löschen")]
