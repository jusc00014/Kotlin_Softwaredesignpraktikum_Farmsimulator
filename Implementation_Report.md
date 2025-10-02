# Implementation Report

## Individual Contributions

The following table summarizes the actual contributions of each group member, including deviations from the planned responsibilities. 
The "implemented components" column should only include major contributions to the respective components (i.e., no bugfixes or small additions to existing components).

| Member    | Implemented Components                                                                 | Testing Contributions                                                           | Additional Work    |
|-----------|----------------------------------------------------------------------------------------|---------------------------------------------------------------------------------|--------------------|
| Julia     | Farms Package, PlantType, Coordinate, TileType                                         | FarmHandler, PathFinder, Drought, Sowing, Irrigating, Cloud Package, FarmParser | Dummy-classes      |
| Christian | FarmParser, ScenarioParser, BoardHandler, Fertile, Field, Plantation, Incident Package | Plant, SystemTests                                                              |                    |
| Julian    | Plant, PlantData, Main, ScenarioParser, BoardData, Simulator, PlantTile                | Fertile, MapParser, ScenarioParser, SystemTests                                 | Implementation Plan |
| Leonard   | Logger, Cloud Package, CloudCreationIncident, CloudParsingValidation, Plant            | BoardDataUnit, SystemTestCloudDebug, SystemTestsIncidents                       |                 |
| Laurenz   | MapParser, LogLevel, Direction, Tile                                                   | Plant, ScenarioParser, FarmParser, MapParser, SystemTests                       |                 |

---

## Adjustments from the Implementation Plan

- FarmHandler took longer, as it was planned for 1 day, but took 6 days.
- All Parser took longer than 1 day (mostly 2 to 3 days), because we needed time to figure out how to use the schema.
- Every Component by Artemiy and Jens were reassigned, like in the table above.
- UnitTests were reassigned to persons with free time and/or too few UnitTests written, as the UnitTests of Artemiy and Jens were also reassigned.
- Logger and Incidents was reassigned from Laurenz to Leonard and Christian, as the MapParser took 2 days instead of 1.

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
- **Weekend:** Integration test for AnimalAttack-incident and HarvestEstimate, Unit Test for BeeHappy, OneFieldTestTrial, fix of commonFields in executeSowingPlan
- **Day 8:** OneFieldTestTrials, MachineTakes20-test, correction assembleMachines().sortBy, correction of unit tests after FarmParser.parse(...) changed, corrected machineCanHandle, fixes everywhere after irrigatable(...) changed
- **Day 9:** More FarmParser SystemTests (trials), fixed continueWithIrrigating() to not, Systemtest NonPrioritizedTest/Trial, SystemTest SowingEarlyTest, Systemtest IrrigatingOrder, MachineBehaviorTest, Machine returns logging fix
- **Day 10:** Fixed BuildFailed, FarmParserTests, fix in harvest log, unitTests for parser, fixed location when machine stuck, new/faster pathfinder, reordered every map and list in FarmHandler
- **Day 11:** AnotherIrrigationTest + HarvestTypeTest + rewriting FarmHandler.continueWithIrrigating() for the 4th time

### Christian

- **Day 1:** built Skeleton for Plant and FarmParser; implemented logic for FarmParser, ScenarioParser, Boardhandler and SowingPlan
- **Day 2:** changes to FarmParser, ScenarioParser, BoardHandler; minor changes to Fertile, BrokenMachine; implemented logic for all incidents; implementation for Plantation and Field
- **Weekend 1:** added Validation to FarmParser
- **Day 3:** minor Changes in ScenarioParser, FarmParser, BeeHappy and Plant; Harvest Estimate test
- **Day 4:** Plant, Moisture reduction and Harvest Estimate Unit Tests, added Validation in FarmParser
- **Day 5:** changes to FarmParser and previous Tests; Cloud Simulation test
- **Day 6:** more work on Cloud Tests; bug fix in CloudHandler; detekt fixes
- **Day 7:** changes to Cloud Simulation Test; Farm Behavior and Harvest Unit Tests
- **Weekend 2:** fixes to Cloud Simulation Test
- **Day 8:** more minor Bug fixes CityExpansion; Bug Fixes and changes in FarmParser; more FarmHandler and Harvest Tests
- **Day 9:** minor changes in FarmParser; added full Simulation Test, Harvest Estimate Simulation Test; Machine Duration Unit Test
- **Day 10:** changes to FarmParser; changes to Harvest Simulation Test; Farm Validation test; Moisture System tests
- 
### Julian

- **Day 1:**
  - Main, BoardData, PlantData implemented 
  - started implementing Plant
- **Day 2:** 
  - Changes to Tile and Fertile behaviour
  - BoardData's neighbours and getFertiles function reworked
  - implemented Simulator
  - fixed HarvestEstimate in Plants
- **Weekend 1:**
  - CityExpansion cross-validation in ScenarioParser implemented
  - Main reworked for other class changes
- **Day 3:**
  - added missing Logger prints to Main
  - made resetForNexTick and irrigate private in Fertile
  - Plant initializes with HarvestEstimate penalties
  - fixes to harvestPenalty calculation
- **Day 4:**
  - harvestEstimate now updated by sow and harvest directly
  - corrected PlantHarvestEstimateBigTest harvestEstimate values
  - added Fertile UnitTests
  - fixed Plant cutting, isFallow, isSown and cutting missed penalty
  - fixed Fertile's loseMoisture and irrigatable
  - fixed Main to use fileName instead of the whole Path
  - fixed argument parsing of --map
  - fixed Simulation loop executing maxTick
  - added getter for tiles in BoardData
  - added Constants data class to the main project scope
  - added check for Forest around Village tile created by CityExpansion
- **Day 5:**
  - performance increase of the neighbours function
  - changed behaviour of neighbours function to include the center tile in return
  - fixed CrossValidation of CityExpansion
  - fixed Fertile.irrigatable
  - fixed CloudCreation validation
- **Day 6:**
  - added Parser system tests
    - fixed CloudCreation validation skipping instructions
- **Day 7:**
  - Parser system tests reworked
  - wrote Implementation_Report.md
- **Weekend 2:**
  - added ScenarioParserTest, testing cloud in the ScenarioParser
- **Day 8:**
  - reworked system test: DroughtTest
  - added some system tests
  - fixed Simulator correct tick log at end of Simulation
  - fix Fertile's irrigatable
  - fix Fertile's performableActions to return an empty list on Drought
  - fix Plant's Harvest Estimate and drought behaviour
- **Day 9:** 
  - extended SimulationTestExtension with new string factory functions
  - fixed SowingPlant.isActive to use correctly the tick instead of the yearTick in FarmHandler.sow
  - added + fixed system tests (FallowSowPlanPotato, some ParserTests)
  - drought in Fertile now only managed by Fertile with hasDrought and addDrought functions
  - fixed BeeHappy for Drought and unsown plants
  - reworked Plant:
    - missed logic
    - sowTime also used for Plantations
    - fixes to isFallow and weedable
    - added exits()
- **Day 10:**
  - fixed (again) Simulator correct tick log at end of Simulation
  - fixed FallowSowPlanPotato and ParserTests
  - added new ParserTests
  - fixed Plant Harvest Estimate logging
  - added UnitTests for MapParser
  - added + fixed PlantationLateHarvestTest system tests for harvest estimate specification validation
- **Day 11:**
  - updated Implementation Report
  - bugfixes
  - systemTest for mutant catching

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
- **Monday:** Incident Rework & SomeFixes & SystemTests
- **Tuesday:** AnimalAttack / Drought SyTests & Minor Changes
- **Wednesday:** TestFixes & BeeHappyTesting & BrokenMachine + Mutants & IncidentUnit & NightlyMutantHuntMachines & PathfinderFix
- **Thursday:** MutantHuntOngoing

### Laurenz

- **Day 1:**
  - Create skeleton for some classes
  - Started work on MapParser parsing & validation
- **Day 2:** 
  - Finished parsing of MapParser & simple validation in MapParser
- **Weekend 1:**
  - 
- **Day 3:**
  - Simple test to check Parser usability, added equals() in various classes for later testing
  - added Schema Loader to MapParser + fixed usage of Schema Loader
  - fix to loseMoisture()
- **Day 4:**
  - Started unit testing plants
  - more validation for MapParser
  - fixed handling of negative coords in MapParser (1st half)
- **Day 5:**
  - fixed handling of negative coords in MapParser (2nd half)
  - creating json for systemtests
  - fix for Cloud IDs
  - more validation for MapParser & error messages
- **Day 6:**
  - (work pushed onto weekend)
- **Day 7:**
  - Fixed Plant Creation in MapParser
  - systemtests for machine
  - fix of sowing and executing correct sowing plans
- **Weekend 2:**
  - Unit & System tests for ScenarioParser Incidents
- **Day 8:**
  - Unit tests for Farm
  - fix for BrokenMachine overriding -1 duration
  - Unit & System tests for Cloud Creation
  - fix in ScenarioParser
- **Day 9:** 
  - changed behaviour of neighbour function to get all necessary tiles for larger radii while keeping speed
  - Scenario & FarmParser tests
  - Systemtest for sowing Plan execution
  - changed logging of harvestPenalty
- **Day 10:**
  - changed calculation of harvestPenalty to be correctly applied in the right tick
  - fixed initalHarvestEstimate of plantations
  - systemtests for BeeHappy & BrokenMachine
  - fixes in BeeHappy
  - fixed wrong pollination for 2 plants
  - fixed pollination effeect being applied on potatoes
- **Day 11:**
  - fixed logging of missed actions if plant reaches moisture 0


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
- ChatGPT (GPT-4): function lookups, Debugging, test statement generation

**Laurenz:**
- Did not use generative AI in the implementation phase (neither for code-completion, rewriting code, nor testing)

We are aware of the potential dangers of using these tools and take full responsibility for any code, documents and other content produced during the group phase.
