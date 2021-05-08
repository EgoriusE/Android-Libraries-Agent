package core

import com.android.tools.idea.gradle.dsl.api.GradleBuildModel
import com.android.tools.idea.gradle.dsl.api.GradleModelProvider
import com.android.tools.idea.projectsystem.ProjectSystemSyncManager
import com.android.tools.idea.projectsystem.gradle.GradleProjectSystemSyncManager
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
import utils.extensions.canCreateFile

@Service
class ActionHandler(
    private val project: Project
) {
    private val projectGradleBuildModel: GradleBuildModel? = GradleModelProvider.get().getBuildModel(project)
    private val gradleDependenciesManager = GradleDependenciesManager()
    private val notificationFactory by lazy { NotificationsFactory.getInstance(project) }
    private val gradleProjectSystemSyncManager = GradleProjectSystemSyncManager(project)

    companion object {
        fun getInstance(project: Project): ActionHandler = project.service()
    }

    fun handle(model: ModificationModel) {
        val packageHelper = PackageHelper(model.module)

        model.steps.forEach { step ->
            when (step) {

                is ModificationStep.GradleModificationStep -> {
                    processGradleModificationStep(step, model)
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
                            generatedPackage?.let { dir ->
                                if (dir.canCreateFile(fileModel.name)) {
                                    val addedPsiElement = dir.add(fileModel.psiFile)
                                    if (fileModel.isOpenInEditor) {
                                        addedPsiElement?.openInEditor()
                                    }
                                } else {
                                    notificationFactory.error(
                                        "Failure to create template file ${fileModel.name}. A file with this name already exists."
                                    )
                                }
                            }
                        }
                    }
                }

//                is ModificationStep.ExistingFiles -> {
//                    SrcModifier(project).modify(model)
//                }

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
                            OpenInEditorFileType.BUILD_GRADLE_PROJECT -> {
                                val rootDir = packageHelper.rootDir
                                val buildFile = rootDir?.findFile(BUILD_GRADLE_FILE_NAME)
                                buildFile?.openInEditor()
                            }
                            else -> {
                            } // TODO (not implemented yet)
                        }
                    }
                }

                is ModificationStep.CopyJsonToProjectStep -> {
                    FirebaseHelper.execute(model.module)
                }
            }
        }

        gradleProjectSystemSyncManager.syncProject(ProjectSystemSyncManager.SyncReason.PROJECT_MODIFIED)
    }

    private fun processGradleModificationStep(
        step: ModificationStep.GradleModificationStep,
        model: ModificationModel
    ) {

        val buildModuleModel: GradleBuildModel? = GradleModelProvider.get().getBuildModel(model.module)

        when (step) {
            is ModificationStep.GradleModificationStep.DependencyModification -> {

                if (buildModuleModel != null && step.moduleDependencies.isNotEmpty()) {
                    gradleDependenciesManager.addDependencies(
                        buildModel = buildModuleModel,
                        dependencies = step.moduleDependencies
                    )
                }

                if (projectGradleBuildModel != null && step.projectDependencies.isNotEmpty()) {
                    gradleDependenciesManager.addDependencies(
                        buildModel = projectGradleBuildModel.buildscript(),
                        dependencies = step.projectDependencies
                    )
                    execRunWriteAction { projectGradleBuildModel.applyChanges() }
                }
            }
            is ModificationStep.GradleModificationStep.PluginModification -> {
                if (buildModuleModel != null && step.modulePlugins.isNotEmpty()) {
                    gradleDependenciesManager.addPlugins(buildModuleModel, step.modulePlugins)
                }
            }

        }
    }
}