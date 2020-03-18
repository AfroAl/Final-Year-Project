package fyp_project.detector.patterns;

import java.util.regex.Pattern;

public class LDAPPattern {

    private static String regex = "(\\(&\\(\\w+\\s*=\"\\s*\\+\\s*\\w+\\s*\\+\\s*\"\\))|(\\(&\\(\\w+\\s*='\\s*\\+\\s*\\w+\\s*\\+\\s*'\\))";
    private static Pattern pattern = Pattern.compile(regex);

    public LDAPPattern() { }

    public static Pattern getPattern() {
        return pattern;
    }
}
