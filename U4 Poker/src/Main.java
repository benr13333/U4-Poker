import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.*; // <-- can only use arrays.




public class Main {
    public static void main(String[] args) {


        final String filePath = "src/data";
        int dataLines = Tools.countLines(Path.of(filePath));


        // COUNTS
        int fiveOfKindCount = 0;
        int fourOfKindCount = 0;
        int fullHouseCount = 0;
        int threeOfKindCount = 0;
        int twoPairCount = 0;
        int onePairCount = 0;
        int highCardCount = 0;


        // CONNECTING HANDS
        int checkCount = 0;
        int[] patternType = new int[Tools.countLines(Path.of(filePath))];


        String fileData = "";
        try
        {
            File f = new File(filePath);
            Scanner s = new Scanner(f);








            while (s.hasNextLine()) {
                String line = s.nextLine();
                fileData += line + "\n";
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found");
        }




        String[] lines = fileData.split("\n");




        // System.out.println(Arrays.toString(lines)); // <-- delete later.




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


        for (String line : lines) {


            String[] parts = line.split("\\|");
            String[] cards = parts[0].split(",");




            int[] hand = new int[5];




            for (int i = 0; i < 5; i++) {
                try {
                    hand[i] = Integer.parseInt(cards[i]);
                }
                catch (NumberFormatException e)
                {
                    switch (cards[i])
                    {
                        // This should handle all non int Strings. I might want to allow for lowercase later
                        case "Ace" -> hand[i] = 14;
                        case "King" -> hand[i] = 13;
                        case "Queen" -> hand[i] = 12;
                        case "Jack" -> hand[i] = 11;
                    }
                }
            }




          /*
           Since I can't use a map, I'll create a type of primitive one by using an array, and storing the card number in the index and the number
           of times it occurs as the value of the index.
          */
            int[] freq = new int[15];
            for (int card : hand)
            {
                freq[card]++;
            }




          /*
               Using the map above I can collect the number of singles, double, triples, quadruples, and fives
               for any given hand.
          */




            int doubles = 0;
            int triples = 0;
            int quadruples = 0;
            int fives = 0;




            // We'll start the loop as two because we made aces = 14 and not 1.
            for(int i = 2; i <= 14; i++)
            {
                if (freq[i] == 2)
                {
                    doubles++;
                }
                else if (freq[i] == 3)
                {
                    triples++;
                }
                else if (freq[i] == 4)
                {
                    quadruples++;
                }
                else if (freq[i] == 5)
                {
                    fives++;
                }
            }


            // These variables are going to exist within the for loop intentionally.
            if (fives == 1)
            {
                fiveOfKindCount++;
                patternType[checkCount] = 7;
            }
            else if (quadruples == 1)
            {
                fourOfKindCount++;
                patternType[checkCount] = 6;
            }
            else if (triples == 1 && doubles == 1)
            {
                fullHouseCount++;
                patternType[checkCount] = 5;
            }
            else if (triples == 1)
            {
                threeOfKindCount++;
                patternType[checkCount] = 4;
            }
            else if (doubles == 2)
            {
                twoPairCount++;
                patternType[checkCount] = 3;
            }
            else if (doubles == 1)
            {
                onePairCount++;
                patternType[checkCount] = 2;
            }
            else
            {
                highCardCount++;
                patternType[checkCount] = 1;
            }


            checkCount++;


            System.out.println(Arrays.toString(hand));
        }








        System.out.println("Number of five of a kind hands: " + fiveOfKindCount);
        System.out.println("Number of four of a kind hands: " + fourOfKindCount);
        System.out.println("Number of full house hands: " + fullHouseCount);
        System.out.println("Number of three of a kind hands: " + threeOfKindCount);
        System.out.println("Number of two pair hands: " + twoPairCount);
        System.out.println("Number of one pair hands: " + onePairCount);
        System.out.println("Number of high card hands: " + highCardCount);


       /*
           Now, we'll create a way of ranking the cards.


           Since we can't use an ArrayList, and doing something like ranking = new int[1000] seems inefficient
           and won't always work. We'll initialize a new list based off the amount of lines that are in our data.
           I'll handle this in a new class so our Main doesn't get clogged up.
       */


        int[] ranking = new int[Tools.countLines(Path.of(filePath))];
        // System.out.println("Ranking Length = " + ranking.length);


       /*


           KEEPING COUNT OF PATTERN TYPE -


           At this point, we have properly initialized an array for our ranking that is the perfect length. Now,


           I'll start by ordering the list in terms of pattern, and worry about ties later.
           My first idea for doing this is creating a new array that will tell me what type of pattern
           my hand is. So when we do something like threeOfKindCount++; we'll also do pattern[i] = 3, which means
           that we'll also have to initialize some variable named i that keeps count.


           If this doesn't work ^, I might try using 2d arrays. I might just do this anyways even if the above does work
           since it'll probably be easier to manage in the future.




           RANKING BASED OFF PATTERN TYPE -
           Now that we have another array which manages the pattern type, I'll start putting them in ranking based off the number in the pattern.
           I can do this by looping through the pattern type array to find the ones with the highest score and place them in my rank array.




           (i.e.) given a patternType list of 2, 3, 1.
           Meaning our first hand has a score of 2, our second hand has a score of 3, and our last hand has a score of 1.
           Our ranking should be [hand] 2, 1, 3.
           What we can do is




        */














        // DEBUG


       /*
           Print the patternType[] array. This is part of a parallel array that will give me the type of pattern
           of each hand.


           An example output is [7, 5, 1]
           which would mean the first hand will be a five of a kind, then a three of a kind, then a high count.


           Pattern Type Codes:
           7 = Five of a Kind
           6 = Four of a Kind
           5 = Full House
           4 = Three of a Kind
           3 = Two Pair
           2 = One Pair
           1 = High Card
        */


        System.out.println("PatternType: " + Arrays.toString(patternType));


       /*
       Prints out the ranking list.


        */


        System.out.println(Arrays.toString(ranking));
    }




}


