package cn.com.infosec.qrcode.controller;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.infosec.qrcode.utils.QRCodeUtils;

@Controller
public class QRCodeController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/")
	public String indexController(ModelMap modelMap) {

		logger.info("qrcode: /WEB-INF/jsp/qrcode.jsp");
		return "qrcode";
	}

	@RequestMapping("code.do")
	public void codeController(HttpServletResponse response, ModelMap modelMap) throws Exception {

		int min = 100000;
		int max = 999999;
		int random = (int) (min + Math.random() * (max - min + 1));

		QRCodeUtils code = new QRCodeUtils();

		response.setContentType("image/png");
		OutputStream out = response.getOutputStream();

		code.createQRCode("https://github.com/lijk8090?random=" + random, 256, 256, out);

		out.flush();
		out.close();
	}

	@ResponseBody
	@RequestMapping("qrcode.do")
	public Map<String, Object> qrcodeController(HttpServletResponse response, ModelMap modelMap) throws Exception {

		int min = 100000;
		int max = 999999;
		int random = (int) (min + Math.random() * (max - min + 1));

		QRCodeUtils qrcodeUtils = new QRCodeUtils();
		byte[] out = qrcodeUtils.createQRCodeWithOverlay("https://github.com/lijk8090?random=" + random, 256, 256,
				"src/main/resources/static/img/lijk.jpg");

		String type = "data:image/png;base64,";
		String base64 = Base64.encodeBase64String(out);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("qrcode", type + base64);

		System.out.println(map);
		return map;
	}
}
