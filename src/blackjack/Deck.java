package blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Deck {

    private final List<Card> cards = new ArrayList<>();
    private final Random random = new Random();

    public Deck() {
        reset();
    }

    public void reset() {
        cards.clear();

        char[] suits = {'H', 'D', 'C', 'S'};
        char[] ranks = {'2','3','4','5','6','7','8','9','T','J','Q','K','A'};

        for (char suit : suits) {
            for (char rank : ranks) {
                cards.add(new Card(rank, suit));
            }
        }

        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards, random);
    }

    public Card drawCard() {
        if (cards.isEmpty()) {
            reset();
        }
        return cards.remove(cards.size() - 1);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }


    public List<Card> getRemainingCards() {
        return new ArrayList<>(cards);
    }

    public void setFromCardCodes(List<String> codes) {
        cards.clear();
        for (String code : codes) {
            cards.add(Card.fromCode(code));
        }
    }
}
