/* RegEx Injection

String isNotRegex = "hello world";
String isNotVulnerableRegex = "a*b";
String isVulnerableRegex = "a*b*";
String isVulnerableRegexButUnused = "a*b*";

Pattern isNotVulnerablePattern = Pattern.compile(isNotVulnerableRegex);
Pattern isVulnerablePattern = Pattern.matches(isVulnerableRegex);
*/