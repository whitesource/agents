package org.whitesource.agent.parser;

import org.apache.commons.lang3.StringUtils;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.Comment;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.util.SortedSet;

/**
 * Parser for JavaScript files.
 *
 * @author tom.shapira
 */
public class JavaScriptParser {

    /* --- Static members --- */

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(JavaScriptParser.class);

    private static final String EMPTY_STRING = "";

    private static final String LINE_COMMENT_PREFIX = "//";
    private static final String HTML_OPEN_COMMENT_PREFIX = "<!--";
    private static final String HTML_CLOSE_COMMENT_PREFIX = "-->";

    /* --- Public methods --- */

    /**
     * Parse the file content and return variants of the file.
     *
     * @param fileContent to parse
     * @return the parsing result
     */
    public ParseResult parse(String fileContent) {
        ParseResult result = null;

        // setup environment and initialize the parser.
        CompilerEnvirons environment = new CompilerEnvirons();
        environment.setLanguageVersion(180);
        environment.setStrictMode(false);
        environment.setRecordingComments(true);
        environment.setAllowSharpComments(true);
        environment.setRecordingLocalJsDocComments(true);

        // IMPORTANT: the parser can only be used once!
        Parser parser = new Parser(environment);
        try {
            AstRoot root = parser.parse(new StringReader(fileContent), null, 1);
            result = new ParseResult();

            // remove all comments
            result.setContentWithoutComments(root.toSource());

            // remove all header comments
            SortedSet<Comment> comments = root.getComments();
            if (comments != null && !comments.isEmpty()) {
                String headerlessFileContent = removeHeaderComments(fileContent, comments);
                result.setContentWithoutHeaderComments(headerlessFileContent);
            }
        } catch (Exception e) {
            logger.debug("Error parsing JavaScript file: {}", e.getMessage());
        }
        return result;
    }

    /* --- Private methods --- */

    /**
     * Strip leading comments (and the whitespace between them) until reaching
     * the first non-comment content.
     *
     * For line-style comments (LINE / HTML) we scan to the next line terminator
     * rather than relying on {@link Comment#getValue()}: Rhino 1.7.7.2 returns
     * a value that is one character short when the comment is not newline-
     * terminated (e.g. at EOF), which would leak the trailing character of the
     * file into the headerless content and produce hash collisions in the
     * Index. See TKA-8744 / SCA-5117.
     *
     * For block-style comments (BLOCK / JSDOC) the closing &#42;/ delimiter is
     * required to terminate the comment, so {@link Comment#getValue()} is
     * reliable. Stripping is anchored at offset 0 ({@code substring}) instead
     * of using {@code String.replace}, which would also strip later in-body
     * occurrences of the same string.
     */
    private String removeHeaderComments(String fileContent, SortedSet<Comment> comments) {
        String headerlessFileContent = fileContent;
        for (Comment comment : comments) {
            String afterRemoval = removeLeadingHeader(headerlessFileContent, comment);
            if (afterRemoval == null) {
                // no longer at a header — body reached
                break;
            }
            headerlessFileContent = StringUtils.stripStart(afterRemoval, null);
        }
        return headerlessFileContent;
    }

    /**
     * Returns the content with the given header comment removed from offset 0,
     * or {@code null} if the content does not currently start with that
     * comment (i.e. we've reached the body of the file).
     */
    private String removeLeadingHeader(String content, Comment comment) {
        Token.CommentType type = comment.getCommentType();
        if (type == Token.CommentType.LINE || type == Token.CommentType.HTML) {
            if (!startsWithLineCommentDelimiter(content)) {
                return null;
            }
            int terminatorIdx = indexOfLineTerminator(content);
            return terminatorIdx < 0 ? EMPTY_STRING : content.substring(terminatorIdx);
        }
        String commentValue = comment.getValue();
        if (!content.startsWith(commentValue)) {
            return null;
        }
        return content.substring(commentValue.length());
    }

    private static boolean startsWithLineCommentDelimiter(String s) {
        return s.startsWith(LINE_COMMENT_PREFIX)
                || s.startsWith(HTML_OPEN_COMMENT_PREFIX)
                || s.startsWith(HTML_CLOSE_COMMENT_PREFIX);
    }

    private static int indexOfLineTerminator(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\n' || c == '\r') {
                return i;
            }
        }
        return -1;
    }
}
