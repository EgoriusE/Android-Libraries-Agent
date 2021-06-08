package com.bobrusik.plugin.android_libraries_agent.actions

import com.bobrusik.plugin.android_libraries_agent.constants.*
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

class KoinAction : BaseAction() {

    override fun actionPerformed(e: AnActionEvent) {
        super.actionPerformed(e)
        val templateModel = mapOf(PACKAGE_KEY to module!!.getPackageName() + Char.DOT + KOIN_F_NAME)

        if (project != null && module != null) {
            val dataModel = ModificationModel(
                steps = listOf(
                    ModificationStep.GradleModificationStep.DependencyModification(
                        moduleDependencies = listOf(
                            DependencyModel(
                                name = KOIN_D_SCOPE,
                                version = KOIN_VERSION,
                                componentName = IMPLEMENTATION_CONFIG_NAME
                            ),
                            DependencyModel(
                                name = KOIN_D_CORE,
                                version = KOIN_VERSION,
                                componentName = IMPLEMENTATION_CONFIG_NAME
                            ),
                            DependencyModel(
                                name = KOIN_D_VIEW_MODEL,
                                version = KOIN_VERSION,
                                componentName = IMPLEMENTATION_CONFIG_NAME
                            )
                        )
                    ),
                    ModificationStep.GenerateCodeStep(
                        files = listOf(
                            FileModel(
                                name = KOIN_T,
                                templateModel = templateModel,
                                isOpenInEditor = true
                            ),
                        ),
                        dirName = KOIN_F_NAME
                    ),
                    ModificationStep.NotificationStep(
                        message = KOIN_N_MSG
                    )
                ),
                module = module!!
            )

            project!!.service<ActionHandler>().handle(dataModel)
        }
    }
}