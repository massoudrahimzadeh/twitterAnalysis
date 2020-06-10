import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.ArrayUtils;
import edu.stanford.nlp.util.CoreMap;
import twitter4j.Status;

import java.util.*;

import static com.vdurmont.emoji.EmojiParser.extractEmojis;

public class TwitterAnalyzer {
    static void analyzeTweet(String tweet) {
        Document doc = new Document(tweet);
        System.out.println("#1 " + tweet + ": " + doc.sentences().size());

    }
    static String mostUsedEmojis(List<Status> tweets) {
        Map<String, Integer> tweetsEmojiCount = new HashMap<String, Integer>();

        for(Status tweet:tweets){
            String sentence = tweet.getText();
            List<String> emojiList = extractEmojis(sentence);

            for(String e:emojiList){
                if(tweetsEmojiCount.containsKey(e)){
                    tweetsEmojiCount.put(e, tweetsEmojiCount.get(e)+1 );
                }else if(!tweetsEmojiCount.containsKey(e)){
                    tweetsEmojiCount.put(e, 1 );
                }
            }
        }
        int maximum = 0;
        String mostUsedEmoji = "";
        for( String item : tweetsEmojiCount.keySet()){
            if(tweetsEmojiCount.get(item)>maximum){
                mostUsedEmoji=item;
                maximum=tweetsEmojiCount.get(item);
            }
        }
        return mostUsedEmoji;

// Massoud
    }

    static String mostMentionedWord(List<Status> tweets) {
        Map<String, Integer> tweetsWordsCount = new HashMap<String, Integer>();
        String str= "& a about above after again against all am an and any are aren't as at be because been before being below between both but by can't cannot could couldn't did didn't do does doesn't doing don't down during each few for from further had hadn't has hasn't have haven't having he he'd he'll he's her here here's hers herself him himself his how how's i i'd i'll i'm i've if in into is isn't it it's its itself let's me more most mustn't my myself no nor not of off on once only or other ought our ours ourselves out over own same shan't she she'd she'll she's should shouldn't so some such than that that's the their theirs them themselves then there there's these they they'd they'll they're they've this those through to too under until up very was wasn't we we'd we'll we're we've were weren't what what's when when's where where's which while who who's whom why why's with won't would wouldn't you you'd you'll you're you've your yours yourself yourselves";
        String[] stopWords=str.split(" ");

        for(Status tweet:tweets){
            String sentence = tweet.getText();
            sentence=sentence.toLowerCase();
            String[] words=sentence.split("[, ?]+");

            for(String word:words){
                if(tweetsWordsCount.containsKey(word)){
                    tweetsWordsCount.put(word, tweetsWordsCount.get(word)+1 );
                }else if(!tweetsWordsCount.containsKey(word) && !ArrayUtils.contains(stopWords,word)){
                    tweetsWordsCount.put(word, 1 );
                }
            }
        }

        int maximum = 0;
        String mostUsedWord = "";
        for( String item : tweetsWordsCount.keySet()){
            if(tweetsWordsCount.get(item)>maximum){
                if(!item.equals("")){
                    mostUsedWord=item;
                    maximum=tweetsWordsCount.get(item);
                }
            }
        }
        return mostUsedWord;
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


    private static SentimentValue getSentiment(String tweet) {
            Properties props = new Properties();
            props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment");
            StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
            Annotation annotation = pipeline.process(tweet);
            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                int value = RNNCoreAnnotations.getPredictedClass(tree);
                SentimentValue sentimentValue = SentimentValue.fromValue(value);
                return sentimentValue;
            }
            return SentimentValue.NEGATIVE;
        }
//    private static List<Status> searchTweets(String query) {
//
//
//    }

//    private static List<Status> mostActiveUser(){
//// Burak
//    }
    static List<Status> filterTweetsBasedOnSentiment(SentimentValue sentiment, List<Status> tweets) {
        ArrayList<Status> result = new ArrayList<Status>();
        int i=1;
            for (Status tweet: tweets) {
                if (getSentiment(tweet.getText()) == sentiment) {
                    result.add(tweet);
                    System.out.println("#"+i+":  " +tweet.getText());
                    System.out.println("=========");
                    i++;
                }
            }
            return result;
        }


}
