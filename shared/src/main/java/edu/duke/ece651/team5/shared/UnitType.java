package edu.duke.ece651.team5.shared;
import java.io.Serial;
import java.io.Serializable;

/**
 * Collection of all the possible unit type
 */
public class UnitType implements Serializable{
    public static final Unit SOLDIER = new Soldier();
    @Serial
    private static final long serialVersionUID = 2970162075841132211L;
}
