#!/bin/bash

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
