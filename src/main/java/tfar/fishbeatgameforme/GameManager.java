package tfar.fishbeatgameforme;

import java.util.UUID;

public class GameManager {

    public static boolean running;

    public static UUID player1;

    public static UUID player2;

    public static void clear() {
        player1 = player2 = null;
        running = false;
    }
}
