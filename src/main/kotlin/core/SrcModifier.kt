package core

import CodeGeneratorConstants.APPLICATION_CLASS_NAME
import com.intellij.openapi.command.executeCommand
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementFactory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiMethod
import com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl
import com.intellij.psi.util.findDescendantOfType
import model.ModificationModel
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.idea.inspections.findExistingEditor
import org.jetbrains.kotlin.idea.util.application.runWriteAction
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtImportDirective
import org.jetbrains.kotlin.psi.KtPsiFactory
import org.jetbrains.kotlin.resolve.ImportPath
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UFile
import org.jetbrains.uast.toUElement

class SrcModifier(private val project: Project) {

    private val psiElementFactory: PsiElementFactory by lazy {
        PsiElementFactory.getInstance(model.module.project)
    }

    private lateinit var model: ModificationModel

    fun modify(model: ModificationModel) {
        this.model = model
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
            println("----------")
        }
    }


    private fun parseKtPsiFile(file: PsiFile) {
        // uast
        val uastFile: UFile? = file.toUElement() as? UFile
        uastFile?.let { uFile ->
            val classes = uFile.classes
            classes.forEach { uClass: UClass ->
                uClass.uastSuperTypes.forEach {
                    if (it.getQualifiedName() == APPLICATION_CLASS_NAME) {
                        addImportsBro(uastFile, file)
                        addMethodInAppClass(uClass)
                    }
                }
            }
        }
    }


    private val methodText = "private fun initKoin() {\n" +
            "        startKoin {\n" +
            "            androidContext(this@App)\n" +
            "            modules()\n" +
            "        }\n" +
            "    }"

    private fun addMethodInAppClass(uClass: UClass) {
        val method: PsiMethod = psiElementFactory.createMethodFromText(methodText, null)

        println(method.name)
        executeCommand {
            runWriteAction {
                val ktClass = (uClass.sourcePsi as KtClass)
                val a = ktClass.body?.addBefore(method, ktClass.body!!.rBrace)
                ktClass.body?.addBefore(PsiWhiteSpaceImpl("whitespace"), a)
            }
        }
    }

    private val import1 = "org.koin.android.ext.koin.androidContext"
    private val import2 = "org.koin.core.context.startKoin"

    private fun addImportsBro(uastFile: UFile, file: PsiFile) {
        val ktPsiFactory = KtPsiFactory(project, false)

        val ktPsiImport1: KtImportDirective = ktPsiFactory.createImportDirective(ImportPath.fromString(import1))
        val ktPsiImport2: KtImportDirective = ktPsiFactory.createImportDirective(ImportPath.fromString(import2))
        val a = uastFile.imports.first().sourcePsi
        println("IMPORT SUKA $a ")
        executeCommand {
            runWriteAction {
                val addedImport = file.addBefore(ktPsiImport1, a)
                file.addBefore(ktPsiImport2, addedImport)
            }
        }
    }

}