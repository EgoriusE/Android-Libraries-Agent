package com.bobrusik.plugin.android_libraries_agent.model

import com.intellij.openapi.module.Module

data class ModificationModel(
    val steps: List<ModificationStep>,
    val module: Module
)