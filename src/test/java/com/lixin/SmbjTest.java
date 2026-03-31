package com.lixin;

import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;

import java.io.*;
import java.util.EnumSet;

/**
 * @author lixin
 * @date 2026/3/31
 */
public class SmbjTest {

    public static void main(String[] args) {
        String serverName = "192.168.1.100";
        String shareName = "shared_folder";
        String username = "yourUsername";
        String password = "yourPassword";

        try (SMBClient client = new SMBClient();
             Connection connection = client.connect(serverName)) {

            // Authenticate with the server
            AuthenticationContext authContext =
                    new AuthenticationContext(username, password.toCharArray(), null);
            Session session = connection.authenticate(authContext);

            // Connect to a shared folder
            DiskShare share = (DiskShare) session.connectShare(shareName);

            // Download file from the share
            String remoteFilePath = "example.txt";
            try (InputStream in = share.openFile(remoteFilePath,
                            EnumSet.of(com.hierynomus.msdtyp.AccessMask.GENERIC_READ),
                            null, null, SMB2CreateDisposition.FILE_OPEN, null)
                    .getInputStream();
                 FileOutputStream out = new FileOutputStream("downloaded_example.txt")) {

                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                System.out.println("File downloaded successfully.");
            }

            // Upload file to the share
            String localFilePath = "local_file.txt";
            try (FileInputStream in = new FileInputStream(new File(localFilePath));
                 OutputStream out = share.openFile("uploaded_example.txt",
                                 EnumSet.of(com.hierynomus.msdtyp.AccessMask.GENERIC_WRITE),
                                 null, null,
                                 SMB2CreateDisposition.FILE_CREATE, null)
                         .getOutputStream()) {

                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                System.out.println("File uploaded successfully.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
