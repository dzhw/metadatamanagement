*** Setting ***
Documentation     Common setup and teardown for all dataprovider tests
Suite Setup       Login as dataprovider
Suite Teardown    Data Provider Logout
Library           SeleniumLibrary
Library           AngularJSLibrary  root_selector=[data-ng-app]
Resource          ../resources/login_resource.robot
Force Tags        dataprovider
