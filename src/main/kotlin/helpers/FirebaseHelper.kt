package helpers

import com.intellij.openapi.module.Module
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VfsUtil
import execRunWriteAction
import showYesNoDialog
import utils.extensions.getModulePackage
import utils.extensions.isJsonFile
import utils.extensions.showChooseSingleFileDialog
import java.io.IOException

object FirebaseHelper {

    fun execute(module: Module) {
        val file = module.project
            .showChooseSingleFileDialog("Choose google-services.json file")
            ?.firstOrNull()

        if (file != null) {
            if (file.isJsonFile()) {
                val modulePackage = module.getModulePackage()
                try {
                    execRunWriteAction { VfsUtil.copy(null, file, modulePackage!!.virtualFile) }
                } catch (e: IOException) {
                    // nothing
                }
            } else {
                val dialogResult = module.showYesNoDialog("It's not a json file! Would you like to try again?")
                if (dialogResult == Messages.YES) execute(module)
            }
        } else {
            val dialogResult = module.showYesNoDialog("You have not selected a file. Would you like to try again?")
            if (dialogResult == Messages.YES) execute(module)
        }
    }
}