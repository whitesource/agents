package org.whitesource.agent.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link JavaScriptParser#parse(String)}, focused on the
 * {@code contentWithoutHeaderComments} output that feeds the
 * {@code SHA1_NO_HEADER} checksum.
 * <p>
 * Reproduces TKA-8744 / SCA-5117: pseudo-elements.js (and any single-line
 * header comment with no trailing newline) was producing a stray
 * trailing character in the headerless content, causing hash collisions in
 * the Index (e.g. with pnotify.tooltip-2.0.0.js).
 */
public class JavaScriptParserTest {

    @Test
    public void headerCommentEndingWithP_isFullyStripped() {
        String fileContent = "//# sourceMappingURL=IRotate.js.map";
        ParseResult parseResult = new JavaScriptParser().parse(fileContent);
        String headerlessContent = parseResult.getContentWithoutHeaderComments();
        Assert.assertNotNull(headerlessContent);
        Assert.assertEquals(0, headerlessContent.length());
    }

    @Test
    public void lineCommentEndingInArbitraryChar_isFullyStripped() {
        // The Rhino EOF bug truncates the comment value by one char regardless
        // of which character is last; "Tooltip" ends in 'p' and was the
        // observed collision driver, but the fix must work for any tail char.
        String fileContent = "// Tooltip";
        ParseResult parseResult = new JavaScriptParser().parse(fileContent);
        String headerlessContent = parseResult.getContentWithoutHeaderComments();
        Assert.assertNotNull(headerlessContent);
        Assert.assertEquals("", headerlessContent);
    }

    // Exact content of the customer file from TKA-8744.
    @Test
    public void pseudoElementsCustomerFile_isFullyStripped() {
        String fileContent = "//# sourceMappingURL=pseudo-elements.js.map";
        ParseResult parseResult = new JavaScriptParser().parse(fileContent);
        String headerlessContent = parseResult.getContentWithoutHeaderComments();
        Assert.assertNotNull(headerlessContent);
        Assert.assertEquals("", headerlessContent);
    }

    /* --- Header stripping correctness --- */

    @Test
    public void lineHeaderFollowedByCode_keepsCode() {
        String fileContent = "// header\nvar x = 1;";
        ParseResult parseResult = new JavaScriptParser().parse(fileContent);
        Assert.assertEquals("var x = 1;", parseResult.getContentWithoutHeaderComments());
    }

    @Test
    public void blockHeaderFollowedByCode_keepsCode() {
        String fileContent = "/* header */\ncode;";
        ParseResult parseResult = new JavaScriptParser().parse(fileContent);
        Assert.assertEquals("code;", parseResult.getContentWithoutHeaderComments());
    }

    @Test
    public void stackedLineHeaders_areAllStripped() {
        String fileContent = "// a\n// b\ncode;";
        ParseResult parseResult = new JavaScriptParser().parse(fileContent);
        Assert.assertEquals("code;", parseResult.getContentWithoutHeaderComments());
    }

    // Guards against the unanchored String.replace bug: a comment string that
    // also occurs later in the file must NOT be stripped from that later
    // occurrence — only the leading header is a "header".
    @Test
    public void duplicateHeaderStringInBody_onlyLeadingOccurrenceIsStripped() {
        String fileContent = "// dup\ncode;\n// dup";
        ParseResult parseResult = new JavaScriptParser().parse(fileContent);
        Assert.assertEquals("code;\n// dup", parseResult.getContentWithoutHeaderComments());
    }
}
