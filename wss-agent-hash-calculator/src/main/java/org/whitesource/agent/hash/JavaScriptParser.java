package org.whitesource.agent.hash;

import org.apache.commons.lang.StringUtils;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.Comment;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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

    /* --- Public methods --- */

    /**
     * Parse .js file content and remove all header comments.
     *
     * @param fileContent File content.
     *
     * @return The file content with the header comments removed.
     */
    public String removeHeaderComments(String fileContent) {
        String result = null;
        try {
            // parse JavaScript file and get comments
            AstRoot astRoot = parse(fileContent);
            SortedSet<Comment> comments = astRoot.getComments();
            if (!comments.isEmpty()) {
                result = fileContent;
            }

            // go over each comment and remove from content until reaching the beginning of the actual code
            for (Comment comment : comments) {
                String commentValue = comment.getValue();
                if (result.startsWith(commentValue)) {
                    result = result.replace(commentValue, EMPTY_STRING);
                    // remove all leading white spaces and new line characters
                    while (StringUtils.isNotBlank(result) && Character.isWhitespace(result.charAt(0))) {
                        result = result.substring(1);
                    }
                } else {
                    // finished removing all header comments
                    break;
                }
            }
        } catch (Exception e) {
            logger.debug("Error parsing JavaScript file: {}", e.getCause());
        }
        return result;
    }

    /* --- Private methods --- */

    private AstRoot parse(String fileContent) throws IOException {
        // Setup environment and initialize the parser.
        CompilerEnvirons environment = new CompilerEnvirons();
        environment.setLanguageVersion(180);
        environment.setStrictMode(false);
        environment.setRecordingComments(true);
        environment.setAllowSharpComments(true);
        environment.setRecordingLocalJsDocComments(true);

        // IMPORTANT: the parser can be used only once!
        Parser parser = new Parser(environment);
        return parser.parse(new StringReader(fileContent), null, 1);
    }
}