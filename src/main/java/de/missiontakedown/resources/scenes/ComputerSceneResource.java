package de.missiontakedown.resources.scenes;

import de.missiontakedown.resources.GameAudioResource;
import de.missiontakedown.resources.GameResource;

public class ComputerSceneResource {

    public static GameResource DESKTOP = new GameResource("/scenes/computer/Desktop.png");
    public static GameResource WIKIPEDIA_NITROGLYCERIN = new GameResource("/scenes/computer/WikipediaNitroglycerin.png");
    public static GameResource WIKIPEDIA_NITROGLYCERIN_SALPETER_MARKED = new GameResource("/scenes/computer/WikipediaNitroglycerinSalpeterMarked.png");

    public static GameResource WIKIPEDIA_SALPETER = new GameResource("/scenes/computer/WikipediaSalpetersäure.png");
    public static GameResource WIKIPEDIA_SALPETER_AMMONIAK_MARKED = new GameResource("/scenes/computer/WikipediaSalpetersäureAmmoniakMarked.png");

    public static GameResource WIKIPEDIA_AMMONIAK = new GameResource("/scenes/computer/WikipediaAmmoniak.png");

    public static GameResource AMAZON_DESKTOP = new GameResource("/scenes/computer/AmazonDesktop.png");

    public static GameResource AMAZON = new GameResource("/scenes/computer/Amazon.png", 0.2f);
    public static GameResource WIKIPEDIA = new GameResource("/scenes/computer/Wikipedia.png", 0.2f);

    public static GameAudioResource AMAZON_BUY = new GameAudioResource("/scenes/computer/AmazonBuyComplete.wav");

    public static GameAudioResource COMPUTER_CLICk = new GameAudioResource("/scenes/computer/MouseClick.wav");

}
