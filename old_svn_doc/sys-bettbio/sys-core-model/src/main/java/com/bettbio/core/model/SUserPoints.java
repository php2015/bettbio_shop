package com.bettbio.core.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.bettbio.core.common.constant.Constants;

@Table(name = "S_USER_POINTS")
public class SUserPoints {
	/**
	 * 主键
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 积分类型
	 */
	@Column(name = "POINTS_TYPE")
	private Integer pointsType;

	/**
	 * 积分
	 */
	@Column(name = "POINTS")
	private Integer points;

	/**
	 * 创建时间
	 */
	@Column(name = "CREATE_DATE")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createDate = new Date();

	/**
	 * 用户编号
	 */
	@Column(name = "CODE")
	private String code;

	/**
	 * 状态
	 */
	@Column(name = "STATE")
	private Integer state;

	/**
	 * 获取用户积分类型
	 * 
	 * @return
	 */
	public String getPointsTypeString() {
		switch (pointsType) {
		case Constants.POINTS_REGISTER:
			return "注册积分";
		case Constants.POINTS_ORDER:
			return "订单积分";
		case Constants.POINTS_FIRST_ORDER:
			return "首单积分";
		default:
			return "订单积分";
		}
	}

	/**
	 * 获取积分状态
	 * @return
	 */
	public String getPointsState() {
		return state == 1 ? "已经到账" : "未到账";
	}

	/**
	 * 获取主键
	 *
	 * @return ID - 主键
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 设置主键
	 *
	 * @param id
	 *            主键
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 获取积分类型
	 *
	 * @return POINTS_TYPE - 积分类型
	 */
	public Integer getPointsType() {
		return pointsType;
	}

	/**
	 * 设置积分类型
	 *
	 * @param pointsType
	 *            积分类型
	 */
	public void setPointsType(Integer pointsType) {
		this.pointsType = pointsType;
	}

	/**
	 * 获取积分
	 *
	 * @return POINTS - 积分
	 */
	public Integer getPoints() {
		return points;
	}

	/**
	 * 设置积分
	 *
	 * @param points
	 *            积分
	 */
	public void setPoints(Integer points) {
		this.points = points;
	}

	/**
	 * 获取创建时间
	 *
	 * @return CREATE_DATE - 创建时间
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * 设置创建时间
	 *
	 * @param createDate
	 *            创建时间
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * 获取用户编号
	 *
	 * @return CODE - 用户编号
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置用户编号
	 *
	 * @param code
	 *            用户编号
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 获取状态
	 *
	 * @return STATE - 状态
	 */
	public Integer getState() {
		return state;
	}

	/**
	 * 设置状态
	 *
	 * @param state
	 *            状态
	 */
	public void setState(Integer state) {
		this.state = state;
	}

}