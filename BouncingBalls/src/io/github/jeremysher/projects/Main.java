package io.github.jeremysher.projects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import io.github.jeremysher.vector.Vector;
import io.github.jeremysher.animation.Animation;

public class Main {

	@SuppressWarnings("serial")
	public static void main(String[] args) {
		System.out.println("starting");
		Animation a = new Animation(600, 500, "Bouncing Balls") {
			
			int infoSpace = 50;
			
			//use addBall() method if uncommenting
			/*Ball b1 = new Ball(new Vector(100, 400), new Vector(100, 0), 30);
			Ball b2 = new Ball(new Vector(400, 300), new Vector(-100, 1), 15);
			Ball b3 = new Ball(new Vector(120, 390), new Vector(100, 300), 25);
			Ball b4 = new Ball(new Vector(400, 380), new Vector(-300, -100));*/
			
			Vector a = new Vector(0, -300); //universal acceleration
			
			int leftBound = 0;
			int rightBound = this.getSize().width;
			int lowerBound = infoSpace;
			int upperBound = this.getSize().height;
			
			double mass = 1.0;
			double radius = 20.0;
			
			double ke = 0;
			double pe = 0;
			
			Vector mousePress = new Vector();
			char keyPress = 0;
			
			boolean defaultMass = true;
			
			
			
			ArrayList<Ball> toAdd = new ArrayList<Ball>();

			@Override
			public void init() {
				
			}
		
			@Override
			public void run() {
				
				ke = 0;
				pe = 0;
				Ball.addBall(toAdd);
				toAdd.clear();
				
				for (Ball ball : Ball.getBalls()) {
					double dt = this.getDeltaTime();
					for (Ball ball2 : Ball.getBalls()) {
						if (ball2 != ball) {
							Vector d1 = ball.getDisplacement();
							Vector d2 = ball2.getDisplacement();
							Vector u = Vector.makeUnitVector(d1, d2); //unit vector from ball 1 to 2
							double v1 = Vector.dot(ball.getVelocity(), u); //velocity in the u direction
							double v2 = Vector.dot(ball2.getVelocity(), u);
							if (Vector.distance(d1, d2) <= ball.getRadius() + ball2.getRadius() && v1 > v2) {
								
								//force method
								/*double mag = -9999999 / Math.pow(Vector.distance(d1, d2), 2);
								ball.applyForce(u.scale(mag), dt);*/
								
								//derived velocity method
								double m1 = ball.getMass();
								double m2 = ball2.getMass();
								
								double vfmag1 = (m1 * v1 - m2 * v1 + 2 * m2 * v2) / (m1 + m2);
								double vfmag2 = (m2 * v2 - m1 * v2 + 2 * m1 * v1) / (m1 + m2);
								
								Vector vf1 = Vector.sum(u.scale(vfmag1), Vector.sum(ball.getVelocity(), u.scale(-v1)));
								Vector vf2 = Vector.sum(u.scale(vfmag2), Vector.sum(ball2.getVelocity(), u.scale(-v2)));
								
								ball.setVelocity(vf1);
								ball2.setVelocity(vf2);
							}
						}
					}
				}
				for (Ball ball : Ball.getBalls()) {
					Vector d = ball.getDisplacement();
					double x = d.getComponent(0);
					double y = d.getComponent(1);
					Vector v = ball.getVelocity();
					double vx = v.getComponent(0);
					double vy = v.getComponent(1);
					double r = ball.getRadius();
					
					double dt = this.getDeltaTime();
					ball.move(dt);
					ball.applyAcceleration(a, dt);
					
					if (x - r <= leftBound) {
						ball.setVelocity(Math.abs(vx) * 0.99, vy * 0.995);
					} else if (x + r >= rightBound) {
						ball.setVelocity(-Math.abs(vx) * 0.99, vy * 0.995);
					}
					if (y - r <= lowerBound) {
						ball.setVelocity(vx * 0.995, Math.abs(vy) * 0.99);
					} else if (y + r >= upperBound) {
						ball.setVelocity(vx * 0.995, -Math.abs(vy) * 0.99);
					}
					
					
					
					ke += 0.5 * ball.getMass() * Math.pow(ball.getVelocity().getMagnitude(), 2);
					pe += ball.getMass() * a.getMagnitude() * ball.getDisplacement().getComponent(1);
					
				}
			}
			
			@Override
			public void draw(Graphics2D g) {
				g.setColor(Color.red);
				for (Ball ball : Ball.getBalls()) {
					int r = (int)ball.getRadius();
					int x = (int)ball.getDisplacement().getComponent(0) - r;
					int y = (int)(this.getSize().height - ball.getDisplacement().getComponent(1)) - r;
					g.fillOval(x, y, 2 * r, 2 * r);
					
				}
				g.drawLine(leftBound, realY(lowerBound), rightBound, realY(lowerBound));
				
				g.drawString("mass: " + (defaultMass ? "default" : mass), 10, realY(lowerBound - 25));
				g.drawString("num balls: " + Ball.getBalls().size(), 10, realY(lowerBound - 15));
				g.drawString("radius: " + radius, 10, realY(lowerBound - 35));
			}
			
			
			@Override
			public void mousePressed(MouseEvent e) {
				mousePress = new Vector(e.getX(), realY(e.getY()));
				
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				int x = e.getX();
				int y = realY(e.getY());
				if (inBounds(new Ball(mousePress, radius))) {
					Vector mouseRelease = new Vector(x, y);
					Vector velocity = Vector.sum(mousePress, mouseRelease.scale(-1));
					//cant add ball directly to list because it may break code
					if (defaultMass)
						toAdd.add(new Ball(mousePress, velocity, radius));
					else
						toAdd.add(new Ball(mousePress, velocity, radius, mass));
				}
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				char key = e.getKeyChar();
				if (Character.isLetter(key) && key != 'd') keyPress = key;
				switch (e.getKeyCode()) {
				case 38:
					addToVariable(1);
					break;
				case 40:
					addToVariable(-1);
					break;
				case 68:
					defaultMass = !defaultMass;
					break;
				}
			}
			
			private void addToVariable(double num) {
				switch (keyPress) {
				case 'm':
					if (mass + num > 0) mass += num;
					break;
				case 'r':
					if (radius + num > 0) radius += num;
					break;
				}
			}
			
			private boolean inBounds(Ball ball) {
				Vector d = ball.getDisplacement();
				double x = d.getComponent(0);
				double y = d.getComponent(1);
				double r = ball.getRadius();
				
				return x - r >= leftBound
						&& x + r <= rightBound
						&& y - r >= lowerBound
						&& y + r <= upperBound;
				
			}
			
			private int realY(int y) {
				return this.getSize().height - y;
			}
			
		};
		a.start();
	}

}
