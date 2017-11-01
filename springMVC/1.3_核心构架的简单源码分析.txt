@SuppressWarnings("serial")
public class DispatcherServlet extends FrameworkServlet {
	
	//初始化
	protected void initStrategies(ApplicationContext context) {
		initMultipartResolver(context);
		initLocaleResolver(context);
		initThemeResolver(context);
		initHandlerMappings(context);//从容器中拿到所有的HandlerMapping
		initHandlerAdapters(context);//从容器中拿到所有的HandlerAdapter
		initHandlerExceptionResolvers(context);
		initRequestToViewNameTranslator(context);
		initViewResolvers(context);//从容器中拿到所有的ViewResolver
		initFlashMapManager(context);
	}

	//前端控制器分派方法
	protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpServletRequest processedRequest = request;
		HandlerExecutionChain mappedHandler = null;
		boolean multipartRequestParsed = false;
		WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
		try {
			ModelAndView mv = null;
			Exception dispatchException = null;
			try {
				processedRequest = checkMultipart(request);
				multipartRequestParsed = (processedRequest != request);//判断是否为文件上传			
				//2.遍历所有HandlerMapping,根据请求获取处理器执行链(HandlerExecutionChain)
				//  HandlerExecutionChain中有处理器(Object handler)对象和拦截器数组(HandlerInterceptor[])
				mappedHandler = getHandler(processedRequest);)
				if (mappedHandler == null || mappedHandler.getHandler() == null) {
					noHandlerFound(processedRequest, response);
					return;
				}			
	
				//3.遍历所有HandlerAdapter,获取支持HandlerExecutionChain中handler类型(Controller,Servlet,HttpRequestHandler)的HandlerAdapter
				HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());
				String method = request.getMethod();
				boolean isGet = "GET".equals(method);
				if (isGet || "HEAD".equals(method)) {
					long lastModified = ha.getLastModified(request, mappedHandler.getHandler());
					if (logger.isDebugEnabled()) {
						logger.debug("Last-Modified value for [" + getRequestUri(request) + "] is: " + lastModified);
					}
					if (new ServletWebRequest(request, response).checkNotModified(lastModified) && isGet) {
						return;
					}
				}
				//顺序执行拦截器的前置方法(HandlerInterceptor.preHandle)
				//  如果返回是false,就倒序执行完成方法（HandlerInterceptor.afterCompletion）
				if (!mappedHandler.applyPreHandle(processedRequest, response)) {
					return;
				}
				//4.处理器适配器(HandlerAdapter)调用处理器的处理方法,返回ModelAndView
				mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
				if (asyncManager.isConcurrentHandlingStarted()) {
					return;
				}		
				applyDefaultViewName(request, mv);		
				// 倒序执行拦截器的后置方法（HandlerInterceptor.postHandle）
				mappedHandler.applyPostHandle(processedRequest, response, mv);
			}
			catch (Exception ex) {
				dispatchException = ex;
			}
			//步骤5,步骤6: 解析视图并进行视图的渲染
			//步骤5 由ViewResolver解析View（View view=viewResolver.resolveViewName(viewName, locale)）
			//步骤6 视图在渲染时会把Model传入（view.render(mv.getModelInternal(), request, response);）
			//视图渲染完成后会倒序执行拦截器的完成方法（HandlerInterceptor.afterCompletion）
			processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
		}
		catch (Exception ex) {
			triggerAfterCompletion(processedRequest, response, mappedHandler, ex);
		}
		catch (Error err) {
			triggerAfterCompletionWithError(processedRequest, response, mappedHandler, err);
		}
		finally {
			if (asyncManager.isConcurrentHandlingStarted()) {
				if (mappedHandler != null) {
					mappedHandler.applyAfterConcurrentHandlingStarted(processedRequest, response);
				}
			}
			else {
				// Clean up any resources used by a multipart request.
				if (multipartRequestParsed) {
					cleanupMultipart(processedRequest);
				}
			}
		}
	}

	//步骤5,步骤6: 解析视图并进行视图的渲染
	protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Locale locale = this.localeResolver.resolveLocale(request);
		response.setLocale(locale);
		View view;
		//5.视图解析,如果ModelAndView中的view是视图名称(String)
		if (mv.isReference()) {
			// 通过视图解析器(ViewResolver),解析视图名称,得到View实例
			view = resolveViewName(mv.getViewName(), mv.getModelInternal(), locale, request);
			if (view == null) {
				throw new ServletException("Could not resolve view with name '" + mv.getViewName() +
						"' in servlet with name '" + getServletName() + "'");
			}
		}
		//5.视图解析,如果ModelAndView中的view是View实例
		else {
			// 直接从ModelAndView中获取View实例
			view = mv.getView();
			if (view == null) {
				throw new ServletException("ModelAndView [" + mv + "] neither contains a view name nor a " +
						"View object in servlet with name '" + getServletName() + "'");
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Rendering view [" + view + "] in DispatcherServlet with name '" + getServletName() + "'");
		}
		try {	
			//6.视图渲染
			view.render(mv.getModelInternal(), request, response);
		}
		catch (Exception ex) {
			if (logger.isDebugEnabled()) {
				logger.debug("Error rendering view [" + view + "] in DispatcherServlet with name '" +
						getServletName() + "'", ex);
			}
			throw ex;
		}
	}

	//5.视图解析具体过程
	protected View resolveViewName(String viewName, Map<String, Object> model, Locale locale,
			HttpServletRequest request) throws Exception {
		//5.遍历所有ViewResolver,将逻辑视图名解析为View对象
		for (ViewResolver viewResolver : this.viewResolvers) {
			View view = viewResolver.resolveViewName(viewName, locale);
			if (view != null) {
				return view;
			}
		}
		return null;
	}
	
	
}