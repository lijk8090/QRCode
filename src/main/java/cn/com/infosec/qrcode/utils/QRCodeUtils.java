package cn.com.infosec.qrcode.utils;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCodeUtils {

	public boolean createQRCode(String contents, int width, int height, OutputStream out) throws Exception {

		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.MARGIN, 1);

		MultiFormatWriter writer = new MultiFormatWriter();
		BitMatrix matrix = writer.encode(contents, BarcodeFormat.QR_CODE, width, height, hints);

		MatrixToImageWriter.writeToStream(matrix, "PNG", out);
		return true;
	}

	public byte[] createQRCodeWithOverlay(String contents, int width, int height, String logo) throws Exception {

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.MARGIN, 1);

		MultiFormatWriter writer = new MultiFormatWriter();
		BitMatrix matrix = writer.encode(contents, BarcodeFormat.QR_CODE, width, height, hints);
		BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);

		BufferedImage overlay = ImageIO.read(new File(logo));
		int deltaWidth = width - overlay.getWidth();
		int deltaHeight = height - overlay.getHeight();

		BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graph = (Graphics2D) combined.getGraphics();

		graph.drawImage(image, 0, 0, null);
		graph.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		graph.drawImage(overlay, Math.round(deltaWidth / 2), Math.round(deltaHeight / 2), null);

		graph.dispose();
		combined.flush();

		ImageIO.write(combined, "PNG", out);
		return out.toByteArray();
	}
}
