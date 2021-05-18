package core

import com.android.tools.idea.gradle.dsl.api.ProjectBuildModel
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import execRunWriteAction
import model.ModificationModel
import model.ModificationStep

@Service
class GradleModificationProcessor(
    private val project: Project
) {
    private val projectGradleBuildModel = ProjectBuildModel.get(project).projectBuildModel
    private val gradleDependenciesManager = GradleDependenciesManager()

    fun process(
        step: ModificationStep.GradleModificationStep,
        model: ModificationModel
    ) {
        val buildModuleModel = ProjectBuildModel.get(project).getModuleBuildModel(model.module)

        when (step) {
            is ModificationStep.GradleModificationStep.DependencyModification -> {

                if (buildModuleModel != null && step.moduleDependencies.isNotEmpty()) {
                    gradleDependenciesManager.addDependencies(
                        buildModel = buildModuleModel,
                        dependencies = step.moduleDependencies
                    )
                }

                if (projectGradleBuildModel != null && step.projectDependencies.isNotEmpty()) {
                    gradleDependenciesManager.addDependencies(
                        buildModel = projectGradleBuildModel.buildscript(),
                        dependencies = step.projectDependencies
                    )
                    execRunWriteAction { projectGradleBuildModel.applyChanges() }
                }
            }
            is ModificationStep.GradleModificationStep.PluginModification -> {
                if (buildModuleModel != null && step.modulePlugins.isNotEmpty()) {
                    gradleDependenciesManager.addPlugins(buildModuleModel, step.modulePlugins)
                }
            }

        }
    }
}