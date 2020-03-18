package fyp_project.detector.refactorings;

import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XSSRefactor {

    private String originalLine;
    private String deletion;
    private String spaces;
    private String objectName;

    private String getObjectName = "(\\w+\\s*=\\s*\\w)";
    private Pattern getObjectNamePattern = Pattern.compile(getObjectName);

    private String getSpaces = "^\\s*//\\s*|^\\s*";
    private Pattern getSpacesPattern = Pattern.compile(getSpaces);

    public XSSRefactor(String line) {
        originalLine = line;
    }

    public String getDeletion() {
        return deletion;
    }

    public String refactor() {

        StringBuilder replacement = new StringBuilder(originalLine);
        setFields();

        String regex = "<\\w+>|<\\w+\\s";
        String checkerLine1 = spaces + "if(" + objectName + ".matches(\"" + regex + "\")) {\r\n";
        String checkerLine2 = spaces + "    throw IllegalArgumentException(\"Invalid Input\");\r\n";
        String checkerLine3 = spaces + "}";
        String checker = checkerLine1 + checkerLine2 + checkerLine3;

        boolean answer = false;
        while(!answer) {
            String isVariableCheckedAlready = JOptionPane.showInputDialog(null, "Has this line already been checked? (yes/no)", originalLine, JOptionPane.INFORMATION_MESSAGE);
            if(isVariableCheckedAlready.equals("yes")) {
                replacement.append("// Checked");
                answer = true;
            } else if(isVariableCheckedAlready.equals("no")) {
                replacement.append("// Checked").append("\r\n").append(checker).append("\r\n");
                answer = true;
            }
        }

        deletion = "";
        return replacement.toString();
    }

    private void setFields() {
        setObjectName();
        setSpaces();
    }

    private void setObjectName() {
        Matcher objectNameM = getObjectNamePattern.matcher(originalLine);
        if(objectNameM.find()) {
            objectName = objectNameM.group();
            String[] arr = objectName.split(" ", 2);
            objectName = arr[0];
        }
    }

    private void setSpaces() {
        Matcher spacesM = getSpacesPattern.matcher(originalLine);
        if(spacesM.find()) {
            spaces = spacesM.group();
        }
    }

}
