package model

import com.intellij.openapi.module.Module

data class ModificationModel(
    val steps: List<ModificationStep>,
    val module: Module
)