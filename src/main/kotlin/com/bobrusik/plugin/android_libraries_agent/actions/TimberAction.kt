package com.bobrusik.plugin.android_libraries_agent.actions

import com.bobrusik.plugin.android_libraries_agent.constants.CodeGeneratorConstants.IMPLEMENTATION_CONFIG_NAME
import com.bobrusik.plugin.android_libraries_agent.constants.TIMBER_D
import com.bobrusik.plugin.android_libraries_agent.constants.TIMBER_N_MSG
import com.bobrusik.plugin.android_libraries_agent.constants.TIMBER_VERSION
import com.bobrusik.plugin.android_libraries_agent.core.ActionHandler
import com.bobrusik.plugin.android_libraries_agent.model.DependencyModel
import com.bobrusik.plugin.android_libraries_agent.model.ModificationModel
import com.bobrusik.plugin.android_libraries_agent.model.ModificationStep
import com.bobrusik.plugin.android_libraries_agent.model.OpenInEditorFileType
import com.intellij.openapi.actionSystem.AnActionEvent

class TimberAction : BaseAction() {

    override fun actionPerformed(e: AnActionEvent) {
        super.actionPerformed(e)

        if (project != null && module != null) {
            val dataModel = ModificationModel(
                steps = listOf(
                    ModificationStep.GradleModificationStep.DependencyModification(
                        moduleDependencies = listOf(
                            DependencyModel(
                                name = TIMBER_D,
                                version = TIMBER_VERSION,
                                componentName = IMPLEMENTATION_CONFIG_NAME
                            ),
                        ),
                    ),
                    ModificationStep.OpenInEditorFiles(
                        fileTypes = listOf(OpenInEditorFileType.BUILD_GRADLE_APP)
                    ),
                    ModificationStep.NotificationStep(
                        message = TIMBER_N_MSG
                    )
                ),
                module = module!!
            )

            ActionHandler.getInstance(project!!).handle(dataModel)
        }
    }
}