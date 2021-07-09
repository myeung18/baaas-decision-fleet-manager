package org.kie.baaas.dfm.app.manager.validation;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.baaas.dfm.app.config.DecisionFleetManagerConfig;

/**
 * Concrete implementation of a Validator.
 * It validates the number of decisions a customer has created with the max. number of
 * decisions is allowed.
 */
@ApplicationScoped
public class MaxAllowedDecisionValidator implements Validator {

    private final DecisionFleetManagerConfig config;
    private String errorMsg = "";

    @Inject
    public MaxAllowedDecisionValidator(DecisionFleetManagerConfig decisionConfig) {
        this.config = decisionConfig;
    }

    @Override
    public boolean validate(ValidateParams vo) {
        if (this.config.isDecisionCountWithinLimit(vo.getDecisionCount())) {
            return true;
        }
        errorMsg = "The number of created decision has reached the limit.";
        return false;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }
}
