package cn.com.infosec.qrcode.controller;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.infosec.qrcode.utils.QRCodeUtils;

@Controller
public class QRCodeController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final String USERNAME = "root";
	private final String PASSWORD = "11111111";
	private int UUID = 0;

	@RequestMapping("/")
	public String indexController(ModelMap modelMap) {

		logger.info("qrcode: /WEB-INF/jsp/qrcode.jsp");
		return "qrcode";
	}

	@RequestMapping("main.do")
	public String mainController(ModelMap modelMap) {

		logger.info("main: /WEB-INF/jsp/main.jsp");
		return "main";
	}

	@RequestMapping("code.do")
	public void codeController(HttpServletResponse response, ModelMap modelMap) throws Exception {

		int min = 100000;
		int max = 999999;
		int uuid = (int) (min + Math.random() * (max - min + 1));

		QRCodeUtils code = new QRCodeUtils();

		response.setContentType("image/png");
		OutputStream out = response.getOutputStream();

		code.createQRCode("https://github.com/lijk8090?uuid=" + uuid, 256, 256, out);

		out.flush();
		out.close();
	}

	@ResponseBody
	@RequestMapping("qrcode.do")
	public Map<String, Object> qrcodeController(HttpServletResponse response, ModelMap modelMap) throws Exception {

		int min = 100000;
		int max = 999999;
		UUID = (int) (min + Math.random() * (max - min + 1));
		System.out.println(UUID);

		QRCodeUtils qrcodeUtils = new QRCodeUtils();
		byte[] out = qrcodeUtils.createQRCodeWithOverlay("https://github.com/lijk8090?uuid=" + UUID, 256, 256,
				"src/main/resources/static/img/lijk.jpg");

		String type = "data:image/png;base64,";
		String base64 = Base64.encodeBase64String(out);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("type", type);
		map.put("base64", base64);

		System.out.println(map);
		return map;
	}

	@ResponseBody
	@RequestMapping("appLogin.do")
	public Map<String, Object> appLoginController(@RequestParam(value = "username") String username,
			@RequestParam(value = "password") String password, @RequestParam(value = "uuid") int uuid,
			HttpSession session) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();

		if (USERNAME.equals(username) == true && PASSWORD.equals(password) && UUID == uuid) {
			session.setAttribute("uuid", uuid);
			map.put("isLogin", "true");
		} else {
			session.removeAttribute("uuid");
			map.put("isLogin", "false");
		}

		System.out.println(map);
		return map;
	}

	@ResponseBody
	@RequestMapping("webLogin.do")
	public Map<String, Object> webLoginController(@RequestParam(value = "webUUID") int webUUID, HttpSession session)
			throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		int uuid = (int) session.getAttribute("uuid");

		if (uuid == webUUID) {
			map.put("isLogin", "true");
		} else {
			map.put("isLogin", "false");
		}

		System.out.println(map);
		return map;
	}

}
