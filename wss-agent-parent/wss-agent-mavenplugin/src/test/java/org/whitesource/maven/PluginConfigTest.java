package org.whitesource.maven;

import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Edo.Shor
 */
public class PluginConfigTest {

    @Test
    public void testMatchPattern() {
        String regex = "org.whitesource:*".replace(".", "\\.").replace("*", ".*");
        assertTrue("org.whitesource:agent:1.0.0".matches(regex));
        assertFalse("orgawhitesource:agent:1.0.0".matches(regex));
        assertFalse("org.whitesource.agent:1.0.0".matches(regex));

        regex = "*white*:*:1.2".replace(".", "\\.").replace("*", ".*");
        assertTrue("org.whitesource:agent:1.2".matches(regex));
        assertFalse("orgawhitesource:agent:1.0.0".matches(regex));
        assertFalse("com.whitesource.agent:1.0.0".matches(regex));

    }
}
