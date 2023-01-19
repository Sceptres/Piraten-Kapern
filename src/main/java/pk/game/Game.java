package pk.game;

import pk.game.player.Player;
import pk.game.score.scorecard.scorecards.GameScoreCard;

public class Game {

    // Players of the game
    private final Player p1;
    private final Player p2;

    public Game() {
        this.p1 = new Player();
        this.p2 = new Player();
    }

    /**
     *
     * @return The first {@link Player} in this game
     */
    public Player getPlayer1() {
        return this.p1;
    }

    /**
     *
     * @return The second {@link Player} in this game
     */
    public Player getPlayer2() {
        return this.p2;
    }

    /**
     * Reset the game so that it can be replayed by the {@link Player}s
     */
    public void reset() {
        this.getPlayer1().resetGame();
        this.getPlayer2().resetGame();
    }

    /**
     * Play the games
     */
    public void play() {
        do {
            // Players play their turns
            this.getPlayer1().play();
            this.getPlayer2().play();

            // Get the scores of the players
            int p1Score = this.p1.getScoreCard().totalScore();
            int p2Score = this.p2.getScoreCard().totalScore();

            // Find who won
            if(p1Score >= GameScoreCard.WIN_SCORE || p2Score >= GameScoreCard.WIN_SCORE) {
                // Find player who gets extra turn
                if(p1Score >= GameScoreCard.WIN_SCORE) {
                    this.getPlayer2().play();
                } else {
                    this.getPlayer1().play();
                }

                if(p1Score > p2Score) { // Did player p1 win?
                    this.getPlayer1().getWins().add(1);
                } else if(p1Score < p2Score) { // Did player p2 win?
                    this.getPlayer2().getWins().add(1);
                }

                // Otherwise, the score of both players are equal it is a draw. Game ends as a tie.

                break;
            }
        } while(true);
    }

}
