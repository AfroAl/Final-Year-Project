package fyp_project.detector.patterns;

import java.util.regex.Pattern;

public class Base64EncoderPattern {

    private static String regex = "(Base64Encoder|Base64.getEncoder)";
    private static Pattern pattern = Pattern.compile(regex);

    public Base64EncoderPattern() { }

    public static Pattern getPattern() {
        return pattern;
    }
}
