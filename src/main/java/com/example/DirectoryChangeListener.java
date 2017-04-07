package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

/**
 *  Created by shailendra.singh on 4/7/17.
 */
@Component
public class DirectoryChangeListener {

    @Value("${folder.path}")
    private String dir;

    @Autowired
    private S3Operations s3Operations;

    public void initDirectoryWatchEvent() {
        try {

            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path folderPath = Paths.get(dir);

            // register the watch service for this directory
            folderPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE);

            System.out.println("Watch event registered for : " + folderPath.getFileName());

            while (true) {
                WatchKey watchKey = null;

                // get the watch key
                try {
                    watchKey = watchService.take();
                } catch (InterruptedException ex) {
                    System.out.println("Error : " + ex.getMessage());
                }

                // process events
                for (WatchEvent<?> watchEvent : watchKey.pollEvents()) {

                    WatchEvent.Kind<?> watchEventKind = watchEvent.kind();

                    File file = ((Path) watchEvent.context()).toFile();

                    System.out.println("Event "+ watchEventKind.name() +  " registered for " + file.getName());

                    switch (watchEventKind.name()) {

                        case "ENTRY_CREATE":
                            s3Operations.createFile(getDirectoryFilePath(file));
                            break;

                        case "ENTRY_DELETE":
                            s3Operations.deleteFile(getDirectoryFilePath(file));
                            break;

                        case "ENTRY_MODIFY":
                            s3Operations.modifyFile(getDirectoryFilePath(file));
                            break;

                    }

                }

                // after each event watch key should be reset
                if(!watchKey.reset()){
                    break;
                }

            }

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private File getDirectoryFilePath(File file) {
        return Paths.get(dir+file.getName()).toFile();
    }

}
