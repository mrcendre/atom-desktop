package me.atom.windowsj;

import java.util.UUID;
import java.net.*;
import java.io.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * 
 * VoiceInputoolbox.java created by Guillaume Cendre (aka chlkbumper)
 *
 * This class is kind of a bridge between the DialogWindow class and
 * the JavaSoundRecorder class. It generates an unique identifier for
 * the audio record that will be used to reference it through all the
 * steps of the speech recognition.
 *
 */


public class VoiceInputToolbox {

	static String uuid;
	
	final static JavaSoundRecorder recorder = new JavaSoundRecorder();
	
	public static void startRecording() {
		///play a Siri-like sound effect ... Just for fun
		try{
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/mictap.wav").getAbsoluteFile());
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    }catch(Exception ex){
	        System.out.println("Error with playing sound.");
	        ex.printStackTrace();
	    }
		//////End
		uuid = UUID.randomUUID().toString();
		System.out.println(uuid);
		recorder.start(uuid);
	}
	public static void stopRecording() throws MalformedURLException{
		try {
			recorder.finishAndProcess(uuid);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}