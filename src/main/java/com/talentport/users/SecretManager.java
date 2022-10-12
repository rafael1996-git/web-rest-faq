package com.talentport.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import com.talentport.utils.StringBuilderConcat;

public class SecretManager {
	private static final String SECRET_MANAGER_REFERENCE = "/aws/reference/secretsmanager/";

	private static AWSSimpleSystemsManagement client = null;
	private static final Logger log = LoggerFactory.getLogger(SecretManager.class);

	public static String getParameter(String parameterName) throws Exception {
		if (client == null) {
			client = AWSSimpleSystemsManagementClientBuilder.standard().build();
		}
		String name = StringBuilderConcat.concat(SECRET_MANAGER_REFERENCE, parameterName);
		GetParameterRequest parameterRequest = new GetParameterRequest().withName(name).withWithDecryption(true);

		GetParameterResult getParameterResult = client.getParameter(parameterRequest);
		if (getParameterResult != null && getParameterResult.getParameter() != null
				&& getParameterResult.getParameter().getValue() != null
				&& !getParameterResult.getParameter().getValue().isEmpty()) {
			return getParameterResult.getParameter().getValue();
		} else {
			log.error("El parametro no existe o no tiene un valor asignado");
			throw new Exception("El parametro no existe o no tiene un valor asignado");
		}
	}

}
