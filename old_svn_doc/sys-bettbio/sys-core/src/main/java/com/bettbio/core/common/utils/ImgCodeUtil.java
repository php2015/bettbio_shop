package com.bettbio.core.common.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import com.bettbio.core.common.constant.Constants;

public class ImgCodeUtil {
	
	
	public static Boolean validate(HttpServletRequest request,String fieldValue){
		return fieldValue.equalsIgnoreCase(request.getSession().getAttribute(Constants.IMG_CODE).toString());
	}
	
	public static BufferedImage getImgCode(HttpServletRequest request) {
		int width = 90;
		int height = 25;
		int codeCount = 4;
		int xx = 15;
		int fontHeight = 20;
		int codeY = 20;
		char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
				'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);

		Graphics gd = buffImg.getGraphics();

		Random random = new Random();

		gd.setColor(Color.WHITE);
		gd.fillRect(0, 0, width, height);

		Font font = new Font("Times New Roman", Font.ROMAN_BASELINE, 18);
		gd.setFont(font);

		// gd.setColor(Color.BLACK);
		gd.drawRect(0, 0, width - 1, height - 1);

		gd.setColor(Color.BLACK);
		for (int i = 0; i < 20; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			gd.drawLine(x, y, x + xl, y + yl);
		}
		StringBuffer randomCode = new StringBuffer();
		int red = 0, green = 0, blue = 0;

		for (int i = 0; i < codeCount; i++) {

			String code = String.valueOf(codeSequence[random.nextInt(36)]);
			red = random.nextInt(255);
			green = random.nextInt(255);
			blue = random.nextInt(255);

			gd.setColor(new Color(red, green, blue));
			gd.drawString(code, (i + 1) * xx, codeY);

			randomCode.append(code);
		}
		request.getSession().setAttribute(Constants.IMG_CODE, randomCode.toString());
		return buffImg;
	}
}
