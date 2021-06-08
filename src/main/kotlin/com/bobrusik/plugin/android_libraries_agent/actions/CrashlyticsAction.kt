package com.bobrusik.plugin.android_libraries_agent.actions

import com.bobrusik.plugin.android_libraries_agent.constants.*
import com.bobrusik.plugin.android_libraries_agent.constants.CodeGeneratorConstants.CLASSPATH_CONFIG_NAME
import com.bobrusik.plugin.android_libraries_agent.constants.CodeGeneratorConstants.IMPLEMENTATION_CONFIG_NAME
import com.bobrusik.plugin.android_libraries_agent.core.ActionHandler
import com.bobrusik.plugin.android_libraries_agent.model.DependencyModel
import com.bobrusik.plugin.android_libraries_agent.model.ModificationModel
import com.bobrusik.plugin.android_libraries_agent.model.ModificationStep
import com.bobrusik.plugin.android_libraries_agent.model.OpenInEditorFileType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service

class CrashlyticsAction : BaseAction() {

    override fun actionPerformed(e: AnActionEvent) {
        super.actionPerformed(e)
        if (project != null && module != null) {

            val dataModel = ModificationModel(
                steps = listOf(
                    ModificationStep.GradleModificationStep.DependencyModification(
                        moduleDependencies = listOf(
                            DependencyModel(
                                name = FIREBASE_D_MODULE,
                                version = FIREBASE_VERSION_MODULE,
                                componentName = IMPLEMENTATION_CONFIG_NAME
                            ),
                            DependencyModel(
                                name = CRASHLYTICS_D_MODULE,
                                version = CRASHLYTICS_VERSION_MODULE_1,
                                componentName = IMPLEMENTATION_CONFIG_NAME
                            ),
                            DependencyModel(
                                name = CRASHLYTICS_D_MODULE_2,
                                version = CRASHLYTICS_VERSION_MODULE_2,
                                componentName = IMPLEMENTATION_CONFIG_NAME
                            )
                        ),
                        projectDependencies = listOf(
                            DependencyModel(
                                name = FIREBASE_D_PROJECT,
                                version = FIREBASE_VERSION_PROJECT,
                                componentName = CLASSPATH_CONFIG_NAME
                            ),
                            DependencyModel(
                                name = CRASHLYTICS_D_PROJECT,
                                version = CRASHLYTICS_VERSION_PROJECT,
                                componentName = CLASSPATH_CONFIG_NAME
                            ),
                        )
                    ),
                    ModificationStep.GradleModificationStep.PluginModification(
                        modulePlugins = listOf(FIREBASE_P_MODULE, CRASHLYTICS_P_MODULE)
                    ),
                    ModificationStep.CopyJsonToProjectStep,
                    ModificationStep.OpenInEditorFiles(listOf(OpenInEditorFileType.BUILD_GRADLE_APP)),
                    ModificationStep.NotificationStep(
                        message = FIREBASE_N_MSG
                    )
                ),
                module = module!!
            )

            project!!.service<ActionHandler>().handle(dataModel)
        }
    }
}