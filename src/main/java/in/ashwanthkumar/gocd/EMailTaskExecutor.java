package in.ashwanthkumar.gocd;

import com.thoughtworks.go.plugin.api.response.execution.ExecutionResult;
import com.thoughtworks.go.plugin.api.task.Console;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;
import com.thoughtworks.go.plugin.api.task.TaskExecutor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import static in.ashwanthkumar.gocd.EMailUtils.addAttachment;
import static in.ashwanthkumar.gocd.EMailUtils.createMailCommand;

public class EMailTaskExecutor implements TaskExecutor {

    @Override
    public ExecutionResult execute(TaskConfig taskConfig, TaskExecutionContext taskExecutionContext) {
        try {
            return runCommand(taskConfig, taskExecutionContext);
        } catch (Exception e) {
            return ExecutionResult.failure(e.getMessage(), e);
        }
    }

    protected ExecutionResult runCommand(TaskConfig taskConfig, TaskExecutionContext context) throws IOException, InterruptedException {
        String toAddress = taskConfig.getValue(EMailTask.TO_ADDRESS);
        String subject = taskConfig.getValue(EMailTask.SUBJECT);
        String messageToSend = taskConfig.getValue(EMailTask.MESSAGE);
        String attachmentsString = taskConfig.getValue(EMailTask.ATTACHMENTS);
        String workingDirectory = System.getProperty("user.dir") + File.separator + context.workingDir();

        ProcessBuilder mail = createMailCommand(workingDirectory, toAddress, subject);

        Console console = context.console();
        console.printLine("Running commands: " + mail.command());

        Process mailProcess = mail.start();

        if (StringUtils.isNotEmpty(attachmentsString)) {
            String[] attachments = StringUtils.split(attachmentsString, "\n");
            for (String attachment : attachments) {
                File attachmentFile = new File(workingDirectory + File.separator + StringUtils.trim(attachment));
                if(!attachmentFile.exists()) {
                    return ExecutionResult.failure("File " + attachmentFile.getAbsolutePath() + " is not found.");
                }
                addAttachment(attachmentFile.getName(), attachmentFile.getAbsolutePath(), mailProcess.getOutputStream());
            }
        }
        IOUtils.copy(new StringReader(messageToSend), mailProcess.getOutputStream());
        IOUtils.closeQuietly(mailProcess.getOutputStream());

        console.readErrorOf(mailProcess.getErrorStream());
        console.readOutputOf(mailProcess.getInputStream());
        int exitCode = mailProcess.waitFor();
        mailProcess.destroy();

        if (exitCode != 0) {
            return ExecutionResult.failure("Unable to send email");
        }

        return ExecutionResult.success("EMail sent.");
    }


}
