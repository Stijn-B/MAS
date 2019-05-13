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
	- optimize heuristic parameters
	- how often should intention ants reconsider
	- (how much feasibility ants per parcel should be active ?)


Delegate MAS - 3 types of ants
(in deze beschrijving staat 'AGV' voor een Intention ant)

1. Feasibility ants
    Reizen door het netwerk tussen de AGVs, parcels en leverplaatsen en laten op elk punt info achter: afstand tussen huidige punt en vorige punt
    Hierdoor zullen alle AGVs, parcels en leverplaatsen een lijst hebben van afstanden tot andere AGVs, parcels en leverplaatsen (road signs)
    Omgaan met opgepikte parcels: De informatie vd ants vervalt na een (best niet te lange) tijd zodat een opgepikt parcel vanzelf uit de lijst verdwijnt.
    Er moeten voldoende ants actief zijn in het systeem, het aantal moet dynamisch zijn in correlatie met het aantal parcels.
        Voorstel: een parcel spawnt 1 ant wnr het ontstaat. De AGVs houden een counter bij die omhoog gaat als ze een parcel
                  afleveren. Wanneer een ant aankomt bij een AGV met counter > 0, sterft de ant en gaat de counter vd AGV omlaag
                  (kan ook meer dan 1 ant per parcel -> research question: hoeveel best? )
    ? hoeveel hops maken deze ants per tick ?
    extra: ants kunnen evt een Map bijhouden v locaties en hoevaak ze daar al geweest zijn, zo kunnen ze prioriteit geven
           aan locaties waar ze nog niet/weinig geweest zijn, dit verhoogt mss de kwaliteit vd roadsigns
           -> mogelijks research question
    (Afstand van  A naar B is mss niet gelijk aan de afstand van B naar A door enkelrichtingstraten)

2. Exploration ants
    Worden uitgezonden door een AGV. Gaan mogelijke paden af en bepalen hoe lang de AGV hier over zou doen.
    Heuristiek vd paden bepalen en de beste kiezen, mogelijke parameters:
        - efficientie (som vd leverafstanden / totale afstand die effectief afgelegd zal worden)
        - een andere AGV die maar een klein beetje minder snel zou zijn heeft mss al gecommit om een vd parcels te leveren
            want kan inefficientie veroorzaken bij deze andere AGV
        - een andere AGV heeft gecommit en zal er eerder zijn -> heuristiek = 0
        - AGV moet mss herladen in de uitvalsbasis (?)
            bezoek uitvalsbasis heeft positief effect op heuristiek, evenredig (of expontentieel) met hoeveel bijgeladen zou worden
        - ...
    Limiet opleggen voor de paden: aantal parcels of lengte/duratie vh pad anders mss oneindige exploratie

3. Intention ants (de real-wordt AGVs)
    Commit aan het beste pad dat de exploration ants vinden
    -> Beste pad volgen en op bepaalde momenten nieuwe exploration ants uitzenden
    De parcels laten weten dat de AGV heeft gecommit zodat exploration ants van andere AGVs dit weten
