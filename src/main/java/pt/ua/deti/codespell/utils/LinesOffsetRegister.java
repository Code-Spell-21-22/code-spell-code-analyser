package pt.ua.deti.codespell.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LinesOffsetRegister {

    LEVEL_1_1(new Level(1,1), 50),
    LEVEL_2_3(new Level(2,3), 50);

    private final Level level;
    private final int linesOffset;

    public static int getOffsetByLevel(Level level) {

        for (LinesOffsetRegister linesOffsetRegister : LinesOffsetRegister.values()) {
            if (linesOffsetRegister.getLevel().equals(level)) {
                return linesOffsetRegister.linesOffset;
            }
        }

        return 0;

    }

}
