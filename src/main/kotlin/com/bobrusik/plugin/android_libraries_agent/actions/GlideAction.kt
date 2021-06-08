package com.bobrusik.plugin.android_libraries_agent.actions

import com.bobrusik.plugin.android_libraries_agent.constants.*
import com.bobrusik.plugin.android_libraries_agent.constants.CodeGeneratorConstants.ANNOTATION_PROCESSOR_CONFIG_NAME
import com.bobrusik.plugin.android_libraries_agent.constants.CodeGeneratorConstants.IMPLEMENTATION_CONFIG_NAME
import com.bobrusik.plugin.android_libraries_agent.core.ActionHandler
import com.bobrusik.plugin.android_libraries_agent.extensions.DOT
import com.bobrusik.plugin.android_libraries_agent.extensions.getPackageName
import com.bobrusik.plugin.android_libraries_agent.model.DependencyModel
import com.bobrusik.plugin.android_libraries_agent.model.FileModel
import com.bobrusik.plugin.android_libraries_agent.model.ModificationModel
import com.bobrusik.plugin.android_libraries_agent.model.ModificationStep
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service

class GlideAction : BaseAction() {

    override fun actionPerformed(e: AnActionEvent) {
        super.actionPerformed(e)
        val templateModel = mapOf(PACKAGE_KEY to module!!.getPackageName() + Char.DOT + GLIDE_F_NAME)

        if (project != null && module != null) {
            val dataModel = ModificationModel(
                steps = listOf(
                    ModificationStep.GradleModificationStep.DependencyModification(
                        moduleDependencies = listOf(
                            DependencyModel(
                                name = GLIDE_D,
                                version = GLIDE_VERSION,
                                componentName = IMPLEMENTATION_CONFIG_NAME
                            ),
                            DependencyModel(
                                name = GLIDE_D_COMPILER,
                                version = GLIDE_VERSION,
                                componentName = ANNOTATION_PROCESSOR_CONFIG_NAME
                            ),
                        ),
                    ),
                    ModificationStep.GenerateCodeStep(
                        listOf(
                            FileModel(GLIDE_T_EXAMPLE, templateModel, true)
                        ),
                        GLIDE_F_NAME
                    ),
                    ModificationStep.NotificationStep(
                        message = GLIDE_N_MSG
                    )
                ),
                module = module!!
            )

            project!!.service<ActionHandler>().handle(dataModel)
        }
    }
}