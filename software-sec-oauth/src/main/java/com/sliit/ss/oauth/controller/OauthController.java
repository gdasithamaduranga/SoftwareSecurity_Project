package com.sliit.ss.oauth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.sliit.ss.oauth.model.GDriveFileUpload;
import com.sliit.ss.oauth.service.OauthAuthorizeService;
import com.sliit.ss.oauth.service.GDriveService;

@Controller
public class OauthController {

	private Logger logger = LoggerFactory.getLogger(OauthController.class);

	@Autowired
	OauthAuthorizeService authorizationService;

	@Autowired
	GDriveService driveService;

	/**
	 * Root Request Handle.
	 * 
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/")
	public String GoHomePage() throws Exception {
		logger.debug("Go home page called...");
		if (authorizationService.checkAuthentiUser()) {
			logger.debug("Authenticated user redirect to home...");
			return "redirect:/home";
		} else {
			logger.debug("Not authenticated user direct to log on...");
			return "redirect:/login";
		}
	}

	/**
	 * Directs to login
	 * 
	 * @return
	 */
	@GetMapping("/login")
	public String OpenLogin() {
		logger.debug("Load index page...");
		return "index.html";
	}

	/**
	 * Directs to home
	 * 
	 * @return
	 */
	@GetMapping("/home")
	public String OpenHome() {
		logger.debug("Load home page...");
		return "home.html";
	}

	/**
	 * Calls the Google OAuth service to authorize the app
	 * 
	 * @param response
	 * @throws Exception
	 */
	@GetMapping("/googlesignin")
	public void doSignIn(HttpServletResponse response) throws Exception {
		logger.debug("Inside google sign in...");
		response.sendRedirect(authorizationService.doUserGoogleAuth());
	}

	/**
	 * Applications Callback URI for redirection from Google auth server after user
	 * approval/consent
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/oauth/callback")
	public String processAuthCode(HttpServletRequest request) throws Exception {
		logger.debug("Authcode Callback invoked...");
		String code = request.getParameter("code");
		logger.debug("Response Code Value..., " + code);

		if (code != null) {
			logger.debug("Response code not null...");
			authorizationService.retriveCodeForTokens(code);
			logger.debug("retriveCodeForTokens invoked...");
			
			return "redirect:/home";
		}
		
		return "redirect:/login";
	}

	/**
	 * Handles logout
	 * 
	 * @return
	 * @throws Exception 
	 */
	@GetMapping("/logout")
	public String goLogout(HttpServletRequest request) throws Exception {
		logger.debug("User logout invoked...");
		authorizationService.logoutSession(request);
		return "redirect:/login";
	}

	/**
	 * Handles the files being uploaded to GDrive
	 * 
	 * @param request
	 * @param uploadedFile
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/upload")
	public String fileUpload(HttpServletRequest request, @ModelAttribute GDriveFileUpload uploadedFile) throws Exception {
		logger.debug("File upload invoked...");
		MultipartFile multipartFile = uploadedFile.getMultipartFile();
		driveService.doFileUpload(multipartFile);
		return "redirect:/home?status=success";
	}
}
