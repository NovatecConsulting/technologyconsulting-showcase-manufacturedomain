#!/bin/bash

# is runnable only once because of the id's of component and assembly which are static!

declare HOST=localhost
declare PORT=9080

. `dirname $0`/options.sh
. `dirname $0`/mockserver.sh
. `dirname $0`/manufacturedomain.sh


function main
{
        script_options
        # setup the database
        setup

        # do some business calls
        start_mockserver
        business_calls
        stop_mockserver
}

main $@
