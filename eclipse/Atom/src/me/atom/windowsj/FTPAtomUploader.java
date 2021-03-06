package me.atom.windowsj;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 
 * FTPAtomUploader.java created by Guillaume Cendre (aka chlkbumper)
 * 
 * This class uploads the recorded audio file to the server side app.
 * The file's name is the UUID given to him when it was created.
 * You have to set few variables such as the server adress, the
 * username, password, etc.
 * 
 */

public class FTPAtomUploader {
	static String appDataPath = System.getenv("APPDATA");

    static void doUpload(String uuid) {

          File fileSource = new File(appDataPath + "\\Atom\\" + uuid + ".flac");

          String fileName = fileSource.getName();

          /** YOUR PERSONNAL INFORMATIONS **/
          String userName = "USERNAME";
          String password = "PASSWORD";
          String ftpServer = "FTP.SERVER.COM";
          /****/

          StringBuffer sb = new StringBuffer("ftp://");

          sb.append(userName);
          sb.append(':');
          sb.append(password);
          sb.append('@');

          sb.append(ftpServer);
          sb.append("/"); /**WARNING: Path extension; it will be added after connection
          *The file must be at your server's root. Otherwise the PHP script won't detect it.*/
          sb.append(fileName);

          sb.append(";type=i");


          BufferedInputStream bis = null;
          BufferedOutputStream bos = null;
          try {

                URL url = new URL(sb.toString());
                URLConnection urlc = url.openConnection();
                bos = new BufferedOutputStream(urlc.getOutputStream());
                bis = new BufferedInputStream(new FileInputStream(fileSource));

                int i;

                // read byte by byte until end of stream
                while ((i = bis.read()) != -1) {
                      bos.write(i);
                }
          } catch (Exception e) {
                e.printStackTrace();
          } finally {

                if (bis != null)
                      try {
                            bis.close();
                      } catch (IOException ioe) {
                            ioe.printStackTrace();
                            System.out.println("IO exception after if bis " + ioe);
                      }
                if (bos != null)
                      try {
                            bos.close();
                      } catch (IOException ioe) {
                            ioe.printStackTrace();
                            System.out.println("IO exception after if " + ioe);
                      }
          }
    }
}