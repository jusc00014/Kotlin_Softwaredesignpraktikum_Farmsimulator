package de.unisaarland.cs.se.selab

import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.BoardHandler
import de.unisaarland.cs.se.selab.clouds.CloudData
import de.unisaarland.cs.se.selab.clouds.CloudHandler
import de.unisaarland.cs.se.selab.farms.Farm
import de.unisaarland.cs.se.selab.farms.FarmHandler
import de.unisaarland.cs.se.selab.farms.Machine
import de.unisaarland.cs.se.selab.farms.PathFinder
import de.unisaarland.cs.se.selab.incidents.Incident
import de.unisaarland.cs.se.selab.incidents.IncidentHandler
import de.unisaarland.cs.se.selab.logger.LogLevel
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.parser.FarmParser
import de.unisaarland.cs.se.selab.parser.MapParser
import de.unisaarland.cs.se.selab.parser.ScenarioParser
import de.unisaarland.cs.se.selab.plants.PlantData
import de.unisaarland.cs.se.selab.plants.PlantType
import java.io.File
import java.io.OutputStreamWriter

const val YEAR_TICK_MIN = 1
const val YEAR_TICK_MAX = 24
const val TICK_MIN = 0
const val TICK_MAX = 1_000

/**
 Main Function
 **/
fun main(args: Array<String>) {
    val data = parseArguments(args) ?: return
    Logger.initLogger(data.logLevel, data.out)
    val mapParser = MapParser(mutableMapOf())
    val boardData: BoardData
    val plantDataMap: Map<PlantType, PlantData>
    val mapFileName = File(data.maps).name
    try {
        val mapData = mapParser.parse(data.maps, data.startYearTick)
        boardData = mapData.first
        plantDataMap = mapData.second
    } catch (_: IllegalArgumentException) {
        return Logger.invalidFile(mapFileName)
    }
    Logger.fileParsed(mapFileName)
    val farmParser = FarmParser()
    val farms: List<Farm>
    val machines: Map<Int, Machine>
    val farmFileName = File(data.farms).name
    try {
        val farmData = farmParser.parse(data.farms, boardData)
        farms = farmData.first
        machines = farmData.second
    } catch (_: IllegalArgumentException) {
        return Logger.invalidFile(farmFileName)
    }
    Logger.fileParsed(farmFileName)
    val scenarioParser = ScenarioParser()
    val incidents: List<Incident>
    val cloudData: CloudData
    val scenarioFileName = File(data.scenario).name
    try {
        val scenarioData = scenarioParser.parse(
            jsonFile = data.scenario,
            board = boardData,
            maxTick = data.maxTick,
            machines = machines,
            farms = farms,
            yearTick = data.startYearTick
        )
        incidents = scenarioData.first
        cloudData = scenarioData.second
    } catch (_: IllegalArgumentException) {
        return Logger.invalidFile(scenarioFileName)
    }
    Logger.fileParsed(scenarioFileName)
    val simulator = buildSimulator(
        farms = farms,
        plantDataMap = plantDataMap,
        machines = machines,
        cloudData = cloudData,
        boardData = boardData,
        incidents = incidents,
        startYearTick = data.startYearTick,
        maxTick = data.maxTick
    )
    Logger.initFarms(farms.map { it.id })
    simulator.start()
}

private fun buildSimulator(
    farms: List<Farm>,
    plantDataMap: Map<PlantType, PlantData>,
    machines: Map<Int, Machine>,
    cloudData: CloudData,
    boardData: BoardData,
    incidents: List<Incident>,
    startYearTick: Int,
    maxTick: Int,
): Simulator {
    val boardHandler = BoardHandler()
    val farmHandler = FarmHandler(
        idToFarm = farms.associateBy { it.id },
        plantData = plantDataMap,
        machines = machines,
        pathFinder = PathFinder()
    )
    val cloudHandler = CloudHandler(cloudData, boardData)
    val incidentHandler = IncidentHandler(
        incidents.sortedBy { it.id }.groupBy { it.tick }.toMutableMap()
    )

    return Simulator(
        boardHandler = boardHandler,
        farmHandler = farmHandler,
        cloudHandler = cloudHandler,
        incidentHandler = incidentHandler,
        boardData = boardData,
        startYearTick = startYearTick,
        maxTick = maxTick
    )
}

/**
 * ReturnData for the parseArguments function
 */
data class ArgumentData(
    val maps: String,
    val farms: String,
    val scenario: String,
    val startYearTick: Int,
    val maxTick: Int,
    val logLevel: LogLevel,
    val out: String?
)

/**
 * Parses and partially validates the argument array given to the program
 *
 * @throws IllegalArgumentException
 * @author Julian Gromov
 */
fun parseArguments(args: Array<String>): ArgumentData? {
    var maps: String? = null
    var farms: String? = null
    var scenario: String? = null
    var startYearTick: Int? = null
    var maxTick: Int? = null
    var logLevel: LogLevel? = null
    var outPath: String? = null
    for (pair in args.toList().chunked(2)) {
        if (pair[0] == "--help") { return printHelp() }
        require(pair.size == 2) { "Argument is missing a value: $pair[0]" }
        when (pair[0]) {
            "--map" -> {
                require(maps == null) { "--maps already set!" }
                maps = pair[1]
            }
            "--farms" -> {
                require(farms == null) { "--farms already set!" }
                farms = pair[1]
            }
            "--scenario" -> {
                require(scenario == null) { "--scenario already set!" }
                scenario = pair[1]
            }
            "--start_year_tick" -> {
                require(startYearTick == null) { "--start_year_tick already set!" }
                startYearTick = pair[1].toInt()
                require(startYearTick in YEAR_TICK_MIN..YEAR_TICK_MAX) {
                    "--startYearTick must be between $YEAR_TICK_MIN and $YEAR_TICK_MAX!"
                }
            }
            "--max_ticks" -> {
                require(maxTick == null) { "--max_ticks already set!" }
                maxTick = pair[1].toInt()
                require(maxTick in TICK_MIN..TICK_MAX) {
                    "--max_ticks must be between $TICK_MIN and $TICK_MAX!"
                }
            }
            "--log_level" -> {
                require(logLevel == null) { "--log_level already set!" }
                logLevel = LogLevel.valueOf(pair[1])
            }
            "--out" -> {
                require(outPath == null) { "--out already set!" }
                outPath = pair[1]
            }
            else -> { throw IllegalArgumentException("Unknown argument: $pair") }
        }
    }

    return ArgumentData(
        maps = maps ?: error("--maps was not set!"),
        farms = farms ?: error("--farms was not set!"),
        scenario = scenario ?: error("--scenario was not set!"),
        startYearTick = startYearTick ?: 1,
        maxTick = maxTick ?: error("--maxTick was not set!"),
        logLevel = logLevel ?: error("--logLevel was not set!"),
        out = outPath
    )
}

/**
 * Prints help for the program
 * @return null
 */
fun printHelp(): ArgumentData? {
    OutputStreamWriter(System.err).use { out ->
        out.write(
            """
            The simulator is started with these command line parameters:
            --map (String): Path to the map. (always required)
            --farms (String): Path to the file with information about the farms. (always required)
            --scenario (String): Path to the scenario file. (always required)
            --start_year_tick (Int): The tick to start with within a year (optional, default value 1, between 1 and 24 inclusive)
            --max_ticks (Int): Maximum allowed number of simulation ticks. (always required, must not exceed 1’000)
            --log_level (String): The level of log detail that shall be output (always required, either DEBUG, INFO or IMPORTANT)
            --out (String): Path to output file. Uses ’stdout’ by default.
            --help: This usage info
            """.trimIndent()
        )
    }
    return null
}
