package com.string.frequencyManager.service.config;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.string.frequencyManager.config.AppConfig;
import com.string.frequencyManager.repository.WordRegister24Hours;

/**
 * @author Kamna
 * PeriodicLogFrequencyManager.java
 *
 */

@Service
@RefreshScope
@Configuration
public class PeriodicLogFrequencyManager {
	
	private static final Logger logger = Logger.getLogger("PeriodicLogFrequencyManager");

	private AppConfig config;
	private WatchService watcher;
	
	private Pattern pattern;
	
	@Autowired
	private WordRegister24Hours wordRegister24Hours;

	public PeriodicLogFrequencyManager(AppConfig config) throws IOException, InterruptedException {

		this.config = config;
		watcher = FileSystems.getDefault().newWatchService();
	}

	@Scheduled(fixedRate = 1000)
	public void watch() {
		String onlyFileName = "";
		int arrayLength = config.getCount();
		String fileExtension = config.getLogFileExt();
		pattern = Pattern.compile(config.getLogFileFormat());

		try {

			Path dir = Paths.get(config.getLogFolder());
			Preconditions.checkNotNull(dir,"Invalid folder for generated log retrieval.");
//			if (dir != null) {
				WatchKey key = dir.register(watcher, ENTRY_CREATE);

				if (null != (key = watcher.take())) {

					long startTime = System.currentTimeMillis();

					for (WatchEvent<?> event : key.pollEvents()) {
						logger.log(Level.INFO,
								String.format("Event kind: %s , file affected: %s", event.kind(), event.context()));
						onlyFileName = event.context().toString();
						processEachFile(arrayLength, onlyFileName, fileExtension);
					}
					key.reset();
					logger.log(Level.INFO, String.format("Completed processing, took %d milliseconds.",
							(System.currentTimeMillis() - startTime)));
				}
//			}
		} catch (IOException | InterruptedException e) {
			int retryCount = 0;
			int retryLimit = 3;
			boolean isOK = false;
			logger.log(Level.SEVERE, e.getMessage(), e);
			while (retryCount < retryLimit) {
				try {

					Preconditions.checkArgument((!"".equals(onlyFileName)), "Invalid FileName");
//					if (!"".equals(onlyFileName)) {
						processEachFile(arrayLength, onlyFileName, fileExtension);
//					}
					isOK = true;
					break;
				} catch (IOException e1) {
					logger.log(Level.SEVERE, String.format("Retrying... %s", e.getMessage()), e);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e2) {
						logger.log(Level.SEVERE, String.format("Retrying... %s", e.getMessage()), e);
					}
				}
				retryCount++;
			}
			if (!isOK) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}

	}

	/**
	 * This will process each file and update word frequency.
	 * 
	 * @param arrayLength - this is word count.
	 * @param onlyFileName - file name with extension, path not included.
	 * @param fileExtension- file extension to be considered for processing.
	 * @throws IOException - if file does not exist.
	 */
	private void processEachFile(int arrayLength, String onlyFileName, String fileExtension) throws IOException {
		if (pattern.matcher(onlyFileName).find() && onlyFileName.endsWith(fileExtension)) {
			try (BufferedReader bufferedLogReader = Files
					.newBufferedReader(Paths.get(config.getLogFolder() + File.separator + onlyFileName))) {

				bufferedLogReader.lines().forEach(e -> {

					String[] fieldValues = e.split(",");
					try {
						long epochTimeStampMillis = Long.parseLong(fieldValues[0]);

						for (int i = 0; i < fieldValues.length; i++) {

							String[] words = fieldValues[i].split("\\s");
							Arrays.stream(words).forEach(eachWord -> {
								if (eachWord.length() > 0) {
									long[] occurrences = wordRegister24Hours.getWordRegister24HoursMap().get(eachWord);

									for (int j = 0; j < occurrences.length - 1; j++) {
										occurrences[j] = occurrences[j + 1];
									}

									occurrences[arrayLength] = epochTimeStampMillis;

									wordRegister24Hours.getWordRegister24HoursMap().put(eachWord, occurrences);
									logger.log(Level.INFO,String.format("Word %s, occurrences %d",eachWord,occurrences[arrayLength]));

								}
							});
						}
					} catch (NumberFormatException nfe) {
						logger.log(Level.SEVERE, String.format("'%s' is not a numeric value", fieldValues[0]));
					}
				});

			}
		}
	}
}
