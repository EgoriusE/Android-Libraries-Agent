package actions

import com.intellij.openapi.actionSystem.AnActionEvent
import constants.*
import core.ActionHandler
import model.DependencyModel
import model.FileModel
import model.ModificationModel
import model.ModificationStep
import utils.extensions.DOT
import utils.extensions.getPackageName


class DatastoreAction : BaseAction() {

    override fun actionPerformed(e: AnActionEvent) {
        super.actionPerformed(e)

        if (project != null && module != null) {
            val templateModel = mapOf(PACKAGE_KEY to module!!.getPackageName() + Char.DOT + DATASTORE_F_NAME)
            val dataModel = ModificationModel(
                steps = listOf(
                    ModificationStep.GradleModificationStep.DependencyModification(
                        moduleDependencies = listOf(
                            DependencyModel(
                                name = DATASTORE_D_PROJECT,
                                version = DATASTORE_VERSION,
                                componentName = CodeGeneratorConstants.IMPLEMENTATION_CONFIG_NAME
                            ),
                            DependencyModel(
                                name = DATASTORE_D_RX_JAVA2,
                                version = DATASTORE_VERSION,
                                componentName = CodeGeneratorConstants.IMPLEMENTATION_CONFIG_NAME
                            ),
                            DependencyModel(
                                name = DATASTORE_D_RX_JAVA3,
                                version = DATASTORE_VERSION,
                                componentName = CodeGeneratorConstants.IMPLEMENTATION_CONFIG_NAME
                            )
                        )
                    ),
                    ModificationStep.GenerateCodeStep(
                        files = listOf(
                            FileModel(DATASTORE_T_EXAMPLE, templateModel, true),
                        ),
                        dirName = DATASTORE_F_NAME
                    ),
                    ModificationStep.NotificationStep(
                        message = DATASTORE_N_MSG
                    ),
                    ModificationStep.OpenInEditorFiles()
                ),
                module = module!!
            )

            ActionHandler.getInstance(project!!).handle(dataModel)
        }
    }
}
