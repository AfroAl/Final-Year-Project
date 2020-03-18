package fyp_project.detector.patterns;

import java.util.regex.Pattern;

public class InsecureRNGPattern {

    private static String regex = "(new\\s+Random\\([\\S]*\\))";
    private static Pattern pattern = Pattern.compile(regex);

    public InsecureRNGPattern() { }

    public static Pattern getPattern() {
        return pattern;
    }
}
