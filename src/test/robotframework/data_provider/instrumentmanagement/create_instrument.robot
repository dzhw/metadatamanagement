*** Settings ***
Documentation     Instrument Creation by Dataprovider
Metadata          Info on data    This test suite uses the project with the name "robotproject4${BROWSER}" which needs to be available in the testing environment
Resource          ../../resources/home_page_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/login_resource.robot

*** Test Cases ***
Create Instrument by Dataprovider
    Go To Instrument Create Page
    Fill up the description and title
    Choose Type
    Choose Survey
    Fill up the Annotations
    Save Changes
    Get back to german home page
    Click on instruments tab
    Delete Instrument

*** Keywords ***
Go To Instrument Create Page
    Select project by name    robotproject4${BROWSER}
    Wait for Angular    2s
    Click on instruments tab
    Click Element Through Tooltips    xpath=//ui-view/descendant::button[md-icon[text()='add']]

Fill up the description and title
    Input Text   xpath=//input[@name='descriptionDe']    Instrument Description De
    Input Text   xpath=//input[@name='descriptionEn']    Instrument Description De
    Input Text   xpath=//input[@name='titleDe']    Instrument Title De
    Input Text   xpath=//input[@name='titleEn']    Instrument Title En
    Input Text   xpath=//input[@name='subtitleDe']    Instrument Title De
    Input Text   xpath=//input[@name='subtitleEn']    Instrument Title En

Choose Type
    Click Element  xpath=//md-select[@name="type"]
    Click Element  xpath=//md-select-menu//md-option[contains(., 'CAPI')]

Choose Survey
    Click Element   xpath=//md-chips[@name="surveys"]//md-autocomplete//input
    Click Element Through Tooltips  xpath=//span[contains(.,'sur-robotproject4${BROWSER}')]

Fill up the Annotations
    Input Text  xpath=//textarea[contains(@name,'annotationsDe')]  This is Annotations in De
    Input Text  xpath=//textarea[contains(@name,'annotationsEn')]  This is Annotation in En

Delete Instrument
    Click Element Through Tooltips    xpath=//button[normalize-space()='Löschen']
    Click Element Through Tooltips    xpath=//button[text()='Ja']
