@echo off

rem get command line args into a variable
set CMD_LINE_ARGS=%1
if ""%1""=="""" goto done
shift
:setup
if ""%1""=="""" goto done
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto setup
:done

IF NOT DEFINED JAVA_OPTS set JAVA_OPTS=

java -cp liquibase/*;liquibase/lib/*;sdk/lib/* %JAVA_OPTS% liquibase.sdk.Main %CMD_LINE_ARGS%
