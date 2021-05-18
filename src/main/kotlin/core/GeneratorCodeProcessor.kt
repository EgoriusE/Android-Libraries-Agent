package core

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import execRunWriteAction
import helpers.NotificationsFactory
import helpers.PackageHelper
import model.ModificationStep
import openInEditor
import utils.extensions.canCreateFile

@Service
class GeneratorCodeProcessor(
    project: Project
) {

    private val notificationFactory = project.service<NotificationsFactory>()
    private val templateGenerator = project.service<TemplateGenerator>()

    fun process(step: ModificationStep.GenerateCodeStep, packageHelper: PackageHelper) {
        val generatedPackage = if (step.dirName != null) {
            packageHelper.generatePackage(step.dirName)
        } else {
            packageHelper.getPackageDir()
        }

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
}