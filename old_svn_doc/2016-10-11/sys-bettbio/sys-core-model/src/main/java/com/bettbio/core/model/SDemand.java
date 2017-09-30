package com.bettbio.core.model;

import javax.persistence.*;

@Table(name = "S_DEMAND")
public class SDemand {
    /**
     * 编号
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;



    /**
     * 用户手机
     */
    @Column(name = "USER_PHONE")
    private Integer userPhone;

    

	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public Integer getUserPhone() {
		return userPhone;
	}



	public void setUserPhone(Integer userPhone) {
		this.userPhone = userPhone;
	}
    
    
}