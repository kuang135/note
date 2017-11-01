
public interface HandlerAdapter {

	//�ж��Ƿ�֧�����ִ�����
	boolean supports(Object handler);

	//ִ�д������ķ���,����ModelAndView
	ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;

	long getLastModified(HttpServletRequest request, Object handler);

}
