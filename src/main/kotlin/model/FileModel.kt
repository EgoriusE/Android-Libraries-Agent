package model

import com.intellij.psi.PsiFile

data class FileModel(
    val name: String,
    val templateModel: Map<String, Any>,
    val isOpenInEditor: Boolean = false
)

data class GeneratedFileModel(
    val psiFile: PsiFile,
    val isOpenInEditor: Boolean = false
) {
    val name = psiFile.name
}
