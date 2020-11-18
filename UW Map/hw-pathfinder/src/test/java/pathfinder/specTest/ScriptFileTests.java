/*
 * Copyright ©2019 Hal Perkins.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Summer Quarter 2019 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package pathfinder.specTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * DO NOT MODIFY
 *
 * <p>This class, along with a complete PathfinderTestDriver implementation, can be used to test your graph
 * and min-cost-path MarvelPaths application using the script file format described in the
 * assignment writeup. It is assumed that the files are located in the same directory as this class.
 *
 * <p>It works by parameterizing test methods over some data values, and then creating an instance
 * for the cross-product of test methods and data values. In this case, it will create one
 * ScriptFileTests instance per .expected file, and for each of those it will run the
 * checkAgainstExpectedOutput() test. See the JUnit4 Javadoc for more information.
 */
@RunWith(Parameterized.class)
public class ScriptFileTests {

    // static fields and methods used during setup of the parameterized runner
    private static FileFilter testFileFilter = file -> file.getName().endsWith(".test");

    /**
     * @return list of argument arrays that should be provided to the two ScriptFileTests public
     * variables by the Parameterized test runner. The first is the file to be tested itself, the
     * second is a name used as a convenience for readable test results, (see argument to the
     * {@code @Parameters} annotation).
     */
    @Parameters(name = "{1}")
    public static List<Object[]> getTestFiles() {
        List<Object[]> filesToTest = new ArrayList<>();
        //
        try {
            File baseDir = new File(ScriptFileTests.class.getResource("ScriptFileTests.class").toURI())
                    .getParentFile();
            File[] contents = baseDir.listFiles(ScriptFileTests.testFileFilter);
            if(contents == null) {
                throw new RuntimeException("Error accessing files in: " + baseDir.getPath());
            }
            for(File f : contents) {
                filesToTest.add(new Object[]{f, f.getName()});
            }
        } catch(URISyntaxException e) {
            throw new RuntimeException("Unable to process script directory URI", e);
        }
        //
        return filesToTest;
    }

    // ============================================================
    // ============================================================

    /**
     * Reads in the contents of a file.
     *
     * @return the contents of that file
     * @throws IOException              if an error occurs accessing the file
     * @throws IllegalArgumentException if f is null or represents a non-existent file
     */
    private String fileContents(File f) throws IOException {
        if(f == null) {
            throw new IllegalArgumentException("No file specified");
        }
        if(!f.exists()) {
            throw new IllegalArgumentException("File does not exist: " + f);
        }
        return Files.readString(f.toPath(), StandardCharsets.UTF_8);
    }

    /**
     * @param newSuffix The new file suffix to use
     * @return a File with the same name as testScriptFile, except that the test suffix is replaced by
     * the given suffix
     */
    private File fileWithSuffix(String newSuffix) {
        File parent = testScriptFile.getParentFile();
        String driverName = testScriptFile.getName();
        String baseName = driverName.substring(0, driverName.length() - "test".length());

        return new File(parent, baseName + newSuffix);
    }

    /**
     * @return the contents of the output file
     * @throws IOException if an error occurs creating outputs or accessing test inputs
     * @spec.requires there exists a test file indicated by testScriptFile
     * @spec.effects runs the test in filename, and output its results to a file in the same directory
     * with name filename+".actual"; if that file already exists, it will be overwritten.
     */
    private String runScriptFile() throws IOException {
        if(testScriptFile == null) {
            throw new RuntimeException("No file specified");
        }

        File actual = fileWithSuffix("actual");

        Reader r = new FileReader(testScriptFile);
        Writer w = new FileWriter(actual);

        PathfinderTestDriver td = new PathfinderTestDriver(r, w);
        td.runTests();

        return fileContents(actual);
    }

    // ============================================================
    // ============================================================

    @Parameterized.Parameter(0)
    public File testScriptFile;

    @Parameterized.Parameter(1)
    public String filename;

    /**
     * The only test that is run: run a script file and test its output.
     *
     * @throws IOException on a failure reading the test files
     */
    @Test(timeout = 30000)
    public void checkAgainstExpectedOutput() throws IOException {
        String expected = fileContents(fileWithSuffix("expected"));
        String actual = runScriptFile();
        // Perform some normalization to be more forgiving with whitespace:
        //  - Sequences of tabs and spaces are compressed to a single space character.
        //  - Sequences of more than one newline are compressed to a single newline.
        //  - Whitespace characters are removed from the beginning and end of the strings.
        String normalizedExpected = expected.replaceAll("[ \\t]+", " ")
                                            .replaceAll("\\n+", "\n")
                                            .trim();
        String normalizedActual = actual.replaceAll("[ \\t]+", " ")
                                        .replaceAll("\\n+", "\n")
                                        .trim();
        assertEquals(filename, normalizedExpected, normalizedActual);
    }
}
