@SuppressWarnings("serial")
public class DispatcherServlet extends FrameworkServlet {
	
	//��ʼ��
	protected void initStrategies(ApplicationContext context) {
		initMultipartResolver(context);
		initLocaleResolver(context);
		initThemeResolver(context);
		initHandlerMappings(context);//���������õ����е�HandlerMapping
		initHandlerAdapters(context);//���������õ����е�HandlerAdapter
		initHandlerExceptionResolvers(context);
		initRequestToViewNameTranslator(context);
		initViewResolvers(context);//���������õ����е�ViewResolver
		initFlashMapManager(context);
	}

	//ǰ�˿��������ɷ���
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
				multipartRequestParsed = (processedRequest != request);//�ж��Ƿ�Ϊ�ļ��ϴ�			
				//2.��������HandlerMapping,���������ȡ������ִ����(HandlerExecutionChain)
				//  HandlerExecutionChain���д�����(Object handler)���������������(HandlerInterceptor[])
				mappedHandler = getHandler(processedRequest);)
				if (mappedHandler == null || mappedHandler.getHandler() == null) {
					noHandlerFound(processedRequest, response);
					return;
				}			
	
				//3.��������HandlerAdapter,��ȡ֧��HandlerExecutionChain��handler����(Controller,Servlet,HttpRequestHandler)��HandlerAdapter
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
				//˳��ִ����������ǰ�÷���(HandlerInterceptor.preHandle)
				//  ���������false,�͵���ִ����ɷ�����HandlerInterceptor.afterCompletion��
				if (!mappedHandler.applyPreHandle(processedRequest, response)) {
					return;
				}
				//4.������������(HandlerAdapter)���ô������Ĵ�����,����ModelAndView
				mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
				if (asyncManager.isConcurrentHandlingStarted()) {
					return;
				}		
				applyDefaultViewName(request, mv);		
				// ����ִ���������ĺ��÷�����HandlerInterceptor.postHandle��
				mappedHandler.applyPostHandle(processedRequest, response, mv);
			}
			catch (Exception ex) {
				dispatchException = ex;
			}
			//����5,����6: ������ͼ��������ͼ����Ⱦ
			//����5 ��ViewResolver����View��View view=viewResolver.resolveViewName(viewName, locale)��
			//����6 ��ͼ����Ⱦʱ���Model���루view.render(mv.getModelInternal(), request, response);��
			//��ͼ��Ⱦ��ɺ�ᵹ��ִ������������ɷ�����HandlerInterceptor.afterCompletion��
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

	//����5,����6: ������ͼ��������ͼ����Ⱦ
	protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Locale locale = this.localeResolver.resolveLocale(request);
		response.setLocale(locale);
		View view;
		//5.��ͼ����,���ModelAndView�е�view����ͼ����(String)
		if (mv.isReference()) {
			// ͨ����ͼ������(ViewResolver),������ͼ����,�õ�Viewʵ��
			view = resolveViewName(mv.getViewName(), mv.getModelInternal(), locale, request);
			if (view == null) {
				throw new ServletException("Could not resolve view with name '" + mv.getViewName() +
						"' in servlet with name '" + getServletName() + "'");
			}
		}
		//5.��ͼ����,���ModelAndView�е�view��Viewʵ��
		else {
			// ֱ�Ӵ�ModelAndView�л�ȡViewʵ��
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
			//6.��ͼ��Ⱦ
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

	//5.��ͼ�����������
	protected View resolveViewName(String viewName, Map<String, Object> model, Locale locale,
			HttpServletRequest request) throws Exception {
		//5.��������ViewResolver,���߼���ͼ������ΪView����
		for (ViewResolver viewResolver : this.viewResolvers) {
			View view = viewResolver.resolveViewName(viewName, locale);
			if (view != null) {
				return view;
			}
		}
		return null;
	}
	
	
}