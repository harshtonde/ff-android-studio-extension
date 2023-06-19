package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import com.intellij.ide.util.PropertiesComponent;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;

public class FlutterFlowConfig extends AnAction {
    private static final String PROJECT_ID_PROPERTY = "flutterflow.projectId";
    private static final String DESTINATION_PATH_PROPERTY = "flutterflow.destinationPath";
    private static final String API_TOKEN_PROPERTY = "flutterflow.apiToken";

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        String savedProjectID = PropertiesComponent.getInstance().getValue(PROJECT_ID_PROPERTY);
        String savedDestinationPath = PropertiesComponent.getInstance().getValue(DESTINATION_PATH_PROPERTY);
        String savedApiToken = PropertiesComponent.getInstance().getValue(API_TOKEN_PROPERTY);

        MyDialog dialog = new MyDialog(project, savedProjectID, savedDestinationPath, savedApiToken);
        dialog.show();
        if (dialog.isOK()) {
            String projectID = dialog.getProjectID();
            String destinationPath = dialog.getDestinationPath();
            String apiToken = dialog.getApiToken();

            PropertiesComponent.getInstance().setValue(PROJECT_ID_PROPERTY, projectID);
            PropertiesComponent.getInstance().setValue(DESTINATION_PATH_PROPERTY, destinationPath);
            PropertiesComponent.getInstance().setValue(API_TOKEN_PROPERTY, apiToken);

            System.out.println("Entered Project ID: " + projectID);
            System.out.println("Entered Destination Path: " + destinationPath);
            System.out.println("Entered API Token: " + apiToken);
        }
    }

    private static class MyDialog extends DialogWrapper {
        private final JTextField projectIDField = new JTextField();
        private final JTextField destinationPathField = new JTextField();
        private final JPasswordField apiTokenField = new JPasswordField();

        protected MyDialog(Project project, String savedProjectID, String savedDestinationPath, String savedApiToken) {
            super(project, true);
            setTitle("FlutterFlow Configuration");

            projectIDField.setText(savedProjectID);
            destinationPathField.setText(savedDestinationPath);
            apiTokenField.setText(savedApiToken);

            init();
        }

        @Override
        protected JComponent createCenterPanel() {
            JPanel panel = new JPanel(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = JBUI.insets(5);
            gbc.gridx = 0;
            gbc.gridy = 0;

            panel.add(new JLabel("Project ID:"), gbc);

            gbc.gridy++;
            panel.add(new JLabel("Destination Path:"), gbc);

            gbc.gridy++;
            panel.add(new JLabel("API Token:"), gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            panel.add(projectIDField, gbc);

            gbc.gridy++;
            panel.add(destinationPathField, gbc);

            gbc.gridy++;
            panel.add(apiTokenField, gbc);

            panel.setPreferredSize(new Dimension(400, 200));

            return panel;
        }

        @Override
        protected void createDefaultActions() {
            super.createDefaultActions();
            myOKAction.putValue(Action.NAME, "Save");
        }

        @Override
        protected void doOKAction() {
            if (validateFields()) {
                String projectID = getProjectID();
                String destinationPath = getDestinationPath();
                String apiToken = getApiToken();

                // Save the data into the persisted fields
                PropertiesComponent.getInstance().setValue(PROJECT_ID_PROPERTY, projectID);
                PropertiesComponent.getInstance().setValue(DESTINATION_PATH_PROPERTY, destinationPath);
                PropertiesComponent.getInstance().setValue(API_TOKEN_PROPERTY, apiToken);

                super.doOKAction();
            }
        }


        private boolean validateFields() {
            // Perform any necessary validation of the input fields
            if (projectIDField.getText().trim().isEmpty()) {
                Messages.showErrorDialog(getContentPane(), "Please enter a Project ID.", "Invalid Project ID");
                return false;
            }

            if(destinationPathField.getText().trim().isEmpty()) {
                Messages.showErrorDialog(getContentPane(), "Please enter a valid Destination path", "Invalid Destination Path");
                return false;
            }

            if(apiTokenField.getPassword().length == 0) {
                Messages.showErrorDialog(getContentPane(), "Please provide a valid API token for your FlutterFlow account", "Invalid API Token");
                return false;
            }

            // Add any other validation logic here as needed

            return true;
        }

        public String getProjectID() {
            return projectIDField.getText().trim();
        }

        public String getDestinationPath() {
            return destinationPathField.getText().trim();
        }

        public String getApiToken() {
            return new String(apiTokenField.getPassword()).trim();
        }
    }
}
