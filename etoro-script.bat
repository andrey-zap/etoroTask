@echo off
IF %1.==. GOTO No1
IF %2.==. GOTO No2
SET job_name=%1
SET build_num=%2
SET mod_success=3
SET /a reminder=build_num%%mod_success
IF %reminder% NEQ 0 (
    ECHO Failing the Script!
    exit /b 1
)

ECHO Job name is %job_name%, build number is %build_num%!
GOTO End1

:No1
  ECHO No param 1
GOTO End1
:No2
  ECHO No param 2
GOTO End1

:End1
  exit /b 0