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

class GsonAction : BaseAction() {

    override fun actionPerformed(e: AnActionEvent) {
        super.actionPerformed(e)
        val templateModel = mapOf(PACKAGE_KEY to module!!.getPackageName() + Char.DOT + GSON_F_NAME)

        if (project != null && module != null) {
            val dataModel = ModificationModel(
                steps = listOf(
                    ModificationStep.GradleModificationStep.DependencyModification(
                        moduleDependencies = listOf(
                            DependencyModel(
                                name = GSON_D,
                                version = GSON_VERSION,
                                componentName = IMPLEMENTATION_CONFIG_NAME
                            ),
                            DependencyModel(
                                name = GSON_D_2,
                                version = GSON_VERSION_2,
                                componentName = IMPLEMENTATION_CONFIG_NAME
                            ),
                        ),
                    ),
                    ModificationStep.GenerateCodeStep(
                        files = listOf(
                            FileModel(GSON_T_EXAMPLE, templateModel, true)
                        ),
                        dirName = GSON_F_NAME
                    ),
                    ModificationStep.NotificationStep(
                        message = GSON_N_MSG
                    )
                ),
                module = module!!
            )

            ActionHandler.getInstance(project!!).handle(dataModel)
        }
    }
}