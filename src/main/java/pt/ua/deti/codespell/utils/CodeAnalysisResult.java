package pt.ua.deti.codespell.utils;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CodeAnalysisResult {

    private final UUID codeUniqueId;
    private final AnalysisStatus analysisStatus;
    private final long time;

    public static class Builder {
        private UUID codeUniqueId = UUID.fromString("00000000-0000-0000-0000-000000000000");
        private AnalysisStatus analysisStatus = AnalysisStatus.NONE;
        private long time = 0;

        public Builder withCodeUniqueId(UUID codeUniqueId) {
            this.codeUniqueId = codeUniqueId;
            return this;
        }

        public Builder withAnalysisStatus(AnalysisStatus analysisStatus) {
            this.analysisStatus = analysisStatus;
            return this;
        }

        public Builder withTime(long time) {
            this.time = time;
            return this;
        }

        public CodeAnalysisResult build() {
            return new CodeAnalysisResult(this);
        }

    }

    protected CodeAnalysisResult(Builder builder) {
        this.codeUniqueId = builder.codeUniqueId;
        this.analysisStatus = builder.analysisStatus;
        this.time = builder.time;
    }

}
