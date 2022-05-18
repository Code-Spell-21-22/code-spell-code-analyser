package pt.ua.deti.codespell;

import com.fasterxml.jackson.databind.ObjectMapper;
import pt.ua.deti.codespell.utils.AnalysisStatus;
import pt.ua.deti.codespell.utils.CodeAnalysisResult;
import pt.ua.deti.codespell.utils.CodeExecution;
import pt.ua.deti.codespell.utils.Level;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.UUID;

public class Main {

    private static PrintWriter errorWriter;
    private static PrintWriter outputWriter;

    public static void main(String[] args) throws IOException {

        File errorFile = new File(File.separator + "errors.txt");
        File outputFile = new File(File.separator + "output.txt");

        errorWriter = new PrintWriter(errorFile, StandardCharsets.UTF_8);
        outputWriter = new PrintWriter(outputFile, StandardCharsets.UTF_8);

        CodeExecution currentCodeExecution = getCurrentCodeExecution();

        if (currentCodeExecution == null) {
            writeAnalysisResults(new CodeAnalysisResult.Builder().withAnalysisStatus(AnalysisStatus.PRE_CHECK_ERROR).build());
            exit(1);
            return;
        }

        try {
            CodeAnalysisResult codeAnalysisResult = remoteCodeAnalyser();
            writeAnalysisResults(codeAnalysisResult);
            exit(codeAnalysisResult.getAnalysisStatus() == AnalysisStatus.SUCCESS ? 0 : 1);
        } catch (Exception e) {
            System.out.println("Exception thrown while compiling remote code: " + e.getMessage());
            writeAnalysisResults(new CodeAnalysisResult.Builder().withAnalysisStatus(AnalysisStatus.COMPILATION_ERROR).build());
            exit(1);
        }

        exit(2);

    }

    /**
     * Remote Code Analyser method
     *
     * This method will have the responsibility of analysing the remote code + support classes.
     */
    private static CodeAnalysisResult remoteCodeAnalyser() {

        CodeExecution currentCodeExecution = getCurrentCodeExecution();

        if (currentCodeExecution == null) {
            return new CodeAnalysisResult.Builder().withAnalysisStatus(AnalysisStatus.PRE_CHECK_ERROR).build();
        }

        Level currentCodeExecutionLevel = currentCodeExecution.getLevel();

        File fileToCompile = new File(String.format("/code-spell-code-executor/src/main/java/pt/ua/deti/codespell/chapters/chapter_%d/Level_%d.java", currentCodeExecutionLevel.getChapterNumber(), currentCodeExecutionLevel.getLevelNumber()));

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> ds = new DiagnosticCollector<>();
        StandardJavaFileManager mgr = compiler.getStandardFileManager( ds, null, null );

        Iterable<? extends JavaFileObject> sources = mgr.getJavaFileObjectsFromFiles(Collections.singletonList(fileToCompile));
        JavaCompiler.CompilationTask task = compiler.getTask( null, mgr, ds, null, null, sources );
        task.call();

        if (ds.getDiagnostics().isEmpty()) {
            outputWriter.println("No errors found!");
            return new CodeAnalysisResult.Builder().withAnalysisStatus(AnalysisStatus.SUCCESS).build();
        }

        for (Diagnostic < ? extends JavaFileObject > d : ds.getDiagnostics()) {
            errorWriter.printf("Error on Line %d in %s\n", d.getLineNumber(), d.getSource().getName());
            errorWriter.printf("Caused by: %s\n",  d.getMessage(null));
        }

        return new CodeAnalysisResult.Builder().withAnalysisStatus(AnalysisStatus.COMPILATION_ERROR).build();

    }

    private static CodeExecution getCurrentCodeExecution() {

        String codeUniqueIdVar = System.getenv("CODE_ID");
        String chapterNumberVar = System.getenv("CHAPTER_NUMBER");
        String levelNumberVar = System.getenv("LEVEL_NUMBER");

        if (!chapterNumberVar.matches("\\d") || !levelNumberVar.matches("\\d")) return null;

        int chapterNumber = Integer.parseInt(chapterNumberVar);
        int levelNumber = Integer.parseInt(levelNumberVar);

        Level codeLevel = new Level(chapterNumber, levelNumber);
        return new CodeExecution(UUID.fromString(codeUniqueIdVar), codeLevel);

    }

    private static void writeAnalysisResults(CodeAnalysisResult codeAnalysisResult) throws IOException {

        File analysisFile = new File(File.separator + "analysis.txt");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(analysisFile, codeAnalysisResult);

    }

    private static void exit(int exitCode) {
        errorWriter.close();
        outputWriter.close();
        System.exit(exitCode);
    }

}
