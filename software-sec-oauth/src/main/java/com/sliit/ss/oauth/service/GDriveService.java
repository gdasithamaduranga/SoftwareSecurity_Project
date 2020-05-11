package com.sliit.ss.oauth.service;

import org.springframework.web.multipart.MultipartFile;

public interface GDriveService {

	public void doFileUpload(MultipartFile multipartFile) throws Exception;
}
