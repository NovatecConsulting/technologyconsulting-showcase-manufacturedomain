# technologyconsulting-security-showcase-manufacturedomain
manufacturedomain is a part of a showcase implementation which is running on a open liberty instance. It is structured right now like this

- **manufacturedomainParent** Parent maven module
    - **manufacturedomainDTO** - contains all classes used in the rest controllers
    - **manufacturedomainWAR** - contains the rest controllers and all EJB classes and entities
    - **manufacturedomainEAR** - contains the war module

#### The project consists of the following packages

- **de.novatec.showcase.manufacture.ejb.entity** - with all related order domain entities
- **de.novatec.showcase.manufacture.ejb.session** - with the order domain EJB session beans
- **de.novatec.showcase.manufacture.controller** - with corresponding REST controllers for Item, Customer and Order
- **de.novatec.showcase.manufacture.mapper** - with orika mapper fro dto/entity mapping


#### build, run and stop manufacturedomain on an open liberty server
- **build:** mvn clean install
- **run:** mvn liberty:run
- **stop:** mvn liberty:stop
- **run open liberty in development mode:** mvn liberty:dev

All commands have to be executed from the manufacturedomainEAR folder. In development mode you can run the the integration tests (*IT.java classes) by pressing RETURN/ENTER when the server is up. Code changes in the IT tests are hot replaced.

#### Smoketest
There is a little script smoketest.sh in the manufacturedomainParent\resources\smoketest folder which could be used to test if the very basic functionality works after staring the open liberty server with the manufacturedomain as EAR. Be careful this smoketest.sh could be run only once!!!

- create three components (parts)
- create two assemblies
- create bill of material (bom) 
    - with three components and two assemblies
    - assembly 1 is build up from part 1,2,3 and 
    - assembly 2 is build up only from part 1 and 2
- create the inventories for 
    - three components and 
    - two assemblies
- create/schedule a workorder
- deliver (ComponentDemand) parts
- move workorder through the workorder states
    - **STAGE1**
    - **STAGE2**
    - **STAGE3**
- complete workorder/ set workorder in state **COMPLETE**
- create second workorder
- cancel the second workorder / set workorder in state **CANCELED**

#### TODOs:

- replace formerly existing MDB/JMS code with REST clients to other domains (order, supplier)
- Better REST Responses including status codes
- some validations to avoid NPE's
