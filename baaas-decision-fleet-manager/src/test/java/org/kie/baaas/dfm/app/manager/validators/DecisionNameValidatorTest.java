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

package org.kie.baaas.dfm.app.manager.validators;

import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.kie.baaas.dfm.api.decisions.DecisionBase;
import org.kie.baaas.dfm.api.decisions.DecisionRequest;
import org.kie.baaas.dfm.api.decisions.Model;

import io.quarkus.test.junit.QuarkusTest;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@QuarkusTest
public class DecisionNameValidatorTest {

    DecisionRequest decisionRequest;

    @Inject
    Validator validator;

    @BeforeAll
    void createBasePayload() {
        decisionRequest = new DecisionRequest();
        decisionRequest.setKind("Decision");
        decisionRequest.setDescription("some test");
        decisionRequest.setModel(new Model());
        decisionRequest.getModel().setDmn("<xml><test>\"hello\"</test></xml>");
    }

    @Test
    void testValidDecisionName() {
        decisionRequest.setName("quarkus-test");
        Set<ConstraintViolation<DecisionBase>> violations = validator.validate(decisionRequest);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    void testInValidDecisionName() {
        decisionRequest.setName("Quarkus Test");
        Set<ConstraintViolation<DecisionBase>> violations = validator.validate(decisionRequest);
        Assertions.assertFalse(violations.isEmpty());
    }
}
