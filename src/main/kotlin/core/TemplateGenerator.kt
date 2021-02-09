package core

import PluginConstants.DEFAULT_TEMPLATES_DIR_NAME
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import extensions.templateNameToFileName
import freemarker.template.Configuration
import freemarker.template.Template
import freemarker.template.TemplateExceptionHandler
import org.jetbrains.kotlin.idea.KotlinFileType
import java.io.StringWriter

class TemplateGenerator(private val project: Project) {

    private val psiFileFactory by lazy {
        PsiFileFactory.getInstance(project)
    }

    private val freeMarkerConfig by lazy {
        Configuration().apply {
            setClassForTemplateLoading(TemplateGenerator::class.java, DEFAULT_TEMPLATES_DIR_NAME)
            defaultEncoding = Charsets.UTF_8.name()
            templateExceptionHandler = TemplateExceptionHandler.RETHROW_HANDLER
            // logTemplateExceptions = false
            // wrapUncheckedExceptions = true
        }
    }

    fun generateBoilerplate(templateNames: List<String>, model: Map<String, Any>): List<PsiFile> =
        templateNames.map { templateName ->

            val template: Template = try {
                freeMarkerConfig.getTemplate(templateName)
            } catch (ex: Exception) {
                ex.printStackTrace()
                throw IllegalArgumentException("Can't find template $templateName")
            }

            val text: String = StringWriter().use { writer ->
                try {
                    template.process(model, writer)
                    writer.buffer.toString()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    throw UnsupportedOperationException()
                }
            }
            println(text)
            val currPsiFile: PsiFile =
                psiFileFactory.createFileFromText(templateName.templateNameToFileName(), KotlinFileType.INSTANCE, text)
            currPsiFile
        }
}