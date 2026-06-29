package com.example.data.sovereign

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.*
import kotlin.random.Random

data class BioEntanglementState(
    val vectorState: FloatArray = floatArrayOf(0f, 0f, 0f),
    val coherence: Float = 0.0f,
    val entanglementStability: Float = 0.0f,
    val houseHolderTransform: FloatArray = floatArrayOf(1f, 0f, 0f),
    val isStable: Boolean = false
)

class BioEntanglementEngine {
    private val _state = MutableStateFlow(BioEntanglementState())
    val state: StateFlow<BioEntanglementState> = _state.asStateFlow()
    
    private val targetCoherence = 0.99f
    private var sampleCount = 0
    private var runningCoherence = 0f
    
    fun processBioData(eeg: FloatArray, ecg: FloatArray, gsr: Float): BioEntanglementState {
        val normalizedEEG = normalizeVector(eeg)
        val normalizedECG = normalizeVector(ecg)
        
        val coupledVector = floatArrayOf(
            normalizedEEG.average().toFloat(),
            normalizedECG.average().toFloat(),
            gsr
        )
        
        val householderResult = householderReflection(coupledVector)
        
        val coherence = calculateCoherence(householderResult)
        val stability = calculateEntanglementStability(householderResult)
        
        sampleCount++
        runningCoherence = runningCoherence * 0.95f + coherence * 0.05f
        
        val isStable = runningCoherence >= targetCoherence
        
        _state.update {
            it.copy(
                vectorState = coupledVector,
                coherence = runningCoherence,
                entanglementStability = stability,
                houseHolderTransform = householderResult,
                isStable = isStable
            )
        }
        
        return _state.value
    }
    
    private fun normalizeVector(vector: FloatArray): FloatArray {
        val norm = sqrt(vector.sumOf { it.toDouble() * it.toDouble() }).toFloat()
        return if (norm > 0) vector.map { it / norm }.toFloatArray() else vector
    }
    
    private fun householderReflection(vector: FloatArray): FloatArray {
        val v = vector.copyOf()
        val norm = sqrt(v.sumOf { it.toDouble() * it.toDouble() }).toFloat()
        
        if (norm == 0f) return floatArrayOf(1f, 0f, 0f)
        
        val reflection = FloatArray(3) { 0f }
        for (i in v.indices) {
            for (j in v.indices) {
                val term = if (i == j) 1f else 0f
                reflection[i] += (term - 2 * v[i] * v[j] / (norm * norm))
            }
        }
        
        return reflection
    }
    
    private fun calculateCoherence(householderResult: FloatArray): Float {
        val dotProduct = householderResult.sumOf { it.toDouble() * it.toDouble() }
        return min(1f, abs(dotProduct).toFloat())
    }
    
    private fun calculateEntanglementStability(vector: FloatArray): Float {
        val mean = vector.average().toFloat()
        val variance = vector.map { (it - mean) * (it - mean) }.average().toFloat()
        return max(0f, min(1f, 1f - sqrt(variance.toDouble()).toFloat()))
    }
}
