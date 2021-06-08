package com.bobrusik.plugin.android_libraries_agent.model

sealed class ModificationStep {

    sealed class GradleModificationStep : ModificationStep() {

        class DependencyModification(
            val moduleDependencies: List<DependencyModel> = emptyList(),
            val projectDependencies: List<DependencyModel> = emptyList(),
        ) : GradleModificationStep()

        class PluginModification(
            val modulePlugins: List<String> = emptyList(),
            val projectPlugins: List<String> = emptyList(),
        ) : GradleModificationStep()

    }

    class GenerateCodeStep(
        val files: List<FileModel>,
        val dirName: String?
    ) : ModificationStep()

    // TODO ()
    class ExistingFiles : ModificationStep()

    class NotificationStep(val message: String) : ModificationStep()

    class OpenInEditorFiles(
        val fileTypes: List<OpenInEditorFileType> = listOf(OpenInEditorFileType.BUILD_GRADLE_APP)
    ) : ModificationStep()

    object CopyJsonToProjectStep : ModificationStep()
}

enum class OpenInEditorFileType {
    BUILD_GRADLE_APP, BUILD_GRADLE_PROJECT, MANIFEST
}