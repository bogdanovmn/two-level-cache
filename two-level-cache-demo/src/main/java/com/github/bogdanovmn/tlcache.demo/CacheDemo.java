package com.github.bogdanovmn.tlcache.demo;


import com.github.bogdanovmn.tlcache.TwoLvlCache;
import com.github.bogdanovmn.tlcache.exception.PutToCacheError;
import com.github.bogdanovmn.tlcache.exception.RotateObjectError;
import com.github.bogdanovmn.tlcache.exception.SerializationError;
import com.github.bogdanovmn.tlcache.exception.DeserializationError;
import com.github.bogdanovmn.tlcache.strategy.CacheRotateStrategy;
import com.github.bogdanovmn.tlcache.strategy.TwoLevelCacheStrategyAlpha;
import com.github.bogdanovmn.tlcache.strategy.TwoLevelCacheStrategyBeta;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.Random;

public class CacheDemo {
	public static void main(String[] args) throws DeserializationError, SerializationError, PutToCacheError, IOException, RotateObjectError {
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
					.desc("some integer for abstract task (requests count)")
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
					: new TwoLevelCacheStrategyBeta();

				TwoLvlCache<Integer, Integer> cache = new TwoLvlCache<>(
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
		try {
			Thread.sleep(500);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		return number;
	}

	private static void doSomething(int requestsCount, TwoLvlCache cache)
		throws DeserializationError, SerializationError, PutToCacheError, RotateObjectError
	{
		Random generator = new Random();
		for (int i = 1; i < requestsCount; i++) {
			int randValue = generator.nextInt(10);

			System.out.printf("get '%d' --> ", randValue);

			Integer obj = (Integer) cache.get(randValue);
			if (obj == null) {
				cache.put(randValue, complexCompute(randValue));
				System.out.print(" compute\t");
			}
			else {
				System.out.print(" hit\t");
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
