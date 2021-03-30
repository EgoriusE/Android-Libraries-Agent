package core

import com.android.tools.idea.gradle.dsl.api.GradleBuildModel
import com.android.tools.idea.gradle.dsl.api.GradleModelProvider
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import constants.CodeGeneratorConstants.BUILD_GRADLE_FILE_NAME
import execRunWriteAction
import model.ModificationModel
import model.ModificationStep
import model.OpenInEditorFileType
import openInEditor
import services.NotificationsFactory
import services.PackageHelper

@Service
class ActionHandler(
    private val project: Project
) {

    private val gradleDependenciesManager = GradleDependenciesManager()
    private val notificationFactory by lazy { NotificationsFactory.getInstance(project) }

    companion object {
        fun getInstance(project: Project): ActionHandler = project.service()
    }

    fun handle(model: ModificationModel) {
        val packageHelper = PackageHelper(model.module)

        model.steps.forEach { step ->
            when (step) {

                is ModificationStep.DependenciesStep -> {
                    val buildModel: GradleBuildModel? = GradleModelProvider.get().getBuildModel(model.module)
                    if (buildModel != null) {
                        gradleDependenciesManager.addDependencies(buildModel, step.dependencies)
                    }
                }

                is ModificationStep.GenerateCodeStep -> {
                    val generatedPackage =
                        if (step.dirName != null) {
                            packageHelper.generatePackage(step.dirName)
                        } else {
                            packageHelper.getPackageDir()
                        }

                    val templateGenerator = TemplateGenerator(project)
                    val generatedFiles = templateGenerator.generateFiles(step.files)

                    execRunWriteAction {
                        generatedFiles.forEach { fileModel ->
                            val addedPsiElement = generatedPackage?.add(fileModel.psiFile)
                            if (fileModel.isOpenInEditor) {
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

                is ModificationStep.OpenInEditorFiles -> {
                    step.fileTypes.forEach { type ->
                        when (type) {
                            OpenInEditorFileType.BUILD_GRADLE_APP -> {
                                val moduleDir = packageHelper.getModulePackage()
                                val buildFile = moduleDir?.findFile(BUILD_GRADLE_FILE_NAME)
                                buildFile?.openInEditor()
                            }
                            else -> {
                            } // TODO (not implemented yet)
                        }
                    }
                }

            }
        }
    }
}