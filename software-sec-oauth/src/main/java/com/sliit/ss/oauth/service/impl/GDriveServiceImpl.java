package com.sliit.ss.oauth.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.sliit.ss.oauth.constant.AppConstant;
import com.sliit.ss.oauth.service.OauthAuthorizeService;
import com.sliit.ss.oauth.service.GDriveService;
import com.sliit.ss.oauth.util.OauthConfig;

@Service
public class GDriveServiceImpl implements GDriveService {

	private Logger logger = LoggerFactory.getLogger(GDriveServiceImpl.class);

	private Drive driveService;

	@Autowired
	OauthAuthorizeService authorizationService;

	@Autowired
	OauthConfig applicationConfig;

	@Override
	public void doFileUpload(MultipartFile multipartFile) throws Exception {
		
		Credential credential = authorizationService.getCredentials();
		//logger.debug("Dasitha test..."+credential.toString());
		driveService = new Drive.Builder(AppConstant.HTTP_TRANSPORT, AppConstant.JSON_FACTORY, credential)
				.setApplicationName(AppConstant.APPLICATION_NAME).build();
		
		logger.debug("Inside Upload Service1...");

		String path = applicationConfig.getTemporaryFolder();
		String fileName = multipartFile.getOriginalFilename();
		String contentType = multipartFile.getContentType();
		logger.debug("Inside Upload Service2...");
		java.io.File transferedFile = new java.io.File(path, fileName);
		multipartFile.transferTo(transferedFile);

		File fileMetadata = new File();
		fileMetadata.setName(fileName);
		logger.debug("Inside Upload Service3 file name..."+fileName);
		logger.debug("Inside Upload Service3 file path..."+path);
		FileContent mediaContent = new FileContent(contentType, transferedFile);
		File file = driveService.files().create(fileMetadata, mediaContent).setFields("id").execute();

        logger.debug("File extension: " + file.getFileExtension() +file.getCreatedTime());
		logger.debug("File ID: " + file.getName() + ", " + file.getId());
	}

}
