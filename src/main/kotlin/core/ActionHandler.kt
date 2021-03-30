package core

import com.android.tools.idea.gradle.dsl.api.GradleBuildModel
import com.android.tools.idea.gradle.dsl.api.GradleModelProvider
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import execRunWriteAction
import model.ModificationModel
import model.ModificationStep
import openInEditor
import services.NotificationsFactory

@Service
class ActionHandler(
    private val project: Project
) {

    private val gradleDependenciesManager = GradleDependenciesManager()
    private val notificationFactory by lazy {
        NotificationsFactory.getInstance(project)
    }

    companion object {
        fun getInstance(project: Project): ActionHandler = project.service()
    }

    fun handle(model: ModificationModel) {
        model.steps.forEach { step ->
            when (step) {

                is ModificationStep.DependenciesStep -> {
                    val buildModel: GradleBuildModel? = GradleModelProvider.get().getBuildModel(model.module)
                    if (buildModel != null) {
                        gradleDependenciesManager.addDependencies(buildModel, step.dependencies)
                    }
                }

                is ModificationStep.GenerateCodeStep -> {
                    val dirHelper = DirHelper(model.module)
                    val boilerPlateDir =
                        if (step.dirName != null) {
                            dirHelper.generateDir(step.dirName)
                        } else {
                            dirHelper.getPackageDir()
                        }

                    val templateGenerator = TemplateGenerator(project)
                    val generatedFiles = templateGenerator.generateFiles(step.files)

                    execRunWriteAction {
                        generatedFiles.forEach { fileModel ->
                            val addedPsiElement = boilerPlateDir?.add(fileModel.psiFile)
                            if(fileModel.isOpenInEditor) {
                                addedPsiElement?.openInEditor()
                            }
                        }
                    }
                }

                is ModificationStep.ExistingFiles -> {
                    SrcModifier(project).modify(model)
                }

                is ModificationStep.NotificationStep -> {
                    notificationFactory.info(step.message)
                }
            }
        }
    }
}