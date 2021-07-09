package org.kie.baaas.dfm.app.manager.validation;

/**
 * Validator validates API customer requests
 * The autual validation logics are defined in the concrete implementation.
 */
public interface Validator {
    /**
     * Validates the ValidateParams with certain criteria. If the ValidateParams are in compliance
     * with the criteria, true is returned, otherwise, false is returned and error message are set to
     * a variable for later retrieve by the caller.
     *
     * @param vp contains all the data needed to perform the validations
     * @return true: criteria met
     *         false: failed to meet any one of the criteria
     */
    boolean validate(ValidateParams vp);

    /**
     * Returns the error message that represents the the violation of a criteria
     * 
     * @return
     *         the reason of failure
     */
    String getErrorMsg();
}
