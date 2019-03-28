package liquibase.change.core

import liquibase.exception.ParseException
import liquibase.exception.SetupException

import liquibase.sdk.supplier.resource.ResourceSupplier
import spock.lang.Shared
import spock.lang.Specification

class StopChangeTest extends Specification {

    @Shared resourceSupplier = new ResourceSupplier()

    def "load sets message to string value"() {
        when:
        def change = new StopChange()
        try {
            change.load(new liquibase.parser.ParsedNode(null, "stop").setValue("stopping..."), resourceSupplier.simpleResourceAccessor)
        } catch (ParseException e) {
            e.printStackTrace()
        } catch (SetupException e) {
            e.printStackTrace()
        }

        then:
        change.message == "stopping..."
    }
}
