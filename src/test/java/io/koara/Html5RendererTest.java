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