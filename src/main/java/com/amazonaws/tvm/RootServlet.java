/*
 * Copyright 2010-2012 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * Copyright 2014 Gleetr SAS or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.amazonaws.tvm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class RootServlet extends HttpServlet {
	
	protected static final Logger log = LoggerFactory.getLogger(RootServlet.class);
	
	protected abstract String processRequest( HttpServletRequest request, HttpServletResponse response ) throws Exception;
	
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException {
		try {
			String forwardTo = null;
			try {
				forwardTo = this.processRequest( request, response );
			}
			catch ( Exception exception ) {
				this.handleException( request, response, exception );
			}
			
			if ( forwardTo != null ) {
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher( forwardTo );
				dispatcher.forward( request, response );
			}
		}
		catch ( Exception exception ) {
			throw new ServletException( exception );
		}
	}
	
	public void doPost( HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException {
		this.doGet( request, response );
	}
	
	protected void handleException( HttpServletRequest request, HttpServletResponse response, Exception exception ) throws Exception {
		if ( exception instanceof com.amazonaws.tvm.MissingParameterException ) {
			log.warn("Missing input parameter. Setting Http status code " + HttpServletResponse.SC_BAD_REQUEST);
			this.sendErrorResponse( HttpServletResponse.SC_BAD_REQUEST, response );
		}
		else {
			log.error("Unexpected exception: [" + exception.getMessage() + "] Setting Http status code " + HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			this.sendErrorResponse( HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response );
		}
	}
	
	protected String getServletParameter( HttpServlet servlet, String parameterName ) {
		String parameterValue = servlet.getInitParameter( parameterName );
		if ( parameterValue == null ) {
			parameterValue = servlet.getServletContext().getInitParameter( parameterName );
		}
		
		return parameterValue;
	}
	
	protected String getRequiredParameter( HttpServletRequest request, String parameterName ) throws MissingParameterException {
		String value = request.getParameter( parameterName );
		if ( value == null ) {
			throw new MissingParameterException( parameterName );
		}
		
		value = value.trim();
		if ( value.length() == 0 ) {
			throw new MissingParameterException( parameterName );
		}
		else {
			return value;
		}
	}
	
	public static void sendErrorResponse( int httpResponseCode, HttpServletResponse response ) throws Exception {
		ServletOutputStream out = null;
		try {
			
			response.setStatus( httpResponseCode );
			response.setContentType( "text/plain; charset=UTF-8" );
			response.setDateHeader( "Expires", System.currentTimeMillis() );

            // FIXME for now, allow everyone to call it
            response.setHeader("Access-Control-Allow-Origin", "*");

			out = response.getOutputStream();
			out.println( Constants.getMsg( httpResponseCode )+"" );
			
		}
		finally {
			if ( null != out ) {
				try {
					out.flush();
					out.close();
				}
				catch ( IOException e ) {
					log.warn("Error closing ServletOutputStream");
				}
			}
		}
	}
	
	public static void sendOKResponse( HttpServletResponse response, String data ) throws Exception {
		ServletOutputStream out = null;
		try {
			response.setStatus( HttpServletResponse.SC_OK );
			response.setContentType( "text/plain; charset=UTF-8" );
			response.setDateHeader( "Expires", System.currentTimeMillis() );

			// FIXME for now, allow everyone to call it
			response.setHeader("Access-Control-Allow-Origin", "*");
			
			if ( null != data ) {
				out = response.getOutputStream();
				out.println( data );
			}
		}
		finally {
			if ( null != out ) {
				try {
					out.flush();
					out.close();
				}
				catch ( IOException e ) {
					log.warn("Error closing ServletOutputStream");
				}
			}
		}
	}
	
}
