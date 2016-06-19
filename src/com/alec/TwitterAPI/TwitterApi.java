package com.alec.TwitterAPI;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import com.google.gson.Gson;

public class TwitterApi
{
	private static TwitterApi	instance	= null;
	private static long			sinceId;			
	private Gson				gson;
	private DateFormat			dateFormat;
	private String				searchTerm;
	private Twitter				twitter;
	private Logger				logger;

	protected TwitterApi() {
		// block instantiation from outside class
	};

	public static TwitterApi getInstance() {
		if (instance == null) {
			instance = new TwitterApi();
			instance.init();
		}

		return instance;
	}

	public void init() {

		logger = LoggerFactory.getLogger(TwitterApi.class);
		logger.info("init() at " + TimeLib.now());
		gson = new Gson();
		sinceId = 0;

		twitter = new TwitterFactory().getInstance();

		// My Applications Consumer and Auth Access Token
		twitter.setOAuthConsumer("4nk1cj69tilk2tf45DQekZNwW",
				"w7iCPa2HhZa4i03oNSvKRTdoF5bO07Qrr9rt44pqDlGR1pdGLu");
		twitter.setOAuthAccessToken(new AccessToken(
				"2758679215-uLFiaC1tBgO6obfIFadXfYAY62iV1QfdjMobZnL",
				"AeJn8G39IFmdNpf1848u0BkHu094WuzSlpsTL62XSqIpK"));
	}

	public ArrayList<Status> searchTweets(Query query) {
		return searchTweets(query, 1, 1);
	}

	public ArrayList<Status> searchTweets(Query query, int maxQueryCount,
			int minRemainingRequestCount)
	{

		logger.info("searchTweets(" + gson.toJson(query) + " , " + maxQueryCount + " , "
				+ minRemainingRequestCount + ") at " + TimeLib.now());
		
		
		if (sinceId != 0) {
			query.setSinceId(sinceId);
			logger.info("searchTweets() : sinceId = " + sinceId);
		}
		
		
		ArrayList<Status> allTweets = new ArrayList<Status>();
		try {

			// get the first set of 100
			QueryResult result;
			RateLimitStatus rateLimitStatus;
			int queryCount = 0;
			long lastId = 0;

			do {
				result = twitter.search(query);
				if (result.getCount() == 0) {
					break;
				}

				queryCount++;
				rateLimitStatus = result.getRateLimitStatus();

				allTweets.addAll(result.getTweets());

				if (allTweets.isEmpty()) {
					break;
				}

				lastId = allTweets.get(allTweets.size() - 1).getId();

				// update the query to get tweets before the last tweet
				query.setMaxId(lastId);

			} while (result.getCount() == 100
					&& rateLimitStatus.getRemaining() > minRemainingRequestCount
					&& queryCount < maxQueryCount);

			// save the lastId
			sinceId = lastId;
			
			logger.info("searchTweets() : rateLimitStatus = {}", result.getRateLimitStatus());

		} catch (TwitterException te) {
			logger.error(te.getLocalizedMessage());
			te.printStackTrace();
			System.out.println("Failed to search tweets: " + te.getMessage());
		}

		return allTweets;
	}

}
