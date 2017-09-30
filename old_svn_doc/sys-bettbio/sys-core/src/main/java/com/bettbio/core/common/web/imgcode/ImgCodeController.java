package com.bettbio.core.common.web.imgcode;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bettbio.core.common.utils.ImgCodeUtil;
import com.bettbio.core.common.web.validate.ValidateResponse;

@Controller
@RequestMapping("imgcode-validate")
public class ImgCodeController {

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public Object jqueryValidationEngineValidate(HttpServletRequest request,
			@RequestParam(value = "fieldId", required = false) String fieldId,
			@RequestParam(value = "fieldValue", required = false) String fieldValue) {

		ValidateResponse response = ValidateResponse.newInstance();

		if (ImgCodeUtil.validate(request, fieldValue) == false) {
			response.validateFail(fieldId, "验证码验证失败!");
		} else {
			response.validateSuccess(fieldId, "验证码验证成功!");
		}

		return response.result();
	}
}
