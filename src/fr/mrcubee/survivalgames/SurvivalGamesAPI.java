package fr.mrcubee.survivalgames;

public class SurvivalGamesAPI {

    private static Game game;

    protected static void setGame(Game game) {
        SurvivalGamesAPI.game = game;
    }

    public static Game getGame() {
        return game;
    }

}
