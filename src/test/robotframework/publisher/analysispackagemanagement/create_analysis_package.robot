*** Settings ***
Documentation     Publisher Create Analysis Package
Resource          ../../resources/login_resource.robot
Resource          ../../resources/click_element_resource.robot
Resource          ./add_data.robot

*** Test Cases ***
Create Analysis Package
    Pass Execution If    '${BROWSER}' == 'ie'    Package Creation not possible in IE
    Create Project  robotsproject${BROWSER}
    Create new analysis package
    Fill out details
    Fill out description
    Fill out authors
    Fill out data curation
    Add institutions
    Add sponsors
    Add license
    Add tags
    Add scripts
    Add analysis data
    Move custom data package
    Delete custom data package
    Save Changes
    Upload attachments
    Save Changes
    Delete script file
    Discard Changes
    [Teardown]    Delete Robotsproject
