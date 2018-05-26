//Brandon Arnold
//Max Conroy
//Group 1
//CET 350
//arn4181@calu.edu
//con3644@calu.edu

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.*;
import java.util.*;

//Java applet fires a ball from a cannon. Cannon ball is affected by a chosen planet's gravitational pull and angle of the cannon.
//Player Scores when cannon hits ball. Computer scores whenever the ball hits the cannon.

public class CannonVSBall extends Applet implements ActionListener, ItemListener, Runnable, MouseListener, MouseMotionListener, AdjustmentListener, WindowListener
{
	//Variable initialization
	private Thread thread1;
	private Panel sheet = new Panel();
	private Panel control = new Panel();
	private Ball ball;
	private static final long serialVersionUID = 10L;
	private Rectangle screen = new Rectangle();
	private Rectangle circ = new Rectangle();
	private static int SW;
	private static int SH;
	String userString = "User: ";
	String cpuString = "CPU: ";

	//Slider for speed
	int initWH;
	int temp;
	int maxSpeed;
	int minSpeed;

	boolean drag = false;
	Boolean more = true;
	int time_delay = 3;
	Boolean time_pause = false;
	Boolean cover_flag = false;
	Boolean shotflag = false;
	Boolean shotflag2 = false;
	Boolean size_check;

	//Initial scrollbars, angle and speed
	Scrollbar AngleSlider, SpeedSlider;

	//Variables of the angle scrollbar and speed scrollbar
	private final int maxAngle=100;
	private final int minAngle=0;
	private final int increment=1;
	private final int block=5;
	private final int SPEED = 10;
	private final int MAXSPEED=1210;
	private final int MINSPEED=100;
	int defaultspeed = 600;

	//Mouse Location variables
	int x;
	int y;
	int old_x;
	int old_y;
	int new_x;
	int new_y;
	int xRemove;

	Graphics g;
	Rectangle r;

	Label compscore,userscore,statuslabel, finallabel;
	Label scorelabel1, scorelabel;
	int user, comp;

	//items and the menu bar
	MenuBar MB;
	Menu controlm;
	Menu sizem;
	Menu envi;
	Menu sizesub;
	Menu speedsub;
	MenuItem pause;
	MenuItem run;
	MenuItem restartm;
	MenuItem quit;
	MenuItem resetm;

//Planet gravity
	final Float gmercury=(float)3.7;
	final Float gvenus=(float)8.87;
	final Float gearth=(float)9.78;
	final Float gmoon=(float)1.622;
	final Float gmars=(float)3.71;
	final Float gjupiter=(float)24.79;
	final Float gsaturn = (float)10.44;
	final Float guranus = (float)8.69;

	//Checkboxes for size
	CheckboxMenuItem xsm;
	CheckboxMenuItem sm;
	CheckboxMenuItem med;
	CheckboxMenuItem lrg;
	CheckboxMenuItem xl;

	//Checkboxes for speed
	CheckboxMenuItem xf;
	CheckboxMenuItem fa;
	CheckboxMenuItem mod;
	CheckboxMenuItem slow;
	CheckboxMenuItem xsl;

	//Checkboxes for planets
	CheckboxMenuItem mercury;
	CheckboxMenuItem venus;
	CheckboxMenuItem earth;
	CheckboxMenuItem moon;
	CheckboxMenuItem mars;
	CheckboxMenuItem jupiter;
	CheckboxMenuItem saturn;
	CheckboxMenuItem uranus;
	CheckboxMenuItem neptune;
	CheckboxMenuItem pluto;

	final Float gneptune = (float)11.15;
	final Float gpluto = (float)0.58;

	//Variables for the cannon
	int cannoninitx;
	int cannoninity;
	int basex;
	int basey;
	int basew;
	int baseh;

	//Polygon of cannon
	int[] xpts= new int[4];
	int[] ypts=new int[4];
	int npts=4;

	//Menu frame extension
	Frame f;

	int z;
	int angle;
	Float vox;
	Float voy;
	int vo;
	int t=0;
	Float gr = gearth;

	Boolean pause2 = false;


	public void init()
	{
		f= new Frame("Cannon VS Ball");
		f.setVisible(true);
		//Size from html file, sheet is set to that
		SW = (getSize().width-1);
		SH = (getSize().height-1);
		f.setSize(SW+17,SH+106);

		f.setLayout(new BorderLayout());

		//setting sheet size
		sheet.setBackground(Color.white);
		sheet.setVisible(true);
		sheet.setLayout(new BorderLayout(0,0));
		sheet.setSize(SW,SH);

		//Cannon pos set
		cannoninitx = (int)(.96*SW);
		cannoninity = (int)(.944898*SH);
		basex = (int)(.896*SW);
		basey = (int)(.86*SH);
		basew = (int)(.275*SH);
		baseh = (int)(.275*SH);

		//Initial cannon angle set at 45 degrees
		int othercannonx=(int)(cannoninitx-(.16*SW)*(Math.cos(Math.toRadians(45))));
		int othercannony= (int)(cannoninity-(.16*SW)*(Math.sin(Math.toRadians(45))));

		//Initial points for x and y
		xpts[3]=(int)(cannoninitx+(.04*SW)*(Math.sin(Math.toRadians(45))));
		xpts[2]=(int)(cannoninitx-(.04*SW)*(Math.sin(Math.toRadians(45))));

		ypts[3]=(int)(cannoninity-(.04*SW)*(Math.cos(Math.toRadians(45))));
		ypts[2]=(int)(cannoninity+(.04*SW)*(Math.cos(Math.toRadians(45))));

		xpts[0]=(int)(othercannonx+(.04*SW)*(Math.sin(Math.toRadians(45))));
		xpts[1]=(int)(othercannonx-(.04*SW)*(Math.sin(Math.toRadians(45))));

		ypts[0]=(int)(othercannony-(.04*SW)*(Math.cos(Math.toRadians(45))));
		ypts[1]=(int)(othercannony+(.04*SW)*(Math.cos(Math.toRadians(45))));

		//ball object
		ball = new Ball(sheet.getSize().width, sheet.getSize().height);

		//Event changes label
		statuslabel = new Label("Status: ");
		statuslabel.setSize(100,20);
		statuslabel.setVisible(false);
		statuslabel.setLocation((int)(.4*SW), (int)(.1*SH));
		sheet.add("Center", statuslabel);

		finallabel = new Label("Final");
		finallabel.setSize(100,20);
		finallabel.setVisible(false);
		finallabel.setLocation((int)(.41*SW), (int)(.18*SH));
		sheet.add("Center", finallabel);


		//adds ball to the sheet
		sheet.add("Center", ball);

		GridBagLayout displ = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		int colWidth[]={1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
		int rowHeight[]={1,1};
		double colWeight[]={1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
		double rowWeight[]={1,1,1};
		displ.rowHeights = rowHeight;
		displ.columnWidths = colWidth;
		displ.rowWeights = rowWeight;
		displ.columnWeights = colWeight;

		//control panel using width and height
		control.setBackground(Color.black);
		control.setVisible(true);
		control.setLayout(displ);
		control.setSize(SW, (int)(.3*SH));

		//center sheet
		f.add("Center", sheet);
		f.add("South", control);
		//mouse listeners
		ball.addMouseMotionListener(this);
		ball.addMouseListener(this);

		MB = new MenuBar();
            sizem = new Menu("Size");
        	sizesub = new Menu("Size");
        	sizesub.add(xsm = new CheckboxMenuItem ("X-Small"));
        	sizesub.add(sm = new CheckboxMenuItem ("Small"));
        	sizesub.add(med = new CheckboxMenuItem ("Medium"));
        	sizesub.add(lrg = new CheckboxMenuItem ("Large"));
        	sizesub.add(xl = new CheckboxMenuItem ("X-Large"));

	//Item listeners for size checkbox
        	xsm.addItemListener(this);
        	sm.addItemListener(this);
        	med.addItemListener(this);
        	lrg.addItemListener(this);
        	xl.addItemListener(this);
        	med.setState(true);

        	speedsub = new Menu("Speed");
        	speedsub.add(xf = new CheckboxMenuItem ("X-Fast"));
        	speedsub.add(fa = new CheckboxMenuItem ("Fast"));
        	speedsub.add(mod = new CheckboxMenuItem ("Moderate"));
        	speedsub.add(slow = new CheckboxMenuItem ("Slow"));
        	speedsub.add(xsl = new CheckboxMenuItem ("X-Slow"));

        	xf.addItemListener(this);
        	fa.addItemListener(this);
        	mod.addItemListener(this);
        	slow.addItemListener(this);
        	xsl.addItemListener(this);
        	fa.setState(true);

            controlm = new Menu("Control");
        	pause = controlm.add(new MenuItem("Pause", new MenuShortcut(KeyEvent.VK_P)));
        	run = controlm.add(new MenuItem("Start", new MenuShortcut(KeyEvent.VK_S)));
        	restartm = controlm.add(new MenuItem("Restart", new MenuShortcut(KeyEvent.VK_R)));
        	resetm = controlm.add(new MenuItem("Reset", new MenuShortcut(KeyEvent.VK_T)));
        	quit = controlm.add(new MenuItem("Quit", new MenuShortcut(KeyEvent.VK_Q)));

        	pause.addActionListener(this);
        	run.addActionListener(this);
        	quit.addActionListener(this);
        	restartm.addActionListener(this);
        	resetm.addActionListener(this);

		//Sets the gravity
        envi = new Menu("Environment");
        	envi.add(mercury = new CheckboxMenuItem ("mercuryury"));
        	envi.add(venus = new CheckboxMenuItem ("Venus"));
        	envi.add(earth = new CheckboxMenuItem ("Earth"));
        	envi.add(moon = new CheckboxMenuItem ("The Moon"));
        	envi.add(mars = new CheckboxMenuItem ("Mars"));
        	envi.add(jupiter = new CheckboxMenuItem ("Jupiter"));
        	envi.add(saturn = new CheckboxMenuItem ("Saturn"));
        	envi.add(uranus = new CheckboxMenuItem ("Uranus"));
        	envi.add(neptune = new CheckboxMenuItem ("Neptune"));
        	envi.add(pluto = new CheckboxMenuItem ("Pluto"));
        	mercury.addItemListener(this);
        	venus.addItemListener(this);
        	earth.addItemListener(this);
        	moon.addItemListener(this);
        	mars.addItemListener(this);
        	jupiter.addItemListener(this);
        	saturn.addItemListener(this);
        	uranus.addItemListener(this);
        	neptune.addItemListener(this);
        	pluto.addItemListener(this);
            sizem.add(sizesub);
            sizem.add(speedsub);

            MB.add(controlm);
            MB.add(sizem);
            MB.add(envi);
		f.setMenuBar(MB);

		//Score for computer and user
		scorelabel1 = new Label("Computer: ");
		scorelabel1.setForeground (Color.white);
		c.gridheight=1;
		c.gridwidth= 1;
		c.weightx=1;
		c.weighty=1;
		c.gridx = 2;
		c.gridy= 0;
		displ.setConstraints(scorelabel1, c);
		control.add(scorelabel1);

		scorelabel = new Label("User: ");
		scorelabel.setForeground (Color.white);
		c.gridheight=1;
		c.gridwidth= 1;
		c.weightx=1;
		c.weighty=1;
		c.gridx = 2;
		c.gridy= 1;
		displ.setConstraints(scorelabel, c);
		control.add(scorelabel);

		//output score
		compscore = new Label("0");
		compscore.setForeground (Color.white);
		c.gridheight=1;
		c.gridwidth= 1;
		c.weightx=1;
		c.weighty=1;
		c.gridx = 3;
		c.gridy= 0;
		displ.setConstraints(compscore, c);
		control.add(compscore);

		//User score
		userscore = new Label("0");
		userscore.setForeground (Color.white);
		c.gridheight=1;
		c.gridwidth= 1;
		c.weightx=1;
		c.weighty=1;
		c.gridx = 3;
		c.gridy= 1;
		displ.setConstraints(userscore, c);
		control.add(userscore);

		Label AngleL = new Label("Angle:");
		AngleL.setForeground (Color.white);
		c.gridheight=1;
		c.gridwidth= 1;
		c.weightx=1;
		c.weighty=1;
		c.gridx = 6;
		c.gridy= 1;
		displ.setConstraints(AngleL, c);
		control.add(AngleL);

		AngleSlider = new Scrollbar(Scrollbar.HORIZONTAL);
		AngleSlider.setMaximum(maxAngle);
		AngleSlider.setMinimum(minAngle);
		AngleSlider.setUnitIncrement(increment);
		AngleSlider.setBlockIncrement(block);
		AngleSlider.setValue(45);
		AngleSlider.setBackground(Color.gray);
		c.gridheight=1;
		c.gridwidth= 12;
		c.weightx=1;
		c.weighty=1;
		c.gridx = 8;
		c.gridy= 1;
		c.fill=GridBagConstraints.BOTH;
		displ.setConstraints(AngleSlider, c);
		control.add(AngleSlider);
		AngleSlider.addAdjustmentListener(this);

		Label speedL = new Label("Speed:");
		speedL.setForeground (Color.white);
		c.gridheight=1;
		c.gridwidth= 1;
		c.weightx=1;
		c.weighty=1;
		c.gridx = 6;
		c.gridy= 0;
		displ.setConstraints(speedL, c);
		control.add(speedL);

		SpeedSlider = new Scrollbar(Scrollbar.HORIZONTAL);
		SpeedSlider.setMaximum(MAXSPEED);
		SpeedSlider.setMinimum(MINSPEED);
		SpeedSlider.setUnitIncrement(increment);
		SpeedSlider.setBlockIncrement(block);
		SpeedSlider.setValue(defaultspeed);
		SpeedSlider.setBackground(Color.gray);
		c.gridheight=1;
		c.gridwidth= 12;
		c.weightx=1;
		c.weighty=1;
		c.gridx = 8;
		c.gridy= 0;
		c.fill=GridBagConstraints.BOTH;
		displ.setConstraints(SpeedSlider, c);
		control.add(SpeedSlider);
		SpeedSlider.addAdjustmentListener(this);

		//determining max object size for size slider
		if (SW <SH)
		{
			maxSpeed=(int)(SW*.6);
			minSpeed=(int)(SW*.025);
		}
		else
		{
			maxSpeed=(int)(SH*.6);
			minSpeed=(int)(SH*.025);
		}


		initWH = 5*minSpeed;
		f.addWindowListener(this);
		angle =45;
		vo =600;
		vox=(float)((Math.cos(Math.toRadians(angle)))*vo);
		voy=(float)((Math.sin(Math.toRadians(angle)))*vo);
		earth.setState(true);

		user=0;
		comp=0;
	}


	//repaint and move ball
	public void run()
	 {
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		while(more)
		{
			if (!time_pause && !pause2)
			 {
				ball.move();
				ball.repaint();
			}
			if (pause2)
			{
				statuslabel.setText("GAME OVER");
				statuslabel.setVisible(true);

				if (user > comp)
				{
					finallabel.setVisible(true);
					finallabel.setText("You WIN!");
				}

				if (comp > user)
				{
					finallabel.setVisible(true);
					finallabel.setText("You LOSE!");
				}

				if (comp == user)
				{
					finallabel.setVisible(true);
					finallabel.setText("TIE GAME!");
				}
			}
			try
			{
				Thread.sleep(time_delay);
			}
			catch(InterruptedException ex) {}
			stop();
			z++;
			if (shotflag)
				t++;
		}
	}

//repaint and set up thread
	public void start()
	 {
		if (thread1 == null)
		 {
			thread1 = new Thread(this);
			thread1.start();
			ball.repaint();
		}
	}
	public void stop()
	{
		this.dispose();
	}
	public void dispose(){}

	//cannon reset
	public void restart()
	{
		ball.BX = 1;
		ball.BY = 1;
		AngleSlider.setValue(45);
		SpeedSlider.setValue(600);
		angle =45;
		vo=600;
		cannoninitx = (int)(.96*SW);
		cannoninity = (int)(.944898*SH);

		int othercannonx=(int)(cannoninitx-(.16*SW)*(Math.cos(Math.toRadians(45))));
		int othercannony= (int)(cannoninity-(.16*SW)*(Math.sin(Math.toRadians(45))));

		xpts[3]=(int)(cannoninitx+(.04*SW)*(Math.sin(Math.toRadians(45))));
		xpts[2]=(int)(cannoninitx-(.04*SW)*(Math.sin(Math.toRadians(45))));

		ypts[3]=(int)(cannoninity-(.04*SW)*(Math.cos(Math.toRadians(45))));
		ypts[2]=(int)(cannoninity+(.04*SW)*(Math.cos(Math.toRadians(45))));

		xpts[0]=(int)(othercannonx+(.04*SW)*(Math.sin(Math.toRadians(45))));
		xpts[1]=(int)(othercannonx-(.04*SW)*(Math.sin(Math.toRadians(45))));

		ypts[0]=(int)(othercannony-(.04*SW)*(Math.cos(Math.toRadians(45))));
		ypts[1]=(int)(othercannony+(.04*SW)*(Math.cos(Math.toRadians(45))));

		ball.BX2= ball.W- ball.WB2-3;
		ball.BY2= ball.H- ball.HB2 -5;
		ball.bouncing_ball.setLocation(1, 1);
		ball.cannon_ball.setLocation(ball.BX2, ball.BY2);
		shotflag=false;
		shotflag2=false;
		t=0;
		ball.destroy=false;
		ball.ballDestroy=false;
		if (!ball.walls.isEmpty())
		{
			for (int i=0; i<ball.walls.size(); i++)
			{
					ball.walls.removeElementAt(i);
					i--;
			}
		}
	}

	//Cannon ball gets moved back to cannon
	public void restart2()
	{
		restart();
		user =0;
		comp=0;
		compscore.setText(Integer.toString(comp));
		userscore.setText(Integer.toString(user));
		pause2 = false;
		statuslabel.setVisible(false);
		finallabel.setVisible(false);
	}


	public void itemStateChanged(ItemEvent e)
	{
		Object s= e.getSource();
		if (s==xf)
		{
			time_delay = 1;
			fa.setState(false);
			mod.setState(false);
			slow.setState(false);
			xsl.setState(false);
		}

		if (s==fa)
		{
			time_delay = 3;
			xf.setState(false);
			mod.setState(false);
			slow.setState(false);
			xsl.setState(false);
		}

		if (s==mod)
		{
			time_delay = 5;
			fa.setState(false);
			xf.setState(false);
			slow.setState(false);
			xsl.setState(false);
		}

		if(s==slow)
		{
			time_delay = 10;
			fa.setState(false);
			mod.setState(false);
			xf.setState(false);
			xsl.setState(false);
		}

		if(s==xsl)
		{
			time_delay = 20;
			fa.setState(false);
			mod.setState(false);
			slow.setState(false);
			xf.setState(false);
		}

		if (s==xsm)
		{
			size_check=ball.SizeChange(minSpeed);
			if (size_check)
			{
				ball.changeSize(minSpeed);
				sm.setState(false);
				med.setState(false);
				lrg.setState(false);
				xl.setState(false);
			}
			else
				xsm.setState(false);
		}

		if (s==sm)
		{
			size_check=ball.SizeChange(((initWH+minSpeed)/2));
			if (size_check)
			 {
				ball.changeSize(((initWH+minSpeed)/2));
				xsm.setState(false);
				med.setState(false);
				lrg.setState(false);
				xl.setState(false);
			}
			 else
				sm.setState(false);
		}

		if (s==med)
		{
			size_check=ball.SizeChange(initWH);
			if (size_check)
			 {
				ball.changeSize(initWH);
				sm.setState(false);
				xsm.setState(false);
				lrg.setState(false);
				xl.setState(false);
			}
			else
				med.setState(false);
		}

		if(s==lrg)
		{
			size_check=ball.SizeChange(((initWH+maxSpeed)/2));
			if (size_check)
			 {
				ball.changeSize(((initWH+maxSpeed)/2));
				sm.setState(false);
				med.setState(false);
				xsm.setState(false);
				xl.setState(false);
			}
			else
				lrg.setState(false);
		}

		if(s==xl)
		{
			size_check=ball.SizeChange(maxSpeed);
			if (size_check)
			{
				ball.changeSize(maxSpeed);
				sm.setState(false);
				med.setState(false);
				lrg.setState(false);
				xsm.setState(false);
			}
			else
				xl.setState(false);
		}

		if (s==mercury)
		{
			gr = gmercury;
			venus.setState(false);
			earth.setState(false);
			moon.setState(false);
			mars.setState(false);
			jupiter.setState(false);
			saturn.setState(false);
			uranus.setState(false);
			neptune.setState(false);
			pluto.setState(false);
		}

		if (s==venus)
		{
			gr = gvenus;
			mercury.setState(false);
			earth.setState(false);
			moon.setState(false);
			mars.setState(false);
			jupiter.setState(false);
			saturn.setState(false);
			uranus.setState(false);
			neptune.setState(false);
			pluto.setState(false);
		}

		if (s==earth)
		{
			gr = gearth;
			venus.setState(false);
			mercury.setState(false);
			moon.setState(false);
			mars.setState(false);
			jupiter.setState(false);
			saturn.setState(false);
			uranus.setState(false);
			neptune.setState(false);
			pluto.setState(false);
		}

		if (s==moon)
		{
			gr = gmoon;
			venus.setState(false);
			earth.setState(false);
			mercury.setState(false);
			mars.setState(false);
			jupiter.setState(false);
			saturn.setState(false);
			uranus.setState(false);
			neptune.setState(false);
			pluto.setState(false);
		}

		if (s==mars)
		{
			gr = gmars;
			venus.setState(false);
			earth.setState(false);
			moon.setState(false);
			mercury.setState(false);
			jupiter.setState(false);
			saturn.setState(false);
			uranus.setState(false);
			neptune.setState(false);
			pluto.setState(false);
		}

		if (s==jupiter)
		{
			gr = gjupiter;
			venus.setState(false);
			earth.setState(false);
			moon.setState(false);
			mars.setState(false);
			mercury.setState(false);
			saturn.setState(false);
			uranus.setState(false);
			neptune.setState(false);
			pluto.setState(false);
		}

		if (s==saturn)
		{
			gr = gsaturn;
			venus.setState(false);
			earth.setState(false);
			moon.setState(false);
			mars.setState(false);
			jupiter.setState(false);
			mercury.setState(false);
			uranus.setState(false);
			neptune.setState(false);
			pluto.setState(false);
		}

		if (s==uranus)
		{
			gr = guranus;
			venus.setState(false);
			earth.setState(false);
			moon.setState(false);
			mars.setState(false);
			jupiter.setState(false);
			saturn.setState(false);
			mercury.setState(false);
			neptune.setState(false);
			pluto.setState(false);
		}

		if (s==neptune)
		{
			gr = gneptune;
			venus.setState(false);
			earth.setState(false);
			moon.setState(false);
			mars.setState(false);
			jupiter.setState(false);
			saturn.setState(false);
			uranus.setState(false);
			mercury.setState(false);
			pluto.setState(false);
		}

		if (s==pluto)
		{
			gr = gpluto;
			venus.setState(false);
			earth.setState(false);
			moon.setState(false);
			mars.setState(false);
			jupiter.setState(false);
			saturn.setState(false);
			uranus.setState(false);
			neptune.setState(false);
			mercury.setState(false);
		}
	}

	public void adjustmentValueChanged(AdjustmentEvent e)
	{
	//Angle scrollbar
	if (e.getSource() == AngleSlider)
	{
		//set angle to value of scrollbar
		angle = e.getValue();
		//create new cannon x and y points and reset cannon
		int othercannonx=(int)(cannoninitx-(.16*SW)*(Math.cos(Math.toRadians(angle))));
		int othercannony= (int)(cannoninity-(.16*SW)*(Math.sin(Math.toRadians(angle))));

		xpts[3]=(int)(cannoninitx+(.04*SW)*(Math.sin(Math.toRadians(angle))));
		xpts[2]=(int)(cannoninitx-(.04*SW)*(Math.sin(Math.toRadians(angle))));
		ypts[3]=(int)(cannoninity-(.04*SW)*(Math.cos(Math.toRadians(angle))));
		ypts[2]=(int)(cannoninity+(.04*SW)*(Math.cos(Math.toRadians(angle))));
		xpts[0]=(int)(othercannonx+(.04*SW)*(Math.sin(Math.toRadians(angle))));
		xpts[1]=(int)(othercannonx-(.04*SW)*(Math.sin(Math.toRadians(angle))));
		ypts[0]=(int)(othercannony-(.04*SW)*(Math.cos(Math.toRadians(angle))));
		ypts[1]=(int)(othercannony+(.04*SW)*(Math.cos(Math.toRadians(angle))));

		//calculate initial speeds based on the angle
		vox=(float)((Math.cos(Math.toRadians(angle))*vo)/100);
		voy=(float)((Math.sin(Math.toRadians(angle))*vo)/100);
	}
	else
	{
		vo = e.getValue();
		vox=(float)((Math.cos(Math.toRadians(angle))*vo)/100);
		voy=(float)((Math.sin(Math.toRadians(angle))*vo)/100);
	}
	}


	public void actionPerformed(ActionEvent e)
	{
		Object s = e.getSource();
		if(s == pause)
		{
			if (time_pause == false)
				time_pause = !time_pause;
				statuslabel.setVisible(true);
				statuslabel.setText("Game is Paused");
		}
		if(s == run)
		{
			if (time_pause == true) statuslabel.setVisible(false);
			time_pause = !time_pause;
		}

		if (s == quit)
	  {
			more = false;
		}
		if (s == restartm)restart();

		if (s== resetm)restart2();
	}

	public void mousePressed(MouseEvent e)
	{
		old_x=e.getX();
		old_y=e.getY();
	}

	public void mouseDragged(MouseEvent e)
	{
		new_x=old_x;
		new_y=old_y;
		x=e.getX();
		y=e.getY();
		drag = true;
	}

	public void mouseReleased(MouseEvent e)
	{
	if(old_x != e.getX())
	{
		drag = false;
		r = new Rectangle(Math.min(x,old_x),Math.min(y,old_y), Math.abs(x-old_x), Math.abs(y-old_y));
		//checks if rectangle is drawn completely covered by a previous rectangle
		for (int i=0; i<ball.walls.size(); i++)
		{
			if (ball.walls.elementAt(i).intersects(r))
			{
				if(r.getX()>=ball.walls.elementAt(i).getX()&&r.getY()>=ball.walls.elementAt(i).getY())
				{
					if((r.getX()+r.getWidth())<=(ball.walls.elementAt(i).getX()+ball.walls.elementAt(i).getWidth()) && (r.getY()+r.getHeight())<=(ball.walls.elementAt(i).getY()+ball.walls.elementAt(i).getHeight()))
					{
						cover_flag=true;
					}
				}
			}
		}
		if (r.intersects(ball.bouncing_ball) || r.intersects(ball.cannon_area)) cover_flag=true;
		if (r.getX()+r.getWidth() > SW || r.getY()+r.getHeight() > SH)
		{
			cover_flag=true;
		}
		//checks for covered rectangle
		if (!ball.walls.isEmpty() && !cover_flag)
		{
			for (int i=0; i<ball.walls.size(); i++)
			{
				if (ball.walls.elementAt(i).intersects(r))
				{
					if(r.getX()<=ball.walls.elementAt(i).getX()&&r.getY()<=ball.walls.elementAt(i).getY())
					{
						if((r.getX()+r.getWidth())>=(ball.walls.elementAt(i).getX()+ball.walls.elementAt(i).getWidth()) && (r.getY()+r.getHeight())>=(ball.walls.elementAt(i).getY()+ball.walls.elementAt(i).getHeight()))
						{
							ball.walls.removeElementAt(i);
							i--;
						}
					}
				}
			}
		}
		//adds if cover flag isnt true
		if (!cover_flag)
			ball.walls.addElement(r);
		cover_flag = false;
		ball.repaint();
	}
	}

	public void mouseClicked(MouseEvent e)
	{
			for (int i=0; i<ball.walls.size(); i++)
			{
				if (e.getX()>=ball.walls.elementAt(i).getX()&&e.getX()<=(ball.walls.elementAt(i).getX()+ball.walls.elementAt(i).getWidth()) && e.getY()>=ball.walls.elementAt(i).getY()&&e.getY()<=(ball.walls.elementAt(i).getY()+ball.walls.elementAt(i).getHeight()))
				{
					ball.walls.removeElementAt(i);
					i--;
				}
			}
			Polygon poly = new Polygon(xpts,ypts, npts);
			Point pt = new Point(e.getX(), e.getY());
			if (e.getButton() == MouseEvent.BUTTON3)
			{
				if (poly.contains(pt) || ball.cannon_circle.contains(e.getX(),e.getY())) shotflag = true;
			}
	}

	public void mouseMoved(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowClosing(WindowEvent e)
	{
		stop();
	}
	public void windowClosed(WindowEvent e)
	{
		stop();
	}


	public class Ball extends Canvas
	{
		Image buffer;
		Graphics g;
		public Vector<Rectangle> walls=new Vector<Rectangle>(100,20);
		int WB, HB, W, H;
		int PWB, PHB;
		int WB2,HB2;
		int BX2;
		int BY2;
		Rectangle bouncing_ball;
		Rectangle cannon_ball;
		Rectangle cannon_circle;
		Rectangle cannon_area;
		int BX = 1;
		int BY = 1;
		int dx = -1;
		int dy = -1;
		int dx2;
		Float dx3;
		Boolean destroy=false;
		Boolean ballDestroy=false;

		//creates the ball width and height
		public Ball(int W, int H)
		{
			this.W= W;
			this.H= H;
			WB = (int)(.125*H);
			HB = WB;
			WB2 = (int)(Math.sqrt(((xpts[0]-xpts[1])*(xpts[0]-xpts[1]))+((ypts[0]-ypts[1])*(ypts[0]-ypts[1])))-5);
			HB2 = WB2;
			BX2= W- WB2-3;
			BY2=H- HB2 -5;

			bouncing_ball = new Rectangle(BX, BY,WB,HB);
			cannon_ball = new Rectangle(BX2, BY2, WB2, HB2);

			cannon_area = new Rectangle((int)(W-WB2*2.89),(int)(H-HB2*2.89),(int)(WB2*2.89),(int)(HB2*2.89));
			cannon_circle = new Rectangle((int)(W-WB2*1.63),(int)(H-HB2*1.63),(int)(WB2*1.65),(int)(HB2*1.65));
		}

		public void paint(Graphics cg)
		{
			update(cg);
		}

		public void update(Graphics p)
		{
			buffer = createImage(getSize().width, getSize().height);
			if (g!=null)//contine drawing
			g.dispose();
			g=buffer.getGraphics();
			g.clearRect(0,0,getSize().width, getSize().height);
			//draws the rectangle that the mouse is dragging
			if (drag)
			{
				g.setColor(Color.black);
				g.clearRect(Math.min(x,new_x),Math.min(y,new_y), Math.abs(x-new_x), Math.abs(y-new_y));
				g.drawRect(Math.min(x,old_x),Math.min(y,old_y), Math.abs(x-old_x), Math.abs(y-old_y));
			}
			//draw bouncing ball as long as it is not destroyed
			if (!ballDestroy)
			{
				g.setColor(Color.green);
				g.drawOval(BX, BY,WB,HB);
				g.fillOval(BX, BY,WB,HB);
			}
			//draws the final rectangle to the screen once mouse is released and adds it to the vector
			if (!walls.isEmpty()) {
				for (int i=0; i<walls.size(); i++)
				{
					g.setColor(Color.black);
					g.drawRect((int)walls.elementAt(i).getX(),(int)walls.elementAt(i).getY(),(int)walls.elementAt(i).getWidth(),(int)walls.elementAt(i).getHeight());
					g.fillRect((int)walls.elementAt(i).getX(),(int)walls.elementAt(i).getY(),(int)walls.elementAt(i).getWidth(),(int)walls.elementAt(i).getHeight());
				}
			}

			//draws cannon ball if shot from cannon
			if(!destroy || shotflag || shotflag2)
			{
				g.setColor(Color.gray);
				g.drawOval(BX2,BY2,WB2,HB2);
				g.fillOval(BX2,BY2,WB2,HB2);
			}

			//draws cannon as long as its not destroyed
			if (!destroy)
			{
				g.setColor(Color.gray);
				g.fillPolygon(xpts,ypts,npts);
				//Arc - (initial x, initial y, width, height, initial angle, angle arc'd too)
				g.fillOval((int)(W-WB2*1.63),(int)(H-HB2*1.63),(int)(WB2*1.65),(int)(HB2*1.65));
				g.setColor(Color.white);
				g.drawOval((int)(W-WB2*1.63),(int)(H-HB2*1.63),(int)(WB2*1.65),(int)(HB2*1.65));
			}
			g.setColor(Color.gray);
			g.drawRect(0,0,SW,SH);
			p.drawImage(buffer,0,0,this);
		}

		public Boolean SizeChange(int r)
		{
			// checking if new size fits within the screen height
			if( BY+r >= H)
			{
				return false;
			}
			// checking if new size fits within the screen width
			if( BX+r >= W)
			{
				return false;
			}
			//checking if size fits within any rectangle horizontal border
			if (!walls.isEmpty())
			{
				for(int i=0; i<walls.size(); i++)
				{
					if ( bouncing_ball.intersects(walls.elementAt(i)))
					{
						return false;
					}
				}
			}
			return true;
		}
		public void changeSize(int r)
		{
			this.WB = r;
			this.HB = r;
			bouncing_ball.setSize(WB, HB);
		}

		public void move()
		{
			if (!ballDestroy)
			{
				BX += dx;
				BY += dy;
			}

			bouncing_ball.setLocation(BX,BY);

			if (shotflag && !shotflag2)
			{
				BX2= (int)((W- WB2-3)-((vo*Math.cos(Math.toRadians(angle))*t)/100));
				dx2 = (int)((vo*Math.cos(Math.toRadians(angle))*t)/100);
				dx2 = (int)((dx2/(W- WB2-3))/2);
				for (int i=0; i<=dx2; i++)
				{
					if (BX2 <= 0)
					 {
						BX2= -BX2;
					}
					if (BX2 +WB2 >= W)
					 {
						BX2= (W-WB2)-(BX2-W);
					}
				}
				//calculate Y
				BY2= (int)((H- HB2 -5)-((-.5*gr*t*t)+(vo*Math.sin(Math.toRadians(angle))*t))/100);
				//if ball 2 hits ground
				if (BY2 +HB2 >= H ){
					shotflag =false;
					shotflag2 = true;
					BY2=H-HB2;
				}
			cannon_ball.setLocation(BX2,BY2);
				//cannon ball 2 will destroy any barriers drawn by mouse
				if (!walls.isEmpty())
				{
					for (int i=0; i<walls.size(); i++)
					{
						if (walls.elementAt(i).intersects(cannon_ball))
						{
							walls.removeElementAt(i);
							i--;
						}
					}
				}
			}

			Rectangle intersect_rect;
			//changes direction of ball if side border is reached
			if ( (BX <= 0) || (BX+dx+ WB >= W) )
			{
				dx= -dx;
			}

			//changes direction of ball if top/bottom border is reached
			if ( (BY <= 0) || (BY+dy+ HB >= H) )
			{
				dy = -dy;
			}

			//change direction of ball
			if (!walls.isEmpty())
			{
				for(int i=0; i<walls.size(); i++)
				{
					if(bouncing_ball.intersects(walls.elementAt(i)))
					{
						intersect_rect = bouncing_ball.intersection(walls.elementAt(i));
						double temp = Double.valueOf(walls.elementAt(i).getX()+walls.elementAt(i).getWidth()-1);
						double temp2 = Double.valueOf(walls.elementAt(i).getY()+walls.elementAt(i).getHeight()-1);
						//if intersection at horizontal border ball changes direction
						if (intersect_rect.getX() == walls.elementAt(i).getX() || Double.compare(intersect_rect.getX(),temp) == 0)
						{
							if (intersect_rect.getY() >= walls.elementAt(i).getY() && intersect_rect.getY() <= (walls.elementAt(i).getY()+walls.elementAt(i).getHeight()))
							{
								dx=-dx;
							}
						}
						if (intersect_rect.getY() == walls.elementAt(i).getY() || Double.compare(intersect_rect.getY(),temp2) == 0)
						{
							if (intersect_rect.getY() >= walls.elementAt(i).getY()-1 && intersect_rect.getY() <= (walls.elementAt(i).getY()+walls.elementAt(i).getHeight()-1))
							{
								dy=-dy;
							}
						}
					}
				}

			//sets polygon object at cannon
			Polygon poly = new Polygon(xpts,ypts, npts);

			//if ball intersects cannon
			if ((bouncing_ball.intersects(cannon_circle) || poly.intersects(bouncing_ball) )&& !destroy)
			{
				//destroy cannon and increment comp score
				destroy =true;
				comp++;
				//game plays until a score of 10
				if (comp == 10)
				{
					pause2 = true;
				}
				//set score label's text and restart game
				compscore.setText(Integer.toString(comp));
				restart();
			}
			//if ball interesects cannon ball
			if (bouncing_ball.intersects(cannon_ball) && shotflag && !ballDestroy)
			 {
				//destroy cannon and increment user score
				ballDestroy =true;
				user++;
				//game plays until a score of 10
				if (user == 10)
				{
					pause2 = true;
				}
				userscore.setText(Integer.toString(user));
				//set score label's text and restart game
				restart();
			}
		}
	}
}
}
