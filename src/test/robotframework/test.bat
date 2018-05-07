ECHO OFF



start cmd /k "cd ..\..\.. & robot -P src\test\robotframework\libs -d target\test\robotframework\logs -v BROWSER:firefox .\src\test\robotframework\public_user\"
::start cmd /k "cd ..\..\.. & robot -P src\test\robotframework\libs -d target\test\robotframework\logs -v BROWSER:firefox -t 'Robotframework.Public User.Studymanagement.Study Search And View.Looking for Absolventenpanel 2005 in german' .\src\test\robotframework\public_user\"
