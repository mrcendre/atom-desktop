package me.atom.windowsj;

import javax.sound.sampled.*;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import javaFlacEncoder.FLAC_FileEncoder;

/**
 * 
 * JavaSoundRecorder.java created by Guillaume Cendre (aka chlkbumper)
 * 
 * This class one of the most important classes for speech recognition :
 * it records an audio file, uploads it to the server side app, trigger
 * it to process the file, retrieves the result text file, and, when it
 * is done, set the JTextField with the recognized sentence(s).
 * 
 */

public class JavaSoundRecorder {
	static final long RECORD_TIME = 5000; //5seconds
	static String appDataPath = System.getenv("APPDATA");
	static File audioDirectory;
	AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
	TargetDataLine line;

	/**
	 * sample rate -> 16000 !important
	 **/
	AudioFormat getAudioFormat() {
		float sampleRate = 16000;
		int sampleSizeInBits = 16;
		int channels = 2;
		boolean signed = true;
		boolean bigEndian = true;
		AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
											 channels, signed, bigEndian);
		return format;
	}

	void start(String id) {
		File wavFile = new File(appDataPath + "\\Atom\\" + id + ".wav");
		
		try {
			AudioFormat format = getAudioFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

			if (!AudioSystem.isLineSupported(info)) {
				System.out.println("Line not supported");
				System.exit(0);
			}
			line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format);
			line.start();
			
			AudioInputStream ais = new AudioInputStream(line);
			
			AudioSystem.write(ais, fileType, wavFile);

		} catch (LineUnavailableException ex) {
			ex.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	void finishAndProcess(String uuid) throws MalformedURLException, IOException {
		line.stop();
		line.close();
		
		FLAC_FileEncoder flacEncoder = new FLAC_FileEncoder();
		File inputFile = new File(appDataPath + "\\Atom\\" + uuid + ".wav");
		File outputFile = new File(appDataPath + "\\Atom\\" + uuid + ".flac");
		
		flacEncoder.encode(inputFile, outputFile);
		
		
		/**
		 * Upload the recorded and converted file, and indicates**/ 
		FTPAtomUploader.doUpload(uuid);
		try {
			String url = "http://YOURSITE.COM/shell.php";
			/**
			 * The String url must be the URL path to the "shell.php" file on your remote
			 * server. This PHP script will send the recorded file (that we are going to
			 * upload on this server and that's why you have to know the FTP identifiers)
			 * to the Google Speech API. You can find the Server side Atom app at my GitHub
			 * repo called "AtomServer"
			 */
			String charset = "iso-8859-1";
			String param1 = uuid;
			String query = String.format("id=%s", URLEncoder.encode(param1, charset));
			
			URLConnection connection = new URL(url + "?" + query).openConnection();
			connection.setRequestProperty("Accept-Charset", charset);
			connection.getInputStream();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		while (!hasRespondedTo(uuid)) {
			
		Thread bitch = new Thread(new Runnable () {
			public void run() {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}}});
		bitch.start();
		
		}
		
		File targetFile = new File(appDataPath + "\\Atom\\" + uuid + ".txt");
		URL idUrl = new URL("http://yourserver.com/" + uuid + ".txt");
		FileUtils.copyURLToFile(idUrl, targetFile);
		String s = readFile(appDataPath + "\\Atom\\" + uuid + ".txt");
		if (s.length() > 0) {
		int x = s.length();
		String result = s.substring(0,1).toUpperCase().concat(s.substring(1, x));
		DialogWindow.printRequest(result);
		} else {
			DialogWindow.voiceFailed(1);
		}
		
		try {
			String url = "http://YOURSERVER.COM/delete.php";
			/**
			 * This one is the link to the script that deletes the uploaded file
			 * on your server. We have to delete it, otherwise it will remain and
			 * an accumulation of all these audio files may take too much storage.
			 * If you are on free hostings that allow like 10Mb of storage, your
			 * web host will probably be mad and delete your domain because you
			 * get too big.
			 */
			String charset = "iso-8859-1";
			String param1 = uuid;
			String query = String.format("id=%s", URLEncoder.encode(param1, charset));
			
			URLConnection connection = new URL(url + "?" + query).openConnection();
			connection.setRequestProperty("Accept-Charset", charset);
			connection.getInputStream();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	private static String readFile(String path) throws IOException {
		
		FileInputStream stream = new FileInputStream(new File(path));
		try {
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			return Charset.defaultCharset().decode(bb).toString();
		} finally {
			stream.close();
		}
	}
	
	public static boolean hasRespondedTo(String uuid) {
		try {
		
			String URL = new String("http://YOURSERVER.COM/" + uuid + ".txt");
			HttpURLConnection.setFollowRedirects(false);
			HttpURLConnection con = (HttpURLConnection) new URL(URL).openConnection();
			con.setRequestMethod("HEAD");
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}