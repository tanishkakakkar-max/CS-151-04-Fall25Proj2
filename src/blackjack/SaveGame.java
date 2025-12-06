package blackjack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import utils.EncryptionUtils;

public class SaveGame {

    public static String createSaveState(Blackjack game) {
        StringBuilder sb = new StringBuilder();

        int turnIndex = game.getCurrentTurnIndex();
        sb.append(turnIndex).append("|");

        // balances
        sb.append(game.getPlayer().getBalance()).append(",")
                .append(game.getBot1().getBalance()).append(",")
                .append(game.getBot2().getBalance()).append(",")
                .append(game.getDealer().getBalance()).append("|");

        // bets
        sb.append(game.getPlayer().getCurrentBet()).append(",")
                .append(game.getBot1().getCurrentBet()).append(",")
                .append(game.getBot2().getCurrentBet()).append("|");

        // hands
        sb.append(cardsToSegment(game.getPlayer().getHand())).append(";")
                .append(cardsToSegment(game.getBot1().getHand())).append(";")
                .append(cardsToSegment(game.getBot2().getHand())).append(";")
                .append(cardsToSegment(game.getDealer().getHand()))
                .append("|");

        // deck
        List<String> deckCodes = game.getDeck().getRemainingCards()
                .stream()
                .map(Card::toCode)
                .collect(Collectors.toList());
        sb.append(String.join(",", deckCodes));

        // Encrypt the entire save state string
        String plainSaveState = sb.toString();
        return EncryptionUtils.encrypt(plainSaveState);
    }

    private static String cardsToSegment(List<Card> cards) {
        if (cards.isEmpty()) {
            return "-";
        }
        return cards.stream()
                .map(Card::toCode)
                .collect(Collectors.joining(","));
    }

    private static List<Card> segmentToCards(String segment) {
        List<Card> result = new ArrayList<>();
        if (segment == null || segment.equals("-") || segment.isEmpty()) {
            return result;
        }
        String[] codes = segment.split(",");
        for (String c : codes) {
            result.add(Card.fromCode(c));
        }
        return result;
    }

    public static void loadFromSaveState(Blackjack game, String saveStateInput) {
        try {
            // Try to decrypt first (new encrypted format)
            String saveState = EncryptionUtils.decrypt(saveStateInput);
            
            // If decryption failed or returned empty, try as plain text (backward compatibility)
            if (saveState == null || saveState.isEmpty()) {
                saveState = saveStateInput;
            }
            
            String[] parts = saveState.split("\\|");
            if (parts.length != 5) {
                throw new IllegalArgumentException("Invalid save state format");
            }

            int turnIndex = Integer.parseInt(parts[0]);
            game.setCurrentTurnIndex(turnIndex);

            // balances
            String[] balanceParts = parts[1].split(",");
            int pBal = Integer.parseInt(balanceParts[0]);
            int ai1Bal = Integer.parseInt(balanceParts[1]);
            int ai2Bal = Integer.parseInt(balanceParts[2]);
            int dealerBal = Integer.parseInt(balanceParts[3]);

            game.getPlayer().balance = pBal;
            game.getBot1().balance = ai1Bal;
            game.getBot2().balance = ai2Bal;
            game.getDealer().balance = dealerBal;

            // bets
            String[] betParts = parts[2].split(",");
            game.getPlayer().currentBet = Integer.parseInt(betParts[0]);
            game.getBot1().currentBet = Integer.parseInt(betParts[1]);
            game.getBot2().currentBet = Integer.parseInt(betParts[2]);

            // hands
            String[] handParts = parts[3].split(";");
            if (handParts.length != 4) {
                throw new IllegalArgumentException("Invalid hand section");
            }

            game.getPlayer().resetForNewRound();
            game.getBot1().resetForNewRound();
            game.getBot2().resetForNewRound();
            game.getDealer().resetForNewRound();

            List<Card> playerCards = segmentToCards(handParts[0]);
            List<Card> ai1Cards = segmentToCards(handParts[1]);
            List<Card> ai2Cards = segmentToCards(handParts[2]);
            List<Card> dealerCards = segmentToCards(handParts[3]);

            game.getPlayer().getHand().addAll(playerCards);
            game.getBot1().getHand().addAll(ai1Cards);
            game.getBot2().getHand().addAll(ai2Cards);
            game.getDealer().getHand().addAll(dealerCards);

            // deck
            List<String> deckCodes = new ArrayList<>();
            if (!parts[4].isBlank()) {
                deckCodes.addAll(Arrays.asList(parts[4].split(",")));
            }
            game.getDeck().setFromCardCodes(deckCodes);

            // recompute busted flags
            game.getPlayer().busted = game.getPlayer().getHandValue() > 21;
            game.getBot1().busted = game.getBot1().getHandValue() > 21;
            game.getBot2().busted = game.getBot2().getHandValue() > 21;
            game.getDealer().busted = game.getDealer().getHandValue() > 21;

            game.setRoundActive(true);

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to load save state: " + e.getMessage(), e);
        }
    }
}