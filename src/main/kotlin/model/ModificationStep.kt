package model


sealed class ModificationStep {

    class DependenciesStep(val dependencies: List<String>) : ModificationStep()

    class GenerateCodeStep(
        val files: List<FileModel>,
        val dirName: String?
    ) : ModificationStep()

    // TODO (not yet not yet) something meaningful will be here
    class ExistingFiles : ModificationStep()

    class NotificationStep(val message: String) : ModificationStep()

    class OpenInEditorFiles(
        val fileTypes: List<OpenInEditorFileType> = listOf(OpenInEditorFileType.BUILD_GRADLE_APP)
    ) : ModificationStep()
}

enum class OpenInEditorFileType {
    BUILD_GRADLE_APP, BUILD_GRADLE_PROJECT, MANIFEST,
}

enum class TypeClassToModification {
    APP, MANIFEST
}