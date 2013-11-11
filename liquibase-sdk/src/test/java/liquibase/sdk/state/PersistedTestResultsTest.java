package liquibase.sdk.state;

import liquibase.util.StreamUtil;
import org.junit.experimental.theories.*;
import org.junit.runner.RunWith;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(Theories.class)
public class PersistedTestResultsTest {

    @Theory
    public void readAndWriteAreConsistant(@ParametersSuppliedBy(ResultProvider.class) String fileContents) throws IOException {
        PersistedTestResults results = PersistedTestResults.parse(new BufferedReader(new StringReader(fileContents)));
        results.copyAcceptedRunsToNewRuns();

        StringWriter out = new StringWriter();
        results.serialize(new BufferedWriter(out));

        assertEquals(fileContents, out.toString());
    }




    public static class ResultProvider extends ParameterSupplier {
        @Override
        public List<PotentialAssignment> getValueSources(ParameterSignature sig) {
            List<PotentialAssignment> returnList = new ArrayList<PotentialAssignment>();

            try {
                returnList.add(PotentialAssignment.forValue("example1", StreamUtil.getStreamContents(getClass().getResourceAsStream("/liquibase/sdk/state/example.results.txt"))));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return returnList;

        }
    }

}
