# Samples

## How to run
Open this project in an IDE, such as IntelliJ IDEA, and run the modules listed below.

------------------------------------------------------------------------
## Summary

|         | description                    | decorators                     | DSL                             |
|---------|--------------------------------|--------------------------------|---------------------------------|
| Sample1 | Value-Object sample            | @value                         | ./src/kotlin/samples/Sample1.kt |
| Sample2 | DTO sample                     | @DTO, @GenerateDTO             | ./src/kotlin/samples/Sample2.kt |
| Sample3 | Service sample                 | @Service, @DI                  | ./src/kotlin/samples/Sample3.kt |
| Sample4 | Rest Controller<br>Rest Client | @RestController<br>@RestClient | ./src/kotlin/samples/Sample4.kt |

[//]: # (| Sample5 | Agent sample                   |                                | ./src/kotlin/samples/Sample5.kt |)


------------------------------------------------------------------------
## Basic
To convert models into code, you need to prepare a DSL written in Kotlin and a text file containing the model definitions.
While this sample uses Gradle to build the Kotlin code, Maven or other build systems can also be used.


### Build.gradle.kts
To create a DSL, you must include at least the core library. The sample below also adds an agent library as an option.

```kotlin
plugins {
    kotlin("jvm") version "2.2.20"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.xfc_arch:code_gen_core")        // CodeGen Core 
    implementation("com.xfc_arch:spring_boot_agents")   // Spring Boot Agents

    testImplementation(kotlin("test"))
}
```
### DSL
```kotlin
fun main() {
    dsl {
        runConfig {
            modelPath = getAbsolutePath("code_gen_samples/samples/sample1/models")  // (1) Source Model Path.
            outPath = getAbsolutePath("code_gen_samples/samples/sample1/out")       // (2) Destination path.
            autoRun = false                                                         // (3) autoRun
        }
        
        // (4) Specify File Header.
        fileHeader("""
            Copyright 2026 xfc_arch.com
              Test...
        """)        
    }
}
```
(1) Source path for model files.   
(2) Destination path for converted files.   
(3) AutoRun flag (default: true). If true, convert process automatically runs.   
(4) The text passed as a parameter is output to the headers of all files.

------------------------------------------------------------------------
## Sample1
Sample1 explains how to use built-in agent, ValueAgent. The agent makes models with @Value immutables.


|        | path                                   | description                          |
|--------|----------------------------------------|--------------------------------------|
| DSL    | ./src/kotlin/samples/Sample1.kt        | execute main() to convert the models |
| Model  | ./samples/sample1/models/sample1.model | text model file(s)                   |
| Output | ./samples/sample1/out                  | converted files                      |


### DSL
```kotlin
fun main() {
    dsl {
        gradle("JavaSample") {                // (1) Module Block
            basePackage("com.example.demo")   // (2) Java package.

            agents {                          // (3) Agents Block
                include(JavaValueAgent())     // (4) Built-in Java ValueObject Agent.
            }
        }
    }
}
```

(1) Module Block. gradle in this sample.   
(2) Java Package Path. The path is prepended to the path specified in models to form the actual path in Java.  
(3) Agents Block. In this Block, users can specify agents to run in the module.
(4) include function adds an agent to the module.

### Model
```text
(domain.values)  // (1) package path

@Value           // (2) Decorator. 
[ProductName]    // (3) Class Definition 
value: String    // (4) Attribute Definition
```
(1) Package Path. models described after this line belongs to the package.   
(2) Decorator Value with no parameter.   
(3) Define ProductName class.   
(4) Define Attribute named value belongs to ProductName class.   

------------------------------------------------------------------------
## Sample2
Sample1 explains how to use built-in agent, DTOAgent.
* DTO agent builds models with @DTO from the Class Model specified in the decorator parameter.
* DTO agent creates new DTO models for base models with @GenerateDTO.

|        | path                                   | description                          |
|--------|----------------------------------------|--------------------------------------|
| DSL    | ./src/kotlin/samples/Sample2.kt        | execute main() to convert the models |
| Model  | ./samples/sample2/models/sample2.model | text model file(s)                   |
| Output | ./samples/sample2/out                  | converted files                      |

### DSL
```kotlin
fun main() {
    dsl {
        gradle("JavaSample") {
            agents {
                include(JavaValueAgent())     
                include(JavaDtoAgent())    // (1) Built-in Java DTO Agent.
            }
        }
    }
}
```
(1) includes Java DTO agent to the module.

### Model
```text
(domain.values)

@Value
[ProductName]
value: String

@GenerateDTO    // (1)  @GenerateDTO creates domain.values.dto.ProductPriceDTO by default.
@Value
[ProductPrice]
value: Int32

(domain.dto)

@DTO("ProductName")  // (2)
[ProductNameDto]
```
(1) DTO agent newly creates DTO models for models with @GenerateDTO. The created DTO models are stored in relative package .dto, i,e. domain.values.dto in this case.   
(2) DTO agent build ProductNameDTO ad mutable from ProductName.   

------------------------------------------------------------------------
## Sample3
Sample3 explains how to use Spring Service Agent.

|        | path                                   | description                          |
|--------|----------------------------------------|--------------------------------------|
| DSL    | ./src/kotlin/samples/Sample3.kt        | execute main() to convert the models |
| Model  | ./samples/sample3/models/sample3.model | text model file(s)                   |
| Output | ./samples/sample3/out                  | converted files                      |

### DSL
```kotlin
fun main() {
    dsl {
        gradle("JavaSample") {
            agents {
                include(JavaValueAgent())     
                include(JavaDtoAgent())    
                include(SpringAgents())    // (1)
            }
        }
    }
}
```
(1) SpringAgents contains Spring Service agent.   

### Model
```text
(domain.services)

<<interface>>
[AppService1]
+ method1(name: String, price: Int32) : Boolean

<<interface>>
[DomService1]
+ method2(name: ProductName, param2: ProductPrice) : Product

(domain.services.impl)

@Service                           // (1)
[DomService1Impl:DomService1]

@Service                          // (1)
@DI("DomService1")                // (2)
[AppService1Impl:AppService1]
```
(1) Spring Service Agent makes models with @Service Spring Services.    
(2) Spring Service Agent injects the model specified in the parameter to the target model.   

------------------------------------------------------------------------
## Sample4
Sample4 explains how to use Spring Rest Controller / Client Agents.

|        | path                                   | description                          |
|--------|----------------------------------------|--------------------------------------|
| DSL    | ./src/kotlin/samples/Sample4.kt        | execute main() to convert the models |
| Model  | ./samples/sample3/models/sample4.model | text model file(s)                   |
| Output | ./samples/sample3/out                  | converted files                      |

### DSL
```kotlin
fun main() {
    dsl {
        gradle("JavaSample") {
            agents {
                include(JavaValueAgent())     
                include(JavaDtoAgent())    
                include(SpringAgents())            // (1)
            }

            sub("share") {                         // (2)
                packages {
                    include("domain.values")
                    include("domain.data")
                    include("domain.dto")
                    include("domain.tables")
                }
            }

            sub("microservice1") {                // (2)
                packages {
                    include("microservice1")
                }
            }

            sub("microservice2") {               // (2)
                packages {
                    include("microservice2")
                }
            }            
        }
    }
}
```
(1) SpringAgents contains both Spring Rest Controller and Client agents.   
(2) Define three subprojects and allocate all models by package path across these subprojects.   

### Model
```text
(domain.services)

<<interface>>
[Service1]
@export("/method1/{param1}/{param2}")                  // (1)  exports the method as GET method as default.
+ method1(param1: String, param2: Int32) : String

@export("/method2", method="Post")                     // (1) exports the method as POST method.
+ method2(param1: Product) : SalesSummary

@export("/method2a", method="Post")
+ method2a(param1: List<Product>) : List<SalesSummary>

(microservice1.controllers)
@RestController              // (2)
@BaseUrl("/base")            // (3)
@DI(["Service1"])            // (4)

@useDTO                      // (5)
[Service1RestController]


(microservice2.clients)

@RestClient("Service1RestController") // (7)
[Service1RestClient]
```
(1) methods with @export are exported by Rest Controller.  Http Get is the default method.   
(2) Rest Controller agent makes models with @RestController RestControllers.   
(3) Optionally users can specify base URL as Rest Controller endpoint.   
(4) In Rest Controller, injected service methods are exported. Call code for the service method is automatically created.     
(5) If the exported method's parameter and result types has DTO model, use it.    
