/*
Copyright (C) 2026 xfc_arch.com

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
 any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
import com.xfc_arch.codegen.domain.agents.java.JavaDataAgent
import com.xfc_arch.codegen.domain.agents.java.JavaDtoAgent
import com.xfc_arch.codegen.domain.agents.java.JavaValueAgent
import com.xfc_arch.codegen.domain.agents.spring.SpringAgents
import com.xfc_arch.codegen.domain.core.dsl.dsl
import com.xfc_arch.codegen.domain.core.modules.gradle
import com.xfc_arch.codegen.utils.getAbsolutePath

fun main() {
    dsl {
        runConfig {
            modelPath = getAbsolutePath("codegen/samples/sample4/models")
            outPath = getAbsolutePath("codegen/samples/sample4/out")
        }

        fileHeader("""
            Copyright (C) 2026 xfc_arch.com            
        """)

        gradle("JavaSpringSample") {
            basePackage("com.example.demo")

            filters {
                attributes {
                    dataObject { model -> true }
                }
            }

            agents {
                include(JavaDataAgent())
                include(JavaValueAgent())
                include(JavaDtoAgent("DTO"))
                include(SpringAgents())
            }

            sub("share") {
                packages {
                    include("domain.values")
                    include("domain.data")
                    include("domain.dto")
                    include("domain.tables")
                }
            }

            sub("microservice1") {
                packages {
                    include("microservice1")
                }
            }

            sub("microservice2") {
                packages {
                    include("microservice2")
                }
            }
        }
    }
}