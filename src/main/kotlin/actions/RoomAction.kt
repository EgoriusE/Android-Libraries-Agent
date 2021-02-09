package actions

import com.intellij.openapi.actionSystem.AnActionEvent
import constants.room.RoomDependenciesConstants
import constants.room.RoomTemplateConstants
import core.ActionHandler
import extensions.DOT
import extensions.getPackageName
import model.ModificationModel
import model.ModificationStep

class RoomAction : LibraryAction() {

    override fun actionPerformed(e: AnActionEvent) {
        super.actionPerformed(e)

        println("actions.RoomAction actionPerformed")

        if (project != null && module != null) {
            val dataModel = ModificationModel(
                steps = listOf(
                    ModificationStep.DependenciesStep(
                        dependencies = listOf(
                            RoomDependenciesConstants.D_KTX,
                            RoomDependenciesConstants.D_RUNTIME
                        )
                    ),
                    ModificationStep.BoilerPlateStep(
                        filesNames = listOf(
                            RoomTemplateConstants.DAO_TEMPLATE,
                            RoomTemplateConstants.DATABASE_TEMPLATE,
                            RoomTemplateConstants.ENTITY_TEMPLATE,
                        ),
                        model = mapOf(
                            RoomTemplateConstants.MODEL_PARAM_PACKAGE_NAME
                                to module!!.getPackageName() + Char.DOT + RoomTemplateConstants.FOLDER
                        ),
                        dirName = RoomTemplateConstants.FOLDER
                    )
                ),
                module = module!!
            )
            ActionHandler(project!!).handle(dataModel)
        }
    }
}
