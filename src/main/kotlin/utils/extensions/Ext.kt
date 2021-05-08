import com.android.tools.idea.gradle.dsl.api.GradleBuildModel
import com.android.tools.idea.gradle.dsl.api.dependencies.ArtifactDependencyModel
import com.android.tools.idea.util.androidFacet
import com.intellij.ide.util.EditorHelper
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.command.executeCommand
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.diagnostic.debug
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.fileChooser.ex.FileChooserDialogImpl
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.psi.PsiBinaryFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.refactoring.copy.CopyHandler
import constants.PluginConstants
import org.jetbrains.android.facet.AndroidFacet
import org.jetbrains.kotlin.idea.core.util.toPsiDirectory
import org.jetbrains.kotlin.idea.util.application.runWriteAction
import org.jetbrains.kotlin.idea.util.module

fun AnActionEvent.getSelectedPsiElement(): PsiElement? = getData(PlatformDataKeys.PSI_ELEMENT)

val AnActionEvent.androidFacet: AndroidFacet?
    get() = getSelectedPsiElement()?.module?.androidFacet

val Module.moduleParentPsiDirectory: PsiDirectory?
    get() = moduleFile?.parent?.parent?.toPsiDirectory(project)

val Module.rootPsiDirectory: PsiDirectory?
    get() = moduleFile?.parent?.toPsiDirectory(project)

fun execRunWriteAction(command: () -> Unit) {
    executeCommand {
        runWriteAction {
            command()
        }
    }
}

fun Any.logDebug(e: Exception? = null, lazyMessage: () -> String) {
    val logger = Logger.getInstance(this::class.java)
    logger.debug(e) { lazyMessage() }
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

fun Project.showChooseSingleFileDialog(title: String? = null): Array<out VirtualFile>? {
    val fileDescriptor = FileChooserDescriptorFactory
        .createSingleFileDescriptor()
        .apply {
            title?.let { this.title = it }
        }
    val dialog = FileChooserDialogImpl(fileDescriptor, this)
    return dialog.choose(this, null)
}

fun Project.showMessage(msg: String) {
    Messages.showMessageDialog(
        this,
        msg,
        PluginConstants.PLUGIN_NAME,
        Messages.getInformationIcon()
    )
}

fun Module.showMessage(msg: String) {
    this.project.showMessage(msg)
}
