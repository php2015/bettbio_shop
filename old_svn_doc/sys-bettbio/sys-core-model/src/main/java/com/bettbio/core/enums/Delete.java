package com.bettbio.core.enums;

/**
 * 删除类型枚举定义
 * @author GuoChunbo
 *
 */
public enum Delete {
	// 0-删除 1-正常
	DELETE(0), UN_DELETE(1);

	private int val;

	private Delete(int val) {
		this.val = val;
	}

	public int getVal() {
		return val;
	}

	public void setVal(int val) {
		this.val = val;
	}
}
