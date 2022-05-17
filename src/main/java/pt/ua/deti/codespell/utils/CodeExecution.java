package pt.ua.deti.codespell.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class CodeExecution {

    private final UUID codeUniqueId;
    private final Level level;

}
