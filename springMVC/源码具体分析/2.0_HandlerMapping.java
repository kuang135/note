
public interface HandlerMapping {
	
	//��ȡHandlerExecutionChain
	HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception;

}
