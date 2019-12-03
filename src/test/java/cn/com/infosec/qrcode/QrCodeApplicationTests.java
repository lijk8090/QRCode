package cn.com.infosec.qrcode;

import java.io.FileOutputStream;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import cn.com.infosec.qrcode.utils.QRCodeUtils;

@SpringBootTest
class QrCodeApplicationTests {

	@Test
	void testQRCode() throws Exception {

		QRCodeUtils code = new QRCodeUtils();
		code.createQRCode("https://github.com/lijk8090", 256, 256, new FileOutputStream("lijk.png"));
	}

	@Test
	void testQRCodeWithOverlay() throws Exception {

		QRCodeUtils code = new QRCodeUtils();
		byte[] out = code.createQRCodeWithOverlay("http://www.scctc.org.cn/", 256, 256,
				"src/main/resources/static/img/logo.png");

		FileOutputStream file = new FileOutputStream("scctc.png");
		file.write(out);
		file.close();

	}

}
