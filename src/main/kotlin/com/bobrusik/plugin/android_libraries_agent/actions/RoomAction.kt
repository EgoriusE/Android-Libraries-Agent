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

class RoomAction : BaseAction() {

    override fun actionPerformed(e: AnActionEvent) {
        super.actionPerformed(e)

        if (project != null && module != null) {
            val templateModel = mapOf(PACKAGE_KEY to module!!.getPackageName() + Char.DOT + ROOM_F_NAME)
            val dataModel = ModificationModel(
                steps = listOf(
                    ModificationStep.GradleModificationStep.DependencyModification(
                        moduleDependencies = listOf(
                            DependencyModel(
                                name = ROOM_D_KTX,
                                version = ROOM_VERSION,
                                componentName = IMPLEMENTATION_CONFIG_NAME
                            ),
                            DependencyModel(
                                name = ROOM_D_KTX,
                                version = ROOM_VERSION,
                                componentName = IMPLEMENTATION_CONFIG_NAME
                            )
                        )
                    ),
                    ModificationStep.GenerateCodeStep(
                        files = listOf(
                            FileModel(ROOM_T_DAO, templateModel, true),
                            FileModel(ROOM_T_DATABASE, templateModel, true),
                            FileModel(ROOM_T_ENTITY, templateModel, true)
                        ),
                        dirName = ROOM_F_NAME
                    ),
                    ModificationStep.NotificationStep(
                        message = ROOM_N_MSG
                    )
                ),
                module = module!!
            )

            project!!.service<ActionHandler>().handle(dataModel)
        }
    }
}
