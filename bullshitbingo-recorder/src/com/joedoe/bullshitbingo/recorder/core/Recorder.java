package com.joedoe.bullshitbingo.recorder.core;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Recorder implements Runnable {

	public final static String RECORDING_DIR = "C:\\tmp\\recording";
		
	public final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmssSSS");
	
	private boolean started = false;
	
	private CountDownLatch cdlStart;
	
	private CountDownLatch cdlStop;
	
    private TargetDataLine line;
    
	private Duration chunkDuration;    
	
	public Recorder() {
		cdlStart = new CountDownLatch(1);
		cdlStop = new CountDownLatch(1);
	}

	@Override
	public void run() {
		// wait before starting
		try {
			cdlStart.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// record until stopped
		while (started) {
			recordSampleStart();
			try {
				Thread.sleep(chunkDuration.toMillis());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			recordSampleStop();
		}		
		cdlStop.countDown();
	}
	
	private void recordSampleStart() {
        try {
            AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
 
            // checks if system supports the data line
            if (!AudioSystem.isLineSupported(info)) {
                System.err.println("Line not supported");
                return;
            }
            
            // open line and recording thread
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();   
            AudioInputStream ais = new AudioInputStream(line);
            new Thread(null, new Runnable() {
				
				@Override
				public void run() {
					try {
						AudioSystem.write(ais, AudioFileFormat.Type.WAVE, getFile());
					} catch (IOException e) {
						e.printStackTrace();
					}					
				}
				
			}, "audio-write-thread)").start(); 
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        }    
        			
	}	

	private void recordSampleStop() {
        line.stop();
        line.close();
	}	
	
	private File getFile() throws IOException {
		String filename =  RECORDING_DIR + "\\" + LocalDateTime.now().format(DATE_TIME_FORMATTER)+".wav";
		File file = new File(filename);
		file.createNewFile();
		return file;
	}
	
    private AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                                             channels, signed, bigEndian);
        return format;
    }

	public synchronized void stop() throws InterruptedException {
		if (!started) return;
		started = false;
		cdlStop.await();
	}
	
	public synchronized void start(int timesInSeconds) {
		if (started) return;
		chunkDuration = Duration.ofSeconds(timesInSeconds);
		cdlStart = new CountDownLatch(1);
		cdlStop = new CountDownLatch(1);		
		started = true;
		new Thread(null, this, "recorder-thread").start();
		cdlStart.countDown();
	}
	
}
