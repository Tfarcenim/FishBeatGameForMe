package tfar.fishbeatgameforme.client;

import tfar.fishbeatgameforme.AbstractGameManager;

import java.util.UUID;

public class ClientGameManager extends AbstractGameManager {

    public static void receive(UUID player1, UUID player2, boolean active) {
        ClientGameManager.player1 = player1;
        ClientGameManager.player2 = player2;
        ClientGameManager.running = active;
    }
}
