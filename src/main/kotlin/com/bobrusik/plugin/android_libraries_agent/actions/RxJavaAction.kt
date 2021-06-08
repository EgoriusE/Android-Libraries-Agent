package com.bobrusik.plugin.android_libraries_agent.actions

import com.bobrusik.plugin.android_libraries_agent.constants.*
import com.bobrusik.plugin.android_libraries_agent.constants.CodeGeneratorConstants.IMPLEMENTATION_CONFIG_NAME
import com.bobrusik.plugin.android_libraries_agent.core.ActionHandler
import com.bobrusik.plugin.android_libraries_agent.extensions.DOT
import com.bobrusik.plugin.android_libraries_agent.extensions.getPackageName
import com.bobrusik.plugin.android_libraries_agent.model.DependencyModel
import com.bobrusik.plugin.android_libraries_agent.model.ModificationModel
import com.bobrusik.plugin.android_libraries_agent.model.ModificationStep
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service

class RxJavaAction : BaseAction() {

    override fun actionPerformed(e: AnActionEvent) {
        super.actionPerformed(e)

        if (project != null && module != null) {

            // TODO: Add template
            val templateModel = mapOf(PACKAGE_KEY to module!!.getPackageName() + Char.DOT + RXJAVA_F_NAME)

            val dataModel = ModificationModel(
                steps = listOf(
                    ModificationStep.GradleModificationStep.DependencyModification(
                        moduleDependencies = listOf(
                            DependencyModel(
                                name = RXJAVA_D_1,
                                version = RXJAVA_VERSION,
                                componentName = IMPLEMENTATION_CONFIG_NAME
                            ),
                            DependencyModel(
                                name = RXJAVA_D_2,
                                version = RXJAVA_VERSION,
                                componentName = IMPLEMENTATION_CONFIG_NAME
                            )
                        )
                    ),
                    ModificationStep.NotificationStep(
                        message = RXJAVA_N_MSG
                    )
                ),
                module = module!!
            )

            project!!.service<ActionHandler>().handle(dataModel)
        }
    }
}
