#!/bin/bash

# start the mockserver
java -Dmockserver.initializationJsonPath=./data/init_expectations.json -jar ./lib/mockserver-netty-5.8.1-jar-with-dependencies.jar -serverPort 9090 -logLevel ERROR &
#wait while mockserver is staring
until $(curl --output /dev/null --silent --head --fail -X PUT "http://localhost:9090/status"); do
    sleep 1
done

# create/schedule workorder
curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/workorder.json http://localhost:9080/manufacturedomain/workorder

# do a delivery
curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/component_demands.json http://localhost:9080/manufacturedomain/component/deliver

#set workorder with id 1 in different states
#from OPEN to STAGE1
curl -u admin:adminpwd --header "Content-Type: application/json" --request PUT --data @data/workorder.json http://localhost:9080/manufacturedomain/workorder/advance_status/1
#from STAGE1 to STAGE2
curl -u admin:adminpwd --header "Content-Type: application/json" --request PUT --data @data/workorder.json http://localhost:9080/manufacturedomain/workorder/advance_status/1
#from STAGE3 to STAGE3
curl -u admin:adminpwd --header "Content-Type: application/json" --request PUT --data @data/workorder.json http://localhost:9080/manufacturedomain/workorder/advance_status/1

#complete with id 1 workorder
curl -u admin:adminpwd --header "Content-Type: application/json" --request PUT --data @data/workorder.json http://localhost:9080/manufacturedomain/workorder/1/2

# cancel a workorder
curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/workorder.json http://localhost:9080/manufacturedomain/workorder
curl -u admin:adminpwd --header "Content-Type: application/json" --request DELETE --data @data/workorder.json http://localhost:9080/manufacturedomain/workorder/2

# wait for still executing calls to mockserver
sleep 1
# stop the mockserver
curl -X PUT "http://localhost:9090/stop" -H  "accept: */*"