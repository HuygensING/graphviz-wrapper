package nl.knaw.huygens.graphviz

/*-
 * #%L
 * GraphVizWrapper
 * =======
 * Copyright (C) 2020 Huygens ING (KNAW)
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

import nl.knaw.huygens.graphviz.GraphVizWrapper.detectDotPath
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.platform.commons.logging.LoggerFactory
import java.io.ByteArrayOutputStream

class GraphVizWrapperTest {

    private val log = LoggerFactory.getLogger(this.javaClass)

    private val simpleDot = "graph{x--y;y--z;z--x}"

    private val dotEngine = DotEngine()

    private val dotExecutableFound = detectDotPath() != null

    @Test
    fun test_DotEngine_with_stream() {
        when_dot_executable_is_present {
            val outputStream = ByteArrayOutputStream()
            dotEngine.renderAs("svg", simpleDot, outputStream)
            val rendered = outputStream.toString("UTF-8")
            assertThat(rendered).contains("<svg", "</svg>")
        }
    }

    @Test
    fun test_renderAs_as_string() {
        when_dot_executable_is_present {
            val rendered = dotEngine.renderAs("svg", simpleDot)
            assertThat(rendered).contains("<svg", "</svg>")
        }
    }

    @Test
    fun test_dot_version() {
        when_dot_executable_is_present {
            val version = dotEngine.dotVersion
            log.info { "version = $version" }
            assertThat(version).isNotEmpty
        }
    }

    @Test
    fun test_has_dot() {
        assertThat(dotEngine.hasDot).isEqualTo(dotExecutableFound)
    }

    private fun when_dot_executable_is_present(func: () -> Unit) =
            if (dotExecutableFound) {
                func()
            } else {
                log.info { "dot executable not found, skipping test" }
            }

}
