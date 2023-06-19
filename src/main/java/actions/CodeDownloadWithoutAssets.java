package actions;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.ide.util.PropertiesComponent;


public class CodeDownloadWithoutAssets extends AnAction {
    private static final String PROJECT_ID_PROPERTY = "flutterflow.projectId";
    private static final String DESTINATION_PATH_PROPERTY = "flutterflow.destinationPath";
    private static final String API_TOKEN_PROPERTY = "flutterflow.apiToken";

    public CodeDownloadWithoutAssets() {
        super("Code Download Without Assets");
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        String projectID = PropertiesComponent.getInstance().getValue(PROJECT_ID_PROPERTY);
        String destinationPath = PropertiesComponent.getInstance().getValue(DESTINATION_PATH_PROPERTY);
        String apiToken = PropertiesComponent.getInstance().getValue(API_TOKEN_PROPERTY);

        // Check if all the required properties are available
        if (projectID == null || projectID.trim().isEmpty() ||
                destinationPath == null || destinationPath.trim().isEmpty() ||
                apiToken == null || apiToken.trim().isEmpty()) {
            Messages.showErrorDialog(project, "FlutterFlow configuration is missing. Please set the configuration in the FlutterFlow Configuration dialog.", "Missing Configuration");
            return;
        }

        // Construct the command
        String command = "dart pub global run flutterflow_cli export-code --project " + projectID + " --dest " + destinationPath + " --no-include-assets --token " + apiToken;

        // Show the toast message
        Messages.showInfoMessage("Starting code download", "Code Download");

        // Execute the command
        boolean success = executeCommand(command);

        if (success) {
            String message = "Code Download is completed. To view the downloaded code, please open the folder manually in Android Studio.";
            Messages.showInfoMessage(message, "Code Download");
        } else {
            // Show the failure message
            Messages.showErrorDialog("Code Download failed. Please try again.", "Code Download");
        }
    }

    private boolean executeCommand(String command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
            Process process = processBuilder.start();

            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
