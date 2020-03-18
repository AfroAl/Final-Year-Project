package fyp_project.detector;
import java.util.ArrayList;
import java.util.HashMap;

import fyp_project.detector.refactorings.Base64EncoderRefactor;
import fyp_project.detector.refactorings.CreateQueryRefactor;
import fyp_project.detector.refactorings.CryptoKeysRefactor;
import fyp_project.detector.refactorings.ExecuteQueryRefactor;
import fyp_project.detector.refactorings.InsecureRNGRefactor;
import fyp_project.detector.refactorings.LDAPRefactor;
import fyp_project.detector.refactorings.PrepareStatementRefactor;
import fyp_project.detector.refactorings.RegExRefactor;
import fyp_project.detector.refactorings.WebServerMainRefactor;
import fyp_project.detector.refactorings.XSSRefactor;

public class SecurityRefactorer {

    private ArrayList<String> lines;
    private HashMap<String, String> deletions = new HashMap<>();
    public SecurityRefactorer(ArrayList<String> fileLines) {
        lines = fileLines;
    }

    public String refactor(String key, String value) {

        String replacement = "";
        switch (key) {
            //SQL Injection
            case("SQL Injection - prepareStatement"):
                PrepareStatementRefactor prepareStatementRefactor = new PrepareStatementRefactor(value, lines);
                replacement = prepareStatementRefactor.refactor();
                deletions.put(key, prepareStatementRefactor.getDeletion());
                break;

            case("SQL Injection - createQuery"):
                CreateQueryRefactor createQueryRefactor = new CreateQueryRefactor(value);
                replacement = createQueryRefactor.refactor();
                deletions.put(key, createQueryRefactor.getDeletion());
                break;

            case("SQL Injection - executeQuery"):
                ExecuteQueryRefactor executeQueryRefactor = new ExecuteQueryRefactor(value, lines);
                replacement = executeQueryRefactor.refactor();
                deletions.put(key, executeQueryRefactor.getDeletion());
                break;

            case("XSS - Input Validation"):
                XSSRefactor xssRefactor = new XSSRefactor(value);
                replacement = xssRefactor.refactor();
                deletions.put(key, xssRefactor.getDeletion());
                break;

            //LDAP Injection
            case("LDAP - Unsafe Search"):
                LDAPRefactor ldapRefactor = new LDAPRefactor(value, lines);
                replacement = ldapRefactor.refactor();
                deletions.put(key, ldapRefactor.getDeletion());
                break;

            //RegEx Injection
            case("RegEx - ReDOS Prevention"):
                RegExRefactor regExRefactor = new RegExRefactor(value);
                replacement = regExRefactor.refactor();
                deletions.put(key, regExRefactor.getDeletion());
                break;

            //Crypto
            case("Sensitive Data Exposure - Insecure Cryptography"):
                CryptoKeysRefactor cryptoKeysRefactor = new CryptoKeysRefactor(value, lines);
                replacement = cryptoKeysRefactor.refactor();
                deletions.put(key, cryptoKeysRefactor.getDeletion());
                break;

            //Main method in Web App
            case("Sensitive Data Exposure - Main Method in Web App"):
                WebServerMainRefactor webServerMainRefactor = new WebServerMainRefactor(value);
                replacement = webServerMainRefactor.refactor();
                deletions.put(key, webServerMainRefactor.getDeletion());
                break;

            //Base64
            case("Sensitive Data Exposure - Base64 Encoding"):
                Base64EncoderRefactor base64EncoderRefactor = new Base64EncoderRefactor(value);
                replacement = base64EncoderRefactor.refactor();
                deletions.put(key, base64EncoderRefactor.getDeletion());
                break;

            //PRNG
            case("Sensitive Data Exposure - Insecure PRNG"):
                InsecureRNGRefactor insecureRNGRefactor = new InsecureRNGRefactor(value);
                replacement = insecureRNGRefactor.refactor();
                deletions.put(key, insecureRNGRefactor.getDeletion());
                break;
        }
        return replacement;
    }

    public HashMap<String, String> getDeletions() {
        return deletions;
    }
}
