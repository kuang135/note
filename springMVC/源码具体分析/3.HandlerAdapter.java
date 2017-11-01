
public interface HandlerAdapter {

	//判断是否支持这种处理器
	boolean supports(Object handler);

	//执行处理器的方法,返回ModelAndView
	ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;

	long getLastModified(HttpServletRequest request, Object handler);

}
