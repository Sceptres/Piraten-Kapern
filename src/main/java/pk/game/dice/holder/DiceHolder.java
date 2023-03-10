package pk.game.dice.holder;

import pk.game.GameRules;
import pk.game.dice.Dice;
import pk.game.score.scorable.Faces;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DiceHolder {

    // Class fields
    private final Dice[] dice;

    public DiceHolder() {
        this.dice = new Dice[GameRules.MAX_DICE];
        for(int i=0; i < this.getDice().length; i++) {
            this.getDice()[i] = new Dice();
        }
    }


    /**
     *
     * @return The array of all {@link Dice}s
     */
    public Dice[] getDice() {
        return this.dice;
    }

    /**
     *
     * @return The {@link Dice} that can be rolled
     */
    public Stream<Dice> getRollableDice() {
        return this.diceStream().filter(d -> !Faces.SKULL.equals(d.getFace()));
    }

    /**
     *
     * @param predicate The {@link Predicate} that will be used to find the requested dice
     * @return The {@link Dice} that match the given conditions of the {@link Predicate}
     */
    public Stream<Dice> getRollableDice(Predicate<? super Dice> predicate) {
        return this.getRollableDice().filter(predicate);
    }

    /**
     *
     * @return The {@link Stream} of {@link Dice}s
     */
    public Stream<Dice> diceStream() {
        return Arrays.stream(this.getDice());
    }


    /**
     *
     * @return The {@link Faces} of the dice
     */
    public Stream<Faces> getFaces() {
        return this.diceStream().map(Dice::getFace);
    }

    /**
     *
     * @return Get the {@link Faces} of the {@link Dice}s as a Map where the key is the {@link Faces} and the value
     * is the number of this {@link Faces} found
     */
    public Map<Faces, Integer> getFacesMap() {
        return this.getFaces().collect(Collectors.groupingBy(
                Function.identity(),
                Collectors.summingInt(e -> 1)
        ));
    }

    /**
     *
     * @return The number of {@link Dice} that have rolled {@link Faces#SKULL}
     */
    public int getSkullCount() {
        return (int) this.getFaces().filter(Faces.SKULL::equals).count();
    }

    /**
     * Reset all the {@link Dice}
     */
    public void reset() {
        this.diceStream().forEach(Dice::reset);
    }

    @Override
    public String toString() {
        return Arrays.toString(this.getDice());
    }
}
