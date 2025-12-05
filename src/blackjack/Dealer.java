package blackjack;

public class Dealer {
    public class Dealer extends Player {

        public Dealer(int startingBalance) {
            super("Dealer", startingBalance);
        }

        @Override
        public boolean shouldHit() {
            int value = getHandValue();
            if (value < 17) {
                return true;
            }

            // soft 17 logic: hit on soft 17 (has ace counted as 11)
            boolean hasAce = hand.stream().anyMatch(c -> c.getRankChar() == 'A');
            return value == 17 && hasAce;
        }
    }
}
