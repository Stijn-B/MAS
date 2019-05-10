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


# Real TODO

- concrete problem definition: road network with possible randomly generated congestions that are scouted by delegate ants.
	Requires congestion information between every two network nodes.
	Optional: only use ants for parcel discovery
- research questions and hypotheses:
	- how do delegate ants best use pheromones?
	- send delegates from parcel or from AGV or ...?


Delegate MAS - 3 types of

1. Feasibility ants
    Reizen door het netwerk tussen de AVGs, parcels en leverplaatsen en laten op elk punt info achter: afstand tussen huidige punt en vorige punt
    Hierdoor zullen alle AVGs, parcels en leverplaatsen een lijst hebben van afstanden tot andere AVGs, parcels en leverplaatsen (road signs)
    Mogelijkheidheden voor de generatie vd ants:
        Een nieuw parcel spawnt x aantal ants die na y aantal hops 'sterven' of blijven tot het parcel geleverd is.

2. Exploration ants
    Worden uitgezonden door een AVG. Gaan mogelijke paden af en bepalen hoe lang de AVG hier over zou doen.
    Heuristiek vd paden bepalen en de beste kiezen, mogelijke parameters:
        - efficientie (som vd leverafstanden / totale afstand die effectief afgelegd zal worden)
        - een andere AVG die maar een klein beetje minder snel zou zijn heeft mss al gecommit om een vd parcels te leveren
            (-> kan inefficientie veroorzaken bij deze andere AVG)
    Limiet opleggen voor de paden: aantal parcels of lengte/duratie vh pad

3. Intention ants (de real-wordt AVGs)
    Commit aan het beste pad dat de exploration ants vinden
    -> Beste pad volgen en op bepaalde momenten nieuwe exploration ants uitzenden

