package twitter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import twitter.Tweet;
import java.time.Instant;

import org.junit.Test;

public class SocialNetworkTest {

    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    // Test case for Task 1: guessFollowsGraph()
    
 // Test case: Empty list of tweets
    @Test
    public void testGuessFollowsGraphEmpty() {
        List<Tweet> tweets = new ArrayList<>();  // Create an empty list of tweets
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        
        // Check that the followsGraph is indeed empty
        assertTrue("Expected an empty graph for an empty tweet list", followsGraph.isEmpty());
    }

    // Test case: Single tweet without mentions
    @Test
    public void testGuessFollowsGraphNoMentions() {
        Tweet tweet = new Tweet(1, "user1", "Just tweeting without mentioning anyone", Instant.now());
        List<Tweet> tweets = new ArrayList<>();
        tweets.add(tweet);

        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);

        assertTrue("Expected user1 to not follow anyone as no mentions", followsGraph.isEmpty());
    }


 // Test case: Single tweet with one mention
    @Test
    public void testGuessFollowsGraphOneMention() {
        Tweet tweet = new Tweet(1, "user1", "Hello @user2!", Instant.now());
        List<Tweet> tweets = new ArrayList<>();
        tweets.add(tweet);

        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);

        assertTrue("Expected user1 to follow user2", followsGraph.get("user1").contains("user2"));
        assertEquals("Expected user1 to have 1 follow", 1, followsGraph.get("user1").size());
    }

    // Test case: Multiple tweets from the same user mentioning different people
    @Test
    public void testGuessFollowsGraphMultipleMentions() {
        Tweet tweet1 = new Tweet(1, "user1", "Hello @user2!", Instant.now());
        Tweet tweet2 = new Tweet(2, "user1", "Also, hi @user3!", Instant.now());
        List<Tweet> tweets = new ArrayList<>();
        tweets.add(tweet1);
        tweets.add(tweet2);

        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);

        assertTrue("Expected user1 to follow user2", followsGraph.get("user1").contains("user2"));
        assertTrue("Expected user1 to follow user3", followsGraph.get("user1").contains("user3"));
        assertEquals("Expected user1 to follow 2 people", 2, followsGraph.get("user1").size());
    }

 // Test case: Multiple users mentioning each other
    @Test
    public void testGuessFollowsGraphMultipleUsers() {
        Tweet tweet1 = new Tweet(1, "user1", "Hey @user2", Instant.now());
        Tweet tweet2 = new Tweet(2, "user2", "Hi @user1", Instant.now());
        List<Tweet> tweets = new ArrayList<>();
        tweets.add(tweet1);
        tweets.add(tweet2);

        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);

        assertTrue("Expected user1 to follow user2", followsGraph.get("user1").contains("user2"));
        assertTrue("Expected user2 to follow user1", followsGraph.get("user2").contains("user1"));
    }

    // Test case for Task 2: influencers()
    
    // Test case: Empty graph for influencers
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);

        assertTrue("expected empty list", influencers.isEmpty());
    }

    // Test case: Single user without followers
    @Test
    public void testInfluencersSingleUserNoFollowers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("user1", new HashSet<>());

        List<String> influencers = SocialNetwork.influencers(followsGraph);

        assertTrue("expected empty list", influencers.isEmpty());
    }

    // Test case: Single influencer
    @Test
    public void testInfluencersSingleInfluencer() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        Set<String> followers = new HashSet<>();
        followers.add("user2");
        followsGraph.put("user1", followers); // user1 follows user2

        List<String> influencers = SocialNetwork.influencers(followsGraph);

        assertEquals("expected user2 to be the top influencer", "user2", influencers.get(0));
    }

    // Test case: Multiple influencers
    @Test
    public void testInfluencersMultipleInfluencers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        Set<String> followers1 = new HashSet<>();
        followers1.add("user2");
        followers1.add("user3");
        followsGraph.put("user1", followers1); // user1 follows user2 and user3

        Set<String> followers2 = new HashSet<>();
        followers2.add("user3");
        followsGraph.put("user2", followers2); // user2 follows user3

        List<String> influencers = SocialNetwork.influencers(followsGraph);

        assertEquals("expected user3 to be the top influencer", "user3", influencers.get(0));
        assertEquals("expected user2 to be second influencer", "user2", influencers.get(1));
    }

    // Test case: Tied influence
    @Test
    public void testInfluencersTiedInfluence() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        Set<String> followers1 = new HashSet<>();
        followers1.add("user2");
        followsGraph.put("user1", followers1); // user1 follows user2

        Set<String> followers2 = new HashSet<>();
        followers2.add("user1");
        followsGraph.put("user2", followers2); // user2 follows user1

        List<String> influencers = SocialNetwork.influencers(followsGraph);

        assertTrue("expected user1 and user2 to both be influencers", influencers.contains("user1"));
        assertTrue("expected user1 and user2 to both be influencers", influencers.contains("user2"));
    }

}
