[![Koara](http://www.koara.io/logo.png)](http://www.koara.io)

[![Build Status](https://img.shields.io/travis/koara/koara-java-html.svg)](https://travis-ci.org/koara/koara-java-html)
[![Coverage Status](https://img.shields.io/coveralls/koara/koara-java-html.svg)](https://coveralls.io/github/koara/koara-java-html?branch=master)
[![Latest Version](https://img.shields.io/maven-central/v/io.koara/koara.svg?label=Maven Central)](http://search.maven.org/#search%7Cga%7C1%7Ckoara-html)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/koara/koara-java-html/blob/master/LICENSE)

# koara-java-html
[Koara](http://www.koara.io) is a modular lightweight markup language. This project is for parsing Koara to Html without external dependencies.

## Getting Started
- Download [JAR file](http://repo1.maven.org/maven2/io/koara/koara-html/0.9.1/koara-html-0.9.1.jar)
- Gradle

  ```groovy
  dependencies {
	compile "io.koara:koara-html:0.9.1"
  }
  ```
  
- Maven

  ```xml
  <dependency>
    <groupId>io.koara</groupId>
    <artifactId>koara-html</artifactId>
    <version>0.9.1</version>
  </dependency>
  ```

## Usage
```java
package io.koara;

import io.koara.ast.Document;
import io.koara.html.Html5Renderer;
import static io.koara.Module.*;

public class Demo {

	public static void main(String[] args) {
		
		Parser parser = new Parser();

		// Parse string and generate AST
		Document document = parser.parse("Hello World!"); 
		
		// Render as Html
		Html5Renderer renderer = new Html5Renderer();
		document.accept(renderer);
		
		System.out.println(renderer.getOutput());
	}
	
}
```

When converting koara documents, there are 2 phases: parsing (Koara -> AST) and rendering (AST -> _Output format_)

### Parsing
#### parse(String text)
Parse a string.

```java
parser.parse("Hello World!"); 
```

#### parseFile(File file)
Parse a koara file.

```java
parser.parse("Hello World!"); 
```

#### setModules(String... modules)
Enable which modules to parse. By default, all are parsed. 

```java
parser.setModules("paragraphs", "headings", "lists", "links", "images", "formatting", "blockquotes", "code");
```

### Rendering
#### getOutput()
Get the HTML output

```java
renderer.getOutput()
```
