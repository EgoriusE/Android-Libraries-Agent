package core

import com.android.tools.idea.gradle.dsl.api.GradleBuildModel
import com.android.tools.idea.gradle.dsl.api.GradleModelProvider
import com.intellij.openapi.project.Project
import model.ModificationModel
import model.ModificationStep

class ActionHandler(
    private val project: Project,
    private val model: ModificationModel
) {

    private val dirHelper = DirHelper(model.module)
    private val fileAdder = FileAdder(project)
    private val gradleDependenciesManager = GradleDependenciesManager()

    fun handle() {
        model.steps.forEach { step ->
            when (step) {

                is ModificationStep.DependenciesStep -> {
                    val buildModel: GradleBuildModel? = GradleModelProvider.get().getBuildModel(model.module)
                    if (buildModel != null) {
                        gradleDependenciesManager.addDependencies(buildModel, step.dependencies)
                    }
                }

                is ModificationStep.GenerateCodeStep -> {
                    val boilerPlateDir =
                        if (step.dirName != null) {
                            dirHelper.generateDir(step.dirName)
                        } else {
                            dirHelper.getPackageDir()
                        }

                    val templateGenerator = TemplateGenerator(project)
                    val generatedFiles = templateGenerator.generateFiles(step.filesNames, step.model)
                    fileAdder.addFiles(generatedFiles, boilerPlateDir)
                }

                is ModificationStep.ExistingFiles -> {
                    SrcModifier(project).modify(model)
                }
            }
        }
    }
}