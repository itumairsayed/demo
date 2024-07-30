# LoggingHandler

## Overview

`LoggingHandler` is a custom handler for Apache Axis 1.4 that provides advanced logging capabilities for SOAP messages. It logs detailed information about SOAP requests and responses, which is useful for debugging and monitoring.

## Integration Instructions

To integrate `LoggingHandler` into your Axis-based project, follow these steps:

### 1. Add the JAR to Your Project

Include the `LoggingHandler` JAR file in your project’s classpath.

### 2. Configure the `wsdd` File

You need to modify your `wsdd` file to register the `LoggingHandler`. Add the following handler configuration:

```xml
<handler name="LoggingHandler" type="java:com.example.axis.client.LoggingHandler"/>
```
Within the <service> tag, include the LoggingHandler in both the requestFlow and responseFlow configurations:
```
<service name="YourServiceName">
    <requestFlow>
        <handler type="LoggingHandler"/>
    </requestFlow>
    <responseFlow>
        <handler type="LoggingHandler"/>
    </responseFlow>
</service>
```

### Important: To avoid conflicts, remove any existing axis.jar files from your project. The LoggingHandler JAR contains the necessary Axis 1.4 library, and having multiple versions of Axis in your classpath can lead to issues.

### Example Configuration
Here’s a complete example of how your wsdd file might look:

```
<deployment>
    <handler name="LoggingHandler" type="java:com.example.axis.client.LoggingHandler"/>

    <service name="YourServiceName">
        <requestFlow>
            <handler type="LoggingHandler"/>
        </requestFlow>
        <responseFlow>
            <handler type="LoggingHandler"/>
        </responseFlow>
    </service>
</deployment>
```
