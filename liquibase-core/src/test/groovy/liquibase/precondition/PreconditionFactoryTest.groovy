package liquibase.precondition

import liquibase.Scope
import spock.lang.Specification
import spock.lang.Unroll

class PreconditionFactoryTest extends Specification {

    def "built in preconditions are found"() {
        expect:
        Scope.getCurrentScope().getSingleton(PreconditionFactory).findAllInstances().size() == 19
    }

    @Unroll
    def "getPrecondition"() {
        when:
        def precondition = Scope.getCurrentScope().getSingleton(PreconditionFactory).getPrecondition(tag)

        then:
        precondition.name == tag

        where:
        tag << [
                "tableExists",
                "dbms",
        ]

    }
}
