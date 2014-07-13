package in.ashwanthkumar.gocd;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class EMailUtils {
    public static ProcessBuilder createMailCommand(String workingDir, String toAddress, String subject) {
        List<String> command = new ArrayList<String>();
        command.add("mail");
        command.add("-s");
        command.add(StringEscapeUtils.escapeJava(subject));
        command.add(toAddress);

        return new ProcessBuilder(command).directory(new File(workingDir));
    }

    public static OutputStream addAttachment(String filename, String path, OutputStream processOutputStream) throws IOException {
        CustomUUEncoder encoder = new CustomUUEncoder(filename);
        encoder.encode(new FileInputStream(path), processOutputStream);
        return processOutputStream;
    }


}
