package com.bobrusik.plugin.android_libraries_agent.actions

import com.bobrusik.plugin.android_libraries_agent.constants.*
import com.bobrusik.plugin.android_libraries_agent.core.ActionHandler
import com.bobrusik.plugin.android_libraries_agent.extensions.DOT
import com.bobrusik.plugin.android_libraries_agent.extensions.getPackageName
import com.bobrusik.plugin.android_libraries_agent.model.DependencyModel
import com.bobrusik.plugin.android_libraries_agent.model.FileModel
import com.bobrusik.plugin.android_libraries_agent.model.ModificationModel
import com.bobrusik.plugin.android_libraries_agent.model.ModificationStep
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service


class DatastoreAction : BaseAction() {

    override fun actionPerformed(e: AnActionEvent) {
        super.actionPerformed(e)

        if (project != null && module != null) {
            val templateModel = mapOf(PACKAGE_KEY to module!!.getPackageName() + Char.DOT + DATASTORE_F_NAME)
            val dataModel = ModificationModel(
                steps = listOf(
                    ModificationStep.GradleModificationStep.DependencyModification(
                        moduleDependencies = listOf(
                            DependencyModel(
                                name = DATASTORE_D_PROJECT,
                                version = DATASTORE_VERSION,
                                componentName = CodeGeneratorConstants.IMPLEMENTATION_CONFIG_NAME
                            ),
                            DependencyModel(
                                name = DATASTORE_D_RX_JAVA2,
                                version = DATASTORE_VERSION,
                                componentName = CodeGeneratorConstants.IMPLEMENTATION_CONFIG_NAME
                            ),
                            DependencyModel(
                                name = DATASTORE_D_RX_JAVA3,
                                version = DATASTORE_VERSION,
                                componentName = CodeGeneratorConstants.IMPLEMENTATION_CONFIG_NAME
                            )
                        )
                    ),
                    ModificationStep.GenerateCodeStep(
                        files = listOf(
                            FileModel(DATASTORE_T_EXAMPLE, templateModel, true),
                        ),
                        dirName = DATASTORE_F_NAME
                    ),
                    ModificationStep.NotificationStep(
                        message = DATASTORE_N_MSG
                    ),
                    ModificationStep.OpenInEditorFiles()
                ),
                module = module!!
            )

            project!!.service<ActionHandler>().handle(dataModel)
        }
    }
}
