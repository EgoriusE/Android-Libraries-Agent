package actions

import com.intellij.openapi.actionSystem.AnActionEvent
import constants.*
import constants.CodeGeneratorConstants.CLASSPATH_CONFIG_NAME
import constants.CodeGeneratorConstants.IMPLEMENTATION_CONFIG_NAME
import core.ActionHandler
import model.DependencyModel
import model.ModificationModel
import model.ModificationStep
import model.OpenInEditorFileType

class FirebaseAction : BaseAction() {

    override fun actionPerformed(e: AnActionEvent) {
        super.actionPerformed(e)
        if (project != null && module != null) {

            val dataModel = ModificationModel(
                steps = listOf(
                    ModificationStep.GradleModificationStep.DependencyModification(
                        moduleDependencies = listOf(
                            DependencyModel(
                                name = FIREBASE_D_MODULE,
                                version = FIREBASE_VERSION_MODULE,
                                componentName = IMPLEMENTATION_CONFIG_NAME
                            )
                        ),
                        projectDependencies = listOf(
                            DependencyModel(
                                name = FIREBASE_D_PROJECT,
                                version = FIREBASE_VERSION_PROJECT,
                                componentName = CLASSPATH_CONFIG_NAME
                            )
                        )
                    ),
                    ModificationStep.GradleModificationStep.PluginModification(
                        modulePlugins = listOf(FIREBASE_P_MODULE)
                    ),
                    ModificationStep.CopyJsonToProjectStep,
                    ModificationStep.OpenInEditorFiles(listOf(OpenInEditorFileType.BUILD_GRADLE_APP)),
                    ModificationStep.NotificationStep(
                        message = FIREBASE_N_MSG
                    )
                ),
                module = module!!
            )

            ActionHandler.getInstance(project!!).handle(dataModel)
        }
    }
}