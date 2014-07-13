package in.ashwanthkumar.gocd;

import com.thoughtworks.go.plugin.api.response.execution.ExecutionResult;
import com.thoughtworks.go.plugin.api.task.Console;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;
import com.thoughtworks.go.plugin.api.task.TaskExecutor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

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
        ProcessBuilder mail = createMailCommand(taskConfig);
        String messageToSend = taskConfig.getValue(EMailTask.MESSAGE);

        Console console = context.console();
        console.printLine("Running command: " + mail.command());

        Process mailProcess = mail.start();

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

    ProcessBuilder createMailCommand(TaskConfig taskConfig) {
        String toAddress = taskConfig.getValue(EMailTask.TO_ADDRESS);
        String subject = taskConfig.getValue(EMailTask.SUBJECT);

        List<String> command = new ArrayList<String>();
        command.add("mail");
        command.add("-s");
        command.add(StringEscapeUtils.escapeJava(subject));
        command.add(toAddress);

        return new ProcessBuilder(command);
    }


}
