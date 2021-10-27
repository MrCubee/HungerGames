package fr.mrcubee.hungergames;

public class HungerGamesAPI {

    private static Game game;

    protected static void setGame(Game game) {
        HungerGamesAPI.game = game;
    }

    public static Game getGame() {
        return game;
    }

}
