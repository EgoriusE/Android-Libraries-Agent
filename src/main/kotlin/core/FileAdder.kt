package core

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import execRunWriteAction

class FileAdder(private val project: Project) {

    fun addFiles(files: List<PsiFile>, dir: PsiDirectory?) {
        execRunWriteAction {
            files.forEach { dir?.add(it) }
        }
    }
}