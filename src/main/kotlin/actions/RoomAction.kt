package actions

import com.intellij.openapi.actionSystem.AnActionEvent
import constants.*
import constants.CodeGeneratorConstants.IMPLEMENTATION_CONFIG_NAME
import core.ActionHandler
import model.DependencyModel
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
