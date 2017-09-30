package com.bettbio.core.enums;

/**
 * 禁用枚举类型定义
 * @author GuoChunbo
 *
 */
public enum Disable {
	//0-禁用 1-正常
	DISABLE(0), UN_DISABLE(1);

	private int val;

	private Disable(int val) {
		this.val = val;
	}
	
	public int getVal() {
		return val;
	}

	public void setVal(int val) {
		this.val = val;
	}

	public static void main(String[] args) {
		System.out.println(Disable.DISABLE.getVal());
	}
}
