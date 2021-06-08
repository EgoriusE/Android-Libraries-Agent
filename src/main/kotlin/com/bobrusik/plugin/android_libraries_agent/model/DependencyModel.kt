package com.bobrusik.plugin.android_libraries_agent.model

data class DependencyModel(
    val name: String,
    val version: String,
    val componentName: String
) {
    val fullName = name + version
}