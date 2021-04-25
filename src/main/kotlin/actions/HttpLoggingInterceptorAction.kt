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

class HttpLoggingInterceptorAction : BaseAction() {

    override fun actionPerformed(e: AnActionEvent) {
        super.actionPerformed(e)

        if (project != null && module != null) {

            val templateModel = mapOf(PACKAGE_KEY to module!!.getPackageName() + Char.DOT + LOG_INTERCEPTOR_F_NAME)

            val dataModel = ModificationModel(
                steps = listOf(
                    ModificationStep.GradleModificationStep.DependencyModification(
                        moduleDependencies = listOf(
                            DependencyModel(
                                name = LOG_INTERCEPTOR_D,
                                version = LOG_INTERCEPTOR_VERSION,
                                componentName = IMPLEMENTATION_CONFIG_NAME
                            ),
                        )
                    ),
                    ModificationStep.GenerateCodeStep(
                        files = listOf(
                            FileModel(LOG_INTERCEPTOR_T, templateModel, true),
                        ),
                        dirName = LOG_INTERCEPTOR_F_NAME
                    ),
                    ModificationStep.NotificationStep(
                        message = LOG_INTERCEPTOR_N_MSG
                    )
                ),
                module = module!!
            )

            ActionHandler.getInstance(project!!).handle(dataModel)
        }
    }
}
