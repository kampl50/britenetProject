package filesUtils;

import java.io.File;

public class FileUtils {

    public static double getSize(File file) {
        if (file.exists()) {

            double bytes = file.length();
            double kilobytes = (bytes / 1024);
            double megabytes = (kilobytes / 1024);
            return (megabytes / 1024);
        } else {
            System.out.println("File does not exists!");
        }
        return 1_000_000;
    }
}
