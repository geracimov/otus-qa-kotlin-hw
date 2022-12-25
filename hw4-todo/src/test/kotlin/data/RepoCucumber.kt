package data

import org.junit.platform.suite.api.ConfigurationParameter
import org.junit.platform.suite.api.SelectClasspathResource
import org.junit.platform.suite.api.Suite
import io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME

@Suite
@SelectClasspathResource("/feature")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value =  "data")
class RepoCucumber {
}