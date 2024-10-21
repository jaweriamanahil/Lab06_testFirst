package twitter;

import java.time.Instant;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Sample tweets to simulate a social network
        List<Tweet> tweets = new ArrayList<>();
        tweets.add(new Tweet(1, "Jaweria", "Hello @Sara! How's it going?", Instant.now()));
        tweets.add(new Tweet(2, "Sara", "Hi @Urwa and @Jaweria!", Instant.now()));
        tweets.add(new Tweet(3, "Urwa", "Good day @Jaweria! @Saba", Instant.now()));
        tweets.add(new Tweet(4, "Saba", "Hey @Sara! Let's connect.", Instant.now()));
        tweets.add(new Tweet(5, "Noor", "Shoutout to @Sara and @Saba!", Instant.now()));
        tweets.add(new Tweet(6, "Sara", "Thanks @Noor!", Instant.now()));

        // Generate follows graph from the tweets
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);

        // Print the follows graph for debugging
        System.out.println("Follows Graph:");
        for (Map.Entry<String, Set<String>> entry : followsGraph.entrySet()) {
            System.out.println(entry.getKey() + " follows: " + entry.getValue());
        }

        // Find and print the top influencers
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        System.out.println("\nTop Influencers:");
        for (int i = 0; i < Math.min(10, influencers.size()); i++) {
            System.out.println((i + 1) + ". " + influencers.get(i));
        }
    }
}
