import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import blackjack.Card;

public class CardTest {

    @Test
    public void testCardCreation() {
        Card card = new Card('A', 'H');
        assertEquals('A', card.getRankChar());
        assertEquals('H', card.getSuitChar());
    }

    @Test
    public void testInvalidRank() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Card('X', 'H');
        });
    }

    @Test
    public void testInvalidSuit() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Card('A', 'X');
        });
    }

    @Test
    public void testGetBaseValue() {
        assertEquals(2, new Card('2', 'H').getBaseValue());
        assertEquals(10, new Card('T', 'H').getBaseValue());
        assertEquals(10, new Card('J', 'H').getBaseValue());
        assertEquals(10, new Card('Q', 'H').getBaseValue());
        assertEquals(10, new Card('K', 'H').getBaseValue());
        assertEquals(11, new Card('A', 'H').getBaseValue());
    }

    @Test
    public void testToCode() {
        Card card = new Card('A', 'H');
        assertEquals("AH", card.toCode());
        
        Card card2 = new Card('K', 'D');
        assertEquals("KD", card2.toCode());
    }

    @Test
    public void testFromCode() {
        Card card = Card.fromCode("AH");
        assertEquals('A', card.getRankChar());
        assertEquals('H', card.getSuitChar());
        
        Card card2 = Card.fromCode("2C");
        assertEquals('2', card2.getRankChar());
        assertEquals('C', card2.getSuitChar());
    }

    @Test
    public void testFromCodeInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            Card.fromCode("A");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            Card.fromCode("ABC");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            Card.fromCode(null);
        });
    }

    @Test
    public void testGetRankName() {
        assertEquals("Ace", new Card('A', 'H').getRankName());
        assertEquals("King", new Card('K', 'H').getRankName());
        assertEquals("Queen", new Card('Q', 'H').getRankName());
        assertEquals("Jack", new Card('J', 'H').getRankName());
        assertEquals("10", new Card('T', 'H').getRankName());
        assertEquals("2", new Card('2', 'H').getRankName());
    }

    @Test
    public void testGetSuitName() {
        assertEquals("Hearts", new Card('A', 'H').getSuitName());
        assertEquals("Diamonds", new Card('A', 'D').getSuitName());
        assertEquals("Clubs", new Card('A', 'C').getSuitName());
        assertEquals("Spades", new Card('A', 'S').getSuitName());
    }

    @Test
    public void testToString() {
        Card card = new Card('A', 'H');
        assertTrue(card.toString().contains("Ace"));
        assertTrue(card.toString().contains("Hearts"));
    }
}

