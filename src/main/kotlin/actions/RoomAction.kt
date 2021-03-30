package actions

import com.intellij.openapi.actionSystem.AnActionEvent
import constants.*
import core.ActionHandlerService
import utils.extensions.DOT
import utils.extensions.getPackageName
import model.ModificationModel
import model.ModificationStep

class RoomAction : BaseAction() {

    override fun actionPerformed(e: AnActionEvent) {
        super.actionPerformed(e)

        if (project != null && module != null) {
            val dataModel = ModificationModel(
                steps = listOf(
                    ModificationStep.DependenciesStep(
                        dependencies = listOf(
                            ROOM_D_KTX,
                            ROOM_D_RUNTIME
                        )
                    ),
                    ModificationStep.GenerateCodeStep(
                        filesNames = listOf(
                            ROOM_TEMPLATE_DAO,
                            ROOM_TEMPLATE_DATABASE,
                            ROOM_TEMPLATE_ENTITY,
                        ),
                        model = mapOf(ROOM_PACKAGE_NAME to module!!.getPackageName() + Char.DOT + ROOM_FOLDER_NAME),
                        dirName = ROOM_FOLDER_NAME
                    ),
                    ModificationStep.NotificationStep(
                        message = "Surprise! Room lib added!"
                    )
                ),
                module = module!!
            )
            ActionHandlerService.getInstance(project!!).handle(dataModel)
        }
    }
}
