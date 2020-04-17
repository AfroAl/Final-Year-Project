package fyp_project.detector.refactorings;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParameterisedStringRefactor {

    private ArrayList<String> lines;
    private String originalLine;
    private String stringName;
    private String spaces;
    private String deletion;
    private String statement;
    private ArrayList<String> variables = new ArrayList<>();
    private ArrayList<String> variableNames = new ArrayList<>();

    private String getStringName = "(?!String\\s*)(\\w+)(?=\\s*=\\s*\")";
    private Pattern getStringNamePattern = Pattern.compile(getStringName);

    private String getSpaces = "^\\s*//\\s*|^\\s*";
    private Pattern getSpacesPattern = Pattern.compile(getSpaces);

    private String getVariables = "('\"\\s*\\+\\s*\\w+(\\.\\w+\\(\\))*\\s*\\+\\s*\"')|(\"\\s*\\+\\s*\\w+(\\.\\w+\\(\\))*\\s*\\+\\s*\")|(\"'\"\\s*\\+\\s*\\w+(\\.\\w+\\(\\))*\\s*\\+\\s*\"'\")|(\"\\s*\\+\\s*\\w(\\.\\w+\\(\\))*)|('\"\\s*\\+\\s*\\w+(.\\w+)*\\s*\\+\\s*\"')|(\"\\s*\\+\\s*\\w+(.\\w+)*\\s*\\+\\s*\")|(\"'\"\\s*\\+\\s*\\w+(.\\w+)*\\s*\\+\\s*\"'\")|(\"\\s*\\+\\s*\\w+(.\\w+)*)|('\"\\s*\\+\\s*\\w+\\s*\\+\\s*\"')|(\"\\s*\\+\\s*\\w+\\s*\\+\\s*\")|(\"'\"\\s*\\+\\s*\\w+\\s*\\+\\s*\"'\")|(\"\\s*\\+\\s*\\w+)";
    private Pattern getVariablesPattern = Pattern.compile(getVariables);

    private String getVariableNames = "(\\w+((\\.\\w+\\(\\))|(\\.\\w+))*)";
    private Pattern getVariableNamesPattern = Pattern.compile(getVariableNames);

    private String getConnection = "(?=\\w+\\s*=)(\\w+)|(?=\\w+\\s*;)(\\w+)";
    private Pattern getConnectionPattern = Pattern.compile(getConnection);

    private String getStatement = "\\w+(?=\\.)";
    private Pattern getStatementPattern = Pattern.compile(getStatement);

    public ParameterisedStringRefactor(String line, ArrayList<String> fileLines) {
        lines = fileLines;
        originalLine = line;
    }

    public String getDeletion() { return deletion; }

    public String refactor() {
        String replacement = "";
        setFields();

        String sqlString = originalLine;

        for(String var : variables) {
            char c = var.charAt(var.length()-1);
            if(Character.isLetter(c)) {
                sqlString = sqlString.replace(var, "?\"");
            } else {
                sqlString = sqlString.replace(var, "? ");
            }
        }

        String newObjectName = "pstmt";
        String connection = "";
        int count = 1;
        for(int i=0; i<lines.size(); i++) {
            if(lines.get(i).contains(newObjectName)) {
                newObjectName = "pstmt" + count;
                i=0;
                count++;
            }
            if(lines.get(i).contains("Connection")) {
                Matcher connectionM = getConnectionPattern.matcher(lines.get(i));
                if(connectionM.find()) {
                    connection = connectionM.group();
                }
            }
        }

        boolean found = false;
        for(String l : lines) {
            if(l.equals(originalLine)) {
                found = true;
                continue;
            }

            if(found) {
                if(l.contains(stringName)) {
                    Matcher statementM = getStatementPattern.matcher(l);
                    if(statementM.find()) {
                        statement = statementM.group();
                        break;
                    }
                }
            }
        }

        StringBuilder setStrings = new StringBuilder();
        for(int i=0; i<variableNames.size()-1; i++) {
            setStrings.append(spaces).append(newObjectName).append(".setString(").append(i + 1).append(", ").append(variableNames.get(i)).append(");\r\n");
        }
        setStrings.append(spaces).append(newObjectName).append(".setString(").append(variableNames.size()).append(", ").append(variableNames.get(variableNames.size() - 1)).append(");");

        String preparedStatement = spaces + "PreparesStatement " + newObjectName + " = " + connection + ".prepareStatement(" + stringName + ");" + "\r\n";

        replacement = sqlString + "\r\n" + preparedStatement + setStrings + "\r\n" + (spaces + newObjectName + ".execute();") + "\r\n" + (spaces + newObjectName + ".close();");
        deletion = statement;
        return replacement;
    }

    private void setFields() {
        setStringName();
        setSpaces();
        setVariables();
        setVariableNames();
    }

    private void setSpaces() {
        Matcher spacesM = getSpacesPattern.matcher(originalLine);
        if(spacesM.find()) {
            spaces = spacesM.group();
        }
    }

    private void setStringName() {
        Matcher stringNameM = getStringNamePattern.matcher(originalLine);
        if(stringNameM.find()) {
            stringName = stringNameM.group();
        }
    }

    private void setVariables() {
        Matcher variablesM = getVariablesPattern.matcher(originalLine);
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

