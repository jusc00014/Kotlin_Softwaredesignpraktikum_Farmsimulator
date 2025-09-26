# Implementation Report

## Individual Contributions

The following table summarizes the actual contributions of each group member, including deviations from the planned responsibilities. 
The "implemented components" column should only include major contributions to the respective components (i.e., no bugfixes or small additions to existing components).

| Member    | Implemented Components                                                                 | Testing Contributions                                                            | Additional Work    |
|-----------|----------------------------------------------------------------------------------------|----------------------------------------------------------------------------------|--------------------|
| Julia     | Farms Package, PlantType, Coordinate, TileType                                         | FarmHandler, PathFinder, Drought, Sowing, Irrigating, Cloud Package, SystemTests |                    |
| Christian | FarmParser, ScenarioParser, BoardHandler, Fertile, Field, Plantation, Incident Package | Plant, SystemTests                                                               |                    |
| Julian    | Plant, PlantData, Main, ScenarioParser, BoardData, Simulator, PlantTile                | Fertile, SystemTests                                                             | Implementation Plan |
| Leonard   | Logger, Cloud Package, CloudCreation                                                   | BoardData, Cloud Parsing, SystemTests                                            |                 |
| Laurenz   | MapParser, LogLevel, Direction, Tile                                                   | Plant, ScenarioParser, FarmParser, MapParser, SystemTests                        |                 |

---

## Adjustments from the Implementation Plan

- FarmHandler took longer
- Every Component by Artemiy and Jens were reassigned
- Logger and Incidents was reassigned from Laurenz to Leonard and Christian, as the MapParser took too much time

---

## Detailed Timeline 

For each student, this section should provide a detailed day-by-day timeline of their activities and progress.

### Julia

- **Day 1:**
- ...

### Christian

- ...
- 
### Julian

- **Day 1:** Main, BoardData, PlantData implemented and started implementing Plant
- **Day 2:** Changes to Tile and Fertile behaviour, BoardData's neighbours and getFertiles function reworked, implemented Simulator, fixed HarvestEstimate in Plants
- **Weekend 1:** CityExpansion cross-validation in ScenarioParser implemented, Main reworked for other class changes
- **Day 3:** added missing Logger prints to Main, made resetForNexTick and irrigate private in Fertile, Plant initializes with HarvestEstimate penalties, fixes to harvestPenalty calculation
- **Day 4:** harvestEstimate now updated by sow and harvest directly, corrected PlantHarvestEstimateBigTest harvestEstimate values, added Fertile UnitTests, fixed Plant cutting; isFallow; isSown and cutting missed penalty, fixed Fertile's loseMoisture and irrigatable, fixed Main to use fileName instead of the whole Path, fixed argument parsing of --map, fixed Simulation loop executing maxTick, added getter for tiles in BoardData, Constants data class added, added check for Forest around Village tile created by CityExpansion
- **Day 5:** performance increase of the neighbours function, changed behaviour of neighbours function to include the center tile in return, fixed CrossValidation of CityExpansion, fixed Fertile.irrigatable, fixed CloudCreation validation
- **Day 6:** added Parser system tests, fixed CloudCreation validation skipping instructions
- **Day 7:** Parser system tests reworked, wrote Implementation_Report.md

### Leonard

- ...

### Laurenz

- ...

---

## Usage of Generative AI

**Julia:**
- ChatGPT (GPT-5): Debugging
- Copilot (GPT-5): Debugging + Code Completion

**Christian:**
- ChatGPT (GPT-5): Research on how things work, e.g. how lateinit works

**Julian:**
- ChatGPT (GPT-5): Research on how things work

**Leonard:**
- ChatGPT (GPT-5): Research on how things work, Debugging

**Laurenz:**
Did not use generative AI in the implementation phase (neither for code-completion, rewriting code, nor testing)

We are aware of the potential dangers of using these tools and take full responsibility for any code, documents and other content produced during the group phase.