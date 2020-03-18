package fyp_project.detector.patterns;
import java.util.regex.Pattern;

public class PrepareStatementPattern {

    private static String regex = "prepareStatement\\s*[(]\\s*\"(select|create|alter|delete|insert|update|with|grant|revoke|savepoint|drop|truncate)";
    private static Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

    public PrepareStatementPattern() { }

    public static Pattern getPattern() {
        return pattern;
    }
}