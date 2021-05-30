import com.android.tools.idea.gradle.dsl.api.GradleBuildModel
import com.android.tools.idea.gradle.dsl.api.dependencies.ArtifactDependencyModel
import com.android.tools.idea.util.androidFacet
import com.intellij.ide.util.EditorHelper
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.command.executeCommand
import com.intellij.openapi.module.Module
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.psi.PsiBinaryFile
import com.intellij.psi.PsiElement
import com.intellij.refactoring.copy.CopyHandler
import org.jetbrains.android.facet.AndroidFacet
import org.jetbrains.kotlin.idea.util.application.runWriteAction
import org.jetbrains.kotlin.idea.util.module
import utils.extensions.isAndroidProject
import utils.extensions.showMessage

fun AnActionEvent.hasAndroidProject(): Boolean {
    return project
        ?.isAndroidProject()
        ?: false
}

fun AnActionEvent.getSelectedPsiElement(): PsiElement? = getData(PlatformDataKeys.PSI_ELEMENT)

val AnActionEvent.androidFacet: AndroidFacet?
    get() = getSelectedPsiElement()
        ?.module
        ?.androidFacet

fun execRunWriteAction(command: () -> Unit) {
    executeCommand {
        runWriteAction {
            command()
        }
    }
}

fun PsiElement.openInEditor() {
    CopyHandler.updateSelectionInActiveProjectView(this, this.project, true)
    if (this !is PsiBinaryFile) {
        EditorHelper.openInEditor(this)
        ToolWindowManager.getInstance(project).activateEditorComponent()
    }
}

fun ArtifactDependencyModel.isEqualName(dependencyName: String) =
    getGroupName() == dependencyName


fun ArtifactDependencyModel.getGroupName(): String {
    return group().toString() + ":" + name().toString() + ":"
}

fun GradleBuildModel.isPluginExist(pluginName: String): Boolean {
    return plugins().any { it.name().toString() == pluginName }
}

fun Module.showMessage(msg: String) {
    project.showMessage(msg)
}
