package com.talentport.users.helpers;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Helpers {

	public static String NameUUID() {
		// TODO Auto-generated method stub
		return UUID.randomUUID().toString();
	}
	
	public static String generateRandomPassword() {
		// ASCII range – alphanumeric (0-9, a-z, A-Z)
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
 
        SecureRandom random = new SecureRandom();
 
        // each iteration of the loop randomly chooses a character from the given
        // ASCII range and appends it to the `StringBuilder` instance
        return IntStream.range(0, 10)
                .map(i -> random.nextInt(chars.length()))
                .mapToObj(randomIndex -> String.valueOf(chars.charAt(randomIndex)))
                .collect(Collectors.joining()) + ".@";
	}
	
	public static ResponseEntity<Map<String, Object>> ResponseClass(int codeError, Map<String, Object> object,
			String msgSucces, String msgError) {
		Map<String, Object> response = new HashMap<>();
		response.put("statusCode", codeError);
		response.put("Data", object);
		response.put("Message", msgSucces);
		response.put("Error", msgError);
		return new ResponseEntity<Map<String, Object>>(response, codeError == 500 ? HttpStatus.INTERNAL_SERVER_ERROR
				: codeError == 200 ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
	}
}
