package utils.extensions

import com.intellij.psi.PsiDirectory
import com.intellij.util.IncorrectOperationException

fun PsiDirectory.canCreateSubdirectory(name: String): Boolean {
    return try {
        checkCreateSubdirectory(name)
        true
    } catch (ex: IncorrectOperationException) {
        false
    }
}

fun PsiDirectory.canCreateFile(fileName: String): Boolean {
    return try {
        checkCreateFile(fileName)
        true
    } catch (ex: IncorrectOperationException) {
        false
    }
}

fun PsiDirectory.findSubdirectoryByPackageName(packageName: String): PsiDirectory {
    val directoriesNames = packageName.split(Char.DOT)
    var result: PsiDirectory = this
    for (item in directoriesNames) {
        result = result.findSubdirectory(item)
            ?: throw RuntimeException()
    }
    return result
}

fun PsiDirectory.createSubdirectoriesForPackageName(packageName: String): PsiDirectory {
    val directoriesNames = packageName.split(Char.DOT)
    var result = this
    for (item in directoriesNames) {
        result = result.createSubdirectory(item)
    }
    return result
}

fun PsiDirectory.hasChildDir(childDirName: String): Boolean {
    return findSubdirectory(childDirName) != null
}