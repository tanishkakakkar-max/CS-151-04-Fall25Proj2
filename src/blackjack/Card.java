package blackjack;

public class Card {

    public class Card {

        private final char rank; // e.g. 'A', 'K', '2', 'T'
        private final char suit; // e.g. 'H', 'D', 'C', 'S'

        public Card(char rank, char suit) {
            if (!isValidRank(rank)) {
                throw new IllegalArgumentException("Invalid rank: " + rank);
            }
            if (!isValidSuit(suit)) {
                throw new IllegalArgumentException("Invalid suit: " + suit);
            }
            this.rank = rank;
            this.suit = suit;
        }

        public char getRankChar() {
            return rank;
        }

        public char getSuitChar() {
            return suit;
        }

        /**
         * Returns the base Blackjack value of this card.
         * Aces are treated as 11 by default (you adjust down to 1 in hand logic).
         */
        public int getBaseValue() {
            return switch (rank) {
                case '2' -> 2;
                case '3' -> 3;
                case '4' -> 4;
                case '5' -> 5;
                case '6' -> 6;
                case '7' -> 7;
                case '8' -> 8;
                case '9' -> 9;
                case 'T', 'J', 'Q', 'K' -> 10;
                case 'A' -> 11;
                default -> 0;
            };
        }


        public String toCode() {
            return "" + rank + suit;
        }


        public static Card fromCode(String code) {
            if (code == null || code.length() != 2) {
                throw new IllegalArgumentException("Invalid card code: " + code);
            }
            char r = code.charAt(0);
            char s = code.charAt(1);
            return new Card(r, s);
        }

        private static boolean isValidRank(char r) {
            return switch (r) {
                case '2','3','4','5','6','7','8','9','T','J','Q','K','A' -> true;
                default -> false;
            };
        }

        private static boolean isValidSuit(char s) {
            return switch (s) {
                case 'H','D','C','S' -> true;
                default -> false;
            };
        }

        // Optional helpers for nicer UI text:

        public String getRankName() {
            return switch (rank) {
                case '2' -> "2";
                case '3' -> "3";
                case '4' -> "4";
                case '5' -> "5";
                case '6' -> "6";
                case '7' -> "7";
                case '8' -> "8";
                case '9' -> "9";
                case 'T' -> "10";
                case 'J' -> "Jack";
                case 'Q' -> "Queen";
                case 'K' -> "King";
                case 'A' -> "Ace";
                default -> "?";
            };
        }

//        public String getSuitName() {
//            return switch (suit) {
//                case 'H' -> "Hearts";
//                case 'D' -> "Diamonds";
//                case 'C' -> "Clubs";
//                case 'S' -> "Spades";
//                default -> "?";
//            };
//        }

        @Override
        public String toString() {
            return getRankName() + " of " + getSuitName();
        }
    }

}
