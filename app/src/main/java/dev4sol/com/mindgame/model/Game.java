package dev4sol.com.mindgame.model;

/**
 * Created by Cclub on 27/06/2018.
 */
import dev4sol.com.mindgame.themes.Theme;

/**
 * This is instance of active playing game
 *
 * @author sromku
 */
public class Game {

    /**
     * The board configuration
     */
    public BoardConfiguration boardConfiguration;

    /**
     * The board arrangment
     */
    public BoardArrangment boardArrangment;

    /**
     * The selected theme
     */
    public Theme theme;

    public GameState gameState;

}
