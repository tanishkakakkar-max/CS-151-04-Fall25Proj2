package blackjack;

/**
 * HumanPlayer represents a human player in the blackjack game.
 * The shouldHit() method is not used for human players as they make decisions via UI buttons.
 */
public class HumanPlayer extends Player {

    public HumanPlayer(String name, int startingBalance) {
        super(name, startingBalance);
    }

    @Override
    public boolean shouldHit() {
        // Human players make decisions via UI, so this is not used
        // Return false as default (stand)
        return false;
    }
}

