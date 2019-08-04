package com.string.frequencyManager.service;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.string.frequencyManager.repository.WordRegister24Hours;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Kamna
 * @see FrequencyServiceImpl.java
 */

@Slf4j
@Service
public class FrequencyServiceImpl implements FrequencyService{

	/**
     * Number of milliseconds in a day.
     */
    private static final long ONE_DAY_BEFORE = 86400000L;

    @Autowired
    private WordRegister24Hours wordRegister24Hours;

    /**
     *
     * @param word to be checked.
     * @return
     */
    @Override
    public boolean isValidString(String word) {
    	long startTime = System.currentTimeMillis();
        long[] timestamps = wordRegister24Hours.getWordRegister24HoursMap().get(word);
        log.info(String.format("Time taken %d milliseconds", System.currentTimeMillis() - startTime));
        long currentTimeStamp = System.currentTimeMillis();
        long oneDayBefore = currentTimeStamp - ONE_DAY_BEFORE;

        log.info(String.format("Before 24 hours %d", oneDayBefore));
        log.info(String.format("Word : %s and occurrences %s",word,Arrays.toString(timestamps)));
        

        return (Arrays.stream(timestamps).parallel().anyMatch(timestamp -> timestamp < oneDayBefore));

    }

	
}
