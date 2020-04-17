package fyp_project.detector.refactorings;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExecuteQueryRefactor {

    private ArrayList<String> lines;
    private String originalLine;
    private String objectName;
    private String bracketedString;
    private String spaces;
    private String resultSet;
    private String deletion;
    private String stringName = "";
    private ArrayList<String> variables = new ArrayList<>();
    private ArrayList<String> variableNames = new ArrayList<>();

    private String getObjectName = "(\\w+)(?=\\.execute)";
    private Pattern getObjectNamePattern = Pattern.compile(getObjectName);

    private String getBracketedString = "\\(\\s*\"[\\s\\S]+?\\)";
    private Pattern getBracketedStringPattern = Pattern.compile(getBracketedString);

    private String getVariables = "('\"\\s*\\+\\s*\\w+(\\.\\w+\\(\\))*\\s*\\+\\s*\"')|(\"\\s*\\+\\s*\\w+(\\.\\w+\\(\\))*\\s*\\+\\s*\")|(\"'\"\\s*\\+\\s*\\w+(\\.\\w+\\(\\))*\\s*\\+\\s*\"'\")|(\"\\s*\\+\\s*\\w(\\.\\w+\\(\\))*)|('\"\\s*\\+\\s*\\w+(.\\w+)*\\s*\\+\\s*\"')|(\"\\s*\\+\\s*\\w+(.\\w+)*\\s*\\+\\s*\")|(\"'\"\\s*\\+\\s*\\w+(.\\w+)*\\s*\\+\\s*\"'\")|(\"\\s*\\+\\s*\\w+(.\\w+)*)|('\"\\s*\\+\\s*\\w+\\s*\\+\\s*\"')|(\"\\s*\\+\\s*\\w+\\s*\\+\\s*\")|(\"'\"\\s*\\+\\s*\\w+\\s*\\+\\s*\"'\")|(\"\\s*\\+\\s*\\w+)";
    private Pattern getVariablesPattern = Pattern.compile(getVariables);

    private String getVariableNames = "(\\w+((\\.\\w+\\(\\))|(\\.\\w+))*)";
    private Pattern getVariableNamesPattern = Pattern.compile(getVariableNames);

    private String getSpaces = "^\\s*//\\s*|^\\s*";
    private Pattern getSpacesPattern = Pattern.compile(getSpaces);

    private String getResultSet = "(ResultSet\\s*\\w+\\s*=\\s*)";
    private Pattern getResultSetPattern = Pattern.compile(getResultSet);

    private String getStringName = "(?!String\\s*)(\\w+)(?=\\s*=\\s*\")";
    private Pattern getStringNamePattern = Pattern.compile(getStringName);

    private String getConnection = "(?=\\w+\\s*=)(\\w+)|(?=\\w+\\s*;)(\\w+)";
    private Pattern getConnectionPattern = Pattern.compile(getConnection);

    public ExecuteQueryRefactor(String line, ArrayList<String> fileLines) {
        lines = fileLines;
        originalLine = line;
    }

    public String getDeletion() {
        return deletion;
    }

    public String refactor() {
        setFields();
        String sqlString = "";

        for(String var : variables) {
            char c = var.charAt(var.length()-1);
            if(Character.isLetter(c)) {
                sqlString = bracketedString.replace(var, "?\"");
            } else {
                sqlString = bracketedString.replace(var, "? ");
            }
        }

        String originalSQLString = bracketedString.substring(1, bracketedString.length()-1);
        if(!sqlString.equals("")) {
            sqlString = sqlString.substring(1, sqlString.length()-1);
        } else {
            sqlString = originalSQLString;
        }

        for (String line : lines) {
            if (line.contains(sqlString)) {
                Matcher stringNameM = getStringNamePattern.matcher(line);
                if (stringNameM.find()) {
                    stringName = stringNameM.group();
                }
                break;
            }
        }

        int count = 1;
        String sqlStringFinal = "";
        if(stringName.equals("")) {
            stringName = "query";
            for(int i=0; i<lines.size(); i++) {
                if(lines.get(i).contains(stringName)) {
                    stringName = "query" + count;
                    i=0;
                    count++;
                }
            }

            sqlStringFinal = spaces + "String " + stringName + " = " + sqlString + ";" + "\r\n";
        }

        String replacement = "";
        if(originalLine.contains("createStatement")) {

            originalLine = originalLine.replace(sqlString, stringName);
            replacement = sqlStringFinal + originalLine;

            deletion = "";
            return replacement;
        } else {
            String newObjectName = "pstmt";
            String connection = "";
            count = 1;
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


            String prepareStatement = spaces + "PreparedStatement " + newObjectName + " = " + connection + ".prepareStatement(" + stringName + ")";
            resultSet = originalLine.replace(originalSQLString, "");
            resultSet = resultSet.replace(objectName, newObjectName);

            StringBuilder setStrings = new StringBuilder();
            for(int i=0; i<variableNames.size()-1; i++) {
                setStrings.append(spaces).append(newObjectName).append(".setString(").append(i + 1).append(", ").append(variableNames.get(i)).append(");\r\n");
            }
            setStrings.append(spaces).append(newObjectName).append(".setString(").append(variableNames.size()).append(", ").append(variableNames.get(variableNames.size() - 1)).append(");");


            replacement = sqlStringFinal + prepareStatement + "\r\n" + setStrings + "\r\n" + resultSet + "\r\n" + newObjectName + ".close()";

            deletion = objectName;
            return replacement;
        }
    }

    private void setFields() {
        setObjectName();
        setResultSet();
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

    private void setResultSet() {
        Matcher resultSetM = getResultSetPattern.matcher(originalLine);
        if(resultSetM.find()) {
            resultSet = resultSetM.group();
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
