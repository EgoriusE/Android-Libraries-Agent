package core

import com.android.tools.idea.gradle.dsl.api.GradleBuildModel
import com.android.tools.idea.gradle.dsl.api.GradleModelProvider
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiRecursiveElementVisitor
import com.intellij.psi.PsiRecursiveElementWalkingVisitor
import model.ModificationModel
import model.ModificationStep
import org.jetbrains.kotlin.idea.KotlinFileType

class ActionHandler(private val project: Project) {

    fun handle(model: ModificationModel) {
        model.steps.forEach { step ->
            when (step) {
                is ModificationStep.DependenciesStep -> {
                    val buildModel: GradleBuildModel? = GradleModelProvider.get().getBuildModel(model.module)
                    if (buildModel != null) {
                        RoomGradleModifier().addDependencies(buildModel, step.dependencies)
                    }
                }
                is ModificationStep.BoilerPlateStep -> {
                    val dirHelper = DirHelper(model.module)
                    val boilerPlateDir = if (step.dirName != null) {
                        dirHelper.createBoilerplateDir(step.dirName)
                    } else dirHelper.getPackageDir()

                    val templateGenerator = TemplateGenerator(project)
                    val generatedFiles = templateGenerator.generateBoilerplate(step.filesNames, step.model)
                    FileAdder(project).addFiles(generatedFiles, boilerPlateDir)
                }
                is ModificationStep.ExistingFiles -> {
                    val dirHelper = DirHelper(model.module)
                    val packageDir = dirHelper.getPackageDir()
                    val files: Array<PsiFile>? = packageDir?.files
                    files?.forEach {
                        if (it.fileType is KotlinFileType) {
                            println("is kotlin File Type")
                            parseKtPsiFile(it)
                        }
                        println(it.fileType.name)
                        println(it.fileType.description)
                        println(it.fileType.displayName)
                        println(it.fileType.defaultExtension)
                        println("----------")
                    }
                }
            }
        }
    }

    private fun parseKtPsiFile(file: PsiFile) {
        val viewProvider = file.viewProvider

        val emptyVisitor = PsiRecursiveElementVisitor.EMPTY_VISITOR
        file.accept(emptyVisitor)
        emptyVisitor.visitFile(file)
    }
}