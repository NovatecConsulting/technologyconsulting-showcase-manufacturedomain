#!/bin/bash

# is runnable only once because of the id's of component and assembly which are static!

# setup the database
source ./setup-db.sh

# do some business calls
source ./business-calls.sh
