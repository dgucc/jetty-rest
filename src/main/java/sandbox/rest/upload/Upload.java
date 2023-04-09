package sandbox.rest.upload;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("upload")
public class Upload {

	private static Logger logger = LoggerFactory.getLogger(Upload.class.getName());
			
	@POST
	@Path("csv")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
	public String uploadFile(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileMetaData) {
		
		try {
			BufferedInputStream inputStream = new BufferedInputStream(uploadedInputStream);
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) > 0) {
				byteArray.write(buffer, 0, length);
			}

			logger.info(byteArray.toString("UTF-8"));
			return byteArray.toString("UTF-8");

		} catch (IOException e) {
			System.out.println(e.getMessage());
			logger.error(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			logger.error(e.getMessage());
		}
		return "Oups... something wrong...";
	}
}
