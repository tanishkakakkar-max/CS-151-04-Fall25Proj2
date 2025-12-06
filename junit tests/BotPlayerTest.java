import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import blackjack.BotPlayer;
import blackjack.Card;

public class BotPlayerTest {

    @Test
    public void testBotPlayerCreation() {
        BotPlayer bot = new BotPlayer("AI 1", 1000, 16);
        assertEquals("AI 1", bot.getName());
        assertEquals(1000, bot.getBalance());
    }

    @Test
    public void testShouldHitBelowThreshold() {
        BotPlayer bot = new BotPlayer("AI 1", 1000, 16);
        bot.addCard(new Card('2', 'H'));
        bot.addCard(new Card('3', 'H'));
        assertTrue(bot.shouldHit());
    }

    @Test
    public void testShouldHitAtThreshold() {
        BotPlayer bot = new BotPlayer("AI 1", 1000, 16);
        bot.addCard(new Card('8', 'H'));
        bot.addCard(new Card('8', 'D'));
        assertFalse(bot.shouldHit());
    }

    @Test
    public void testShouldHitAboveThreshold() {
        BotPlayer bot = new BotPlayer("AI 1", 1000, 16);
        bot.addCard(new Card('9', 'H'));
        bot.addCard(new Card('8', 'D'));
        assertFalse(bot.shouldHit());
    }

    @Test
    public void testShouldHitWhenBusted() {
        BotPlayer bot = new BotPlayer("AI 1", 1000, 16);
        bot.addCard(new Card('K', 'H'));
        bot.addCard(new Card('K', 'D'));
        bot.addCard(new Card('K', 'C'));
        assertTrue(bot.isBusted());
        assertFalse(bot.shouldHit());
    }

    @Test
    public void testShouldHitWhenStanding() {
        BotPlayer bot = new BotPlayer("AI 1", 1000, 16);
        bot.addCard(new Card('2', 'H'));
        bot.stand();
        assertFalse(bot.shouldHit());
    }

    @Test
    public void testDifferentThresholds() {
        BotPlayer bot1 = new BotPlayer("AI 1", 1000, 16);
        BotPlayer bot2 = new BotPlayer("AI 2", 1000, 18);
        
        bot1.addCard(new Card('9', 'H'));
        bot1.addCard(new Card('8', 'D'));
        
        bot2.addCard(new Card('9', 'H'));
        bot2.addCard(new Card('8', 'D'));
        
        assertFalse(bot1.shouldHit());
        assertTrue(bot2.shouldHit());
    }

    @Test
    public void testAceHandling() {
        BotPlayer bot = new BotPlayer("AI 1", 1000, 16);
        bot.addCard(new Card('A', 'H'));
        bot.addCard(new Card('5', 'H'));
        assertFalse(bot.shouldHit());
        
        bot.addCard(new Card('K', 'H'));
        assertFalse(bot.shouldHit());
    }
}

