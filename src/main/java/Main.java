import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

class TwitterAnalyzer {


    private static SentimentValue getSentiment(String tweet) {

    }
    private static List<Status> searchTweets(String query) {
// Aswin sentiment filter
    }
    private static List<Status> mostUsedEmojis() {
// Massoud
    }
    private static List<Status> mostActiveUser(){
// Burak
    }
    private static String mostMentionedWord(List<Status> tweets) {
// Massoud
    }


    private static List<Status> filterTweetsBasedOnSentiment(SentimentValue sentiment, List<Status> tweets) {
        ArrayList<Status> result = new ArrayList<Status>();
        for (Status tweet: tweets) {
            if (getSentiment(tweet.getText()) == sentiment) {
                result.add(tweet);
            }
        }

        return result;
    }

}

public class Main {
    private static void analyzeTweet(String tweet) {
        Document doc = new Document(tweet);
        System.out.println("#1 " + tweet + ": " + doc.sentences().size());

    }
    public static SentimentValue sentimentAnalyse(String tweet) {


        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Annotation annotation = pipeline.process(tweet);
        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            int value = RNNCoreAnnotations.getPredictedClass(tree);
            SentimentValue sentimentValue = SentimentValue.fromValue(value);
            System.out.println("Sentiment analysis of this tweet: "+ sentimentValue);
            return sentimentValue;
        }
        return SentimentValue.NEGATIVE;
    }
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
            Query query = new Query("java");
            QueryResult result = twitter.search(query);
            List<Status> statuses = result.getTweets();
            System.out.println("Showing home timeline.");
            for (Status status : statuses) {
                analyzeTweet(status.getText());
                sentimentAnalyse(status.getText());
                System.out.println("=========");

            }
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }
}