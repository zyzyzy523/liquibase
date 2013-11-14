#!/bin/sh

if [ -z $JAVA_OPTS ]; then JAVA_OPTS=""; fi

java -cp liquibase/*;liquibase/lib/*;sdk/lib/* %JAVA_OPTS% liquibase.sdk.Main ${1+"$@"}
