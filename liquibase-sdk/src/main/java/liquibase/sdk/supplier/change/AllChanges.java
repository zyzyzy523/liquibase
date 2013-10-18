package liquibase.sdk.supplier.change;

import liquibase.change.ChangeFactory;
import org.junit.experimental.theories.ParameterSignature;
import org.junit.experimental.theories.ParameterSupplier;
import org.junit.experimental.theories.PotentialAssignment;

import java.util.ArrayList;
import java.util.List;

public class AllChanges extends ParameterSupplier {
    @Override
    public List<PotentialAssignment> getValueSources(ParameterSignature sig) {
        List<PotentialAssignment> returnList = new ArrayList<PotentialAssignment>();
        for (String change : ChangeFactory.getInstance().getDefinedChanges()) {
            returnList.add(PotentialAssignment.forValue(change, ChangeFactory.getInstance().create(change)));
        }

        return returnList;
    }
}
