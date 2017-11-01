public class ModelAndView {

	// View实例,或视图名称(String) 
	private Object view;

	// 模型(Map) 
	private ModelMap model;



	
	//视图名称的set和get
	public void setViewName(String viewName) {
		this.view = viewName;
	}
	public String getViewName() {
		return (this.view instanceof String ? (String) this.view : null);
	}
	
	//View实例的set和get
	public void setView(View view) {
		this.view = view;
	}
	public View getView() {
		return (this.view instanceof View ? (View) this.view : null);
	}

	
	// 往Map<String,Object>中放入模型数据
	public ModelAndView addObject(String attributeName, Object attributeValue) {
		getModelMap().addAttribute(attributeName, attributeValue);
		return this;
	}
	//获取数据模型
	protected Map<String, Object> getModelInternal() {
		return this.model;
	}
	public ModelMap getModelMap() {
		if (this.model == null) {
			this.model = new ModelMap();
		}
		return this.model;
	}
	public Map<String, Object> getModel() {
		return getModelMap();
	}
	
	//判断view是否是视图名称还是View实例
	public boolean isReference() {
		return (this.view instanceof String);
	}



	
	
	


}
