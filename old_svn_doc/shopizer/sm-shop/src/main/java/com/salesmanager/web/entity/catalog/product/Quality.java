package com.salesmanager.web.entity.catalog.product;

import java.io.Serializable;

public class Quality implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8574729470755070627L;
	private int score = 0;
	private int doc =0;
	private int proof =0;
	private int third =0;
	private int self =0;
	private boolean isFree=true;
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getDoc() {
		return doc;
	}
	public void setDoc(int doc) {
		this.doc = doc;
	}
	public int getProof() {
		return proof;
	}
	public void setProof(int proof) {
		this.proof = proof;
	}
	public int getThird() {
		return third;
	}
	public void setThird(int third) {
		this.third = third;
	}
	public int getSelf() {
		return self;
	}
	public void setSelf(int self) {
		this.self = self;
	}
	public boolean isFree() {
		return isFree;
	}
	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}

}
