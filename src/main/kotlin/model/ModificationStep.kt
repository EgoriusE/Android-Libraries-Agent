package model

sealed class ModificationStep {

    class DependenciesStep(
        val dependencies: List<String>
    ) : ModificationStep()

    class GenerateCodeStep(
        val filesNames: List<String>,
        val model: Map<String, Any>,
        val dirName: String?
    ) : ModificationStep()

    // TODO (not yet not yet) something meaningful will be here
    class ExistingFiles : ModificationStep()

    class NotificationStep(
        val message: String
    ) : ModificationStep()
}

enum class TypeClassToModification {
    APP, MANIFEST
}