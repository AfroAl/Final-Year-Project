package fyp_project.detector.refactorings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExRefactor {

    private String originalLine;
    private String deletion;
    private String spaces;

    private String getSpaces = "^\\s*//\\s*|^\\s*";
    private Pattern getSpacesPattern = Pattern.compile(getSpaces);

    public RegExRefactor(String line) {
        originalLine = line;
    }

    public String getDeletion() {
        return deletion;
    }

    public String refactor() {

        String replacement = originalLine;
        setSpaces();

        String warning  = "N.B. This regular expression is susceptible to Regular Expression Denial Of Service (ReDOS). This can force a web server to spend all its resources evaluating the regular expression. \r\nMore details on this can be found at https://owasp.org/www-community/attacks/Regular_expression_Denial_of_Service_-_ReDoS.";

        deletion = "";
        return replacement + "\r\n" + spaces + "// " + warning + "\r\n";
    }

    private void setSpaces() {
        Matcher spacesM = getSpacesPattern.matcher(originalLine);
        if(spacesM.find()) {
            spaces = spacesM.group();
        }
    }

}
