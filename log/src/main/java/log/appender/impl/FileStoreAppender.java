package log.appender.impl;

import log.appender.Appender;

import java.io.*;
import java.util.Objects;
import java.util.function.Supplier;

public class FileStoreAppender implements Appender {

    private File file;

    public File getFile() {
        return file;
    }

    public FileStoreAppender setFile(File file) {
        this.file = file;
        return this;
    }

    @Override
    public void out(String text) {
        try(
                FileOutputStream fileOutputStream = new FileOutputStream(Objects.requireNonNull(file,"未设置文件"), true);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)
                ) {
            bufferedWriter.write(text+System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Supplier<Appender> getAppender(){
        return FileStoreAppender::new;
    }
}
