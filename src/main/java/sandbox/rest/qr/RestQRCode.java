package sandbox.rest.qr;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * Generate and Read QR code
 *
 */
@Path("qr")
public class RestQRCode {
	private static Logger logger = LoggerFactory.getLogger(RestQRCode.class);

	public static void main(String[] args) {
		try {
			getQRCodeImage("test");
		} catch (WriterException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	@POST
	@Path("download")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadQrFile(@FormParam("data") String text) {
		StreamingOutput fileStream = new StreamingOutput() {
			@Override
			public void write(java.io.OutputStream output) throws IOException, WebApplicationException {
				try {
					byte[] data = getQRCodeImage(text);
					output.write(data);
					output.flush();
				} catch (Exception e) {
					throw new WebApplicationException("File Not Found !!");
				}
			}
		};
		return Response.ok(fileStream, MediaType.APPLICATION_OCTET_STREAM)
				.header("content-disposition", "attachment; filename=test.png").build();
	}

	@POST
	@Path("base64")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
	public String QrToBase64(@FormParam("data") String text) {
		try {
			byte[] data = getQRCodeImage(text);
			return Base64.getEncoder().encodeToString(data).toString();
		} catch (Exception e) {
			throw new WebApplicationException("File Not Found !!");
		}
	}

	// Function to create the QR code
	public static byte[] getQRCodeImage(@PathParam("data") String text) throws WriterException, IOException {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();

		int width = 250;
		int height = 250;

		logger.info(text);

		BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

		ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

		byte[] pngData = pngOutputStream.toByteArray();
		return pngData;

	}

}
