package tfar.fishbeatgameforme;

import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameManager {

    public static boolean running;

    public static UUID player1;

    public static UUID player2;

    public static void clear() {
        player1 = player2 = null;
        running = false;
    }

    public static long fishTimestamp;

    public static List<BlockPos> fishLocations = new ArrayList<>();

}
