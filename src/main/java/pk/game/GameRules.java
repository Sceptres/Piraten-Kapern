package pk.game;

import pk.game.cards.*;
import pk.game.dice.Dice;
import pk.game.player.Player;
import pk.game.score.scorable.Cards;
import pk.game.score.scorable.Faces;
import pk.game.score.scorable.Groups;
import pk.game.score.scorable.Scorable;
import pk.game.score.scorecard.scorecards.TurnScoreCard;
import pk.logging.GameLogger;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class GameRules {

    public static final int MIN_DICE = 2; // Minimum number of dice a player can roll
    public static final int MAX_DICE = 8; // Maximum number of dice a player can roll
    public static final int WIN_SCORE = 6000; // The minimum score needed by a player to win
    public static final int MIN_PLAYERS = 2; // The minimum number of players that can play the game
    public static final int MAX_PLAYERS = 5; // The maximum number of players that can play the game
    public static final int MIN_GAMES = 1; // The minimum number of games to allow a simulation to simulate
    public static final int SKULL_LOSE_COUNT = 3; // The number of skulls that if collected makes a player lose their points
    public static final Map<Integer, Groups> GROUP_MAP = Map.of( // All the groups to score
            Groups.GROUP_OF_3.getGroupSize(), Groups.GROUP_OF_3,
            Groups.GROUP_OF_4.getGroupSize(), Groups.GROUP_OF_4,
            Groups.GROUP_OF_5.getGroupSize(), Groups.GROUP_OF_5,
            Groups.GROUP_OF_6.getGroupSize(), Groups.GROUP_OF_6,
            Groups.GROUP_OF_7.getGroupSize(), Groups.GROUP_OF_7,
            Groups.GROUP_OF_8.getGroupSize(), Groups.GROUP_OF_8
    );
    public static final Card[] GAME_CARDS = { // All the cards in the game
            new SeaBattleCard(Cards.SEA_BATTLE_300, 2),
            new SeaBattleCard(Cards.SEA_BATTLE_300, 2),
            new SeaBattleCard(Cards.SEA_BATTLE_500, 3),
            new SeaBattleCard(Cards.SEA_BATTLE_500, 3),
            new SeaBattleCard(Cards.SEA_BATTLE_1000, 4),
            new SeaBattleCard(Cards.SEA_BATTLE_1000, 4),
            new MonkeyBusinessCard(), new MonkeyBusinessCard(),
            new MonkeyBusinessCard(), new MonkeyBusinessCard(),
            new GoldCoinCard(), new GoldCoinCard(),
            new GoldCoinCard(), new GoldCoinCard(),
            new DiamondCard(), new DiamondCard(),
            new DiamondCard(), new DiamondCard(),
            new SkullCard(1), new SkullCard(1),
            new SkullCard(1), new SkullCard(2),
            new SkullCard(2), new NopCard(),
            new CaptainCard(), new CaptainCard(), new CaptainCard(),
            new CaptainCard(), new NopCard(), new NopCard(),
            new NopCard(), new NopCard(), new NopCard(),
            new NopCard(), new NopCard()
    };

    /**
     *
     * @param player The {@link Player} to check skulls count
     * @return A boolean determining whether the number of dice the {@link Player} rolled end their turn or not
     */
    public static boolean playerSkullsEndTurn(Player player) {
        return player.getTurnScoreCard().getScore(Faces.SKULL) >= GameRules.SKULL_LOSE_COUNT;
    }

    /**
     * Rolls a {@link Player} dice for their first roll in a turn
     * @param player The {@link Player} performing their first roll
     */
    public static void firstRoll(Player player) {
        GameRules.roll(player.getDiceHolder().diceStream(), player);
    }

    /**
     *
     * Ensures {@link Player} rolls at least {@link GameRules#MIN_DICE} {@link Dice}
     * @param dice The {@link Dice} to roll
     * @param player The {@link Player}
     */
    public static void roll(Stream<Dice> dice, Player player) {
        Dice[] diceArr = dice.toArray(Dice[]::new);
        if(diceArr.length < GameRules.MIN_DICE) { // Does the player have enough dice to roll
            GameLogger.debugLog(String.format("Player #%d wants to roll %d dice", player.getId(), diceArr.length));
            player.setTurnOver(true);
            return;
        }

        // Roll the dice
        Arrays.stream(diceArr).forEach(Dice::roll);
    }

    /**
     *
     * @param player The {@link Player} to check if they reached the winning score
     * @return Whether the {@link Player} has reached the winning score or not
     */
    public static boolean didPlayerReachWinScore(Player player) {
        return player.getScoreCard().totalScore() >= GameRules.WIN_SCORE;
    }

    /**
     *
     * @param scoreCard The {@link TurnScoreCard} that the player uses each turn
     * @return Whether the current scorecard has a bonus chest or not
     */
    public static boolean isBonusChestRoll(TurnScoreCard scoreCard) {
        Set<Map.Entry<Scorable, Integer>> scoreEntries = scoreCard.getScoreEntrySet();

        // Get counts
        int goldCount = scoreCard.getScore(Faces.GOLD);
        int diamondCount = scoreCard.getScore(Faces.DIAMOND);
        int scoreContribDice = scoreEntries.stream().filter(e -> e.getKey() instanceof Groups)
                                                    .mapToInt(e -> ((Groups) e.getKey()).getGroupSize())
                                                    .sum();
        scoreContribDice += goldCount + diamondCount;

        // Cancel out any gold and diamond combinations
        scoreContribDice += goldCount >= Groups.GROUP_OF_3.getGroupSize() ? -goldCount : 0;
        scoreContribDice += diamondCount >= Groups.GROUP_OF_3.getGroupSize() ? -diamondCount : 0;

        // Ignore gold coins and diamonds added by the cards
        scoreContribDice -= scoreCard.getFromReserve(Faces.GOLD);
        scoreContribDice -= scoreCard.getFromReserve(Faces.DIAMOND);


        return scoreContribDice == GameRules.MAX_DICE;
    }

}
