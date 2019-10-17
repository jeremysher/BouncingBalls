package io.github.jeremysher.projects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import io.github.jeremysher.vector.Vector;
import io.github.jeremysher.animation.Animation;

public class Main {

	@SuppressWarnings("serial")
	public static void main(String[] args) {
		System.out.println("starting");
		Animation a = new Animation(600, 500) {
			
			Ball b1 = new Ball(new Vector(100, 400), new Vector(100, 0), 30);
			Ball b2 = new Ball(new Vector(400, 300), new Vector(-100, 1), 15);
			Ball b3 = new Ball(new Vector(120, 390), new Vector(100, 300), 25);
			Ball b4 = new Ball(new Vector(400, 380), new Vector(-300, -100));
			
			Vector a = new Vector(0, -300); //universal acceleration
			
			int leftBound = 0;
			int rightBound = this.getSize().width;
			int lowerBound = 0;
			int upperBound = this.getSize().height;

		
			@Override
			public void run() {
				double ke = 0;
				double pe = 0;
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
				//System.out.println("Total energy:" + (ke + pe));
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
			}
			
		};
		a.start();
	}

}
