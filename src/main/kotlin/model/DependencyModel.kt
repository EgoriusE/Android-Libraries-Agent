package model

data class DependencyModel(
    val name: String,
    val version: String,
    val componentName: String
) {
    val fullName = name + version
}