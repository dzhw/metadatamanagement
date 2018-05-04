ECHO OFF



start cmd /k "cd ..\..\.. & robot -P src\test\robotframework\libs -d target\test\robotframework\logs -v USE_SAUCELABS:true -v BROWSER:edge .\src\test\robotframework\public_user\common\survey_page.robot"
