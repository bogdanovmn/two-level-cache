package com.github.bogdanovmn.tlcache.demo;


import com.github.bogdanovmn.cmdlineapp.CmdLineAppBuilder;
import com.github.bogdanovmn.tlcache.*;
import org.apache.commons.cli.ParseException;

import java.util.Random;

public class CacheDemo {
	public static void main(String[] args) throws Exception {
		new CmdLineAppBuilder(args)
			.withJarName("app")
			.withDescription("Two level cache demo")
			.withArg("first-level-limit", "max memory (bytes) for first level cache")
			.withArg("second-level-limit", "max memory (bytes) for second level cache")
			.withArg("rotate-strategy", "old objects rotate strategy: alpha or beta")
			.withArg("task-param", "some integer for abstract task (requests count)")
			.withEntryPoint(
				cmdLine -> {
					String strategyName = cmdLine.getOptionValue("r");

					if (!strategyName.equals("alpha") &&
						!strategyName.equals("beta"))
					{
						throw new ParseException("invalid strategy name");
					}
					else {
						TwoLvlCacheRotateStrategy strategy = strategyName.equals("alpha")
							? new TwoLvlCacheStrategyAlpha()
							: new TwoLvlCacheStrategyBeta();

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
			).build().run();
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

	private static void doSomething(int requestsCount, TwoLvlCache<Integer, Integer> cache)
		throws TwoLvlCacheException
	{
		Random generator = new Random();
		for (int i = 1; i < requestsCount; i++) {
			Integer randValue = generator.nextInt(10);

			System.out.printf("get '%d' --> ", randValue);

			Integer obj = cache.get(randValue);
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
}
