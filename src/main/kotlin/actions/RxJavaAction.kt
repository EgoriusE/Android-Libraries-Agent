package actions

import com.intellij.openapi.actionSystem.AnActionEvent
import constants.*
import constants.CodeGeneratorConstants.IMPLEMENTATION_CONFIG_NAME
import core.ActionHandler
import model.DependencyModel
import model.ModificationModel
import model.ModificationStep
import utils.extensions.DOT
import utils.extensions.getPackageName

class RxJavaAction : BaseAction() {

    override fun actionPerformed(e: AnActionEvent) {
        super.actionPerformed(e)

        if (project != null && module != null) {

            val templateModel = mapOf(PACKAGE_KEY to module!!.getPackageName() + Char.DOT + RXJAVA_F_NAME)

            val dataModel = ModificationModel(
                steps = listOf(
                    ModificationStep.GradleModificationStep.DependencyModification(
                        moduleDependencies = listOf(
                            DependencyModel(
                                name = RXJAVA_D_1,
                                version = RXJAVA_VERSION,
                                componentName = IMPLEMENTATION_CONFIG_NAME
                            ),
                            DependencyModel(
                                name = RXJAVA_D_2,
                                version = RXJAVA_VERSION,
                                componentName = IMPLEMENTATION_CONFIG_NAME
                            )
                        )
                    ),
                    ModificationStep.NotificationStep(
                        message = RXJAVA_N_MSG
                    )
                ),
                module = module!!
            )

            ActionHandler.getInstance(project!!).handle(dataModel)
        }
    }
}
