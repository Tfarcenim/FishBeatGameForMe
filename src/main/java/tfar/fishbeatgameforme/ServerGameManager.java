package tfar.fishbeatgameforme;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServerGameManager extends AbstractGameManager {

    public static void clear() {
        player1 = player2 = Util.NIL_UUID;
        running = false;
    }

    public static long fishTimestamp;

    public static List<BlockPos> fishLocations = new ArrayList<>();

}
