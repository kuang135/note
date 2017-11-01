public class ModelAndView {

	// Viewʵ��,����ͼ����(String) 
	private Object view;

	// ģ��(Map) 
	private ModelMap model;



	
	//��ͼ���Ƶ�set��get
	public void setViewName(String viewName) {
		this.view = viewName;
	}
	public String getViewName() {
		return (this.view instanceof String ? (String) this.view : null);
	}
	
	//Viewʵ����set��get
	public void setView(View view) {
		this.view = view;
	}
	public View getView() {
		return (this.view instanceof View ? (View) this.view : null);
	}

	
	// ��Map<String,Object>�з���ģ������
	public ModelAndView addObject(String attributeName, Object attributeValue) {
		getModelMap().addAttribute(attributeName, attributeValue);
		return this;
	}
	//��ȡ����ģ��
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
	
	//�ж�view�Ƿ�����ͼ���ƻ���Viewʵ��
	public boolean isReference() {
		return (this.view instanceof String);
	}



	
	
	


}
