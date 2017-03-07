package com.github.bogdanovmn.tlcache.demo;


import com.github.bogdanovmn.tlcache.TwoLvlCache;
import com.github.bogdanovmn.tlcache.strategy.CacheRotateStrategy;
import com.github.bogdanovmn.tlcache.strategy.TwoLevelCacheStrategyAlpha;
import org.apache.commons.cli.*;

public class CacheDemo {
	public static void main(String[] args) {
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

				TwoLvlCache cache = new TwoLvlCache(
					Integer.valueOf(cmdLine.getOptionValue("first-level-limit")),
					Integer.valueOf(cmdLine.getOptionValue("second-level-limit")),
					strategy
				);

				doSomething(cache);
			}
		}
		catch (ParseException e) {
			System.err.println(e.getMessage());
			printHelp(cliOptions);
		}
	}

	private static void doSomething(TwoLvlCache cache) {

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
