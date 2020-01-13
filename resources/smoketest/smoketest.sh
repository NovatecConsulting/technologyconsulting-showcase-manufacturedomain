#!/bin/bash

# is runnable only once because of the id's of component and assembly which are static!

java -Dmockserver.initializationJsonPath=./data/init_expectations.json -jar ./lib/mockserver-netty-5.8.1-jar-with-dependencies.jar -serverPort 9090 -logLevel ERROR &
#wait while mockserver is staring
sleep 1

# create components
curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/component_part_1.json http://localhost:9080/manufacturedomain/component
curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/component_part_2.json http://localhost:9080/manufacturedomain/component
curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/component_part_3.json http://localhost:9080/manufacturedomain/component

# create assemblies
for i in {1..21}; do curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/assembly_"$i".json http://localhost:9080/manufacturedomain/assembly; done

# create bill of material (bom) with component and assembly
curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/bom_assembly_4_part_1.json http://localhost:9080/manufacturedomain/component/bom
curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/bom_assembly_4_part_2.json http://localhost:9080/manufacturedomain/component/bom
curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/bom_assembly_4_part_3.json http://localhost:9080/manufacturedomain/component/bom

curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/bom_assembly_5_part_1.json http://localhost:9080/manufacturedomain/component/bom
curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/bom_assembly_5_part_2.json http://localhost:9080/manufacturedomain/component/bom

for i in {6..24}; do 
	curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/bom_assembly_"$i"_part_1.json http://localhost:9080/manufacturedomain/component/bom; 
	curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/bom_assembly_"$i"_part_3.json http://localhost:9080/manufacturedomain/component/bom; 
done

# add boms to assembly
curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/bomPK_4_1_1.json http://localhost:9080/manufacturedomain/component/bom/addToComponent
curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/bomPK_4_2_2.json http://localhost:9080/manufacturedomain/component/bom/addToComponent
curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/bomPK_4_3_3.json http://localhost:9080/manufacturedomain/component/bom/addToComponent

curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/bomPK_5_1_1.json http://localhost:9080/manufacturedomain/component/bom/addToComponent
curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/bomPK_5_2_2.json http://localhost:9080/manufacturedomain/component/bom/addToComponent

for i in {6..24}; do 
	curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/bomPK_"$i"_1_1.json http://localhost:9080/manufacturedomain/component/bom/addToComponent;
	curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/bomPK_"$i"_3_2.json http://localhost:9080/manufacturedomain/component/bom/addToComponent;
done

# create inventory with component
curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/inventory_component_part_1.json http://localhost:9080/manufacturedomain/component/inventory
curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/inventory_component_part_2.json http://localhost:9080/manufacturedomain/component/inventory
curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/inventory_component_part_3.json http://localhost:9080/manufacturedomain/component/inventory

#create inventory with assembly
curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/inventory_assembly_1.json http://localhost:9080/manufacturedomain/component/inventory
curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/inventory_assembly_2.json http://localhost:9080/manufacturedomain/component/inventory

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

curl -X PUT "http://localhost:9090/stop" -H  "accept: */*"