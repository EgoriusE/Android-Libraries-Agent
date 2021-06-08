package com.bobrusik.plugin.android_libraries_agent.core

import com.android.tools.idea.gradle.dsl.api.ProjectBuildModel
import com.bobrusik.plugin.android_libraries_agent.model.ModificationModel
import com.bobrusik.plugin.android_libraries_agent.model.ModificationStep
import com.intellij.openapi.components.Service
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import execRunWriteAction

@Service
class GradleModificationProcessor(
    private val project: Project
) {

    private var projectGradleBuildModel: ProjectBuildModel? = null

    private val gradleDependenciesManager = GradleDependenciesManager()

    fun process(
        step: ModificationStep.GradleModificationStep,
        model: ModificationModel
    ) {

        ProgressManager
            .getInstance()
            .run(object : Task.Modal(project, "Loading Gradle Model", false) {

                override fun onFinished() {
                    super.onFinished()
                    complete(model, step)
                }

                override fun run(indicator: ProgressIndicator) {
                    indicator.text = "Loading Gradle Model"
                    indicator.fraction = 0.0
                    projectGradleBuildModel = ProjectBuildModel.get(project)
                    indicator.fraction = 1.0
                }
            })

    }

    private fun complete(
        model: ModificationModel,
        step: ModificationStep.GradleModificationStep
    ) {
        val buildModuleModel = projectGradleBuildModel?.getModuleBuildModel(model.module)
        val projectBuildScript = projectGradleBuildModel?.projectBuildModel?.buildscript()

        when (step) {
            is ModificationStep.GradleModificationStep.DependencyModification -> {

                if (buildModuleModel != null && step.moduleDependencies.isNotEmpty()) {
                    gradleDependenciesManager.addDependencies(
                        buildModel = buildModuleModel,
                        dependencies = step.moduleDependencies
                    )
                }

                if (projectBuildScript != null && step.projectDependencies.isNotEmpty()) {
                    gradleDependenciesManager.addDependencies(
                        buildModel = projectBuildScript,
                        dependencies = step.projectDependencies
                    )
                    execRunWriteAction { projectGradleBuildModel?.applyChanges() }
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