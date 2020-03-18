package fyp_project.detector.patterns;

import java.util.regex.Pattern;

public class RegExPattern {

    private static String regex = "(=\\s*\"\\S*\")";
    private static Pattern pattern = Pattern.compile(regex);

    public RegExPattern() { }

    public static Pattern getPattern() {
        return pattern;
    }
}
