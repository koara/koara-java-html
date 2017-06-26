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
package com.codeaddslife.koara.html;



import com.codeaddslife.koara.Renderer;
import com.codeaddslife.koara.ast.*;

import java.util.Stack;

public class Html5Renderer implements Renderer {

	private StringBuffer out;
    private int level;
    private Stack<Integer> listSequence = new Stack<Integer>();

    // Render options
    private boolean partial = true;
    private boolean hardwrap = false;
    private boolean headingIds = false;

	public void visit(Document node) {
		out = new StringBuffer();
		node.childrenAccept(this);
	}
	
	public void visit(Heading node) {
		indent();
		out.append("<h" + node.getValue());
        if(headingIds) {
            String id = "";
            for(Node n : node.getChildren()) {
                if(n instanceof Text) {
                    id += n.getValue().toString();
                }
            }
            out.append(" id=\"" + id.toLowerCase().replace(" ", "_")  + "\"");
        }
        out.append(">");
		node.childrenAccept(this);
		out.append("</h" + node.getValue() + ">\n");
		if(!node.isNested()) { out.append("\n"); }
	}
	
	public void visit(BlockQuote node) {
		indent();
		out.append("<blockquote>");
		if(node.getChildren() != null && node.getChildren().length > 0) { out.append("\n"); }
		level++;
		node.childrenAccept(this);
		level--;
		indent();
		out.append("</blockquote>\n");
		if(!node.isNested()) { out.append("\n"); }
	}
	
	public void visit(ListBlock node) {
		listSequence.push(0);
		String tag = node.isOrdered() ? "ol" : "ul";
		indent();
		out.append("<" + tag + ">\n");
		level++;
		node.childrenAccept(this);
		level--;
		indent();
		out.append("</" + tag + ">\n");
		if(!node.isNested()) { out.append("\n"); }
		listSequence.pop();
	}
	
	public void visit(ListItem node) {
		Integer seq = listSequence.peek() + 1;		
		listSequence.set(listSequence.size() - 1, seq);
		indent();
		out.append("<li");
		if(node.getNumber() != null && (!seq.equals(node.getNumber()))) {
			out.append(" value=\"" + node.getNumber() + "\"");
			listSequence.push(node.getNumber());
		}
		out.append(">");
		if(node.getChildren() != null) {
			boolean block = (node.getChildren()[0].getClass() == Paragraph.class || node.getChildren()[0].getClass() == BlockElement.class);
			
			if(node.getChildren().length > 1 || !block) { out.append("\n"); }
			level++;
			node.childrenAccept(this);
			level--;
			if(node.getChildren().length > 1 || !block) { indent(); }
		}
		out.append("</li>\n");
	}
	
	public void visit(CodeBlock node) {
		indent();
		out.append("<pre><code");
		if(node.getLanguage() != null) {
			out.append(" class=\"language-" + escape(node.getLanguage()) + "\"");
		}
		out.append(">");
		out.append(escape(node.getValue().toString()) + "</code></pre>\n");
		if(!node.isNested()) { out.append("\n"); }
	}

	public void visit(Paragraph node) {
		if(node.isNested() && (node.getParent() instanceof ListItem) && node.isSingleChild()) {
			node.childrenAccept(this);
		} else {
			indent();
			out.append("<p>");
			node.childrenAccept(this);
			out.append("</p>\n");
			if(!node.isNested()) { out.append("\n"); }
		}
	}
	
	@Override
	public void visit(BlockElement node) {
		if(node.isNested() && (node.getParent() instanceof ListItem) && node.isSingleChild()) {
			node.childrenAccept(this);
		} else {
			indent();
			node.childrenAccept(this);
			if(!node.isNested()) { out.append("\n"); }
		}
	}
		
	public void visit(Image node) {
		out.append("<img src=\"" + escapeUrl(node.getValue().toString()) + "\" alt=\"");
		node.childrenAccept(this);
		out.append("\" />");
	}
	
	public void visit(Link node) {
		out.append("<a href=\"" + escapeUrl(node.getValue().toString()) + "\">");
		node.childrenAccept(this);
		out.append("</a>");
	}
	
	public void visit(Strong node) {
		out.append("<strong>");
		node.childrenAccept(this);
		out.append("</strong>");
	}
	
	public void visit(Em node) {
		out.append("<em>");
		node.childrenAccept(this);
		out.append("</em>");
	}
	
	public void visit(Code node) {
		out.append("<code>");
		node.childrenAccept(this);
		out.append("</code>");
	}
		
	public void visit(Text node) {
		out.append(escape(node.getValue().toString()));
	}
	
	public String escape(String text) {
		return text.replaceAll("&", "&amp;")
				.replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;")
				.replaceAll("\"", "&quot;");
	}
	
	public void visit(LineBreak node) {
		if(hardwrap || node.isExplicit()) {
			out.append("<br>");
		}
		out.append("\n");
		indent();
		node.childrenAccept(this);
	}
	
	public String escapeUrl(String text) {
		return text.replaceAll(" ", "%20")
				.replaceAll("\"", "%22")
				.replaceAll("`", "%60")
				.replaceAll("<", "%3C")
				.replaceAll(">", "%3E")
				.replaceAll("\\[", "%5B")
				.replaceAll("\\]", "%5D")
				.replaceAll("\\\\", "%5C");
	}
	
	public void indent() {
		int repeat = level * 2;
		for (int i = repeat - 1; i >= 0; i--) {
		  out.append(" ");
		} 
	}

	public String getOutput() {
		if(!partial) {
			StringBuffer wrapper = new StringBuffer("<!DOCTYPE html>\n");
			wrapper.append("<html>\n");
			wrapper.append("  <body>\n");
			wrapper.append(out.toString().trim().replaceAll("(?m)^", "    ") + "\n");
			wrapper.append("  </body>\n");
			wrapper.append("</html>\n");
			return wrapper.toString();
		}
        return out.toString().trim();
    }

    public void setPartial(boolean partial) {
        this.partial = partial;
    }

    public void setHardwrap(boolean hardwrap) {
        this.hardwrap = hardwrap;
    }

    public void setHeadingIds(boolean headingIds) {
        this.headingIds = headingIds;
    }

}