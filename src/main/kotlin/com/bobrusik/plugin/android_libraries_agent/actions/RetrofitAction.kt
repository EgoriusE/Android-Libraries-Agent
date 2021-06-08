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

class RetrofitAction : BaseAction() {

    override fun actionPerformed(e: AnActionEvent) {
        super.actionPerformed(e)

        if (project != null && module != null) {
            val templateModel = mapOf(PACKAGE_KEY to module!!.getPackageName() + Char.DOT + RETROFIT_F_NAME)

            val dataModel = ModificationModel(
                steps = listOf(
                    ModificationStep.GradleModificationStep.DependencyModification(
                        moduleDependencies = listOf(
                            DependencyModel(
                                name = RETROFIT_D,
                                version = RETROFIT_VERSION,
                                componentName = IMPLEMENTATION_CONFIG_NAME
                            ),
                        ),
                    ),
                    ModificationStep.GenerateCodeStep(
                        files = listOf(
                            FileModel(
                                name = RETROFIT_T_API,
                                templateModel = templateModel,
                                isOpenInEditor = true
                            ),
                        ),
                        dirName = RETROFIT_F_NAME
                    ),
                    ModificationStep.NotificationStep(
                        message = RETROFIT_N_MSG
                    )
                ),
                module = module!!
            )

            ActionHandler.getInstance(project!!).handle(dataModel)
        }
    }
}