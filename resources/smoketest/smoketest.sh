# is runnable only once because of the id's of component and assembly which are static!

# create components
curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/component_part_1.json http://localhost:9080/manufacturedomain/component
curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/component_part_2.json http://localhost:9080/manufacturedomain/component
curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/component_part_3.json http://localhost:9080/manufacturedomain/component

# create assemblies
curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/assembly_1.json http://localhost:9080/manufacturedomain/assembly
curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/assembly_2.json http://localhost:9080/manufacturedomain/assembly

# create bill of material with component and assembly
curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/bom_assembly_1_part_1.json http://localhost:9080/manufacturedomain/component/bom
curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/bom_assembly_1_part_2.json http://localhost:9080/manufacturedomain/component/bom
curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/bom_assembly_1_part_3.json http://localhost:9080/manufacturedomain/component/bom

curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/bom_assembly_2_part_1.json http://localhost:9080/manufacturedomain/component/bom
curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/bom_assembly_2_part_2.json http://localhost:9080/manufacturedomain/component/bom

# add boms to assembly
curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/bomPK_1_4_1.json http://localhost:9080/manufacturedomain/component/bom/addToComponent
curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/bomPK_2_4_2.json http://localhost:9080/manufacturedomain/component/bom/addToComponent
curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/bomPK_3_4_3.json http://localhost:9080/manufacturedomain/component/bom/addToComponent

curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/bomPK_1_5_1.json http://localhost:9080/manufacturedomain/component/bom/addToComponent
curl -u admin:adminpwd --header "Content-Type: application/json" --request POST --data @data/bomPK_2_5_3.json http://localhost:9080/manufacturedomain/component/bom/addToComponent

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