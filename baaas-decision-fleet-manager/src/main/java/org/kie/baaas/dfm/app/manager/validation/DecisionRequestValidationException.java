package org.kie.baaas.dfm.app.manager.validation;

import javax.ws.rs.core.Response;

import org.kie.baaas.dfm.app.exceptions.DecisionFleetManagerException;

public class DecisionRequestValidationException extends DecisionFleetManagerException {
    public DecisionRequestValidationException(String message) {
        super(message);
    }

    public DecisionRequestValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return Response.Status.BAD_REQUEST.getStatusCode();
    }
}
