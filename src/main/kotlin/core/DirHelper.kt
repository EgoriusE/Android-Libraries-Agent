package core

import CodeGeneratorConstants.JAVA_SOURCE_FOLDER_NAME
import CodeGeneratorConstants.MAIN_SOURCE_SET_FOLDER_NAME
import CodeGeneratorConstants.SRC_FOLDER_NAME
import com.intellij.openapi.command.executeCommand
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.guessProjectDir
import com.intellij.psi.PsiDirectory
import extensions.DOT
import extensions.findSubdirectoryByPackageName
import extensions.getPackageName
import org.jetbrains.kotlin.idea.core.util.toPsiDirectory
import org.jetbrains.kotlin.idea.util.application.runWriteAction

class DirHelper(private val module: Module) {

    private val project = module.project
    private val rootDir = project.guessProjectDir()?.toPsiDirectory(project)

    fun getModuleDir(): PsiDirectory? {
        val fullModuleNameList: List<String> = module.name.split(Char.DOT)
        val moduleName = fullModuleNameList[fullModuleNameList.size - 1]
        return rootDir?.findSubdirectory(moduleName)
    }

    fun getSrcDir(): PsiDirectory? {
        val moduleDir = getModuleDir()
        return moduleDir?.findSubdirectory(SRC_FOLDER_NAME)?.findSubdirectory(MAIN_SOURCE_SET_FOLDER_NAME)
            ?.findSubdirectory(JAVA_SOURCE_FOLDER_NAME)
    }

    fun getPackageDir(): PsiDirectory? {
        val packageName: String? = module.getPackageName()
        return if (packageName != null) {
            getSrcDir()?.findSubdirectoryByPackageName(packageName)
        } else null
    }

    fun createBoilerplateDir(dirName: String): PsiDirectory? {
        var dir: PsiDirectory? = null
        executeCommand {
            runWriteAction {
                dir = getPackageDir()?.createSubdirectory(dirName)
            }
        }
        return dir
    }
}