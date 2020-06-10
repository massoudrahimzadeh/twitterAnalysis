import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("YKE6GZQQb30QL1VmkNGZ9PDuc")
                .setOAuthConsumerSecret("YKiPXe6sY4uHnEKVSPsDU5aOV8JNUijKf3V4qPr9juRkn5FOJm")
                .setOAuthAccessToken("143509053-OSU1SOsrfWXXpyDblNv8s26dUBKapvFvCReZJ8pc")
                .setOAuthAccessTokenSecret("ZwZU7j0Ac5VozEgoFTXHdWAmr5FKfuleaIuvDgAoclRqP");
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        // The factory instance is re-useable and thread safe.
        try {
            Query query = new Query("#redi");
            QueryResult result = twitter.search(query);
            List<Status> statuses = result.getTweets();
            System.out.println("Showing home timeline.");
            for (Status status : statuses) {
                TwitterAnalyzer.analyzeTweet(status.getText());
                TwitterAnalyzer.sentimentAnalyse(status.getText());
                System.out.println("=========");
            }

        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }
}