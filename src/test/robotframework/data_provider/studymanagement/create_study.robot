*** Settings ***
Documentation     Tests if study can be created.
Resource      ../resources/home_page_resource.robot
Resource      ../resources/search_resource.robot
Resource      ../resources/login_resource.robot

*** Test Cases ***
Create Study
  [Setup]  Create RobotsProject
  Pass Execution If    '${BROWSER}' == 'ie'  Study Creation not possible in IE
  Click on study tab
  Open Study Create Page
  Input Text    name=titleDe                 Test Studie
  Input Text    name=titleEn                 Test Study
  Input Text    name=studySeriesDe           Roboter Studien ${BROWSER}
  Input Text    name=studySeriesEn           Robot Studies ${BROWSER}
  Choose Panel As Survey Design
  Input Text    name=institutionDe           DZHW ${BROWSER}
  Input Text    name=institutionEn           DZHW ${BROWSER}
  Input Text    name=sponsorDe               BMBF ${BROWSER}
  Input Text    name=sponsorEn               BMBF ${BROWSER}
  Input Text    name=annotationsDe           Diese Studie wurde von Robot automatisch erstellt.
  Input Text    name=annotationsEn           This study was created automatically by Robot.x
  Choose In Aufbereitung as Data Availibility
  Input Text    name=descriptionDe           Diese Studie wurde automatisch erstellt und überprüft die Eingabe valider Eigenschaften.
  Input Text    name=descriptionEn           This study was created automatically and test the input of valid attributes.
  Input Text    name=authorsFirstName_0      Anne
  Input Text    name=authorsMiddleName_0     noMiddleName
  Input Text    name=authorsLastName_0       Droid
  Add Another Author
  Input Text    name=authorsFirstName_1      R2
  Input Text    name=authorsMiddleName_1     D2
  Input Text    name=authorsLastName_1       noLastName
  Move Second Author To Place One
  Save Changes
  Page Should Contain Element  xpath=//md-toolbar//a[contains(.,'robotsproject${BROWSER}')]
  [Teardown]  Delete Robotsproject

*** Keywords ***
Create RobotsProject
  Pass Execution If    '${BROWSER}' == 'ie'  Study Creation not possible in IE
  Login as dataprovider
  Click Element Through Tooltips		xpath=//md-sidenav//button[md-icon[text()='add']]
  Input Text			name=id		robotsproject${BROWSER}
  Wait Until Keyword Succeeds  5s  0.5s    Page Should Contain Element  xpath=//button[@type='submit' and not(contains(@disabled, 'disabled'))]
  Click Element Through Tooltips		xpath=//button[@type='submit']

Open Study Create Page
  Click Element Through Tooltips          xpath=//ui-view/descendant::button[md-icon[text()='add']]
  Wait Until Element Is Visible   xpath=//ui-view/descendant::a[md-icon[text()='add']]
  Click Element Through Tooltips          xpath=//ui-view/descendant::a[md-icon[text()='add']]

Choose Panel As Survey Design
  Click Element Through Tooltips             xpath=//md-select[@name = 'surveyDesign']
  Click Element Through Tooltips             xpath=//md-select-menu//md-option[contains(., 'Panel')]

Choose In Aufbereitung as Data Availibility
  Click Element Through Tooltips             xpath=//md-select[@name = 'dataAvailability']
  Click Element Through Tooltips             xpath=//md-select-menu//md-option[contains(., 'In Aufbereitung')]

Add Another Author
  Click Element Through Tooltips  xpath=//md-card/descendant::button[md-icon[text()='add']]

Move Second Author To Place One
  Click Element Through Tooltips  xpath=//md-card/descendant::button[md-icon[text()='keyboard_arrow_up']]
