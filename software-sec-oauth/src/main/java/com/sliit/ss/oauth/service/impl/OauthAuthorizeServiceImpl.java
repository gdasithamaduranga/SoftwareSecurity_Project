package com.sliit.ss.oauth.service.impl;

import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.sliit.ss.oauth.constant.AppConstant;
import com.sliit.ss.oauth.service.OauthAuthorizeService;
import com.sliit.ss.oauth.util.OauthConfig;

@Service
public class OauthAuthorizeServiceImpl implements OauthAuthorizeService {

	private Logger logger = LoggerFactory.getLogger(OauthAuthorizeServiceImpl.class);
	private GoogleAuthorizationCodeFlow flow;
	private FileDataStoreFactory dataStoreFactory;

	@Autowired
	private OauthConfig config;

	@PostConstruct
	public void init() throws Exception {
		InputStreamReader reader = new InputStreamReader(config.getDriveSecretKeys().getInputStream());
		dataStoreFactory = new FileDataStoreFactory(config.getCredentialsFolder().getFile());

		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(AppConstant.JSON_FACTORY, reader);
		flow = new GoogleAuthorizationCodeFlow.Builder(AppConstant.HTTP_TRANSPORT, AppConstant.JSON_FACTORY, clientSecrets,
				AppConstant.SCOPES).setDataStoreFactory(dataStoreFactory).build();
	}

	@Override
	public boolean checkAuthentiUser() throws Exception {
		logger.debug("checkAuthentiUser called...");
		Credential credential = getCredentials();
		if (credential != null) {
			boolean isTokenValid = credential.refreshToken();
			logger.debug("Token validity check, " + isTokenValid);
			return isTokenValid;
		}
		return false;
	}

	@Override
	public Credential getCredentials() throws IOException {
		return flow.loadCredential(AppConstant.USER_IDENTIFIER_KEY);
	}

	@Override
	public String doUserGoogleAuth() throws Exception {
		logger.debug("doUserGoogleAuth invoked...");
		GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
		String redirectUrl = url.setRedirectUri(config.getCALLBACK_URI()).setAccessType("offline").build();
		logger.debug("redirectUrl, " + redirectUrl);
		return redirectUrl;
	}

	@Override
	public void retriveCodeForTokens(String code) throws Exception {
		logger.debug("exchange the code against the access token and refresh token invoked...");
		GoogleTokenResponse tokenResponse = flow.newTokenRequest(code).setRedirectUri(config.getCALLBACK_URI()).execute();
		flow.createAndStoreCredential(tokenResponse, AppConstant.USER_IDENTIFIER_KEY);
		init(); 
		checkAuthentiUser();
	}

	@Override
	public void logoutSession(HttpServletRequest request) throws Exception {
		logger.debug("logout session invoked...");
		// Revoke token
		dataStoreFactory.getDataStore(config.getCredentialsFolder().getFilename()).clear();
	}

}
