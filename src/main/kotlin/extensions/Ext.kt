import com.android.tools.idea.util.androidFacet
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.module.Module
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import org.jetbrains.android.facet.AndroidFacet
import org.jetbrains.kotlin.idea.core.util.toPsiDirectory
import org.jetbrains.kotlin.idea.util.module

fun AnActionEvent.getSelectedPsiElement(): PsiElement? = getData(PlatformDataKeys.PSI_ELEMENT)

val AnActionEvent.androidFacet: AndroidFacet?
    get() = getSelectedPsiElement()?.module?.androidFacet

val Module.moduleParentPsiDirectory: PsiDirectory?
    get() = moduleFile?.parent?.parent?.toPsiDirectory(project)

val Module.rootPsiDirectory: PsiDirectory?
    get() = moduleFile?.parent?.toPsiDirectory(project)
