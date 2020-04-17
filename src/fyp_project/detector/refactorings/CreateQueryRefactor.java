package fyp_project.detector.refactorings;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateQueryRefactor {

    private String originalLine;
    private String objectName;
    private String bracketedString;
    private String spaces;
    private String deletion;

    private ArrayList<String> variables = new ArrayList<>();
    private ArrayList<String> variableNames = new ArrayList<>();

    private String getObjectName = "(\\w+\\s*=\\s*\\w)";
    private Pattern getObjectNamePattern = Pattern.compile(getObjectName);

    private String getBracketedString = "\\([\\s\\S]+?\\)";
    private Pattern getBracketedStringPattern = Pattern.compile(getBracketedString);

    private String getVariables = "('\"\\s*\\+\\s*\\w+(\\.\\w+\\(\\))*\\s*\\+\\s*\"')|(\"\\s*\\+\\s*\\w+(\\.\\w+\\(\\))*\\s*\\+\\s*\")|(\"'\"\\s*\\+\\s*\\w+(\\.\\w+\\(\\))*\\s*\\+\\s*\"'\")|(\"\\s*\\+\\s*\\w(\\.\\w+\\(\\))*)|('\"\\s*\\+\\s*\\w+(.\\w+)*\\s*\\+\\s*\"')|(\"\\s*\\+\\s*\\w+(.\\w+)*\\s*\\+\\s*\")|(\"'\"\\s*\\+\\s*\\w+(.\\w+)*\\s*\\+\\s*\"'\")|(\"\\s*\\+\\s*\\w+(.\\w+)*)|('\"\\s*\\+\\s*\\w+\\s*\\+\\s*\"')|(\"\\s*\\+\\s*\\w+\\s*\\+\\s*\")|(\"'\"\\s*\\+\\s*\\w+\\s*\\+\\s*\"'\")|(\"\\s*\\+\\s*\\w+)";
    private Pattern getVariablesPattern = Pattern.compile(getVariables);

    private String getVariableNames = "(\\w+((\\.\\w+\\(\\))|(\\.\\w+))*)";
    private Pattern getVariableNamesPattern = Pattern.compile(getVariableNames);

    private String getSpaces = "^\\s*//\\s*|^\\s*";
    private Pattern getSpacesPattern = Pattern.compile(getSpaces);

    public CreateQueryRefactor(String line) {
        originalLine = line;
    }

    public String getDeletion() {
        return deletion;
    }

    public String refactor() {
        setFields();
        String sqlString = "";

        if(originalLine.contains("?")) {
            deletion = "";
            return originalLine;
        }

        for(String var : variables) {
            char c = var.charAt(var.length()-1);
            if(Character.isLetter(c)) {
                sqlString = bracketedString.replace(var, " ?\"");
            } else {
                sqlString = bracketedString.replace(var, " ? ");
            }
        }

        if(sqlString.equals("")) {
            String replacement = "";
            String sqlStringFinal = spaces + "String createQuery1" + bracketedString.substring(1, bracketedString.length()-1) + ";" + "\r\n";
            replacement = sqlStringFinal + originalLine.replace(bracketedString.substring(1, bracketedString.length()-1), "createQuery1");

            deletion = "";
            return replacement;
        }

        String newCreateQuery = originalLine.replace(bracketedString, sqlString);

        StringBuilder setStrings = new StringBuilder();
        for(int i=0; i<variableNames.size()-1; i++) {
            setStrings.append(spaces).append(objectName).append(" = ").append(objectName).append(".setParameter(").append(i).append(", ").append(variableNames.get(i)).append(");\r\n");
        }
        setStrings.append(spaces).append(objectName).append(" = ").append(objectName).append(".setParameter(").append(variableNames.size() - 1).append(", ").append(variableNames.get(variableNames.size() - 1)).append(");");


        String replacement = newCreateQuery + "\r\n" + setStrings;

        deletion = "";
        return replacement;
    }

    private void setFields() {
        setObjectName();
        setBracketedString();
        setSpaces();
        setVariables();
        setVariableNames();
    }

    private void setObjectName() {
        Matcher objectNameM = getObjectNamePattern.matcher(originalLine);
        if(objectNameM.find()) {
            objectName = objectNameM.group();
            String[] arr = objectName.split(" ", 2);
            objectName = arr[0];
        }
    }

    private void setBracketedString() {
        Matcher bracketedStringM = getBracketedStringPattern.matcher(originalLine);
        if(bracketedStringM.find()) {
            bracketedString = bracketedStringM.group();
        }
    }

    private void setSpaces() {
        Matcher spacesM = getSpacesPattern.matcher(originalLine);
        if(spacesM.find()) {
            spaces = spacesM.group();
        }
    }

    private void setVariables() {
        Matcher variablesM = getVariablesPattern.matcher(bracketedString);
        int i=0;
        while(variablesM.find()) {
            variables.add(variablesM.group(i));
            i++;
        }
    }

    private void setVariableNames() {
        for(String var : variables) {
            Matcher variableNameM = getVariableNamesPattern.matcher(var);
            if(variableNameM.find()) {
                variableNames.add(variableNameM.group());
            }
        }
    }
}
