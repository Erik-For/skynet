package se.skynet.skywars.timing;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import se.skynet.skywars.Skywars;

public class Timer {

    private int time;
    private final int seconds;
    private final RunSecond runSecond;
    private final RunSecond runEnd;

    public Timer(int seconds, RunSecond runSecond, RunSecond runEnd) {
        this.seconds = seconds;
        this.time = seconds;
        this.runSecond = runSecond;
        this.runEnd = runEnd;
    }


    public void start(Skywars plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                System.out.println(time);
                if(time == 0){
                    runEnd.run(time);
                    this.cancel();
                } else {
                    runSecond.run(time);
                }
                time -= 1;
            }
        }.runTaskTimer(plugin, 0, 20);
    }
}
