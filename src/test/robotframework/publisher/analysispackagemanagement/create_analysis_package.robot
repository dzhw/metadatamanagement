*** Settings ***
Documentation     Publisher Create Analysis Package
Metadata          Info on data  Creates a temporary analysis project with the name tempanalysis${BROWSER}.
Resource          ../../resources/login_resource.robot
Resource          ../../resources/click_element_resource.robot
Resource          ./add_data.robot

*** Test Cases ***
Create Analysis Package
    Pass Execution If    '${BROWSER}' == 'ie'    Package Creation not possible in IE
    ${created}  Run Keyword and return status  Create Project  tempanalysis${BROWSER}
    Run Keyword If  ${created}==False  Fail  Could not create new project 'tempanalysis${BROWSER}'
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
    [Teardown]    Delete project by name  tempanalysis${BROWSER}
