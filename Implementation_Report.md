# Implementation Report

## Individual Contributions

The following table summarizes the actual contributions of each group member, including deviations from the planned responsibilities. 
The "implemented components" column should only include major contributions to the respective components (i.e., no bugfixes or small additions to existing components).

| Member    | Implemented Components                                                                 | Testing Contributions                                                            | Additional Work    |
|-----------|----------------------------------------------------------------------------------------|----------------------------------------------------------------------------------|--------------------|
| Julia     | Farms Package, PlantType, Coordinate, TileType                                         | FarmHandler, PathFinder, Drought, Sowing, Irrigating, Cloud Package, SystemTests | Dummy-classes      |
| Christian | FarmParser, ScenarioParser, BoardHandler, Fertile, Field, Plantation, Incident Package | Plant, SystemTests                                                               |                    |
| Julian    | Plant, PlantData, Main, ScenarioParser, BoardData, Simulator, PlantTile                | Fertile, SystemTests                                                             | Implementation Plan |
| Leonard   | Logger, Cloud Package, CloudCreationIncident, CloudParsingValidation                   | BoardDataUnit, SystemTestCloudDebug, SystemTestsIncidents                        |                 |
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

- **Day 0:** All dummy classes to get started + FarmHandler.assembleMachines (Tuesday+Wednesday)
- **Day 1:** PathFinder.reachable, PathFinder.canContinue
- **Day 2:** PathFinder.findNearestShed + FarmHandler.assembleSowableFields + FarmHandler.sow + FarmHandler.executeSowingPlan
- **Weekend:** FarmHandler.performPrioritizedAction + FarmHandler.continueWithHarvesting + FarmHandler.nextField + FarmHandler.PerformNonPriorizedActions
- **Day 3:** Fix in FarmHandler irrigating to continue with plantations after fields + debugging detect-mistakes in Farm-Package
- **Day 4:** Unit and integration Tests for Pathfinder and BoardHandler.Neigbor + Unit test and fixes for sowing fields
- **Day 5:** Testing and fixes for irrigating + OneField-System test and fixes in fertile
- **Day 6:** Test and debugging for drought-incident and following actions
- **Day 7:** Unit tests for Cloud-Handler + debugging of given farm-Tests + Fix of System tests after logger was fixes
- **Weekend:** Integration test for AnimalAttack-incident and HarvestEstimate, Unit Test for BeeHappy, OneFieldTestTrial

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

- **Thursday:** FolderStruct & CloudPackageSkeleton & CloudDataLogic & CloudCreationIncident & CloudHandlerPartialLogic main WorkFlow aka Iteration and acting/ all without LoggerCalls
- **Friday:** Adding LoggerCalls to Clouds & Imp Rain & SingleMovement Logic & Adjustments for Dissipation & DetektIntegration & PostMovementFunc & Commenting & LoggerPrintingLogic
- **Weekend:** LoggerImplementation & Formatting
- **Monday:** LoggerStatisticsStoringLogic & further DetektFormatting & CloudParsingValidationLogic & BugFixIncidentParsing & cloudLogicSmallFixes
- **Tuesday:** LoggerFixes & LoggerFarmInitRework & BoardDataUnitTests & CloudParserDebug(InputFix)
- **Wednesday:** First SystemTests for Incidents & Small Logical Adjustments
- **Thursday:** More SystemTests & SystemTest assertFix
- **Friday:** SystemTestClouds for debug
- **Weekend:** New SystemTests & MutantRegis/Finding & ImplementationReport
- **Monday:**
- **Tuesday:**
- **Wednesday:**
- **Thursday:**

### Laurenz

- ...

---

## Usage of Generative AI

**Julia:**
- ChatGPT (GPT-5): Debugging and research
- Copilot (GPT-5): Debugging

**Christian:**
- ChatGPT (GPT-5): Research on how things work, e.g. how lateinit works

**Julian:**
- ChatGPT (GPT-5): Research on how things work

**Leonard:**
- ChatGPT (GPT-5): Research on how things work, Debugging

**Laurenz:**
Did not use generative AI in the implementation phase (neither for code-completion, rewriting code, nor testing)

We are aware of the potential dangers of using these tools and take full responsibility for any code, documents and other content produced during the group phase.
