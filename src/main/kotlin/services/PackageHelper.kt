package services

import com.intellij.openapi.module.Module
import com.intellij.openapi.project.guessProjectDir
import com.intellij.psi.PsiDirectory
import constants.CodeGeneratorConstants.JAVA_SOURCE_FOLDER_NAME
import constants.CodeGeneratorConstants.MAIN_SOURCE_SET_FOLDER_NAME
import constants.CodeGeneratorConstants.SRC_FOLDER_NAME
import execRunWriteAction
import org.jetbrains.kotlin.idea.core.util.toPsiDirectory
import utils.extensions.DOT
import utils.extensions.findSubdirectoryByPackageName
import utils.extensions.getPackageName

class PackageHelper(private val module: Module) {

    private val project = module.project
    val rootDir = project.guessProjectDir()?.toPsiDirectory(project)

    fun getModulePackage(): PsiDirectory? {
        val fullModuleNameList: List<String> = module.name.split(Char.DOT)
        val moduleName = fullModuleNameList[fullModuleNameList.size - 1]
        return rootDir?.findSubdirectory(moduleName)
    }

    fun getSrcPackage(): PsiDirectory? {
        val moduleDir = getModulePackage()
        return moduleDir?.findSubdirectory(SRC_FOLDER_NAME)?.findSubdirectory(MAIN_SOURCE_SET_FOLDER_NAME)
            ?.findSubdirectory(JAVA_SOURCE_FOLDER_NAME)
    }

    fun getPackageDir(): PsiDirectory? {
        val packageName: String? = module.getPackageName()
        return if (packageName != null) {
            getSrcPackage()?.findSubdirectoryByPackageName(packageName)
        } else null
    }

    fun generatePackage(dirName: String): PsiDirectory? {
        var dir: PsiDirectory? = null
        execRunWriteAction { dir = getPackageDir()?.createSubdirectory(dirName) }
        return dir
    }
}