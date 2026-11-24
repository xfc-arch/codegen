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
import com.xfc_arch.codegen.domain.agents.spring.SpringAgents
import com.xfc_arch.codegen.domain.agents.spring.SpringMybatis
import com.xfc_arch.codegen.domain.core.classes.LangType
import com.xfc_arch.codegen.domain.core.dsl.dsl
import com.xfc_arch.codegen.domain.core.modules.gradle
import com.xfc_arch.codegen.domain.exporters.openapi.OpenAPIPlugin
import com.xfc_arch.codegen.utils.RdbDialect
import com.xfc_arch.codegen.utils.getAbsolutePath
import com.xfc_arch.codegen.utils.getCanonicalPath

fun main() {
    dsl {
        runConfig {
            modelPath = getAbsolutePath("codegen/samples/sample5/models")
            outPath = getAbsolutePath("codegen/samples/sample5/out")
        }

        fileHeader("""
            Copyright (C) 2026 xfc_arch.com            
        """)

        plugins {
            include(OpenAPIPlugin())
        }

        gradle("KotlinSample", langType = LangType.Kotlin) {
            basePackage("com.example.demo")

            moduleSettings {
                addSchema("schema", RdbDialect.H2)
                addSchema("schema-postgres", RdbDialect.PostgresSQL)
            }

            filters {
                attributes {
                    dataObject { attribute -> true }
                }
            }

            agents {
                include(SpringAgents())
                include(SpringMybatis())
            }
        }
    }
}