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
import kotlin.test.assertNotNull

class GraphVizWrapperTest {

    private val log = LoggerFactory.getLogger(this.javaClass)

    private val simpleDot = "graph{x--y;y--z;z--x}"

    @Test
    fun test_detect_dot_path() {
        val dotPath = detectDotPath()
        log.info { "dotPath = $dotPath" }
        assertNotNull(dotPath)
        println(dotPath)
    }

    @Test
    fun test_DotEngine_with_stream() {
        val outputStream = ByteArrayOutputStream()
        DotEngine().renderAs("svg", simpleDot, outputStream)
        val rendered = outputStream.toString("UTF-8")
        assertThat(rendered).contains("<svg", "</svg>")
    }

    @Test
    fun test_renderAs_as_string() {
        val rendered = DotEngine().renderAs("svg", simpleDot)
        assertThat(rendered).contains("<svg", "</svg>")
    }

    @Test
    fun test_dot_version() {
        val version = DotEngine().dotVersion
        log.info { "version = $version" }
        assertThat(version).isNotEmpty
    }

}
