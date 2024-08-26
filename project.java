import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.util.Timer;
import javax.swing.Timer;
import java.awt.Color; // Import Color class
import java.awt.Graphics; 
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Font;
/////////////////////////////////////////////sound///////////////////////////
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineUnavailableException;
import java.io.File;
import java.io.IOException;

class Instructions extends JPanel {
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.setFont(new Font("serif", Font.BOLD, 30));
        g.drawString("Breakout Game Instructions", 150, 100);
        g.setFont(new Font("serif", Font.PLAIN, 20));
        g.drawString("Use the left and right arrow keys to move the paddle.", 100, 150);
        g.drawString("Bounce the ball off the paddle to destroy the bricks.", 100, 180);
        g.drawString("Press Enter to start the game.", 100, 210);
    }
}


 class Play extends JPanel implements KeyListener , ActionListener
{
private boolean displayInstructions = true;


    boolean play = true;
	int score = 0;
	int totalBricks = 21;
	Timer timer;
    int delay = 2;///////////// speed of ball /////
	int playerX = 310;
	int ballposX = 120;
    int ballposY = 350;
    int ballXdir = -1;
    int ballYdir = -2;
    Clip hitSound;
    Clip winSound;
    Clip lostSound;
    Rectangle ballRect;  // Declare ball rectangle
    Rectangle brickRect;
    private Map map;

	public Play()
	{
		map=new Map(3,7);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();

		try
		{
        File hitSoundFile = new File("C:/Users/hp/Downloads/mixkit-tree-branch-brake-2943.wav"); // Replace with the actual path to your sound file
        AudioInputStream hitSoundStream = AudioSystem.getAudioInputStream(hitSoundFile);
        hitSound = AudioSystem.getClip();
        hitSound.open(hitSoundStream);

        File winSoundFile = new File("C:/Users/hp/Downloads/mixkit-video-game-win-2016.wav");// Replace with the actual path to your sound file
        AudioInputStream winSoundStream = AudioSystem.getAudioInputStream(winSoundFile);
        winSound = AudioSystem.getClip();
        winSound.open(winSoundStream);

         File lostSoundFile = new File("C:/Users/hp/Downloads/mixkit-sad-game-over-trombone-471.wav");// Replace with the actual path to your sound file
        AudioInputStream lostSoundStream = AudioSystem.getAudioInputStream(lostSoundFile);
        lostSound = AudioSystem.getClip();
        lostSound.open(lostSoundStream);
        } 
        catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
        e.printStackTrace();
        }
	}

	public void paintComponent(Graphics g) 
	{
        super.paintComponent(g);
        if (displayInstructions) {
            // Show instructions
            Instructions instructions = new Instructions();
            instructions.paintComponent(g);
        } else {
            // Show the game
            draw(g);
        } // Call the parent class's paintComponent method
        
    }
	public void draw(Graphics g)
	{
		//background///
		g.setColor(new Color(0X49DCBF));
		g.fillRect(1, 1, 692, 592);
		////border///
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691,0,3,592);

		//score//
		g.setColor(new Color(0XF27E48));
		g.setFont(new Font("serif",Font.BOLD,20));
		g.drawString("Score : "+score,390,30);

		//paddle//
		g.setColor(new Color(0X5D5959));
		g.fillRect(playerX,550,100,20);

		//Ball//
		g.setColor(new Color(0XF8F050));
		g.fillOval(ballposX,ballposY,20,20);


		if (totalBricks <= 0) { // if all bricks are destroyed then you win
			play = false;
			ballXdir = 0;
			ballYdir = 0;

			g.setColor(new Color(0XFF6464));
			g.setFont(new Font("MV Boli", Font.BOLD, 30));
			g.drawString("You Won, Score: " + score, 190, 300);
			
			g.setFont(new Font("MV Boli", Font.BOLD, 20));
			g.drawString("Press Enter to Restart.", 230, 350);
		}
		if (totalBricks <= 0)
		 {
   			play = false;
    		ballXdir = 0;
   		    ballYdir = 0;
            winSound.setFramePosition(0); // Rewind the sound to the beginning
            winSound.start();
            // Rest of the code...
         }

		if(ballposY>570)
		{
			play=false;
			ballXdir=0;
			ballYdir=0;
			g.setColor(new Color(0XF24C2F));
			g.setFont(new Font("serif",Font.BOLD,30));
		    g.drawString("GAME OVER , Score: "+score,190,300);

		    g.setFont(new Font("serif",Font.BOLD,20));
		    g.drawString("PRESS ENTER TO RESTART ",230,350);
		}

		if (ballposY>570)
		 {
   			play = false;
    		ballXdir = 0;
   		    ballYdir = 0;
            lostSound.setFramePosition(0); // Rewind the sound to the beginning
            lostSound.start();
            // Rest of the code...
         }
         //draw bricks//
		 map.draw((Graphics2D)g);

		
	}
	 public void actionPerformed(ActionEvent e) {
	 	timer.start();
	 	if(play && !displayInstructions)
	 	{
	 		// Ball - Pedal  interaction 
			if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) 
			{
				ballYdir = -ballYdir;
			}
			


			for(int i=0;i<map.map.length;i++)// ball-bricks intersections
			{
				for(int j=0;j<map.map[0].length;j++)
				{
					if(map.map[i][j]>0)
					{
						 int brickX= j* map.brickWidth + 80;
						 int brickY= i* map.brickHeight + 50;
						 int brickWidth= map.brickWidth;
						 int brickHeight= map.brickHeight;

						 Rectangle rect = new Rectangle(brickX,brickY,brickWidth,brickHeight);
						 Rectangle ballRect= new Rectangle(ballposX,ballposY,20,20);
						 Rectangle brickRect= rect;


			
						 if(ballRect.intersects(brickRect))
						 {
						 	map.setBrickValue(0,i,j);
						 	totalBricks--;
						 	score+=5;
						 	  hitSound.setFramePosition(1); // Rewind the sound to the beginning
   			                  hitSound.start();


						if(ballposX + 19 <= brickRect.x || ballposX +1 >= brickRect.x + brickRect.width) 
						{
							ballXdir = -ballXdir;
						}
					    else 
					    {
							ballYdir=-ballYdir;
						}
					}
			
				}
			}

	 		ballposX += ballXdir;
	 		ballposY += ballYdir;
	 		if(ballposX<0)
	 		{
	 			ballXdir =-ballXdir;
	 		}
	 		if(ballposY<0)
	 		{
	 			ballYdir =-ballYdir;
	 		}
	 		if(ballposX>670)
	 		{
	 			ballXdir =-ballXdir;
	 		}
	 	}
	 	repaint();
	 }
}
     public void keyTyped(KeyEvent e) {}
     public void keyReleased(KeyEvent e) {}

    
    public void keyPressed(KeyEvent e)
     {
     	if (displayInstructions) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                displayInstructions = false; // Start the game when Enter is pressed
            }
        

            }else{
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){ // if right arrow key is pressed then paddle moves right ///
		  if(playerX >= 600) 
		{
		  playerX = 600;
		}
		 else 
		{
		  moveRight();
		}
	}

		if(e.getKeyCode() == KeyEvent.VK_LEFT) { // if left arrow key is pressed then paddle moves left///
		if(playerX < 10) 
		{
			playerX = 10;
		} 
		else 
		{
			moveLeft();
		}
		}

		    if(e.getKeyCode() == KeyEvent.VK_ENTER) { // if enter key is pressed then game restarts
			if(!play)
			 {
				play = true;
				ballposX = 120;
				ballposY = 350;
				ballXdir = -1;
				ballYdir = -2;
				score = 0;
				totalBricks = 21;
				map = new Map(3,7);
				repaint();
			}
		}
	}
}




  	public void moveRight() { // paddle moves right by 50 pixels
		play = true;
		playerX += 20;
	}
	public void moveLeft() { // paddle moves left by 50 pixels
		play = true;
		playerX -= 20;
	}
	}


//////////////////////////////////////////class 2 //////////////////////////////////

class Map
{
	public int map[][],brickWidth,brickHeight;
	public Map(int row,int col)
	{
		map = new int[row][col];
		for(int i=0;i<map.length;i++)
		{
			for(int j=0;j<map[0].length;j++)
			{
				map[i][j]=1;
			}
		}
		brickWidth=540/col;
		brickHeight=150/row;
	}

	public void draw(Graphics2D g)
	{
		for(int i=0;i<map.length;i++)
		{
			for(int j=0;j<map[0].length;j++)
			{
				if(map[i][j]>0)
				{
					g.setColor(new Color(0XD26148));
					g.fillRect(j* brickWidth +80, i* brickHeight +50, brickWidth,brickHeight);
					g.setStroke(new BasicStroke(3));
					g.setColor(Color.BLACK);
					g.drawRect(j* brickWidth +80, i* brickHeight +50, brickWidth,brickHeight);
				}
			}
		}
	}

		public void setBrickValue(int value, int row, int col)
		{
			map[row][col] = value;
	    }

}



////////////////////////////////// main class ///////////////////////////////////////
public class project
{
	public static void main (String []args)
	{
		JFrame obj= new JFrame();
		Play play= new Play();
		obj.setBounds(10,10,700,600);
		obj.setTitle("Welcome to Breakout Ball");
		obj.setResizable(false);////changed
		obj.setVisible(true);
		obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		obj.add(play);
		play.requestFocusInWindow();
	}

}
