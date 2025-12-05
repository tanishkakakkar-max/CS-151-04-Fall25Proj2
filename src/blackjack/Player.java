package blackjack;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {

    protected final String name;
    protected final List<Card> hand = new ArrayList<>();
    protected int balance;
    protected int currentBet;
    protected boolean standing;
    protected boolean busted;

    public Player(String name, int startingBalance) {
        this.name = name;
        this.balance = startingBalance;
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }

    public int getBalance() {
        return balance;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public boolean isStanding() {
        return standing;
    }

    public boolean isBusted() {
        return busted;
    }

    public void resetForNewRound() {
        hand.clear();
        currentBet = 0;
        standing = false;
        busted = false;
    }

    public void placeBet(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Bet must be positive");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Not enough balance");
        }
        currentBet = amount;
    }

    public void addCard(Card card) {
        hand.add(card);
        int value = getHandValue();
        if (value > 21) {
            busted = true;
        }
    }

    /**
     * Basic blackjack hand value with ace adjustment.
     */
    public int getHandValue() {
        int total = 0;
        int aceCount = 0;
        for (Card c : hand) {
            total += c.getBaseValue();
            if (c.getRankChar() == 'A') {
                aceCount++;
            }
        }
        // adjust aces from 11 to 1 as needed
        while (total > 21 && aceCount > 0) {
            total -= 10;
            aceCount--;
        }
        return total;
    }

    public boolean hasBlackjack() {
        return hand.size() == 2 && getHandValue() == 21;
    }

    public void winBet() {
        balance += currentBet;
    }

    public void loseBet() {
        balance -= currentBet;
    }

    public void pushBet() {
        // no change
    }

    public void stand() {
        standing = true;
    }

    /**
     * Each subclass can decide how to act automatically.
     */
    public abstract boolean shouldHit();
}
