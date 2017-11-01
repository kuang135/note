
public interface HandlerMapping {
	
	//ªÒ»°HandlerExecutionChain
	HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception;

}
