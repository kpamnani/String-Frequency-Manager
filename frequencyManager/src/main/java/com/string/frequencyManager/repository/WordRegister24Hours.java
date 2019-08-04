package com.string.frequencyManager.repository;

import com.string.frequencyManager.config.AppConfig;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Repository
public class WordRegister24Hours {

	/**
	 * This will store word and their last 6 occurrence time.
	 */
	private static final DB db = DBMaker.fileDB(new File("src/main/resources/db/WordRegister24Hours.db"))
			.fileMmapEnableIfSupported().checksumHeaderBypass().closeOnJvmShutdown()
			// Allocation increment has side effect on performance with mmap
			// files. MapDB maps file in series of DirectByteBuffer. Size of each buffer is equal to size increment
			// 1 MB by default, so larger size increment means less buffers for the same disk store size. Operations such
			// as sync, flush, and close have to traverse all buffers. So larger size increment could speedup commit 
			// and close operations. 
			.allocateIncrement(512 * 1024 * 1024) // 512 MB
			.cleanerHackEnable()
			.make();

	@Autowired
	private AppConfig config;
	private HTreeMap<String, long[]> wordRegister24HoursMap;

	public WordRegister24Hours() {

		wordRegister24HoursMap = db.hashMap("WordRegister24HoursMap").keySerializer(Serializer.STRING)
				.valueSerializer(Serializer.LONG_ARRAY)
				// to load a value (new long[6] in this case) if the existing
				// key is not found.
				.valueLoader(s -> new long[config.getCount() + 1])
				.expireAfterCreate(24, TimeUnit.HOURS)
				//.expireAfterUpdate(24, TimeUnit.HOURS)
				//.expireAfterGet(24, TimeUnit.HOURS)
				// Entry expiration in 3 background threads
				.expireExecutor(Executors.newScheduledThreadPool(3))
				// trigger space compact if 40% of space is free
				.expireCompactThreshold(0.4).createOrOpen();

	}

	public static DB getDb() {
		return db;
	}

	public HTreeMap<String, long[]> getWordRegister24HoursMap() {
		return wordRegister24HoursMap;
	}
}
