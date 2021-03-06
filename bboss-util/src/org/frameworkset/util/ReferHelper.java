package org.frameworkset.util;

import com.frameworkset.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ReferHelper {
	private static Logger logger = LoggerFactory.getLogger(ReferHelper.class);
	private String[] refererwallwhilelist;
	public static final String REQUEST_HEADER_REFER_CHECKED = "REQUEST_HEADER_REFER_CHECKED";
	// -------------------------------------------------- CORS Request Headers
	/**
	 * The Origin header indicates where the cross-origin request or preflight
	 * request originates from.
	 */
	public static final String REQUEST_HEADER_ORIGIN = "Origin";
	/**
	 * The Access-Control-Request-Headers header indicates which headers will be
	 * used in the actual request as part of the preflight request.
	 */
	public static final String REQUEST_HEADER_ACCESS_CONTROL_REQUEST_HEADERS =
			"Access-Control-Request-Headers";

	private boolean refererDefender = false;
	private PathMatcher pathMatcher;
	private String[] wallfilterrules;
	private String[] wallwhilelist;
	public final static String[] wallfilterrules_default = new String[] {
			"<script", "%3Cscript", "script", "<img", "%3Cimg", "alert(",
			"alert%28", "eval(", "eval%28", "style=", "style%3D", "javascript",
			"update ", "drop ", "delete ", "insert ", "create ", "select ",
			"truncate " };

	public ReferHelper() {
		pathMatcher = new AntPathMatcher();
	}

	private boolean iswhilerefer(String referer) {
		if (this.refererwallwhilelist == null
				|| this.refererwallwhilelist.length == 0)
			return false;
		for (String whilereferername : this.refererwallwhilelist) {

			if (pathMatcher.urlContain(whilereferername, referer))
				return true;
		}
		return false;
	}

	public void recordNoCros(HttpServletRequest request,
							 HttpServletResponse response){
		request.setAttribute(ReferHelper.REQUEST_HEADER_REFER_CHECKED,false);
	}
	public boolean dorefer(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Boolean checked = (Boolean) request.getAttribute(ReferHelper.REQUEST_HEADER_REFER_CHECKED);
		if(checked != null )
			return checked;
		checked = false;
		if (refererDefender) {

			/**
			 * 跨站点请求伪造。修复任务： 拒绝恶意请求。解决方案，过滤器中
			 * 
			 */
			String referer = request.getHeader("Referer"); // REFRESH
			// if(!iswhilerefer(referer))

			if (referer != null) {
				String basePath = null;
				String basePath80 = null;
				if (!request.getContextPath().equals("/")) {
					if (request.getServerPort() != 80) {
						basePath = new StringBuilder().append(request.getScheme() ).append( "://")
						.append( request.getServerName() )
								.append( ":")
								.append( request.getServerPort())
								.append( request.getContextPath() )
								.append( "/").toString();
					} else {
						basePath = new StringBuilder().append(request.getScheme() )
								.append( "://")
								.append( request.getServerName() )
								.append( ":")
								.append( request.getServerPort())
								.append( request.getContextPath() )
								.append( "/").toString();
						basePath80 = new StringBuilder().append(request.getScheme() )
								.append( "://")
								.append(request.getServerName())
								.append(request.getContextPath() )
								.append( "/").toString();
					}
				} else {
					if (request.getServerPort() != 80) {
						basePath = new StringBuilder().append(request.getScheme() )
								.append( "://")
								.append( request.getServerName() )
								.append( ":")
								.append( request.getServerPort())
								.append( request.getContextPath()).toString();
					} else {
						basePath = new StringBuilder().append(request.getScheme() )
								.append(  "://")
								.append(  request.getServerName() )
								.append(  ":")
								.append(  request.getServerPort())
								.append(  request.getContextPath()).toString();
						basePath80 = new StringBuilder().append(request.getScheme() )
								.append( "://")
								.append(request.getServerName())
								.append( request.getContextPath()).toString();
					}
				}
				if (basePath80 == null) {
					if (referer.indexOf(basePath) < 0) {
						if (this.iswhilerefer(referer)) {

							checked = false;
						} else {
							sendInvalidCORS(request, response);
							checked = true;
						}
					}
				} else {
					if (referer.indexOf(basePath) < 0
							&& referer.indexOf(basePath80) < 0) {
						// String context = request.getContextPath();
						// if(!context.equals("/"))
						// {
						// String uri = request.getRequestURI();
						// uri =
						// uri.substring(request.getContextPath().length());
						// request.getRequestDispatcher(uri).forward(request,
						// response);
						// }
						// else
						// {
						// request.getRequestDispatcher(context).forward(request,
						// response);
						// }
						// return;
						if (this.iswhilerefer(referer)) {
							checked = false;
						} else {
							sendInvalidCORS(request, response);
							checked = true;
						}
					}
				}

			}
		}
		else {
			checked = false;
		}
		request.setAttribute(ReferHelper.REQUEST_HEADER_REFER_CHECKED,checked);
		return checked;
	}
	public void sendInvalidCORS(HttpServletRequest request,
								HttpServletResponse response)   {
		String origin = request.getHeader(REQUEST_HEADER_ORIGIN);
		String method = request.getMethod();
		String accessControlRequestHeaders = request.getHeader(
				REQUEST_HEADER_ACCESS_CONTROL_REQUEST_HEADERS);

		response.setContentType("text/plain");
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.resetBuffer();

		if (logger.isDebugEnabled()) {
			// Debug so no need for i18n
			StringBuilder message =
					new StringBuilder("Invalid CORS request; Origin=");
			message.append(origin);
			message.append(";Method=");
			message.append(method);
			if (accessControlRequestHeaders != null) {
				message.append(";Access-Control-Request-Headers=");
				message.append(accessControlRequestHeaders);
			}
			logger.debug(message.toString());
		}
	}
	public void sendRedirect403(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if (!response.isCommitted()) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
		}
	}

	public String[] getRefererwallwhilelist() {
		return refererwallwhilelist;
	}

	public void setRefererwallwhilelist(String[] refererwallwhilelist) {
		this.refererwallwhilelist = refererwallwhilelist;
		if (StringUtil.isNotEmpty(this.refererwallwhilelist)) {
			for (int i = 0; i < this.refererwallwhilelist.length; i++) {
				this.refererwallwhilelist[i] = this.refererwallwhilelist[i]
						.trim();
			}
		}
	}

	public boolean isRefererDefender() {
		return refererDefender;
	}

	public void setRefererDefender(boolean refererDefender) {
		this.refererDefender = refererDefender;
	}

	public String[] getWallfilterrules() {
		return wallfilterrules;
	}

	public void setWallfilterrules(String[] wallfilterrules) {
		this.wallfilterrules = wallfilterrules;
	}

	public String[] getWallwhilelist() {
		return wallwhilelist;
	}

	public void setWallwhilelist(String[] wallwhilelist) {
		this.wallwhilelist = wallwhilelist;
	}

	public boolean iswhilename(String name) {
		if (this.wallwhilelist == null || this.wallwhilelist.length == 0)
			return true;
		for (String whilename : this.wallwhilelist) {
			if (whilename.equals(name))
				return true;
		}
		return false;
	}

	public void wallfilter(String name, String[] values) {
		if (this.wallfilterrules == null || this.wallfilterrules.length == 0
				|| values == null || values.length == 0 || iswhilename(name))
			return;

		int j = 0;
		for (String value : values) {
			if (value == null || value.equals("")) {
				j++;
				continue;
			}

			for (int i = 0; i < wallfilterrules.length; i++) {

				if (value.indexOf(wallfilterrules[i]) >= 0) {
					values[j] = null;
					if(logger.isWarnEnabled())
					logger.warn(new StringBuilder().append("参数" ).append( name ).append( "值" ).append( value ).append( "包含敏感词:"
					).append( wallfilterrules[i] ).append( ",存在安全隐患,系统自动过滤掉参数值!").toString());
					break;
				}
			}
			j++;

		}
	}

}
