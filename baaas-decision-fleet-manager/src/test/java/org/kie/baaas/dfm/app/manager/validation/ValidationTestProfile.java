package org.kie.baaas.dfm.app.manager.validation;

import java.util.Collections;
import java.util.Map;

import io.quarkus.test.junit.QuarkusTestProfile;

public class ValidationTestProfile implements QuarkusTestProfile {

    @Override
    public Map<String, String> getConfigOverrides() {
        return Collections.singletonMap("baaas.dfm.max.allowed.decisions", "1");
    }
}
