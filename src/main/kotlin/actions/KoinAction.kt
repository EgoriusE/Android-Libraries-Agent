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

class KoinAction : BaseAction() {

    override fun actionPerformed(e: AnActionEvent) {
        super.actionPerformed(e)
        val templateModel = mapOf(PACKAGE_KEY to module!!.getPackageName() + Char.DOT + KOIN_F_NAME)

        if (project != null && module != null) {
            val dataModel = ModificationModel(
                steps = listOf(
                    ModificationStep.GradleModificationStep.DependencyModification(
                        moduleDependencies = listOf(
                            DependencyModel(
                                name = KOIN_D_SCOPE,
                                version = KOIN_VERSION,
                                componentName = IMPLEMENTATION_CONFIG_NAME
                            ),
                            DependencyModel(
                                name = KOIN_D_CORE,
                                version = KOIN_VERSION,
                                componentName = IMPLEMENTATION_CONFIG_NAME
                            ),
                            DependencyModel(
                                name = KOIN_D_VIEW_MODEL,
                                version = KOIN_VERSION,
                                componentName = IMPLEMENTATION_CONFIG_NAME
                            )
                        )
                    ),
                    ModificationStep.GenerateCodeStep(
                        files = listOf(
                            FileModel(
                                name = KOIN_T,
                                templateModel = templateModel,
                                isOpenInEditor = true
                            ),
                        ),
                        dirName = KOIN_F_NAME
                    ),
                    ModificationStep.NotificationStep(
                        message = KOIN_N_MSG
                    )
                ),
                module = module!!
            )
            ActionHandler.getInstance(project!!).handle(dataModel)
        }
    }
}