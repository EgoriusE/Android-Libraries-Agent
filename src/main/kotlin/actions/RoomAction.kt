package actions

import com.intellij.openapi.actionSystem.AnActionEvent
import constants.*
import core.ActionHandler
import model.FileModel
import model.ModificationModel
import model.ModificationStep
import utils.extensions.DOT
import utils.extensions.getPackageName

class RoomAction : BaseAction() {

    override fun actionPerformed(e: AnActionEvent) {
        super.actionPerformed(e)

        if (project != null && module != null) {
            val templateModel = mapOf(ROOM_PACKAGE_NAME to module!!.getPackageName() + Char.DOT + ROOM_FOLDER_NAME)
            val dataModel = ModificationModel(
                steps = listOf(
                    ModificationStep.DependenciesStep(
                        dependencies = listOf(
                            ROOM_D_KTX,
                            ROOM_D_RUNTIME
                        ),
                        versionName = ROOM_VERSION
                    ),
                    ModificationStep.GenerateCodeStep(
                        files = listOf(
                            FileModel(ROOM_TEMPLATE_DAO, templateModel),
                            FileModel(ROOM_TEMPLATE_DATABASE, templateModel, true),
                            FileModel(ROOM_TEMPLATE_ENTITY, templateModel)
                        ),
                        dirName = ROOM_FOLDER_NAME
                    ),
                    ModificationStep.NotificationStep(
                        message = "Surprise! Room lib added!"
                    ),
                ModificationStep.OpenInEditorFiles()
                ),
                module = module!!
            )
            ActionHandler.getInstance(project!!).handle(dataModel)
        }
    }
}
