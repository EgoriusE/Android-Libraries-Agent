package core

import com.intellij.openapi.module.Module
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiDirectory
import showChooseSingleFileDialog
import showMessage
import utils.extensions.getModulePackage
import utils.extensions.isJsonFile
import java.io.IOException

class FirebaseHelper {

    companion object {

        fun execute(module: Module) {
            val file = module.project
                .showChooseSingleFileDialog("Choose google-services.json file")
                ?.firstOrNull()

            if (file != null) {
                if (file.isJsonFile()) {
                    val modulePackage: PsiDirectory? = module.getModulePackage()
                    try {
                        VfsUtil.copy(null, file, modulePackage!!.virtualFile)
                    } catch (e: IOException) {
                        // nothing
                    }
                } else {
                    module.showMessage("It's not a json file!")
                }
            }
        }
    }
}

