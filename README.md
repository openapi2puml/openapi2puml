# OpenAPI2Puml

OpenApi to Plant UML conversion tool generates UML Class Diagrams from an Open API definition.

This is a fork of the original project Swagger2puml (https://github.com/kicksolutions/swagger2puml) which seems to have been dormant for some time.

The original developers of Swagger2puml are:
- Santosh Manapragada https://github.com/msantosh1188
- Manisha Bardiya https://github.com/manishabardiya

This project is based on Maven.
Following are modules we currently have 

- swagger2puml-core

Following are the tools which this project internally uses:

- [Swagger Parser]
- [Plant UML]
- [Graphviz]
- [Mustache]

Please [install](https://graphviz.gitlab.io/download/) Graphviz on your machines, ensure you edit PATH varible of your machine and add bin folder of graphviz.

Create a new System Variable called GRAPHVIZ_DOT and point to dot.exe for windows and dot in case of Linux in bin folder of graphviz.

# How does it work

- Swagger2Puml internally reads the swagger definition using [Swagger Parser] which then converts the Swagger Definition to swagger.puml
- Once the swagger.puml gets generated sucessfully it then calls [Plant UML] to generate swagger.svg


## swagger2puml-core: 

This utility takes Swagger Yaml as input and as response it generates swagger.puml and swagger.svg files as output.

Below is the Sample Class Diagram which gets generated.
To see the generated PUML file, please click [here](examples/swagger.puml)

![Swagger-Class-Diagram-Sample](examples/swagger.svg)

### Usage:

```
java -cp swagger2puml.jar com.kicksolutions.swagger.Swagger2PlantUML [options]

-i {Path of Swagger Definition (Can be either Yaml or json)}
-o {Target location where Puml File and Image should generated}
-generateDefinitionModelOnly {true/flase Defult False (Optional)}
-includeCardinality {true/flase Defult true (Optional)}
-includeCardinality {true/flase Defult true (Optional)}
```

License
----

Apacahe 2.0

[Plant UML]: <https://github.com/plantuml/plantuml>
[Swagger]: <https://swagger.io/>
[Swagger Parser]: <https://github.com/swagger-api/swagger-parser>
[Graphviz]: <https://graphviz.gitlab.io/>
[Mustache]: <https://github.com/spullara/mustache.java>
