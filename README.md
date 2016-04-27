[![Koara](http://www.koara.io/logo.png)](http://www.koara.io)

[![Build Status](https://img.shields.io/travis/koara/koara-java-html.svg)](https://travis-ci.org/koara/koara-java-html)
[![Coverage Status](https://img.shields.io/coveralls/koara/koara-java-html.svg)](https://coveralls.io/github/koara/koara-java-html?branch=master)
[![Latest Version](https://img.shields.io/maven-central/v/io.koara/koara-html.svg?label=Maven Central)](http://search.maven.org/#search%7Cga%7C1%7Ckoara-html)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/koara/koara-java-html/blob/master/LICENSE)

# Koara-java-html
[Koara](http://www.koara.io) is a modular lightweight markup language. This project can render the koara AST to Html in Java.  
The AST is created by the [core koara parser](https://github.com/koara/koara-java).

## Getting started
- Download [JAR file](http://repo1.maven.org/maven2/io/koara/koara/0.11.0/koara-html-0.11.0.jar)
- Gradle

  ```groovy
  dependencies {
	compile "io.koara:koara-html:0.11.0"
  }
  ```
  
- Maven

  ```xml
  <dependency>
    <groupId>io.koara</groupId>
    <artifactId>koara-html</artifactId>
    <version>0.11.0</version>
  </dependency>
  ```

## Usage
```java
package demo;

import io.koara.Parser;
import io.koara.ast.Document;
import io.koara.html.Html5Renderer;

public class App {

	public static void main(String[] args) {
		Parser parser = new Parser();
		Document result = parser.parse("Hello World!");
		Html5Renderer renderer = new Html5Renderer();
		result.accept(renderer);
		System.out.println(renderer.getOutput());
	}
	
}
```

## Configuration
You can configure the Renderer:

-  **renderer.setPartial(boolean partial)**  
   Default:	`true`
   
   When false, the output will be wrapped with a `<html>` and `<body>` tag to make a complete Html document.