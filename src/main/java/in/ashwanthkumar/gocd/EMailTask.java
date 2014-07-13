package in.ashwanthkumar.gocd;

import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.config.Property;
import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import com.thoughtworks.go.plugin.api.response.validation.ValidationResult;
import com.thoughtworks.go.plugin.api.task.Task;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutor;
import com.thoughtworks.go.plugin.api.task.TaskView;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

@Extension
public class EMailTask implements Task {
    public static final String TO_ADDRESS = "To";
    public static final String SUBJECT = "Subject";
    public static final String MESSAGE = "Message";
    public static final String ATTACHMENTS = "Attachments";

    @Override
    public TaskConfig config() {
        TaskConfig config = new TaskConfig();
        config.addProperty(TO_ADDRESS);
        config.addProperty(SUBJECT);
        config.addProperty(MESSAGE);
        config.addProperty(ATTACHMENTS);
        return config;
    }

    @Override
    public TaskExecutor executor() {
        return new EMailTaskExecutor();
    }

    @Override
    public TaskView view() {
        return new TaskView() {
            @Override
            public String displayValue() {
                return "EMail";
            }

            @Override
            public String template() {
                try {
                    return IOUtils.toString(getClass().getResourceAsStream("/views/task.template.html"), "UTF-8");
                } catch (Exception e) {
                    return "Failed to find template: " + e.getMessage();
                }
            }
        };
    }

    @Override
    public ValidationResult validate(TaskConfig taskConfig) {
        ValidationResult validationResult = new ValidationResult();
        if (StringUtils.isEmpty(taskConfig.getValue(TO_ADDRESS))) {
            validationResult.addError(new ValidationError(TO_ADDRESS, "To whom should we be sending this email?"));
        }

        if (StringUtils.isEmpty(taskConfig.getValue(SUBJECT))) {
            validationResult.addError(new ValidationError(SUBJECT, "What's the subject of the email?"));
        }

        if (StringUtils.isEmpty(taskConfig.getValue(MESSAGE))) {
            validationResult.addError(new ValidationError(MESSAGE, "Don't we need to send a message?"));
        }
        return validationResult;
    }
}
