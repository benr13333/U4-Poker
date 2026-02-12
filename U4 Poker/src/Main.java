import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.*; // <-- can only use arrays.


public class Main {
   public static void main(String[] args) {
       // String that I can use to refer to the filepath instead of typing out "src/data" every time
       final String filePath = "src/data";
       // Since I can't add elements dynamically, and creating a large array would waste memory, I get the lines of data with this custom method.
       int dataLines = Tools.countLines(Path.of(filePath));


       // Initialize an array of hands that holds the number of lines in our data.
       Hand[] hands = new Hand[dataLines];


       // COUNTS
       int fiveOfKindCount = 0;
       int fourOfKindCount = 0;
       int fullHouseCount = 0;
       int threeOfKindCount = 0;
       int twoPairCount = 0;
       int onePairCount = 0;
       int highCardCount = 0;

       // VARS
       int totalBidValue = 0;
       int totalJackBidValue = 0;
       Scanner s;

       try
       {
           File f = new File(filePath);
           s = new Scanner(f);
       }
       catch (FileNotFoundException e)
       {
           System.out.println("File not found");
           return;
       }


     /*
         (Lines will look something like
         3,2,10,3,King|765
         at this point.


        However, this is a String, and we'll want to parse this string into an int. This is
        easy to do with regular ints. However, we have four other cases to check for. I'll just
        use a switch case for this.


        Bid values will be stored in a separate array such that they are accessible later.
        (i.e if a hand is 3,2,10,3,King|765 we'll store our hand on one array then our bid on another array,
        but at the same index.)
      */


       // Read hands from file
       int index = 0;
       while (s.hasNextLine())
       {
           String line = s.nextLine();

           /*
           Parts will consist of the hand itself and the bid value
           i.e
           hand is 5, 3, 2, 1, King | 634\


               parts[0] = 5, 3, 2, 1, King
               parts[1] = 634

            */

           String[] parts = line.split("\\|"); // Since | is an operator I used escape chars to use it as the regex
           String[] cardsStr = parts[0].split(",");


           int[] cards = new int[5];

           // Switched this from a try catch
           for (int i = 0; i < 5; i++)
           {
               switch (cardsStr[i]) {
                   case "Ace" -> cards[i] = 14;
                   case "King" -> cards[i] = 13;
                   case "Queen" -> cards[i] = 12;
                   case "Jack" -> cards[i] = 11;


                   default -> cards[i] = Integer.parseInt(cardsStr[i]);
               }
           }


           int bid = Integer.parseInt(parts[1]);
           hands[index] = new Hand(cards, bid);
           index++;
       }


       // Count pattern types
       for (Hand h : hands)
       {
           switch (h.patternType) {
               case 7 -> fiveOfKindCount++;
               case 6 -> fourOfKindCount++;
               case 5 -> fullHouseCount++;
               case 4 -> threeOfKindCount++;
               case 3 -> twoPairCount++;
               case 2 -> onePairCount++;
               case 1 -> highCardCount++;
           }
       }

       for (int i = 0; i < hands.length; i++)
       {
           // It's important that weakerCount gets reset every time we loop. It also starts at 1 because ranks start at 1; for the weakest hand, if weakerCount was at 0
           // and it isn't stronger than anything, then it's rank would be 1 rather than 0. It
           int weakerCount = 1;


           // Since this is a nested loop, j will get reset back to 0 every time, so if we are on hand 2, it still gets compared to hand 1. But thanks to the safe guard
           // in the next check, we'll still avoid checking if hand 2 is stronger than hand 2. We can also do hands[i].rank = weakerCount + 1; but it's less intuitive.
           for (int j = 0; j < hands.length; j++)
           {
               // Account for the upper and lower bounds of the array (i.e we don't want the first hand to be compared to itself)
               if (i == j)
               {
                   continue;
               }


               if (hands[i].isStrongerThan(hands[j]))
               {
                   weakerCount++;
               }
           }


           hands[i].rank = weakerCount;
       }


       /*
           Now we calculate our total bid value by multiplying the bid value of each hand by it's ranking, which is super easy considering they're both objects that store their own bid value
           and rank.
        */
       for (int i = 0; i < hands.length; i++)
       {
           totalBidValue += hands[i].bid * hands[i].rank;
       }


       System.out.println("Number of five of a kind hands: " + fiveOfKindCount);
       System.out.println("Number of four of a kind hands: " + fourOfKindCount);
       System.out.println("Number of full house hands: " + fullHouseCount);
       System.out.println("Number of three of a kind hands: " + threeOfKindCount);
       System.out.println("Number of two pair hands: " + twoPairCount);
       System.out.println("Number of one pair hands: " + onePairCount);
       System.out.println("Number of high card hands: " + highCardCount);
       System.out.println("---------------------------------------------------------------");
       System.out.println("Total Bid Value: " + totalBidValue);


       for (Hand diffHands : hands)
       {
           diffHands.switchJacks();
       }


       // This is all exactly the same as the one above.
       for (int i = 0; i < hands.length; i++)
       {
           int weakerCount = 1;


           for (int j = 0; j < hands.length; j++)
           {
               if (i == j)
               {
                   continue;
               }
               if (hands[i].isStrongerThanWildJacks(hands[j]))
               {
                   weakerCount++;
               }
           }
           hands[i].rank = weakerCount;
       }

       for (int i = 0; i < hands.length; i++)
       {
           totalJackBidValue += hands[i].bid * hands[i].rank;
       }

       System.out.println("Total Bid Value With Jacks Wild: " + totalJackBidValue);


       //  hands[0].printStats();
   }
}
