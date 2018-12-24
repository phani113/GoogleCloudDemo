package com.cloud.google.demo.drive;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

@Service
public class GoogleCloudServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleCloudServiceImpl.class);

    @Autowired
    private CommonUitil commonUitil;
    @Autowired
    private GoogleCloudAuthServiceImpl googleCloudAuthServiceImpl;

    public String getJsonContent(String fileName) throws IOException {
        FileList result = googleCloudAuthServiceImpl.getDriveService().files().list().execute();
        List<File> files = result.getFiles();
        if (files == null || files.size() == 0) {
            return "no json files found with this " + fileName;
        }
        File file = commonUitil.getFile(fileName);

        if (null != file && file.getMimeType().equalsIgnoreCase("application/json")) {
            return commonUitil.getJsonResponse(googleCloudAuthServiceImpl.getDriveService(), file.getId());
        }
        return fileName + " is not json file";
    }

    //https://accounts.google.com/signin/oauth/oauthchooseaccount?
    // client_id=198541576821-khmmmhbijsa1t7gkct3smnlqohk5njn2.apps.googleusercontent.com&as=lI7Ga3quxgAkmAvnsT4wEA&nosignup=1&destination=http%3A%2F%2Flocalhost%3A8080&approval_state=!ChRrcV9iQm5mQU9fSTA5UHVGMl9aVhIfVTF1MlgwWnFRTGdYOEhuU1JuY2dubW8tTGJqQWZSWQ%E2%88%99APNbktkAAAAAXCES6VqnwirjFAYowSrnIvDm3tj6tMJW&xsrfsig=AHgIfE_CdGa_HIptgy-koVTg1C8Now6U4A&flowName=GeneralOAuthFlow
    public List<String> getFileNames(int pageSize) throws IOException {
        FileList result = googleCloudAuthServiceImpl.getDriveService().files().list().setPageSize(pageSize).execute();
        List<String> resp = new ArrayList<>();
        List<File> files = result.getFiles();
        if (files == null || files.size() == 0) {
            LOGGER.info("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                LOGGER.info("FileName: {}", file.getName());
                resp.add(file.getName());
            }
        }
        return resp;
    }

    public byte[] downloadFile(String fileName) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        File file = commonUitil.getFile(fileName);
        googleCloudAuthServiceImpl.getDriveService().files().get(file.getId())
                .executeMediaAndDownloadTo(outputStream);
        return outputStream.toByteArray();

    }

}
