public interface ViewResolver {

	//���ModelAndView�е�view��������ͼ����(String����)
	//��ô�ͱ��뽫������(String)����Ϊһ��Viewʵ��
	View resolveViewName(String viewName, Locale locale) throws Exception;

}