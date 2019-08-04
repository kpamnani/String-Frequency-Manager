package com.string.frequencyManager.controller;

import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.string.frequencyManager.domain.Response;
import com.string.frequencyManager.service.FrequencyService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author kamnaprateek
 * @see FrequencyManagerRestController.java
 */

@Slf4j
@RestController
public class FrequencyManagerRestController {
	
	@Autowired
	private FrequencyService freqService;

    @RequestMapping(value = "/isStringValid",produces = "application/json")
    public ResponseEntity<Response> isValid(@RequestParam(value = "string", required = true)String record){
        ResponseEntity<Response> jsonResponse = null;
        try {
           boolean result =  freqService.isValidString(record);
            Response response = new Response();
           if (result){
               response.setResponse("true");
           }else{
               response.setResponse("false");
           }
           jsonResponse = new ResponseEntity<>(response,HttpStatus.OK);
        }catch(Throwable th){
//            log(Level.SEVERE,th.getMessage(),th);
            throw new RuntimeException(th.getMessage());
        }
        return jsonResponse ;
    }
}
