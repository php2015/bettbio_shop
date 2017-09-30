package com.test.mode;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bettbio.core.common.constant.Constants;
import com.bettbio.core.common.web.utils.CodeUtils;
import com.bettbio.core.model.SUserPoints;
import com.bettbio.core.service.SUserPointsService;
import com.test.mode.base.BaseT;

public class UserProintsTest extends BaseT {
	@Autowired
	SUserPointsService sUserPointsService;

	@Test
	public void save() {
		SUserPoints userPoints = new SUserPoints();
		userPoints.setCode(CodeUtils.getCode());
		userPoints.setPoints(Constants.DEFAULT_REGISTER_POINTS);
		userPoints.setPointsType(Constants.POINTS_REGISTER);
		sUserPointsService.save(userPoints);
		System.out.println("添加成功");
	}
}
