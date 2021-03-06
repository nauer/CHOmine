#!/bin/bash

set -e

if [ -z $(which wget) ]; then
    # use curl
    GET='curl'
else
    GET='wget -O -'
fi

GIT_GET="git clone --single-branch --depth 1"

BUILD_LOG=${HOME}/build.log

export PSQL_USER=postgres
export KEYSTORE=${PWD}/keystore.jks

echo "#---> Running $TEST_SUITE tests"

if [ "$TEST_SUITE" = "checkstyle" ]; then
    exit 0 # nothing to do
else
    # Set up properties
    source config/create-ci-properties-files.sh

    echo '#---> Installing python requirements'
    # Install lib requirements
    pip install -r config/lib/requirements.txt
    
#    if [[ "$TEST_SUITE" = "bio" ]]; then
        # we depend on a flymine data source
        #$GIT_GET https://github.com/intermine/flymine-bio-sources.git flymine-bio-sources
        #(cd flymine-bio-sources && ./gradlew bio-source-flymine-static:install)
#    fi

    if [[ "$TEST_SUITE" = "ws" ]]; then

        # set up database for testing
        (cd intermine && ./gradlew createUnitTestDatabases)

        # We will need a fully operational web-application
        echo '#---> Building and releasing web application to test against'
        ./testmine/setup.sh

        sleep 60 # let webapp startup

        # Warm up the keyword search by requesting results, but ignoring the results
        $GET "$TESTMODEL_URL/service/search" > /dev/null
        # Start any list upgrades
        $GET "$TESTMODEL_URL/service/lists?token=test-user-token" > /dev/null

        if [[ "$CLIENT" = "JS" ]]; then
            # We need the imjs code to exercise the webservices
            $GIT_GET https://github.com/intermine/imjs.git client
        elif [[ "$CLIENT" = "PY" ]]; then
            $GIT_GET https://github.com/intermine/intermine-ws-client.py client
        fi
    fi
fi
