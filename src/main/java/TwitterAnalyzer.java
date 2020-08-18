import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.ArrayUtils;
import edu.stanford.nlp.util.CoreMap;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import javax.swing.*;
import java.util.*;

import static com.vdurmont.emoji.EmojiParser.extractEmojis;

public class TwitterAnalyzer {

    static Twitter config(){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("PZWBGuVP3hhkPRvD0p9xdH9cf")
                .setOAuthConsumerSecret("2e5w43IAuopqeVoKYQ5yeA7vNNyYZSPrV3Fd5dQeA5dBGuabGW")
                .setOAuthAccessToken("1268222770116165633-kjbHW53Lie0k3tSF9LScJKIsl8RhP6")
                .setOAuthAccessTokenSecret("QTe3KxJbC6tkveL2Yo8N12Tt8PXDzfAXVsl5FAhllvT5k")
                .setTweetModeExtended(true);


        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        return twitter;
    }
    static List<Status> getStatuses() {
        ResponseList<Status> statuses =null;
        try {
            Twitter twitter = config();
            statuses = twitter.getHomeTimeline();

        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return statuses;
    }
    static List<Status> getStatusesBaseOnTheKeyWord(String keyWord) {
        List<Status> statuses = null;
        List<Status> statuses1 = null;

        try {
            Twitter twitter = config();
            Query query = new Query(keyWord);
            QueryResult result = twitter.search(query);
            statuses = result.getTweets();
            
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return statuses;
    }
    static void displayTweets(String keyword){
        List<Status> statuses = getStatusesBaseOnTheKeyWord(keyword);
        String str="";
        for (Status status : statuses) {
            str+= "\n"+ TwitterAnalyzer.analyzeTweet(status.getText()) + "\n"+ TwitterAnalyzer.sentimentAnalyse(status.getText()) +"\n=========";
        }
        TwitterAnalyzer.showResultJTextArea(str);
    }
    static void showResultJTextArea(String str){
        JTextArea textArea = new JTextArea(60, 100);
        textArea.setText(str);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(null, scrollPane);
    }

    static String analyzeTweet(String tweet) {
        String str = "";
        Document doc = new Document(tweet);
        str+= tweet + ": " + doc.sentences().size();
        return str;
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
        JOptionPane.showMessageDialog(null,mostUsedEmoji);
        return mostUsedEmoji;
    }

    static String mostMentionedWord(List<Status> tweets) {
        Map<String, Integer> tweetsWordsCount = new HashMap<String, Integer>();
        String str= "rt - & a about above after again against all am an and any are aren't as at be because been before being below between both but by can't cannot could couldn't did didn't do does doesn't doing don't down during each few for from further had hadn't has hasn't have haven't having he he'd he'll he's her here here's hers herself him himself his how how's i i'd i'll i'm i've if in into is isn't it it's its itself let's me more most mustn't my myself no nor not of off on once only or other ought our ours ourselves out over own same shan't she she'd she'll she's should shouldn't so some such than that that's the their theirs them themselves then there there's these they they'd they'll they're they've this those through to too under until up very was wasn't we we'd we'll we're we've were weren't what what's when when's where where's which while who who's whom why why's with won't would wouldn't you you'd you'll you're you've your yours yourself yourselves";
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
        JOptionPane.showMessageDialog(null,mostUsedWord);
        return mostUsedWord;
    }

    public static String sentimentAnalyse(String tweet) {

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Annotation annotation = pipeline.process(tweet);
        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            int value = RNNCoreAnnotations.getPredictedClass(tree);
            SentimentValue sentimentValue = SentimentValue.fromValue(value);
//            System.out.println("Sentiment analysis of this tweet: "+ sentimentValue);
            return "Sentiment analysis of this tweet: " +sentimentValue;
        }
        return SentimentValue.NEGATIVE.toString();
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

     static String mostActiveUser(List<Status> tweets){
        Map<String, Integer> activeUsers = new HashMap<String, Integer>();

        for(Status tweet:tweets){
            String userName = tweet.getUser().getName();
            if(activeUsers.containsKey(userName)){
                activeUsers.put(userName,activeUsers.get(userName)+1);
            }else{
                activeUsers.put(userName,1);

            }
        }
        int maximum = 0;
        String mostActiveUser= "";
        for( String user : activeUsers.keySet()){
            if(activeUsers.get(user)>maximum){
                mostActiveUser=user;
                maximum=activeUsers.get(user);
            }
        }
        JOptionPane.showMessageDialog(null,mostActiveUser);
        return mostActiveUser;

// Burak
    }
    static void filterTweetsBasedOnSentiment(SentimentValue sentiment, List<Status> tweets) {
        ArrayList<Status> result = new ArrayList<Status>();
        int i=1;
        StringBuilder sb = new StringBuilder();
        String str = "";
            for (Status tweet: tweets) {
                if (getSentiment(tweet.getText()) == sentiment) {
                    str+="\n#"+i+":  " +tweet.getText() + "\n"+ getSentiment(tweet.getText())+"\n =========";
                    result.add(tweet);
                    i++;
                }
            }
        JTextArea textArea = new JTextArea(60, 100);
        textArea.setText(str);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(null, scrollPane);
        }
}
