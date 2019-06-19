import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import io.qameta.allure.junit4.AllureJunit4;
import org.junit.runner.Result;

public class ProjectAllureListener extends AllureJunit4 {

    private final static String ALLURE_RESULT_DIRECTORY = "allure.results.directory";
    private final static String ENVIRONMENT_PROPERTIES = "environment.properties";

    @Override
    public void testRunFinished(Result result) throws Exception {
        generateEnvReport();
        super.testRunFinished(result);
    }

    private static void generateEnvReport() {
        try {
            final String dir = System.getProperty(ALLURE_RESULT_DIRECTORY);
            final String path = String.format("%s/%s", dir, ENVIRONMENT_PROPERTIES);
            final File environmentFile = new File(path);
            final FileWriter writer = new FileWriter(environmentFile);
            //write environment data here
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
