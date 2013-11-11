package liquibase.sdk.state;

import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PersistedTestResultsParser {

    private enum Section {
        DEFINITION,
        INFORMATION,
        DATA
    }

    public PersistedTestResults parse(BufferedReader fileContents) throws IOException {
        PersistedTestResults results = null;


        Pattern permutationStartPattern = Pattern.compile("## Permutation: (.*) ##");
        Pattern internalKeyValuePattern = Pattern.compile("\\- _(.+):_ (.+)");
        Pattern keyValuePattern = Pattern.compile("\\- \\*\\*(.+):\\*\\* (.*)");
        Pattern multiLineKeyValuePattern = Pattern.compile("\\- \\*\\*(.+)~\\*\\*");

        TestPermutation currentPermutation = null;

        String line;
        int lineNumber = 0;
        Section section = null;
        String multiLineKey = null;
        String multiLineValue = null;
        while ((line = fileContents.readLine()) != null) {
            lineNumber++;

            if (lineNumber == 1) {
                Matcher matcher = Pattern.compile("# Test Output: (.*)\\.(.*) #").matcher(line);
                if (!matcher.matches()) {
                    throw new IOException("Invalid header: "+line);
                } else {
                    String testClass = matcher.group(1);
                    String testName = matcher.group(2);

                    results = new PersistedTestResults(testClass, testName, false);
                }

                continue;
            }

            if (multiLineKey != null) {
                if (line.equals("") || line.startsWith("    ")) {
                    multiLineValue += line+"\n";
                    continue;
                } else {
                    multiLineValue = multiLineValue.trim();
                    if (section.equals(Section.DEFINITION)) {
                        currentPermutation.addDefinition(multiLineKey, multiLineValue, OutputFormat.FromFile);
                    } else if (section.equals(Section.INFORMATION)) {
                        currentPermutation.addInfo(multiLineKey, multiLineValue, OutputFormat.FromFile);
                    } else if (section.equals(Section.DATA)) {
                        currentPermutation.addData(multiLineKey, multiLineValue, OutputFormat.FromFile);
                    } else {
                        throw new UnexpectedLiquibaseException("Unknown multiline section on line "+lineNumber+": "+section);
                    }
                    multiLineKey = null;
                    multiLineValue = null;
                }
            }

            if (StringUtils.trimToEmpty(line).equals("")) {
                continue;
            }

            if (line.equals("#### Permutation Information: ####")) {
                section = Section.INFORMATION;
                continue;
            } else if (line.equals("#### Permutation Data: ####")) {
                section = Section.DATA;
                continue;
            }

            Matcher permutationStartMatcher = permutationStartPattern.matcher(line);
            if (permutationStartMatcher.matches()) {
                if (currentPermutation != null) {
                    results.addAcceptedRun(currentPermutation);
                }

                String permutationName = permutationStartMatcher.group(1);
                currentPermutation = new TestPermutation(permutationName);
                section = Section.DEFINITION;
                continue;
            }

            Matcher internalKeyValueMatcher = internalKeyValuePattern.matcher(line);
            if (internalKeyValueMatcher.matches()) {
                String key = internalKeyValueMatcher.group(1);
                String value = internalKeyValueMatcher.group(2);
                if (key.equals("VALIDATED")) {
                    currentPermutation.setValidated(Boolean.valueOf(value));
                } else {
                    throw new UnexpectedLiquibaseException("Unknown internal parameter "+ key);
                }
                continue;
            }

            Matcher keyValueMatcher = keyValuePattern.matcher(line);
            if (keyValueMatcher.matches()) {
                String key = keyValueMatcher.group(1);
                String value = keyValueMatcher.group(2);

                if (section.equals(Section.DEFINITION)) {
                    currentPermutation.addDefinition(key, value, OutputFormat.FromFile);
                } else if (section.equals(Section.INFORMATION)) {
                    currentPermutation.addInfo(key, value, OutputFormat.FromFile);
                } else if (section.equals(Section.DATA)) {
                    currentPermutation.addData(key, value, OutputFormat.FromFile);
                } else {
                    throw new UnexpectedLiquibaseException("Unknown section "+section);
                }
                continue;
            }

            Matcher multiLineKeyValueMatcher = multiLineKeyValuePattern.matcher(line);
            if (multiLineKeyValueMatcher.matches()) {
                multiLineKey = multiLineKeyValueMatcher.group(1);
                multiLineValue = "";
                continue;
            }

            if (currentPermutation == null) {
                //in the header section describing what the file is for
            } else {
                throw new UnexpectedLiquibaseException("Could not parse line "+lineNumber+": "+line);
            }
        }

        if (currentPermutation != null) {
            results.addAcceptedRun(currentPermutation);
        }

        return results;
    }
}
