package com.alec.TwitterAPI.Controllers;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import twitter4j.Query;
import twitter4j.Status;

import com.alec.TwitterAPI.TimeLib;
import com.alec.TwitterAPI.TwitterApi;
import com.google.gson.Gson;

@ComponentScan
@Controller
@RequestMapping("/")
public class TwitterController {

	private Gson		gson	= new Gson();

	private TwitterApi	twitterApi;

	private HttpHeaders	responseHeaders;

	private SimpleDateFormat	dateFormat;
	
	private Logger logger;
	

	// init
	public TwitterController() {
		
		logger = LoggerFactory.getLogger(TwitterController.class);
		
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		// init response headers
		responseHeaders = new HttpHeaders();
		responseHeaders.add("Access-Control-Allow-Origin", "*");
		responseHeaders.add("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		responseHeaders.add("Access-Control-Max-Age", "3600");
		responseHeaders.add("Access-Control-Allow-Headers", "Content-Type");
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);

		// init api
		twitterApi = TwitterApi.getInstance();

	}

	@RequestMapping(method = RequestMethod.GET, value = "/test")
	public ResponseEntity<String> test() {
		String gString = gson.toJson("success");
		return new ResponseEntity<String>(gString, responseHeaders,
				HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.POST, value = "searchTweets")
	public ResponseEntity<String> searchTweets(
			@RequestParam int maxQueryCount,
			@RequestParam int minRemainingRequestCount,
			@RequestBody Query searchParams
			)
	{
		
		ArrayList<Status> tweets = twitterApi.searchTweets(searchParams);

		String gString = gson.toJson(tweets);
		return new ResponseEntity<String>(gString, responseHeaders,
				HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.POST, value = "searchAndSaveTweets")
	public ResponseEntity<String> searchAndSaveTweets(
			@RequestParam int maxQueryCount,
			@RequestParam int minRemainingRequestCount,
			@RequestBody Query searchParams) {
		logger.info("searchAndSaveTweets( " + gson.toJson(searchParams) + " , " + maxQueryCount + " , " + minRemainingRequestCount + " ) at " + TimeLib.now());
		
		ArrayList<Status> tweets = twitterApi.searchTweets(searchParams, maxQueryCount, minRemainingRequestCount);


		String fileName = searchParams.getQuery().split(" ")[0] + " at " + TimeLib.now();

		File file = new File(fileName + ".json");

		// make the file
		try {
			file.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			PrintWriter writer = new PrintWriter(file);
			writer.println(gson.toJson(tweets));
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		logger.info("searchAndSaveTweets() : file written = " + file.getAbsolutePath() );
		
		String gString = gson.toJson("file written = " + file.getAbsolutePath());
		return new ResponseEntity<String>(gString, responseHeaders,
				HttpStatus.OK);
	}
}
