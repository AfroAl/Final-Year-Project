package fyp_project.detector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fyp_project.detector.patterns.Base64EncoderPattern;
import fyp_project.detector.patterns.CreateQueryPattern;
import fyp_project.detector.patterns.CryptoKeysPattern;
import fyp_project.detector.patterns.ExecuteQueryPattern;
import fyp_project.detector.patterns.InsecureRNGPattern;
import fyp_project.detector.patterns.LDAPPattern;
import fyp_project.detector.patterns.PrepareStatementPattern;
import fyp_project.detector.patterns.RegExPattern;
import fyp_project.detector.patterns.WebServerMainPattern;
import fyp_project.detector.patterns.XSSPattern;

public class SecurityMatcher
{
    private HashMap<Integer , Pattern> patterns = new HashMap<>();
    private String[] keys = {"SQL Injection - prepareStatement", "SQL Injection - executeQuery", "SQL Injection - createQuery", "LDAP - Unsafe Search",
                             "RegEx - ReDOS Prevention", "XSS - Input Validation", "Sensitive Data Exposure - Insecure Cryptography",
                             "Sensitive Data Exposure - Main Method in Web App", "Sensitive Data Exposure - Base64 Encoding", "Sensitive Data Exposure - Insecure PRNG"};
    
    private String[] desc = { "<html><body><p><b><u>SQL Injection - prepareStatement</u></b></p> <br> <p><b>Exploitability: 3\u00A0\u00A0\u00A0\u00A0Prevalence: 2\u00A0\u00A0\u00A0\u00A0Detectability: 3\u00A0\u00A0\u00A0\u00A0Technical: 3</b></p> <br> <p>A SQL injection attack consists of insertion or “injection” of a SQL query via the input data from the client to the application. A successful SQL injection exploit can read sensitive data from the database, modify database data (Insert/Update/Delete), execute administration operations on the database (such as shutdown the DBMS), recover the content of a given file present on the DBMS file system and in some cases issue commands to the operating system. SQL injection attacks are a type of injection attack, in which SQL commands are injected into data-plane input in order to effect the execution of predefined SQL commands.</p> <br> <p>To prevent SQL Injections we must write parameterized queries. To create perameterised query in java we have PreparedStatement. It can take parameters by passing question marks (?) in the query and then by replacing each question mark index with required values.</p></body></html>",
    							"<html><body><p><b><u>SQL Injection - executeQuery</u></b></p> <br> <p><b>Exploitability: 3\u00A0\u00A0\u00A0\u00A0Prevalence: 2\u00A0\u00A0\u00A0\u00A0Detectability: 3\u00A0\u00A0\u00A0\u00A0Technical: 3</b></p> <br> <p>A SQL injection attack consists of insertion or “injection” of a SQL query via the input data from the client to the application. A successful SQL injection exploit can read sensitive data from the database, modify database data (Insert/Update/Delete), execute administration operations on the database (such as shutdown the DBMS), recover the content of a given file present on the DBMS file system and in some cases issue commands to the operating system. SQL injection attacks are a type of injection attack, in which SQL commands are injected into data-plane input in order to effect the execution of predefined SQL commands.</p> <br> <p>To prevent SQL Injections we must write parameterized queries. To create perameterised query in java we have PreparedStatement. It can take parameters by passing question marks (?) in the query and then by replacing each question mark index with required values.</p></body></html>",
    							"<html><body><p><b><u>SQL Injection - createQuery</u></b></p> <br> <p><b>Exploitability: 3\u00A0\u00A0\u00A0\u00A0Prevalence: 2\u00A0\u00A0\u00A0\u00A0Detectability: 3\u00A0\u00A0\u00A0\u00A0Technical: 3</b></p> <br> <p>A SQL injection attack consists of insertion or “injection” of a SQL query via the input data from the client to the application. A successful SQL injection exploit can read sensitive data from the database, modify database data (Insert/Update/Delete), execute administration operations on the database (such as shutdown the DBMS), recover the content of a given file present on the DBMS file system and in some cases issue commands to the operating system. SQL injection attacks are a type of injection attack, in which SQL commands are injected into data-plane input in order to effect the execution of predefined SQL commands.</p> <br> <p>To prevent SQL Injections we must write parameterized queries. To create perameterised query in java we have PreparedStatement. It can take parameters by passing question marks (?) in the query and then by replacing each question mark index with required values.</p></body></html>",
    							"<html><body><p><b><u>LDAP - Unsafe Search</u></b></p> <br> <p><b>Exploitability: 3\u00A0\u00A0\u00A0\u00A0Prevalence: 2\u00A0\u00A0\u00A0\u00A0Detectability: 3\u00A0\u00A0\u00A0\u00A0Technical: 3</b></p> <br> <p>LDAP Injection is an attack used to exploit web based applications that construct LDAP statements based on user input. When an application fails to properly sanitize user input, it's possible to modify LDAP statements through techniques similar to SQL Injection. LDAP injection attacks could result in the granting of permissions to unauthorized queries, and content modification inside the LDAP tree.</p></body></html>",
    							"<html><body><p><b><u>RegEx - ReDOS Prevention</u></b></p> <br> <p><b>Exploitability: 3\u00A0\u00A0\u00A0\u00A0Prevalence: 2\u00A0\u00A0\u00A0\u00A0Detectability: 3\u00A0\u00A0\u00A0\u00A0Technical: 3</b></p> <br> <p>The Regular expression Denial of Service (ReDoS) is a Denial of Service attack, that exploits the fact that most Regular Expression implementations may reach extreme situations that cause them to work very slowly (exponentially related to input size). An attacker can then cause a program using a Regular Expression to enter these extreme situations and then hang for a very long time.</p> <br> <p>The Regular Expression naïve algorithm builds a Nondeterministic Finite Automaton (NFA), which is a finite state machine where for each pair of state and input symbol there may be several possible next states. Then the engine starts to make transition until the end of the input. Since there may be several possible next states, a deterministic algorithm is used. This algorithm tries one by one all the possible paths (if needed) until a match is found (or all the paths are tried and fail).</p> <p>For the input aaaaX there are 16 possible paths in the above graph. But for aaaaaaaaaaaaaaaaX there are 65536 possible paths, and the number is double for each additional a. This is an extreme case where the naïve algorithm is problematic, because it must pass on many many paths, and then fail.</p></body></html>",
    							"<html><body><p><b><u>XSS - Input Validation</u></b></p> <br> <p><b>Exploitability: 3\u00A0\u00A0\u00A0\u00A0Prevalence: 3\u00A0\u00A0\u00A0\u00A0Detectability: 3\u00A0\u00A0\u00A0\u00A0Technical: 2</b></p> <br> <p>Cross-Site Scripting (XSS) attacks are a type of injection, in which malicious scripts are injected into otherwise benign and trusted websites. XSS attacks occur when an attacker uses a web application to send malicious code, generally in the form of a browser side script, to a different end user. Flaws that allow these attacks to succeed are quite widespread and occur anywhere a web application uses input from a user within the output it generates without validating or encoding it.</p> <br> <p>An attacker can use XSS to send a malicious script to an unsuspecting user. The end user’s browser has no way to know that the script should not be trusted, and will execute the script. Because it thinks the script came from a trusted source, the malicious script can access any cookies, session tokens, or other sensitive information retained by the browser and used with that site. These scripts can even rewrite the content of the HTML page.</p></body></html>",
    							"<html><body><p><b><u>Sensitive Data Exposure - Insecure Cryptography</u></b></p> <br> <p><b>Exploitability: 2\u00A0\u00A0\u00A0\u00A0Prevalence: 3\u00A0\u00A0\u00A0\u00A0Detectability: 2\u00A0\u00A0\u00A0\u00A0Technical: 3</b></p> <br> <p>Sensitive Data Exposure occurs when an application does not adequately protect sensitive information. The data can vary and anything from passwords, session tokens, credit card data to private health data and more can be exposed.</p> <br> <p>Cryptography is the practice of securing communications that can be open to third-party attackers. This can be done using encryption algorithms. However, some algorithms can be theoretically sound but not have a strong enough level of protection to defend against attacks such as a Brute Force Attack. The solution to this is to use an encryption scheme that is considered strong enough by security professionals. E.G. Use AES-128 instead of AES-64.</p></body></html>",
    							"<html><body><p><b><u>Sensitive Data Exposure - Main Method in Web App</u></b></p> <br> <p><b>Exploitability: 2\u00A0\u00A0\u00A0\u00A0Prevalence: 3\u00A0\u00A0\u00A0\u00A0Detectability: 2\u00A0\u00A0\u00A0\u00A0Technical: 3</b></p> <br> <p>Sensitive Data Exposure occurs when an application does not adequately protect sensitive information. The data can vary and anything from passwords, session tokens, credit card data to private health data and more can be exposed.</p> <br> <p>When developing a web application, a developer may include a main method within the application for debug purposes. If these main methods are left in, it can leave some vulnerabilites open to the public. An attacker may get access to this method and thus access to the code within it. This can release information about how other parts of the application interaact with the servlet or how other servlets work entirely. So while having a main method in a web application is not a direct vulnmerability itself, it can indicate to an attacker that other vulnerabilites may be present.</p></body></html>",
    							"<html><body><p><b><u>Sensitive Data Exposure - Base64 Encoding</u></b></p> <br> <p><b>Exploitability: 2\u00A0\u00A0\u00A0\u00A0Prevalence: 3\u00A0\u00A0\u00A0\u00A0Detectability: 2\u00A0\u00A0\u00A0\u00A0Technical: 3</b></p> <br> <p>Sensitive Data Exposure occurs when an application does not adequately protect sensitive information. The data can vary and anything from passwords, session tokens, credit card data to private health data and more can be exposed.</p> <br> <p>Base64 Encoding is not an inherently insecure encoding but developers can sometimes confuse it for an encryption method. Encoding does not mean encryption. An encoding is a system that converts a set of data from one form to another. This means that data encoded by Base64 can be easily changed back to its original form using a Base64 decoder. For this reason, valuable data being sent using Base64 encoding should be sent using a secure encryption scheme instead.</p></body></html>",
    							"<html><body><p><b><u>Sensitive Data Exposure - Insecure PRNG</u></b></p> <br> <p><b>Exploitability: 2\u00A0\u00A0\u00A0\u00A0Prevalence: 3\u00A0\u00A0\u00A0\u00A0Detectability: 2\u00A0\u00A0\u00A0\u00A0Technical: 3</b></p> <br> <p>Sensitive Data Exposure occurs when an application does not adequately protect sensitive information. The data can vary and anything from passwords, session tokens, credit card data to private health data and more can be exposed.</p> <br> <p>While computers are very powerful, they can never generate a truly random number. Instead they generate a pseudorandom number based on a given seed (usually the computer's time). However, this means that if an application uses a random number, it could be guessed what the next \"random\" number will be. To prevent this, using a secure method of generating pseudorandom numbers should be used such as SecureRandom.class in Java.</p></body></html>" }; 
    
    private String getReDOSValues = "[+*{]";
    private Pattern getReDOSValuesPattern = Pattern.compile(getReDOSValues);

    private String getObjectName = "(\\w+\\s*=\\s*\")";
    private Pattern getObjectNamePattern = Pattern.compile(getObjectName);

    private boolean matchFound = false;
    private boolean isServlet = false;
    private String objectName;
    private ArrayList<String> lines;

    public SecurityMatcher(ArrayList<String> lines) {
    	
    	this.lines = lines;
    	
        //SQL Injection
        patterns.put(0, PrepareStatementPattern.getPattern());
        patterns.put(1, ExecuteQueryPattern.getPattern());
        patterns.put(2, CreateQueryPattern.getPattern());

        //LDAP Injection
        patterns.put(3, LDAPPattern.getPattern());

        //RegEx Injection
        patterns.put(4, RegExPattern.getPattern());

        //XSS
        patterns.put(5, XSSPattern.getPattern());

        //Sensitive Data Exposure
        patterns.put(6, CryptoKeysPattern.getPattern());
        patterns.put(7, WebServerMainPattern.getPattern());
        patterns.put(8, Base64EncoderPattern.getPattern());
        patterns.put(9, InsecureRNGPattern.getPattern());
    }

    //Match a line with a given pattern number
    public String[] patternMatch(String line, int i, int patternNumber, int patternValue) {
        String[] results = new String[4];
        boolean skip = false;
        int count=0;

        if(patternValue == 1 && patternNumber > 4) {
            return results;
        } else if(patternValue  == 2 && patternNumber != 5) {
            return results;
        } else if(patternValue == 3 && patternNumber < 6) {
            return results;
        } else if(patternValue == 4 && patternNumber == 5) {
            return results;
        } else if(patternValue == 5 && patternNumber < 5) {
            return results;
        }

        // Go through each line looking for a match
//        for(int i=0; i<lines.size(); i++) {
//            String line = lines.get(i);

            if(patternNumber == 7) {
                if(line.contains("class") && line.contains("extends HttpServlet")) {
                    isServlet = true;
                } else if(line.contains("class") && !line.contains("extends HttpServlet")) {
                    isServlet = false;
                }
            }


            Matcher m = patterns.get(patternNumber).matcher(line);
            if(m.find()) {

                // Checking for the number of harmful regex symbols
                if(patternNumber == 4) {
                    Matcher redos = getReDOSValuesPattern.matcher(m.group());
                    while(redos.find()) { count++; }

                    setObjectName(line);
                    System.out.println("YOYO " + objectName);
                    for(String l : lines) {
                        if(!l.contains(".compile(" + objectName + ")") || !l.contains(".matches(" + objectName + ")") || !l.contains(".replace(" + objectName + ")") || !l.contains(".replaceAll(" + objectName + ")") || !l.contains(".replaceFirst(" + objectName + ")")) {
                            skip = true;
                        } else {
                            skip = false;
                            break;
                        }
                    }

                }

                if(patternNumber == 7 && isServlet) {
                    boolean inRightClass = false;
                    for(String l : lines) {
                        if(l.equals(line)) {
                            inRightClass = true;
                        } else if(l.contains("class") && !l.equals(line)) {
                            inRightClass = false;
                        }

                        if(inRightClass) {
                            if(l.contains("main(")) {
                            	skip = false;
                                break;
                            } else {
                            	skip = true;
                            }
                        }
                    }
                }

                // Make sure any already changed lines aren't counted again
                if(patternNumber == 2 && line.contains("?")) {
                    skip = true;
                } else if(patternNumber == 3 && line.contains("Sanitised")) {
                    skip = true;
                } else if( patternNumber == 4 && count < 2) {
                    skip = true;
                //} //else if( patternNumber == 4 && !line.equals(lines.get(lines.size()-1)) && lines.get(i+1).contains("N.B.")) {
                    //skip = true;
                } else if(patternNumber == 5 && line.contains("//Potentially vulnerable.")) {
                    skip = true;
                } else if(patternNumber == 6 && line.contains("// Safe"))  {
                    skip = true;
                } else if(patternNumber == 7 && !isServlet) {
                    skip = true;
                } else if(patternNumber == 7 && line.contains("// Unsafe")) {
                    skip = true;
                } else if(patternNumber == 8 && line.contains("// Unsafe")) {
                    skip = true;
                }

                // Skip any unwanted lines
                if(skip) {
                    skip = false;
                    return results;
                }

                //System.out.println("***** " + keys[patternNumber] + " *****");
                //System.out.print("Found value at line " + (i + 1) + ":\r\n" + line);
                results[0] = keys[patternNumber];
                results[1] = line;
                results[2] = String.valueOf(i+1);
                results[3] = desc[patternNumber];
                matchFound = true;
                
            }
        //}

        // No match found
        if(!matchFound) {
            //System.out.println("***** " + keys[patternNumber] + " ***** .......... No match found in this file!");
        }

        return results;
    }

    private void setObjectName(String l) {
        Matcher objectNameM = getObjectNamePattern.matcher(l);
        if(objectNameM.find()) {
            objectName = objectNameM.group();
            String[] arr = objectName.split(" ", 2);

            if(arr[0] == null) {
                String[] arr2 = objectName.split("=", 2);
                objectName = arr2[0];
            } else {
                objectName = arr[0];
            }
        }
    }
}
