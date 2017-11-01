public interface ViewResolver {

	//如果ModelAndView中的view属性是视图名称(String类型)
	//那么就必须将该名称(String)解析为一个View实例
	View resolveViewName(String viewName, Locale locale) throws Exception;

}