package com.novadart.novabill.service.mail.mailgun;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class IntegrityValidationService {

    @Value("${mailgun.api.key}")
    private String apiKey;

    private BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();

    private String stringifyVars(Map<String, String> vars){
        return vars.entrySet().stream().map(e->e.getKey() + ":" + e.getValue()).sorted().reduce("", String::concat) + "#" + apiKey;
    }


    public String produceHash(Map<String, String> vars){
        return bcryptEncoder.encode(stringifyVars(vars));
    }

    public boolean isValid(Map<String, String> vars, String hash){
        return bcryptEncoder.matches(stringifyVars(vars), hash);
    }

}
