import kotlinx.coroutines.experimental.*
import org.jetbrains.kotlin.script.util.*
import java.io.*
import kotlin.script.experimental.annotations.*
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.*
import kotlin.script.experimental.jvm.*
import kotlin.script.experimental.jvmhost.*

fun main(args: Array<String>) {
    runBlocking {
        val myScript = instantiateScript<MyScript>("src/main/kotlin/hello.kts")
        val result = myScript.runThis("testing1")
        println("got something, here it is: $result")
    }
}

object TestScriptConfiguration : ScriptCompilationConfiguration(
        {
            jvm {
                dependenciesFromCurrentContext(
                        "kotlinscript-sandbox"
                )
            }
        }
)

@KotlinScript(
        compilationConfiguration = TestScriptConfiguration::class
)
abstract class MyScript {
    abstract fun runThis(param1: String): String
}

suspend inline fun <reified T: Any> instantiateScript(scriptPath: String): T {
    val scriptCompiler = JvmScriptCompiler()
    val compiledScriptResult = scriptCompiler(File(scriptPath).toScriptSource(), createJvmCompilationConfigurationFromTemplate<T>())
    val compiledScript = when (compiledScriptResult) {
        is ResultWithDiagnostics.Success -> compiledScriptResult.value
        is ResultWithDiagnostics.Failure -> {
            println(compiledScriptResult.reports)
            compiledScriptResult.reports[0].exception?.printStackTrace()
            null
        }
    }
    val compiledScriptClassResult = compiledScript!!.getClass(null)
    val compiledScriptClass = when (compiledScriptClassResult) {
        is ResultWithDiagnostics.Success -> compiledScriptClassResult.value
        is ResultWithDiagnostics.Failure -> {
            println(compiledScriptClassResult.reports)
            compiledScriptClassResult.reports[0].exception?.printStackTrace()
            null
        }
    }
    return compiledScriptClass!!.java.constructors.single().newInstance() as T
}