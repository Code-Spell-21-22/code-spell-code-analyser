package pt.ua.deti.codespell.utils;

public class Level {

    private final int chapterNumber;
    private final int levelNumber;

    public Level(int chapterNumber, int levelNumber) {
        this.chapterNumber = chapterNumber;
        this.levelNumber = levelNumber;
    }

    public int getChapterNumber() {
        return chapterNumber;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

}
