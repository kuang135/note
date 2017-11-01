package cn.itcast.domain;

import java.util.List;

public class PageBean {

	//���ܼ���������ֶζ��ṩget,set����,
	//�ܼ���������ֶ�,��Դ��set�����и�ֵ,�������Դ��������ʱ����???
	//1.�õ�pageNow
	//2.����������ѯ����,�õ�recordTotal
	//3.����pageNow��recordTotal,�����pageDataStart,pageDataEnd
	//4.��������,pageDataStart,pageDataEndȥ���ݿ��ѯ,�õ���ǰҳ��products
	//5.ǰ����ʾpageNow,pageTotal,products,pageStart,pageEnd
	
	//Ĭ��ǰ5��4��ʾ,ÿҳ��ʾ12������
	private int before;
	private int after;
	private int howMany;//ÿҳ����,

	private int pageNow;//��ǰҳ
	private int recordTotal;//�ܼ�¼��
	private int pageTotal;//��ҳ��, (recordTotal-1)/howMany+1
	private int pageDataStart;//ÿҳ��ʼ,(pageNow-1)*howMany+1
	private int pageDataEnd;//ÿҳ����,pageNow*howMany �� recordTotal
	private List<Product> pageProducts;//ÿҳ��ʾ������,����pageDataStart��pageDataEnd��SearchProductBean��ѯ�õ�
	private int pageStart;//�ڵ�ǰҳǰ����ҳ,����before,after,pageNow,pageTotal�������
	private int pageEnd;//�ڵ�ǰҳ�����ҳ,����before,after,pageNow,pageTotal�������
	//����
	public PageBean(){
		this.howMany=2;
		this.before=3;
		this.after=2;
	}
	public PageBean(int howMany,int before,int after){
		this.howMany=howMany;
		this.before=before;
		this.after=after;
	}
	//set
	//����pageNow
	public void setPageNow(int pageNow) {
		this.pageNow = pageNow;
		this.pageDataStart=(this.pageNow-1)*this.howMany+1;
	}
	//����recordTotal
	public void setRecordTotal(int recordTotal) {
		this.recordTotal = recordTotal;
		//����pageTotal
		this.pageTotal=(this.recordTotal-1)/this.howMany+1;
		//����pageDataEnd
		if(this.pageNow*this.howMany<=this.recordTotal){
			this.pageDataEnd=this.pageNow*this.howMany;
		}
		if(this.pageNow*this.howMany>this.recordTotal){
			this.pageDataEnd=this.recordTotal;
		}
		//����pageStart,pageEnd
		if(this.pageTotal<=this.before+this.after+1){
			this.pageStart=1;
			this.pageEnd=pageTotal;
		}else{
			if(this.pageNow-this.before<=0){
				pageStart=1;
				pageEnd=this.before+this.after+1;
			}
			if(this.pageNow+this.after>=this.pageTotal){
				this.pageStart=this.pageTotal-this.before-this.after;
				this.pageEnd=this.pageTotal;
			}
			if(this.pageNow-this.before>0&&this.pageNow+this.after<this.pageTotal){
				this.pageStart=this.pageNow-this.before;
				this.pageEnd=this.pageNow+this.after;
			}
		}
	}
	//����pageProduct
	public void setPageProducts(List<Product> pageProducts) {
		this.pageProducts = pageProducts;
	}
	
	//get,
	public int getPageNow() {
		return pageNow;
	}
	public int getRecordTotal() {
		return recordTotal;
	}
	public List<Product> getPageProducts() {
		return pageProducts;
	}
	public int getPageTotal() {
		return pageTotal;
	}
	public int getPageDataStart() {
		return pageDataStart;
	}
	public int getPageDataEnd() {
		return pageDataEnd;
	}
	public int getPageStart() {
		return pageStart;
	}
	public int getPageEnd() {
		return pageEnd;
	}
}
