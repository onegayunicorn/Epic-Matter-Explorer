package com.example.data.sovereign

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.*
import kotlin.random.Random

data class MythicAssemblyState(
    val status: String = "Idle",
    val invocationPhase: String = "Ready",
    val materializationProgress: Float = 0f,
    val assemblerReadiness: Float = 0f,
    val currentMythic: String = "None",
    val coherenceLevel: Float = 0f,
    val isMaterializing: Boolean = false
)

data class MythicEntity(
    val name: String,
    val domain: String,
    val affinity: Float,
    val materializationCost: Float
)

class MythicAssembler {
    private val _state = MutableStateFlow(MythicAssemblyState())
    val state: StateFlow<MythicAssemblyState> = _state.asStateFlow()
    
    private val mythicEntities = listOf(
        MythicEntity("Aether", "Cosmic", 0.95f, 10f),
        MythicEntity("Chronos", "Time", 0.90f, 12f),
        MythicEntity("Nyx", "Night", 0.85f, 8f),
        MythicEntity("Erebus", "Shadow", 0.80f, 6f),
        MythicEntity("Pegasus", "Mythic", 0.92f, 9f)
    )
    
    suspend fun invokeMythic(entityName: String, coherence: Float): Boolean {
        val entity = mythicEntities.find { it.name.equals(entityName, ignoreCase = true) }
            ?: return false
        
        if (coherence < 0.85f) {
            _state.update { it.copy(
                status = "Coherence Low",
                invocationPhase = "Failed"
            )}
            return false
        }
        
        _state.update { it.copy(
            status = "Invoking",
            invocationPhase = "Calling ${entity.name}",
            currentMythic = entity.name,
            coherenceLevel = coherence,
            isMaterializing = true
        )}
        
        var progress = 0f
        while (progress < 1f) {
            progress += entity.affinity * 0.02f + Random.nextFloat() * 0.01f
            _state.update { it.copy(
                materializationProgress = min(1f, progress),
                assemblerReadiness = progress * entity.affinity
            )}
            kotlinx.coroutines.delay(50)
        }
        
        _state.update { it.copy(
            status = "Materialized",
            invocationPhase = "Complete: ${entity.name}",
            isMaterializing = false,
            materializationProgress = 1f
        )}
        
        return true
    }
    
    fun getAvailableEntities(): List<MythicEntity> {
        return mythicEntities
    }
}
