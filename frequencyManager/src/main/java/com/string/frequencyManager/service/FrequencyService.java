package com.string.frequencyManager.service;

import org.springframework.stereotype.Service;

/**
 * @author Kamna
 * @see com.string.frequencyManager.service.FrequencyService.java
 *
 */

@Service
public interface FrequencyService {
	/**
    *
    * @param word to be checked.
    * @return false if word appears more than 5 times in the last 24 hours, true otherwise.
    */
   public boolean isValidString(String word);

}
