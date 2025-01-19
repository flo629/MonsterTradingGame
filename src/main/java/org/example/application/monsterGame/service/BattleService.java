package org.example.application.monsterGame.service;

import org.example.application.monsterGame.entity.Card;
import org.example.application.monsterGame.entity.Deck;
import org.example.application.monsterGame.entity.User;
import org.example.application.monsterGame.repository.BattleRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BattleService {
    private final BattleRepository battleRepository;

    // Aktive Battles (Key: Spieler 1, Value: Spieler 2)
    private static final Set<String> activeBattles = ConcurrentHashMap.newKeySet();

    public BattleService(BattleRepository battleRepository) {
        this.battleRepository = battleRepository;
    }

    public String enqueueForBattle(String username) {
        User user = battleRepository.getUserByName(username);
        if (user == null) throw new IllegalArgumentException("User not found");

        synchronized (activeBattles) {
            if (activeBattles.contains(user.getId())) {
                return "User is already in a battle.";
            }
        }

        boolean matched = battleRepository.enqueueUser(user);
        if (matched) {
            User opponent = battleRepository.getOpponent(user);
            if (opponent != null) {
                // Prüfen, ob die Battle bereits gestartet wurde
                String battleKey = generateBattleKey(user.getId(), opponent.getId());
                synchronized (activeBattles) {
                    if (activeBattles.contains(battleKey)) {
                        return "Battle is already in progress.";
                    }
                    activeBattles.add(battleKey);
                }

                // Battle ausführen
                try {
                    return playBattle(user, opponent);
                } finally {
                    synchronized (activeBattles) {
                        activeBattles.remove(battleKey);
                    }
                }
            }
        }

        boolean timedOut = battleRepository.waitForOpponent(user, 20); // Timeout: 20 Sekunden
        if (timedOut) {
            battleRepository.dequeueUser(user);
            return "Queue timed out. No opponent found.";
        }

        return "Waiting for an opponent...";
    }

    private String playBattle(User user1, User user2) {
        Deck deck1 = battleRepository.getDeckByUserId(user1.getId());
        Deck deck2 = battleRepository.getDeckByUserId(user2.getId());

        List<Card> user1Cards = new ArrayList<>(deck1.getCards());
        List<Card> user2Cards = new ArrayList<>(deck2.getCards());

        int rounds = 0;
        StringBuilder battleLog = new StringBuilder();

        while (!user1Cards.isEmpty() && !user2Cards.isEmpty() && rounds < 100) {
            rounds++;
            battleLog.append(playRound(user1, user2, user1Cards, user2Cards)).append("\n");
        }

        if (rounds >= 100) {

            battleLog.append("The battle ends in a draw after 100 rounds.");
            return battleLog.toString();
        }

        String winner = determineWinner(user1, user2, user1Cards, user2Cards);
        battleLog.append(winner);

        battleRepository.dequeueUser(user1);
        battleRepository.dequeueUser(user2);
        return battleLog.toString();
    }

    private String playRound(User user1, User user2, List<Card> user1Cards, List<Card> user2Cards) {
        Card card1 = getRandomCard(user1Cards);
        Card card2 = getRandomCard(user2Cards);

        // Prüfe, ob ein Spieler mehr als 50 Münzen hat
        if (battleRepository.payToWin(user1)) {
            user2Cards.remove(card2);
            return user1.getUsername() + " wins the round with " + card1.getName() + " using a coin booster!";
        }

        if (battleRepository.payToWin(user2)) {
            user1Cards.remove(card1);
            return user2.getUsername() + " wins the round with " + card2.getName() + " using a coin booster!";
        }

        double damage1 = calculateDamage(card1, card2);
        double damage2 = calculateDamage(card2, card1);

        String result;
        if (damage1 > damage2) {
            user2Cards.remove(card2);
            result = user1.getUsername() + " wins the round with " + card1.getName();
        } else if (damage2 > damage1) {
            user1Cards.remove(card1);
            result = user2.getUsername() + " wins the round with " + card2.getName();
        } else {
            result = "Round ends in a draw between " + card1.getName() + " and " + card2.getName();
        }

        battleRepository.saveRound(user1, user2, card1, card2, damage1, damage2);
        return result;
    }

    private String determineWinner(User user1, User user2, List<Card> user1Cards, List<Card> user2Cards) {
        if (user1Cards.isEmpty() && user2Cards.isEmpty()) {
            return "The battle ends in a draw.";
        }

        User winner;
        User loser;

        if (user1Cards.isEmpty()) {
            winner = user2;
            loser = user1;
        } else {
            winner = user1;
            loser = user2;
        }

        battleRepository.updateStats(winner.getId(), true);
        battleRepository.updateStats(loser.getId(), false);
        battleRepository.updateElo(winner, loser);

        return winner.getUsername() + " wins the battle!";
    }

    private Card getRandomCard(List<Card> deck) {
        Random random = new Random();
        return deck.get(random.nextInt(deck.size()));
    }


    private double calculateDamage(Card attacker, Card defender) {
        if (attacker.getName() == Card.CardName.WaterGoblin && defender.getName() == Card.CardName.Ork) return 0;
        if (attacker.getName() == Card.CardName.FireGoblin && defender.getName() == Card.CardName.Ork) return 0;
        if (attacker.getName() == Card.CardName.RegularGoblin && defender.getName() == Card.CardName.Ork) return 0;
        if (attacker.getName() == Card.CardName.Wizzard && defender.getName() == Card.CardName.Ork) return 0;
        if (attacker.getName() == Card.CardName.WaterSpell && defender.getName() == Card.CardName.Knight) return Double.MAX_VALUE;
        if (attacker.getName() == Card.CardName.Kraken && defender.getType() == Card.CardType.spell) return 0;
        if (attacker.getName() == Card.CardName.FireElf && defender.getName() == Card.CardName.Dragon) return 0;

        if (attacker.getType() == Card.CardType.spell) {
            if (attacker.getElement() == Card.Element.water && defender.getElement() == Card.Element.fire) return attacker.getDamage() * 2;
            if (attacker.getElement() == Card.Element.fire && defender.getElement() == Card.Element.water) return attacker.getDamage() / 2.0;
            if (attacker.getElement() == Card.Element.regular && defender.getElement() == Card.Element.water) return attacker.getDamage() * 2;
        }

        return attacker.getDamage();
    }

    private String generateBattleKey(String user1Id, String user2Id) {
        List<String> users = Arrays.asList(user1Id, user2Id);
        Collections.sort(users); // Sortiere, um konsistente Schlüssel zu erstellen
        return String.join("-", users);
    }
}
