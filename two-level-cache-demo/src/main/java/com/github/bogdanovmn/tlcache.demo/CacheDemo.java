package com.github.bogdanovmn.tlcache.demo;


import com.github.bogdanovmn.tlcache.TwoLvlCache;
import com.github.bogdanovmn.tlcache.exception.PutToCacheError;
import com.github.bogdanovmn.tlcache.exception.SerializationError;
import com.github.bogdanovmn.tlcache.exception.DeserializationError;
import com.github.bogdanovmn.tlcache.strategy.CacheRotateStrategy;
import com.github.bogdanovmn.tlcache.strategy.TwoLevelCacheStrategyAlpha;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.Random;

public class CacheDemo {
	public static void main(String[] args) throws DeserializationError, SerializationError, PutToCacheError, IOException {
		Options cliOptions = new Options();
		cliOptions
			.addOption(
				Option.builder("f")
					.longOpt("first-level-limit")
					.hasArg().argName("LIMIT")
					.desc("max memory (bytes) for first level cache")
					.required()
				.build()
			)
			.addOption(
				Option.builder("s")
					.longOpt("second-level-limit")
					.hasArg().argName("LIMIT")
					.desc("max memory (bytes) for second level cache")
					.required()
					.type(Integer.class)
				.build()
			)
			.addOption(
				Option.builder("r")
					.longOpt("rotate-strategy")
					.hasArg().argName("alpha | beta")
					.desc("old objects rotate strategy")
					.required()
				.build()
			)
			.addOption(
				Option.builder("t")
					.longOpt("task-param")
					.hasArg().argName("NUMBER")
					.desc("some integer for abstract task (objects count)")
					.required()
					.build()
			);

		CommandLineParser cmdLineParser = new DefaultParser();
		try {
			CommandLine cmdLine = cmdLineParser.parse(cliOptions, args);
			String strategyName = cmdLine.getOptionValue("r");
			if (!strategyName.equals("alpha") &&
				!strategyName.equals("beta"))
			{
				printHelp(cliOptions);
			}
			else {
				CacheRotateStrategy strategy = strategyName.equals("alpha")
					? new TwoLevelCacheStrategyAlpha()
					: new TwoLevelCacheStrategyAlpha();

				TwoLvlCache cache = new TwoLvlCache<Integer, Integer>(
					Integer.valueOf(cmdLine.getOptionValue("first-level-limit")),
					Integer.valueOf(cmdLine.getOptionValue("second-level-limit")),
					strategy
				);

				doSomething(
					Integer.valueOf(cmdLine.getOptionValue("task-param")),
					cache
				);
			}
		}
		catch (ParseException e) {
			System.err.println(e.getMessage());
			printHelp(cliOptions);
		}
	}
	/**
		Some complex compute... :)
	 */
	private static int complexCompute(int number) {
		return number;
	}

	private static void doSomething(int objectsCount, TwoLvlCache cache)
		throws DeserializationError, SerializationError, PutToCacheError {
		System.out.println(cache);

		Random generator = new Random();
		for (int i = 1; i < objectsCount; i++) {
			int randValue = generator.nextInt(i);

			System.out.printf("get %d --> ", randValue);

			Integer obj = (Integer) cache.get(randValue);
			if (obj == null) {
				cache.put(randValue, complexCompute(randValue));
				System.out.print(" compute  ");
			}
			else {
				System.out.printf(" hit (%d)  ", obj);
			}

			System.out.println(cache);
		}
	}

	private static void printHelp(Options opts) {
		new HelpFormatter()
			.printHelp(
				"app",
				"Two level cache demo",
				opts,
				".",
				true
			);
	}
}
