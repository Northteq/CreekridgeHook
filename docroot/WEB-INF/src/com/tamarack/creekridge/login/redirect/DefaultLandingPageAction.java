package com.tamarack.creekridge.login.redirect;

import java.util.List;

import com.liferay.portal.kernel.events.Action;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.struts.LastPath;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.UserGroupServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;


public class DefaultLandingPageAction extends Action {
	@Override
	  public void run(HttpServletRequest request, HttpServletResponse response)
	    throws ActionException {

	    try {
	      doRun(request, response);
	    }
	    catch (Exception e) {
	      throw new ActionException(e);
	    }
	  }

	  protected void doRun(HttpServletRequest request, HttpServletResponse response) throws Exception {

	    long userId = PortalUtil.getUserId(request);
	    _log.info("Default Portal site urI "+ PortalUtil.getPortalURL(request));
	    
	    String path=PortalUtil.getPortalURL(request);
	   
      
	    	User user= UserLocalServiceUtil.getUser(userId);
      
	    	List<Role> userRoles=user.getRoles();
	    
	    	boolean hasPortalRole=false;
      
	    	for (int i=0;i<userRoles.size();i++){
      	
	    		Role role=userRoles.get(i);
      	   		if ("creekRidgeManager".equalsIgnoreCase(role.getName()) || "Administrator".equalsIgnoreCase(role.getName())){
      		      hasPortalRole=true;
      		    break;
      	       }
        }
      
      if(hasPortalRole){
      	    path = PortalUtil.getPortalURL(request);
	    	_log.info("Default site urI for admin portal role "+ path);
         } else {
	       List<Group> userSites=user.getSiteGroups(true);
	       for (int i=0;i<userSites.size();i++){
	         	
	    	   Group userSite=userSites.get(i);
	    	   if( userSite.isRegularSite()){
     	   		 Group defaultGroup = userSite;
     	   		path = PortalUtil.getPathFriendlyURLPrivateGroup() + defaultGroup.getFriendlyURL();
     		    break;
     	       }
	    	}
	  
	    if (Validator.isNotNull(path)) {
	      LastPath lastPath = new LastPath(StringPool.BLANK, path);

	      HttpSession session = request.getSession();

	      session.setAttribute(WebKeys.LAST_PATH, lastPath);
	      
	      _log.info("THE lastpath urI "+ lastPath);
	    }
	   
	    }
	  }
	  private static Logger _log = Logger.getLogger(DefaultLandingPageAction.class);

	}