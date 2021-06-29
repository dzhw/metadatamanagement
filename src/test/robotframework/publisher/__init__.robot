*** Setting ***
Documentation     Publisher Common setup and teardown for all publisher tests
Suite Setup       Login as publisher
Suite Teardown    Publisher Logout
Library           SeleniumLibrary
Library           AngularJSLibrary  root_selector=[data-ng-app]
Resource          ../resources/click_element_resource.robot
Resource          ../resources/login_resource.robot
Force Tags        publisher
