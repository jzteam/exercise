package cn.jzteam.tools.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public class MyMultipartFile implements MultipartFile{
	
	private File file;
	
	public MyMultipartFile(File file) {
		this.file = file;
	}

	@Override
	public String getName() {
		return file.getName();
	}

	@Override
	public String getOriginalFilename() {
		return file.getName();
	}

	@Override
	public String getContentType() {
		return "multipart/form-data";
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public long getSize() {
		return file.length();
	}

	@Override
	public byte[] getBytes() throws IOException {
		FileInputStream fis = new FileInputStream(file);
		int available = fis.available();
		byte[] buf = new byte[available];
		fis.read(buf);
		fis.close();
		
		return buf;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new FileInputStream(file);
	}

	@Override
	public void transferTo(File dest) throws IOException, IllegalStateException {
		FileOutputStream fos = new FileOutputStream(dest);
		FileInputStream fis = new FileInputStream(file);
		byte[] buf = new byte[1024];
		int len = 0;
		while((len = fis.read(buf)) != -1){
			fos.write(buf, 0, len);
		}
		fos.close();
		fis.close();
	}

}
