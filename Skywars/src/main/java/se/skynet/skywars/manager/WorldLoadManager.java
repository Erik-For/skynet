package se.skynet.skywars.manager;

import org.bukkit.generator.ChunkGenerator;
import se.skynet.skywars.Game;

public class WorldLoadManager  {

    private final Game game;
    private int xl;
    private int yl;
    private int ym;
    private int zl;

    public WorldLoadManager(Game game) {
        this.game = game;
        this.xl = xl;
        this.yl = yl;
        this.ym = ym;
        this.zl = zl;
    }

    public void setBounds(int xl, int yl, int ym, int zl) {
        this.xl = xl;
        this.yl = yl;
        this.ym = ym;
        this.zl = zl;
    }

}
