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

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.kie.baaas.dfm.api.decisions.DecisionRequest;
import org.kie.baaas.dfm.app.dao.DecisionVersionDAO;

/**
 * A validator validates the decision name in a DecisionRequest to ensure the length is between 1 and 253,
 * the content is of lower case alphanumeric characters or '-', start with an alphabetic and end with
 * alphanumeric characters.
 *
 * @see DecisionName
 */
public class DecisionNameValidator implements ConstraintValidator<DecisionName, DecisionRequest> {

    @Inject
    DecisionVersionDAO decisionVersionDAO;

    @Override
    public boolean isValid(DecisionRequest dr, ConstraintValidatorContext constraintValidatorContext) {
        if (dr == null || dr.getName() == null || dr.getName().isEmpty() || dr.getName().length() > 249) {
            return false;
        }

        String sanName = Util.sanitizeName(dr.getName().toLowerCase());
        long cnt = decisionVersionDAO.getDecisionNameCountExcludeDeleted(sanName);
        return cnt == 0;
    }
}
