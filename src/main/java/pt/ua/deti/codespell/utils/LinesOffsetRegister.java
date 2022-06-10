package pt.ua.deti.codespell.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LinesOffsetRegister {

    LEVEL_1_1(new Level(1,1), 49);

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
