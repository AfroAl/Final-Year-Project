package fyp_project.detector.patterns;

import java.util.regex.Pattern;

public class WebServerMainPattern {

    private static String regex = "(class\\s*\\w+\\s*extends)";
    private static Pattern pattern = Pattern.compile(regex);

    public WebServerMainPattern() { }

    public static Pattern getPattern() {
        return pattern;
    }
}
