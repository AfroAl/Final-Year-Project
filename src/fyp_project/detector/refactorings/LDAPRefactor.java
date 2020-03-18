package fyp_project.detector.refactorings;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LDAPRefactor {

    private String originalLine;
    private String deletion;
    private String spaces;
    private String objectName;
    private String namingEnumeration = "";
    private ArrayList<String> lines;
    private ArrayList<String> variables = new ArrayList<>();
    private ArrayList<String> variableNames = new ArrayList<>();

    private String getObjectName = "(\\w+\\s*=\\s*\\w+\\.)";
    private Pattern getObjectNamePattern = Pattern.compile(getObjectName);

    private String getVariables = "(\"\\s*\\+\\s*\\w+\\s*\\+\\s*\")|(\"'\"\\s*\\+\\s*\\w+\\s*\\+\\s*\"'\")|(\"\\s*\\+\\s*\\w+)";
    private Pattern getVariablesPattern = Pattern.compile(getVariables);

    private String getVariableNames = "([\\w]+)";
    private Pattern getVariableNamesPattern = Pattern.compile(getVariableNames);

    private String getSpaces = "^\\s*//\\s*|^\\s*";
    private Pattern getSpacesPattern = Pattern.compile(getSpaces);

    private String getParameters = "\\([\\s\\S]*,";
    private Pattern getParametersPattern = Pattern.compile(getParameters);

    public String getDeletion() {
        return deletion;
    }

    public LDAPRefactor(String line, ArrayList<String> fileLines) {
        lines = fileLines;
        originalLine = line;
    }

    public String refactor() {

        setFields();
        String replacement = originalLine;

        int i=0;
        for(String var : variables) {
            replacement = replacement.replace(var, "{"+ i +"}");
            i++;
        }

        boolean bool = false;
        for(String line : lines) {
            if(line.equals(originalLine)) {
                   bool = true;
            }
            if(bool) {
                setObjectName(line);
                if(line.contains("NamingEnumeration<SearchResult>") && line.contains(objectName)) {
                    namingEnumeration = line;
                    break;
                }
            }
        }

        StringBuilder newParameter = new StringBuilder(" new String[]{");
        for(i=0; i<variableNames.size() - 1; i++) {
            newParameter.append(variableNames.get(i)).append(", ");
        }
        newParameter.append(variableNames.get(variableNames.size() - 1)).append("},");

        Matcher m = getParametersPattern.matcher(namingEnumeration);
        String oldNamingEnumeration = namingEnumeration;
        if(m.find()) {
            namingEnumeration = namingEnumeration.replace(m.group(), m.group() + newParameter);
        }

        replacement = replacement + "\r\n" + spaces + namingEnumeration;

        deletion = oldNamingEnumeration;
        return replacement;
    }

    private void setFields() {
        setVariables();
        setVariableNames();
        setSpaces();
    }

    // Get all variable shapes
    private void setVariables() {
        Matcher variablesM = getVariablesPattern.matcher(originalLine);
        int i=0;
        while(variablesM.find()) {
            variables.add(variablesM.group(i));
            i++;
        }
    }

    // Get all variable names
    private void setVariableNames() {
        for(String var : variables) {
            Matcher variableNameM = getVariableNamesPattern.matcher(var);
            if(variableNameM.find()) {
                variableNames.add(variableNameM.group());
            }
        }
    }

    // Get spaces at the start of the line
    private void setSpaces() {
        Matcher spacesM = getSpacesPattern.matcher(originalLine);
        if(spacesM.find()) {
            spaces = spacesM.group();
        }
    }

    // Get the name of the variable in the given line
    private void setObjectName(String line) {
        Matcher objectNameM = getObjectNamePattern.matcher(line);
        if(objectNameM.find()) {
            objectName = objectNameM.group();
            String[] arr = objectName.split(" ", 2);
            objectName = arr[0];
        }
    }
}
