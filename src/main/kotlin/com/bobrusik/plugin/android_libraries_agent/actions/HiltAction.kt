package com.bobrusik.plugin.android_libraries_agent.actions

import com.bobrusik.plugin.android_libraries_agent.constants.*
import com.bobrusik.plugin.android_libraries_agent.constants.CodeGeneratorConstants.CLASSPATH_CONFIG_NAME
import com.bobrusik.plugin.android_libraries_agent.constants.CodeGeneratorConstants.IMPLEMENTATION_CONFIG_NAME
import com.bobrusik.plugin.android_libraries_agent.constants.CodeGeneratorConstants.KAPT_CONFIG_NAME
import com.bobrusik.plugin.android_libraries_agent.core.ActionHandler
import com.bobrusik.plugin.android_libraries_agent.model.DependencyModel
import com.bobrusik.plugin.android_libraries_agent.model.ModificationModel
import com.bobrusik.plugin.android_libraries_agent.model.ModificationStep
import com.bobrusik.plugin.android_libraries_agent.model.OpenInEditorFileType
import com.intellij.openapi.actionSystem.AnActionEvent

class HiltAction : BaseAction() {

    override fun actionPerformed(e: AnActionEvent) {
        super.actionPerformed(e)

        if (project != null && module != null) {
            val dataModel = ModificationModel(
                steps = listOf(
                    ModificationStep.GradleModificationStep.DependencyModification(
                        moduleDependencies = listOf(
                            DependencyModel(
                                name = HILT_D_COMPILER,
                                version = HILT_VERSION,
                                componentName = KAPT_CONFIG_NAME
                            ),
                            DependencyModel(
                                name = HILT_D_MODULE,
                                version = HILT_VERSION,
                                componentName = IMPLEMENTATION_CONFIG_NAME
                            )
                        ),
                        projectDependencies = listOf(
                            DependencyModel(
                                name = HILT_D_PROJECT,
                                version = HILT_VERSION,
                                componentName = CLASSPATH_CONFIG_NAME
                            )
                        )
                    ),
                    ModificationStep.GradleModificationStep.PluginModification(
                        modulePlugins = listOf(HILT_P, HILT_P_KAPT)
                    ),
                    ModificationStep.OpenInEditorFiles(
                        fileTypes = listOf(
                            OpenInEditorFileType.BUILD_GRADLE_PROJECT, OpenInEditorFileType.BUILD_GRADLE_APP
                        )
                    ),
                    ModificationStep.NotificationStep(
                        message = HILT_N_MSG
                    )
                ),
                module = module!!
            )
            ActionHandler.getInstance(project!!).handle(dataModel)
        }
    }
}