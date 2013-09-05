package liquibase.sdk;

import liquibase.sdk.change.StandardChangeTests;
import liquibase.util.StringUtils;
import org.apache.commons.cli.*;
import org.junit.internal.TextListener;
import org.junit.runner.Computer;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import java.util.*;

public class Main {

    private static final String ARG_PACKAGES = "packages";
    private CommandLine arguments;
    private Options commandLineOptions;

    public static void main(String[] args) {
        printHeader("Liquibase Extension SDK");

        Main main = new Main();
        try {
            main.init(args);

            if (main.arguments.hasOption("help")) {
                main.printHelp();
                return;
            }
        } catch (UserError userError) {
            System.out.println(userError.getMessage());
            System.out.print("\n\n");
            main.printHelp();
            return;
        }

        Context context = Context.getInstance();
        if (context.getSeenExtensionClasses().size() == 0) {
            System.out.println("No extension classes found in "+StringUtils.join(context.getPackages(), ","));
            return;
        }

        System.out.println("Extension classes found:");
        for (Map.Entry<Class, Set<Class>> entry : context.getSeenExtensionClasses().entrySet()) {
            System.out.println(StringUtils.indent(entry.getKey().getName()+" extensions:", 4));

            System.out.println(StringUtils.indent(StringUtils.join(entry.getValue(), "\n", new StringUtils.StringUtilsFormatter() {
                @Override
                public String toString(Object obj) {
                    return ((Class) obj).getName();
                }
            }), 8));
        }

        printHeader("Running Tests");

        JUnitCore junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));
        Result result = junit.run(new Computer(), StandardChangeTests.class);

    }

    private static void printHeader(String header) {
        System.out.println("-------------------------------------------------");
        System.out.println(header);
        System.out.println("-------------------------------------------------");
    }

    public Main() {
        commandLineOptions = new Options();
        commandLineOptions.addOption(OptionBuilder.withArgName(ARG_PACKAGES).hasArg().withDescription("Comma separated list of packages containing extensions").isRequired(true).create("packages"));
    }

    public void init(String[] args) throws UserError {
        Context.reset();
        CommandLineParser parser = new PosixParser();
        try {
            arguments = parser.parse(commandLineOptions, args);
        } catch (ParseException e) {
            throw new UserError("Error parsing command line: " + e.getMessage());
        }


        Set<String> packages = new HashSet<String>(Arrays.asList(arguments.getOptionValue(ARG_PACKAGES).split("\\s*,\\s*")));

        Context.getInstance().init(packages);
    }



    public void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("liquibase-sdk", commandLineOptions);
    }

    public static class UserError extends Exception {

        public UserError(String message) {
            super(message);
        }
    }

}
