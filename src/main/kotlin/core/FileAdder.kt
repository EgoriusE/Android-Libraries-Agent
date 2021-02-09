package core

import com.intellij.openapi.command.executeCommand
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import org.jetbrains.kotlin.idea.util.application.runWriteAction

class FileAdder(private val project: Project) {

    fun addFiles(files: List<PsiFile>, dir: PsiDirectory?) {
        executeCommand {
            runWriteAction {
                files.forEach { dir?.add(it) }
            }
        }
    }
}