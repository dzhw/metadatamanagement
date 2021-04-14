*** Settings ***
Documentation     Publisher Create Data Package
Resource          ../../resources/login_resource.robot
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/home_page_resource.robot

*** Test Cases ***
Create Data Package by Publisher
    Pass Execution If    '${BROWSER}' == 'ie'    Data Package Creation not possible in IE
    Create Project  robotsproject${BROWSER}
    Get Back to german home page
    Click on data package tab
    Open Data Package Create Page
    Input Text    name=titleDe    Test Datenpaket
    Input Text    name=titleEn    Test Data Package
    Input Text    name=studySeriesDe    Roboter Datenpakete ${BROWSER}
    Input Text    name=studySeriesEn    Robot Data Packages ${BROWSER}
    Choose Panel As Survey Design
    Input Text    name=institutionDe_0    DZHW ${BROWSER}
    Input Text    name=institutionEn_0    DZHW ${BROWSER}
    Input Text    name=sponsorDe    BMBF ${BROWSER}
    Input Text    name=sponsorEn    BMBF ${BROWSER}
    Input Text    name=annotationsDe    Dieses Datenpaket wurde von Robot automatisch erstellt.
    Input Text    name=annotationsEn    This data package was created automatically by Robot.x
    Focus    xpath=//textarea[@name = 'descriptionDe']
    Input Text    name=descriptionDe    Dieses Datenpaket wurde automatisch erstellt und überprüft die Eingabe valider Eigenschaften.
    Input Text    name=descriptionEn    This data package was created automatically and test the input of valid attributes.
    Focus    xpath=//edit-people-component[@people-id="'projectContributors'"]//input[@name="peopleFirstName_0"]
    Input Text    xpath=//edit-people-component[@people-id="'projectContributors'"]//input[@name="peopleFirstName_0"]    Anne
    Input Text    xpath=//edit-people-component[@people-id="'projectContributors'"]//input[@name="peopleMiddleName_0"]    noMiddleName
    Input Text    xpath=//edit-people-component[@people-id="'projectContributors'"]//input[@name="peopleLastName_0"]    Droid
    Add Another Contributor
    Focus    xpath=//edit-people-component[@people-id="'projectContributors'"]//input[@name="peopleFirstName_1"]
    Input Text    xpath=//edit-people-component[@people-id="'projectContributors'"]//input[@name="peopleFirstName_1"]    R2
    Input Text    xpath=//edit-people-component[@people-id="'projectContributors'"]//input[@name="peopleMiddleName_1"]    D2
    Input Text    xpath=//edit-people-component[@people-id="'projectContributors'"]//input[@name="peopleLastName_1"]    noLastName
    Move Second Contributor To Place One
    Focus    xpath=//edit-people-component[@people-id="'dataCurators'"]//input[@name="peopleFirstName_0"]
    Input Text    xpath=//edit-people-component[@people-id="'dataCurators'"]//input[@name="peopleFirstName_0"]    Anne
    Input Text    xpath=//edit-people-component[@people-id="'dataCurators'"]//input[@name="peopleMiddleName_0"]    noMiddleName
    Input Text    xpath=//edit-people-component[@people-id="'dataCurators'"]//input[@name="peopleLastName_0"]    Droid
    Input Text    xpath=//md-autocomplete[@md-search-text="tagSearchTextDe"]//input   Deutsche Tags Schlüsselwörter
    Run Keyword And Ignore Error  Click Element Through Tooltips    xpath=//md-virtual-repeat-container//span[text()='Deutsche Tags Schlüsselwörter']
    Input Text    xpath=//md-autocomplete[@md-search-text="tagSearchTextEn"]//input   English Days Keyword
    Run Keyword And Ignore Error  Click Element Through Tooltips    xpath=//md-virtual-repeat-container//span[text()='English Days Keyword']
    Save Changes
    Page Should Contain Element    xpath=//fdz-breadcrumbs//span[contains(.,'robotsproject${BROWSER}')]
    Delete Robotsproject

*** Keywords ***
Open Data Package Create Page
    Click Element Through Tooltips    xpath=//ui-view/descendant::button[md-icon[text()='add']]

Choose Panel As Survey Design
    Click Element Through Tooltips    xpath=//md-select[@name = 'surveyDesign']
    Click Element Through Tooltips    xpath=//md-select-menu//md-option[contains(., 'Panel')]

Add Another Contributor
    Click Element Through Tooltips    xpath=//md-card//button[contains(@aria-label,'Mitarbeiter') and contains(@aria-label,'hinzuzufügen')]

Move Second Contributor To Place One
    Click Element Through Tooltips    xpath=//edit-people-component[@people-id="'projectContributors'"]/descendant::button[md-icon[text()='keyboard_arrow_up']]
