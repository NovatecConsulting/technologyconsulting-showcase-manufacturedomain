# is runnable only once because of the id's of component and assembly which are static!

# create components
curl --header "Content-Type: application/json" --request POST --data @data/component_part_1.json http://localhost:9080/manufacturedomain/component
curl --header "Content-Type: application/json" --request POST --data @data/component_part_2.json http://localhost:9080/manufacturedomain/component
curl --header "Content-Type: application/json" --request POST --data @data/component_part_3.json http://localhost:9080/manufacturedomain/component

# create assemblies
curl --header "Content-Type: application/json" --request POST --data @data/assembly_1.json http://localhost:9080/manufacturedomain/assembly
curl --header "Content-Type: application/json" --request POST --data @data/assembly_2.json http://localhost:9080/manufacturedomain/assembly

# create bill of material with component and assembly
curl --header "Content-Type: application/json" --request POST --data @data/bom_assembly_1_part_1.json http://localhost:9080/manufacturedomain/component/bom
curl --header "Content-Type: application/json" --request POST --data @data/bom_assembly_1_part_2.json http://localhost:9080/manufacturedomain/component/bom
curl --header "Content-Type: application/json" --request POST --data @data/bom_assembly_1_part_3.json http://localhost:9080/manufacturedomain/component/bom

curl --header "Content-Type: application/json" --request POST --data @data/bom_assembly_2_part_1.json http://localhost:9080/manufacturedomain/component/bom
curl --header "Content-Type: application/json" --request POST --data @data/bom_assembly_2_part_2.json http://localhost:9080/manufacturedomain/component/bom

# add boms to assembly
curl --header "Content-Type: application/json" --request POST http://localhost:9080/manufacturedomain/component/bom/1/4/1
curl --header "Content-Type: application/json" --request POST http://localhost:9080/manufacturedomain/component/bom/2/4/2
curl --header "Content-Type: application/json" --request POST http://localhost:9080/manufacturedomain/component/bom/3/4/3

curl --header "Content-Type: application/json" --request POST http://localhost:9080/manufacturedomain/component/bom/1/5/1
curl --header "Content-Type: application/json" --request POST http://localhost:9080/manufacturedomain/component/bom/2/5/3

# create inventory with component
curl --header "Content-Type: application/json" --request POST --data @data/inventory_component_part_1.json http://localhost:9080/manufacturedomain/component/inventory
curl --header "Content-Type: application/json" --request POST --data @data/inventory_component_part_2.json http://localhost:9080/manufacturedomain/component/inventory
curl --header "Content-Type: application/json" --request POST --data @data/inventory_component_part_3.json http://localhost:9080/manufacturedomain/component/inventory

curl --header "Content-Type: application/json" --request POST --data @data/inventory_assembly_1.json http://localhost:9080/manufacturedomain/component/inventory
curl --header "Content-Type: application/json" --request POST --data @data/inventory_assembly_2.json http://localhost:9080/manufacturedomain/component/inventory

# create/schedule workorder
curl --header "Content-Type: application/json" --request POST --data @data/workorder.json http://localhost:9080/manufacturedomain/workorder

# do a delivery
curl --header "Content-Type: application/json" --request POST --data @data/component_demands.json http://localhost:9080/manufacturedomain/component/deliver

#set workorder with id 1 in different states
#from OPEN to STAGE1
curl --header "Content-Type: application/json" --request PUT --data @data/workorder.json http://localhost:9080/manufacturedomain/workorder/advance_status/1
#from STAGE1 to STAGE2
curl --header "Content-Type: application/json" --request PUT --data @data/workorder.json http://localhost:9080/manufacturedomain/workorder/advance_status/1
#from STAGE3 to STAGE3
curl --header "Content-Type: application/json" --request PUT --data @data/workorder.json http://localhost:9080/manufacturedomain/workorder/advance_status/1

#complete with id 1 workorder
curl --header "Content-Type: application/json" --request PUT --data @data/workorder.json http://localhost:9080/manufacturedomain/workorder/1/2

# cancel a workorder
curl --header "Content-Type: application/json" --request POST --data @data/workorder.json http://localhost:9080/manufacturedomain/workorder
curl --header "Content-Type: application/json" --request DELETE --data @data/workorder.json http://localhost:9080/manufacturedomain/workorder/2