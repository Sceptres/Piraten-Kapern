package pk.game.player;

import pk.game.count.Counter;
import pk.game.dice.Dice;
import pk.game.score.ScoreCard;
import pk.game.strategy.player.PlayerStrategy;
import pk.game.strategy.player.strategies.RandomStrategy;

public class Player {

    private final PlayerStrategy strategy;
    private final ScoreCard scoreCard;
    private final ScoreCard turnScoreCard;
    private final Dice dice;
    private final Counter wins;
    private final Counter turnsPlayed;
    private final Counter rollsPlayed;
    private final Counter skullsRolled;
    private boolean isTurnOver;

    public Player() {
        this.strategy = RandomStrategy.getInstance();
        this.scoreCard = new ScoreCard();
        this.turnScoreCard = new ScoreCard();
        this.dice = new Dice();
        this.wins = new Counter();
        this.turnsPlayed = new Counter();
        this.rollsPlayed = new Counter();
        this.skullsRolled = new Counter();
        this.isTurnOver = false;
    }

    /**
     *
     * @return The scorecard that belongs to this user
     */
    public ScoreCard getScoreCard() {
        return this.scoreCard;
    }

    /**
     *
     * @return The scorecard used to keep track of the score during the current turn
     */
    public ScoreCard getTurnScoreCard() {
        return this.turnScoreCard;
    }

    /**
     *
     * @return The dice that belongs to this player
     */
    public Dice getDice() {
        return this.dice;
    }

    /**
     *
     * @return The counter keeping track of the number of wins this player has
     */
    public Counter getWins() {
        return this.wins;
    }

    /**
     *
     * @return The counter keeping track of the number of turns this player has played
     */
    public Counter getTurnsPlayed() {
        return this.turnsPlayed;
    }

    /**
     *
     * @return The number of rolls used by the player in this turn
     */
    public Counter getRollsPlayed() {
        return this.rollsPlayed;
    }

    /**
     *
     * @return The counter used to keep track of the number of skulls rolled during current turn
     */
    public Counter getSkullsRolled() {
        return this.skullsRolled;
    }

    /**
     *
     * @return Is the players turn over?
     */
    public boolean isTurnOver() {
        return this.isTurnOver;
    }

    /**
     *
     * @param turnOver The new value indicating whether the players turn is over or not
     */
    public void setTurnOver(boolean turnOver) {
        this.isTurnOver = turnOver;
    }

    /**
     * Reset the player so they can play their next turn
     */
    private void resetTurn() {
        this.getTurnScoreCard().clear();
        this.getSkullsRolled().reset();
        this.getRollsPlayed().reset();
        this.setTurnOver(false);
    }

    /**
     * Reset the player so they can play a new game
     */
    public void resetGame() {
        this.resetTurn();
        this.getScoreCard().clear();
    }

    /**
     * Let this player play their turn
     */
    public void play() {
        do {
            this.strategy.use(this);
        } while (!this.isTurnOver());

        if(this.getSkullsRolled().getCount() < 3) { // Did the player not roll 3 or more skulls?
            // Count score collected in this turn
            this.getScoreCard().merge(this.getTurnScoreCard());
        }

        // Count this turn to the players turns
        this.getTurnsPlayed().add(1);

        // Reset the player so they can play their next turn
        this.resetTurn();
    }
}
