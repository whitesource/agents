package org.whitesource.agent.parser;

import org.apache.commons.lang.StringUtils;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Parser;
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

    private static final String COMMENT_REGEX = "(/\\*[\\s\\S]*?\\*/)";

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
     * Go over each comment and remove from content until reaching the beginning of the actual code.
     */
    private String removeHeaderComments(String fileContent, SortedSet<Comment> comments) {
        String headerlessFileContent = fileContent;
        for (Comment comment : comments) {
            String commentValue = comment.getValue();
            if (headerlessFileContent.startsWith(commentValue)) {
                headerlessFileContent = headerlessFileContent.replace(commentValue, EMPTY_STRING);
                // remove all leading white spaces and new line characters
                while (StringUtils.isNotBlank(headerlessFileContent) && Character.isWhitespace(headerlessFileContent.charAt(0))) {
                    headerlessFileContent = headerlessFileContent.substring(1);
                }
            } else {
                // finished removing all header comments
                break;
            }
        }
        return headerlessFileContent;
    }
}