package io.github.jeremysher.projects;

import java.util.ArrayList;

import io.github.jeremysher.vector.Vector;

public class Ball {
	private Vector displacement;
	private Vector velocity;
	private double mass;
	private double radius;
	private static ArrayList<Ball> balls = new ArrayList<Ball>();
	private static final Vector DEFAULT_VELOCITY = new Vector(0, 0);
	private static final double DEFAULT_MASS = 1.0;
	private static final double DEFAULT_RADIUS = 20.0;
	private static final double DEFAULT_DENSITY = 3.0 / 4.0 * DEFAULT_MASS / (Math.PI * Math.pow(DEFAULT_RADIUS, 3));
	
	public Ball(Vector d) {
		displacement = d;
		velocity = DEFAULT_VELOCITY;
		mass = DEFAULT_MASS;
		radius = DEFAULT_RADIUS;
		balls.add(this);
	}
	
	public Ball(Vector d, Vector v) {
		displacement = d;
		velocity = v;
		mass = DEFAULT_MASS;
		radius = DEFAULT_RADIUS;
		balls.add(this);
	}
	
	public Ball(Vector d, Vector v, double r) {
		displacement = d;
		velocity = v;
		radius = r;
		mass = DEFAULT_DENSITY * 4.0 / 3.0 * Math.PI * Math.pow(r, 3);
		balls.add(this);
	}
	
	public Ball(Vector d, Vector v, double r, double m) {
		displacement = d;
		velocity = v;
		radius = r;
		mass = m;
		balls.add(this);
	}
	
	public void move(double dt) {
		displacement = Vector.sum(displacement, velocity.scale(dt));
	}
	
	public void applyAcceleration(Vector a, double dt) {
		velocity = Vector.sum(velocity, a.scale(dt));
	}
	
	public void applyForce(Vector f, double dt) {
		Vector acceleration = f.scale(1 / mass);
		applyAcceleration(acceleration, dt);
	}
	
	public static ArrayList<Ball> getBalls() {
		return balls;
	}
	
	public Vector getDisplacement() {
		return displacement;
	}
	
	public Vector getVelocity() {
		return velocity;
	}
	
	public double getMass() {
		return mass;
	}
	
	public double getRadius() {
		return radius;
	}
	
	public void setDisplacement(Vector d) {
		displacement = d;
	}
	
	public void setVelocity(Vector v) {
		velocity = v;
	}
	
	public void setDisplacement(double x, double y) {
		displacement = new Vector(x, y);
	}
	
	public void setVelocity(double vx, double vy) {
		velocity = new Vector(vx, vy);
	}
	
	public void setMass(double m) {
		mass = m;
	}
	
	public void setRadius(double r) {
		radius = r;
	}
	
}
