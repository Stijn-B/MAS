Project van het vak Multi-Agent Systems

RinSim Docu https://rinsim.rinde.nl/design/core/

~ ~ Objectives / TODO ~ ~

- Define a concrete pickup-and-delivery problem (environment, AGVs with their capabilities, characteristics of tasks,
scale, communication constraints, dynamism, potential AGV crashes, etc.).
    -> Zie "TaxiExample.java" com\github\rinde\rinsim-example\4.4.6\rinsim-example-4.4.6-sources.jar!\com\github\rinde\rinsim\examples\taxi\TaxiExample.java

- Develop a multi-agent system for a pickup-and-delivery problem domain using AGVs. This solution should be based on BDI
and delegate MAS. The solution should at least account for both task allocation and route planning.
    - Environment maken -> PDPGraphRoadModel gebruiken
        - AGVs en pickup-delivery objecten spawnen op het model
            zie RouteFollowingVehicle
            spawnen adhv https://rinsim.rinde.nl/design/scenario/
    - AGVs programmeren (planning en communicatie)
        - Communicatie adhv: rinsim/core/model/comm/CommModel.java gebruiken om de AGVs te laten communiceren
            Onderlinge communicatie is mogelijks overbodig afhankelijk van het soort planningsalgoritme
        - Planning evt samen uitwerken (?)
            Maak gebruik van Solver interface: https://rinsim.rinde.nl/design/central/



- Make a thorough scientific study of the behavior of the MAS (following the U-model).