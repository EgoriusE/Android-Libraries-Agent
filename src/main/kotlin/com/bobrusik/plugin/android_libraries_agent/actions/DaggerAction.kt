package com.bobrusik.plugin.android_libraries_agent.actions

import com.bobrusik.plugin.android_libraries_agent.constants.*
import com.bobrusik.plugin.android_libraries_agent.constants.CodeGeneratorConstants.IMPLEMENTATION_CONFIG_NAME
import com.bobrusik.plugin.android_libraries_agent.constants.CodeGeneratorConstants.KAPT_CONFIG_NAME
import com.bobrusik.plugin.android_libraries_agent.core.ActionHandler
import com.bobrusik.plugin.android_libraries_agent.extensions.DOT
import com.bobrusik.plugin.android_libraries_agent.extensions.getPackageName
import com.bobrusik.plugin.android_libraries_agent.model.DependencyModel
import com.bobrusik.plugin.android_libraries_agent.model.FileModel
import com.bobrusik.plugin.android_libraries_agent.model.ModificationModel
import com.bobrusik.plugin.android_libraries_agent.model.ModificationStep
import com.intellij.openapi.actionSystem.AnActionEvent

class DaggerAction : BaseAction() {

    override fun actionPerformed(e: AnActionEvent) {
        super.actionPerformed(e)

        if (project != null && module != null) {

            val templateModel = mapOf(PACKAGE_KEY to module!!.getPackageName() + Char.DOT + DAGGER_F_NAME)

            val dataModel = ModificationModel(
                steps = listOf(
                    ModificationStep.GradleModificationStep.DependencyModification(
                        moduleDependencies = listOf(
                            DependencyModel(
                                name = DAGGER_D,
                                version = DAGGER_VERSION,
                                componentName = IMPLEMENTATION_CONFIG_NAME
                            ),
                            DependencyModel(
                                name = DAGGER_D_2,
                                version = DAGGER_VERSION,
                                componentName = IMPLEMENTATION_CONFIG_NAME
                            ),
                            DependencyModel(
                                name = DAGGER_D_COMPILER,
                                version = DAGGER_VERSION,
                                componentName = KAPT_CONFIG_NAME
                            ),
                            DependencyModel(
                                name = DAGGER_D_COMPILER_2,
                                version = DAGGER_VERSION,
                                componentName = KAPT_CONFIG_NAME
                            ),
                        )
                    ),
                    ModificationStep.GradleModificationStep.PluginModification(
                        modulePlugins = listOf(DAGGER_P_KAPT)
                    ),
                    ModificationStep.GenerateCodeStep(
                        files = listOf(
                            FileModel(DAGGER_T_COMPONENT, templateModel, true),
                            FileModel(DAGGER_T_MODULE, templateModel, true)
                        ),
                        dirName = DAGGER_F_NAME
                    ),
                    ModificationStep.NotificationStep(
                        message = DAGGER_N_MSG
                    )
                ),
                module = module!!
            )

            ActionHandler.getInstance(project!!).handle(dataModel)
        }
    }
}
