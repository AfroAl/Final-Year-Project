package fyp_project.detector.refactorings;

public class InsecureRNGRefactor {

    private String originalLine;
    private String deletion;

    public InsecureRNGRefactor(String line) {
        originalLine = line;
    }

    public String getDeletion() {
        return deletion;
    }

    public String refactor() {

        String replacement = originalLine;
        replacement = replacement.replace("Random", "SecureRandom");

        deletion = "";
        return replacement;
    }
}
