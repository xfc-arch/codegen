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
import com.xfc_arch.codegen.domain.agents.typescript.TypeScriptDataAgent
import com.xfc_arch.codegen.domain.agents.typescript.TypeScriptDtoAgent
import com.xfc_arch.codegen.domain.agents.typescript.TypeScriptRestClient
import com.xfc_arch.codegen.domain.agents.typescript.TypeScriptValueAgent
import com.xfc_arch.codegen.domain.core.converters.StdTypeScriptConverter
import com.xfc_arch.codegen.domain.core.dsl.dsl
import com.xfc_arch.codegen.domain.core.modules.gradle
import com.xfc_arch.codegen.domain.core.modules.typescript
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

        typescript("TsSample") {
            basePackage("app")

            filters {
                attributes {
                    dataObject { attribute -> true }
                }
            }

            agents {
                include(TypeScriptDataAgent())
                include(TypeScriptValueAgent())
                include(TypeScriptDtoAgent())
                include(TypeScriptRestClient())
            }

            packages {
                include("domain.values")
                include("domain.data")
                include("microservice2.clients")
            }
        }
    }
}