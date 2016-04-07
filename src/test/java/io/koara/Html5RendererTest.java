/*
 * Copyright 2015-2016 the original author or authors.
 *
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
 */
package io.koara;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import io.koara.ast.Document;
import io.koara.html.Html5Renderer;

public class Html5RendererTest {

	private Html5Renderer renderer;
	private Document document;
	
	@Before
	public void setUp() {
		Parser parser = new Parser();
		document = parser.parse("Test");
		renderer = new Html5Renderer();
	}
	
	@Test
	public void testBasic() {
		document.accept(renderer);
		assertEquals("<p>Test</p>", renderer.getOutput());
	}
	
	@Test
	public void testNoPartialResult() {
		StringBuilder expected = new StringBuilder("<!DOCTYPE html>\n");
		expected.append("<html>\n");
		expected.append("  <body>\n");
		expected.append("    <p>Test</p>");
		expected.append("  </body>\n");
		expected.append("</html>\n");
		
		renderer.setPartial(false);
		document.accept(renderer);
		assertEquals(expected.toString(), renderer.getOutput());
	}

}