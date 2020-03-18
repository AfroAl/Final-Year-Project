package fyp_project.detector.patterns;

import java.util.regex.Pattern;

public class XSSPattern {

    private static String regex = "(\\w+\\.getParameter\\(\"\\w+\"\\))";
    private static Pattern pattern = Pattern.compile(regex);

    public XSSPattern() {}

    public static Pattern getPattern() {
        return pattern;
    }
}
