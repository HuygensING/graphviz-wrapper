package nl.knaw.huygens.graphviz

/*-
 * #%L
 * GraphVizWrapper
 * =======
 * Copyright (C) 2020 - 2021 Huygens ING (KNAW)
 * =======
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.util.concurrent.CompletableFuture
import java.util.logging.Level
import java.util.logging.Logger

object GraphVizWrapper {
    private val LOG = Logger.getLogger(GraphVizWrapper::class.java.name)
    private const val WHICH_DOT = "which dot"
    private const val WHERE_DOT = "where dot.exe"

    fun detectDotPath(): String? {
        val options = if (System.getProperty("os.name").contains("Windows"))
            arrayOf(WHERE_DOT, WHICH_DOT)
        else
            arrayOf(WHICH_DOT, WHERE_DOT)

        for (detectionCommand in options) {
            try {
                val process = Runtime.getRuntime().exec(detectionCommand)
                val inputStreamReader = InputStreamReader(process.inputStream, Charset.defaultCharset())
                BufferedReader(inputStreamReader).use { processReader ->
                    val path = CompletableFuture.supplyAsync {
                        processReader
                                .lines()
                                .map { line: String -> line.trim { it <= ' ' } }
                                .filter { line: String -> line.toLowerCase().contains("dot") }
                                .findFirst()
                    }
                    process.waitFor()
                    return path.get().get()
                }
            } catch (t: Throwable) {
                LOG.log(Level.FINE, detectionCommand, t)
            }
        }
        return null
    }
}
