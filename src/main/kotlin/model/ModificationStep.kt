package model

sealed class ModificationStep {
    class DependenciesStep(val dependencies: List<String>) : ModificationStep()
    class BoilerPlateStep(val filesNames: List<String>, val model: Map<String, Any>, val dirName: String?) :
        ModificationStep()

    class ExistingFiles() : ModificationStep()
}

enum class TypeClassToModification {
    APP, MANIFEST
}