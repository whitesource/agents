package org.whitesource.agent.hash;

import org.apache.commons.lang.StringUtils;

/**
 * Utility class for regular expressions related functionality.
 *
 * @author Edo.Shor
 */
public final class RegexUtils {

	/* --- Public methods --- */

    public static String toJava(String regex) {
        if (StringUtils.isBlank(regex)) {
            return regex;
        } else {
            return regex.replaceAll("\\.", "\\\\.")
                    .replaceAll("\\*",".*");
        }
    }

	/* --- Constructors --- */

    /**
     * Private default constructor
     */
    private RegexUtils() {
        // avoid instantiation
    }
}
