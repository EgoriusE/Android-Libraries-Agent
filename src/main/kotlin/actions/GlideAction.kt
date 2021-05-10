package actions

import com.intellij.openapi.actionSystem.AnActionEvent
import constants.*
import constants.CodeGeneratorConstants.ANNOTATION_PROCESSOR_CONFIG_NAME
import constants.CodeGeneratorConstants.IMPLEMENTATION_CONFIG_NAME
import core.ActionHandler
import model.DependencyModel
import model.FileModel
import model.ModificationModel
import model.ModificationStep
import utils.extensions.DOT
import utils.extensions.getPackageName

class GlideAction : BaseAction() {

    override fun actionPerformed(e: AnActionEvent) {
        super.actionPerformed(e)
        val templateModel = mapOf(PACKAGE_KEY to module!!.getPackageName() + Char.DOT + GLIDE_F_NAME)

        if (project != null && module != null) {
            val dataModel = ModificationModel(
                steps = listOf(
                    ModificationStep.GradleModificationStep.DependencyModification(
                        moduleDependencies = listOf(
                            DependencyModel(
                                name = GLIDE_D,
                                version = GLIDE_VERSION,
                                componentName = IMPLEMENTATION_CONFIG_NAME
                            ),
                            DependencyModel(
                                name = GLIDE_D_COMPILER,
                                version = GLIDE_VERSION,
                                componentName = ANNOTATION_PROCESSOR_CONFIG_NAME
                            ),
                        ),
                    ),
                    ModificationStep.GenerateCodeStep(
                        listOf(
                            FileModel(GLIDE_T_EXAMPLE, templateModel, true)
                        ),
                        GLIDE_F_NAME
                    ),
                    ModificationStep.NotificationStep(
                        message = GLIDE_N_MSG
                    )
                ),
                module = module!!
            )

            ActionHandler.getInstance(project!!).handle(dataModel)
        }
    }
}