package fyp_project.detector.refactorings;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CryptoKeysRefactor {

    private String originalLine;
    private String deletion = "";
    private ArrayList<String> lines;
    private String objectName;
    private String bracketedString;
    private String instance;
    private String keygen;

    private String getObjectName = "(\\w+\\.)";
    private Pattern getObjectNamePattern = Pattern.compile(getObjectName);

    private String getInstance = "(\\.getInstance\\(\"\\w+\"\\))";
    private Pattern getInstancePattern = Pattern.compile(getInstance);

    private String getBracketedString = "\\([\\s\\S]+?\\)";
    private Pattern getBracketedStringPattern = Pattern.compile(getBracketedString);

    public CryptoKeysRefactor(String line, ArrayList<String> fileLines) {
        originalLine = line;
        lines = fileLines;
    }

    public String getDeletion() {
        return deletion;
    }

    public String refactor() {

        String replacement = originalLine;
        String value;
        String newValue;
        keygen = "";

        setObjectName();
        for(String line : lines) {
            if(line.contains(objectName) && line.contains("getInstance")) {
                keygen = line;
                break;
            }
        }

        setInstance();
        setBracketedString();
        value = bracketedString.substring(1, bracketedString.length() - 1);
        newValue = value;

        if(instance.contains("AES")) {
            if(Integer.parseInt(value) < 128) {
                newValue = String.valueOf(128);
            }
        } else if(instance.contains("RSA")) {
            if(Integer.parseInt(value) < 2048) {
                newValue = String.valueOf(2048);
            }
        }

        replacement = replacement.replace(value, newValue);
        replacement = replacement + " // Safe";

        deletion = "";
        return replacement;
    }

    private void setObjectName() {
        Matcher objectNameM = getObjectNamePattern.matcher(originalLine);
        if(objectNameM.find()) {
            objectName = objectNameM.group();
            objectName = objectName.substring(0, objectName.length()-1);
        }
    }

    private void setBracketedString() {
        Matcher bracketedStringM = getBracketedStringPattern.matcher(originalLine);
        if(bracketedStringM.find()) {
            bracketedString = bracketedStringM.group();
        }
    }

    private void setInstance() {
        Matcher instanceM = getInstancePattern.matcher(keygen);
        if(instanceM.find()) {
            instance = instanceM.group();
        }
    }
}
