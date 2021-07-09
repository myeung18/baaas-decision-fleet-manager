/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.kie.baaas.dfm.app.manager.validation;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kie.baaas.dfm.api.decisions.DecisionRequest;
import org.kie.baaas.dfm.api.decisions.Model;
import org.kie.baaas.dfm.app.dao.DecisionDAO;
import org.kie.baaas.dfm.app.dao.DecisionVersionDAO;
import org.kie.baaas.dfm.app.manager.DecisionManager;
import org.kie.baaas.dfm.app.model.DecisionVersion;
import org.kie.baaas.dfm.app.storage.DMNStorageRequest;
import org.kie.baaas.dfm.app.storage.DecisionDMNStorage;
import org.kie.baaas.dfm.app.storage.s3.S3DMNStorage;
import org.mockito.Mockito;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.kie.baaas.dfm.app.TestConstants.DEFAULT_CUSTOMER_ID;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@QuarkusTest
@TestProfile(ValidationTestProfile.class)
public class DecisionManagerTest {

    @Inject
    DecisionDAO decisionDAO;

    @Inject
    DecisionVersionDAO decisionVersionDAO;

    @Inject
    DecisionManager decisionManager;

    @Inject
    DecisionDMNStorage dmnStorage;

    private DecisionRequest createApiRequest() {

        Model model = new Model();
        model.setDmn("foo");

        DecisionRequest decisions = new DecisionRequest();
        decisions.setDescription("The Best Decision Ever");
        decisions.setName("robs-first-decision");
        decisions.setModel(model);
        return decisions;
    }

    @BeforeAll
    public static void beforeAll() {
        S3DMNStorage storage = Mockito.mock(S3DMNStorage.class);
        QuarkusMock.installMockForType(storage, S3DMNStorage.class);
    }

    private DMNStorageRequest createStorageRequest() {
        DMNStorageRequest request = new DMNStorageRequest("provider-url", "hash");
        when(dmnStorage.writeDMN(anyString(), Mockito.any(DecisionRequest.class), Mockito.any(DecisionVersion.class))).thenReturn(request);
        return request;
    }

    @TestTransaction
    @Test
    public void newDecision_exceedsDecisionCreationLimit() {
        createStorageRequest();

        DecisionRequest apiRequest = createApiRequest();
        decisionManager.createOrUpdateVersion(DEFAULT_CUSTOMER_ID, apiRequest);

        apiRequest.setName("2nd-decision");
        assertThrows(DecisionRequestValidationException.class, () -> decisionManager.createOrUpdateVersion(DEFAULT_CUSTOMER_ID, apiRequest));
    }

}
