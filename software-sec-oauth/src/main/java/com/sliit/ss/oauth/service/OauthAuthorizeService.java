package com.sliit.ss.oauth.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.google.api.client.auth.oauth2.Credential;

public interface OauthAuthorizeService {

	public boolean checkAuthentiUser() throws Exception;

	public Credential getCredentials() throws IOException;

	public String doUserGoogleAuth() throws Exception;

	public void retriveCodeForTokens(String code) throws Exception;
	
	public void logoutSession(HttpServletRequest request) throws Exception;
}
