package io.koara;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import io.koara.ast.Document;
import io.koara.html.Html5Renderer;

public class Html5RendererTest {

    private Document document;
    private Parser parser;
    private Html5Renderer renderer;

    @Before
    public void setUp() {
        parser = new Parser();
        renderer = new Html5Renderer();
    }

    @Test
    public void testRender() {
        document = new Parser().parse("Test");
        document.accept(renderer);
        assertEquals("<p>Test</p>", renderer.getOutput());
    }

    @Test
    public void testRenderHardwrapTrue() {
        renderer.setHardwrap(true);
        document = new Parser().parse("a\nb");
        document.accept(renderer);
        assertEquals("<p>a<br>\nb</p>", renderer.getOutput());
    }

	@Test
	public void testRenderPartialFalse() {
        StringBuilder expected = new StringBuilder("<!DOCTYPE html>\n");
		expected.append("<html>\n");
		expected.append("  <body>\n");
		expected.append("    <p>Test</p>\n");
		expected.append("  </body>\n");
		expected.append("</html>\n");

        renderer.setPartial(false);
        document = new Parser().parse("Test");
        document.accept(renderer);
		assertEquals(expected.toString(), renderer.getOutput());
	}


    @Test
    public void testHeadingIdsTrue() {
        renderer.setHeadingIds(true);
        document = new Parser().parse("= A");
        document.accept(renderer);
        assertEquals("<h1 id=\"a\">A</h1>", renderer.getOutput());
    }

    @Test
    public void testHeadingIdsTrueMultipleWords() {
        renderer.setHeadingIds(true);
        document = new Parser().parse("= This is a test");
        document.accept(renderer);
        assertEquals("<h1 id=\"this_is_a_test\">This is a test</h1>", renderer.getOutput());
    }


}