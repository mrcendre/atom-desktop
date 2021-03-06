package me.atom.windowsj;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import static java.awt.GraphicsDevice.WindowTranslucency.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * 
 * DialogWindow.java created by Guillaume Cendre (aka chlkbumper)
 * 
 * This class creates the User Interface of Atom. If you want a simplified version
 * of Atom, you should use AtomConsole. Find it next to AtomDesktop repo on GitHub.
 * 
 */

public class DialogWindow extends JFrame implements ActionListener {
	
	
	
	private static final long serialVersionUID = 1L;
	private static JTextField requestField;
	private static TransparentTextArea status;
	private static JPanel panel;
	private static JButton btnSpeech;
	private static JButton submitButton;
	static int recordingStatus = 0;
	static String uuid;
	
	public DialogWindow() throws IOException, FontFormatException, InterruptedException {
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int)screenSize.getWidth();
        int x = screenWidth - (screenWidth/3);
        
        this.setLocation(x, 10);
        
		this.setUndecorated(true);
		this.setVisible(true);
		this.setTitle("Atom");
		
		setBackground(new Color(0,0,0,0));
		setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        panel = new JPanel() {
        
			private static final long serialVersionUID = 1L;

			@Override
            protected void paintComponent(Graphics g) {
            	try {
					Image img = ImageIO.read(new File("images/bg.png"));
					g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
            	} catch (IOException e) {
					e.printStackTrace();
				}
              
            	if (g instanceof Graphics2D) {
                    final int R = 240;
                    final int G = 240;
                    final int B = 240;

                    Paint p =
                        new GradientPaint(0.0f, 0.0f, new Color(R, G, B, 0),
                            0.0f, getHeight(), new Color(R, G, B, 0), true);
                    Graphics2D g2d = (Graphics2D)g;
                    g2d.setPaint(p);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        setContentPane(panel);
        getContentPane().setLayout(null);
                
        Font defaultFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/OpenSans-CondLight.ttf")).deriveFont(Font.PLAIN, 17f);
        Font defaultFontIndice = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/OpenSans-CondLight.ttf")).deriveFont(Font.PLAIN, 15f);
        
        
        submitButton = new JButton("Submit typed question");
        submitButton.setBounds(64, 103, 153, 43);
        submitButton.addActionListener(this);
        
        
        
        requestField = new JTextField();
        requestField.addActionListener(this);
        requestField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {
        		requestField.selectAll();				
			}
			@Override
			public void focusLost(FocusEvent arg0) {				
			}
        });
        status = new TransparentTextArea();
        status.setAutoscrolls(true);
        status.setBackground(new Color(0,0,0,0));
        status.setEditable(false);
        status.setBounds(64, 175, 260, 467);
        status.setForeground(Color.BLACK);
        status.setFont(defaultFont);
        status.setDragEnabled(false);   
        status.setText("Hi, I am Atom, your personnal assistant. May I help you ?");
        
        requestField.setBounds(64, 58, 260, 34);
        requestField.setColumns(10);
        
        
        btnSpeech = new JButton("Speak");
        btnSpeech.setBounds(227, 103, 97, 43);
        btnSpeech.addActionListener(this);
        
        panel.add(btnSpeech);
        panel.add(requestField);
        panel.add(submitButton);
        panel.add(status);
        
        JLabel lblAtomCopyright = new JLabel("Atom \u00A9 2013 Guillaume Cendre (aka chlkbump)");
        lblAtomCopyright.setForeground(Color.GRAY);
        lblAtomCopyright.setBounds(64, 20, 260, 27);
        lblAtomCopyright.setFont(defaultFontIndice);

        panel.add(lblAtomCopyright);
        

	}
    
    public static void printError() {
    	panel.add(status);
    	status.setText("Sorry, an error occured... Please check your Internet connection and retry.");
    	status.setForeground(Color.RED);
    }
    
	public void actionPerformed(ActionEvent arg0) {
			
		if (arg0.getSource() == submitButton) {
			status.setText("Let me check on that...");
    		try {WolframRequest.query(requestField.getText());}
    		catch (Exception e) {
    			status.setText("Sorry, an error occured... Please check your Internet connection and retry.");
    			status.setForeground(Color.RED);
    		}
		}
		
		if (arg0.getSource() == btnSpeech) {
			if (recordingStatus == 0) { try { beginSpeech();} catch (Exception e) {e.printStackTrace();}}
			else if (recordingStatus == 1) {try {stopSpeech();} catch (Exception e) {}}
		}
		
		if (arg0.getSource() == requestField) {
    		requestField.selectAll();
    		status.setText("Let me check on that...");
    		try {WolframRequest.query(requestField.getText());}
    		catch (Exception e) {
    			status.setText("Sorry, an error occured... Please check your Internet connection and retry.");
    			status.setForeground(Color.RED);		
    		}
		}
		
		status.removeAll();
		
	}
	
	public static void beginSpeech() throws InterruptedException {
		recordingStatus = 1;
		btnSpeech.setText("Done");
		submitButton.setEnabled(false);
		requestField.setEnabled(false);
		status.setText("I'm listening");
		
		Thread startVoiceThread = new Thread(new Runnable() {
		@Override
		public void run() {try {VoiceInputToolbox.startRecording();} catch (Exception e) {e.printStackTrace();}}});
		startVoiceThread.start();		
	}
	
	public static void stopSpeech() throws IOException, InterruptedException{
		recordingStatus = 0;
		btnSpeech.setText("Speak");
		submitButton.setEnabled(true);
		requestField.setEnabled(true);
		status.setText("I'm processing your request...");
		

		Thread stopVoiceThread = new Thread(new Runnable() {
		@Override
		public void run() {try {VoiceInputToolbox.stopRecording();} catch (Exception e) {e.printStackTrace();}}});
		stopVoiceThread.start();
		Thread.sleep(2000);
		
	}
	
	public static void voiceFailed(int errno) {
		
		if (errno == 101) {
			status.setText("I did not understand what you said... Could you ask louder?");
			status.setForeground(Color.RED);		
		}
		
	}
	

    public static void main(String[] args) {
        GraphicsEnvironment ge = 
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        boolean isPerPixelTranslucencySupported = 
            gd.isWindowTranslucencySupported(PERPIXEL_TRANSLUCENT);
        
        if (!isPerPixelTranslucencySupported) {
            System.out.println(
                "Per-pixel translucency is not supported");
                System.exit(0);
        }

        JFrame.setDefaultLookAndFeelDecorated(false);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GradientTranslucentWindowDemo gtw = new
                    GradientTranslucentWindowDemo();

                gtw.setVisible(true);
            }
        });
		
	}
    
    public static void print(String answer) {
    	status.setText(answer);    	
    }
    public static void printRequest(String answer) {
    	requestField.setText(answer);
    	status.setText("Let me check on that...");
		try {WolframRequest.query(requestField.getText());}
		catch (Exception e) {
			status.setText("Sorry, an error occured... Please check your Internet connection and retry.");
			status.setForeground(Color.RED);		
		}
    }
}
