<!--
 * Copyright 2010-2011 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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
-->

<%@page import="com.amazonaws.tvm.Configuration"%>
<%@ page session="true" %>

<html>
	<head>
		<title><%=Configuration.APP_NAME %> - Success</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=1.0">
		<link rel="stylesheet" href="css/styles.css" type="text/css" media="screen" charset="utf-8">
		<link rel="stylesheet" href="css/styles-mobile.css" type="text/css" media="screen" charset="utf-8">
		<link rel="stylesheet" href="css/styles-tablet.css" type="text/css" media="screen" title="no title" charset="utf-8">
	</head>
	<body class="success">

		<div id="header">
			<h1><%=Configuration.APP_NAME %></h1>
		</div>

		<div id="body">
			<fieldset>
				<legend>Success!</legend>
				<p class="message">You have been successfully registered.</p>
			</fieldset>
		</div>

		<div id="footer">
			<p class="footnote"><%=Configuration.APP_NAME %> - Token Vending Machine</p>
		</div>

	</body>
</html>
