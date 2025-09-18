package de.unisaarland.cs.se.selab.plants

import plants.PlantData
import plants.PlantType

class Plant(type: PlantType, data: PlantData) {
    private var pollinationEffect = 0.0
    private var harvestEstimate = 0
    private var sowTime = -1
    private var harvestTime = -1
    private var actionPerformed = null
    private var stampedeCount = 0
    private var mowedFor = 0

    private fun harvestPenalty(): Double {

    }

    fun getHarvestEstimate(): Int {
        return harvestEstimate
    }

    fun getMinMoisture(): Int {

    }

    private fun blooming(year_tick: Int): Boolean {

    }

    fun pollinateable(year_tick: Int): Boolean {

    }

    fun addPollination(power: Double) {
        pollinationEffect = power
    }

    fun addStampede(): Boolean {

    }

    fun updateHarvestEstimate(year_tick: Int,
                              drought: Boolean,
                              sunlight: Int,
                              moisture: Int,
                              tileID: Int) {

    }

    fun isFallow(year_tick: Int): Boolean {

    }

    fun harvestable(year_tick: Int): Boolean {

    }

    fun cuttable(year_tick: Int): Boolean {

    }

    fun mowable(year_tick: Int): Boolean {

    }

    fun weedable(year_tick: Int): Boolean {

    }

    private fun harvest(year_tick: Int): Int {

    }

    fun performAction(action: Action): Int? {

    }

    fun isSown(): Boolean {
        return (harvestEstimate > 0) && (type == PlantType.FIELD)
    }

    fun sow(plantType: PlantType, plantData: PlantData, year_tick: Int) {

    }

    private fun resetForNextTick() {

    }
}