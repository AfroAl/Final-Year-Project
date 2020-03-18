package fyp_project.detector.patterns;

import java.util.regex.Pattern;

public class CryptoKeysPattern {

    private static String regex = "(\\.initialize\\(\\d+\\))|(\\.init\\(\\d+\\))";
    private static Pattern pattern = Pattern.compile(regex);

    public CryptoKeysPattern() { }

    public static Pattern getPattern() {
        return pattern;
    }
}