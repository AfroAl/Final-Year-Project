package fyp_project.detector.patterns;

import java.util.regex.Pattern;

public class CreateQueryPattern {

    private static String regex = "createQuery\\s*[(]\\s*\"(select|create|alter|delete|insert|update|with|grant|revoke|savepoint|drop|truncate|from)";
    private static Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

    public CreateQueryPattern() { }

    public static Pattern getPattern() {
        return pattern;
    }
}
