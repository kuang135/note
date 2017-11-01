package cn.itcast.domain;

import java.util.List;

public class PageBean {

	//不能计算出来的字段都提供get,set方法,
	//能计算出来的字段,在源的set方法中赋值,但是如果源有两个的时候呢???
	//1.拿到pageNow
	//2.根据条件查询所有,得到recordTotal
	//3.根据pageNow和recordTotal,计算出pageDataStart,pageDataEnd
	//4.根据条件,pageDataStart,pageDataEnd去数据库查询,得到当前页的products
	//5.前端显示pageNow,pageTotal,products,pageStart,pageEnd
	
	//默认前5后4显示,每页显示12个数据
	private int before;
	private int after;
	private int howMany;//每页个数,

	private int pageNow;//当前页
	private int recordTotal;//总记录数
	private int pageTotal;//总页数, (recordTotal-1)/howMany+1
	private int pageDataStart;//每页开始,(pageNow-1)*howMany+1
	private int pageDataEnd;//每页结束,pageNow*howMany 或 recordTotal
	private List<Product> pageProducts;//每页显示的数据,根据pageDataStart和pageDataEnd和SearchProductBean查询得到
	private int pageStart;//在当前页前多少页,根据before,after,pageNow,pageTotal计算出来
	private int pageEnd;//在当前页后多少页,根据before,after,pageNow,pageTotal计算出来
	//构造
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
	//设置pageNow
	public void setPageNow(int pageNow) {
		this.pageNow = pageNow;
		this.pageDataStart=(this.pageNow-1)*this.howMany+1;
	}
	//设置recordTotal
	public void setRecordTotal(int recordTotal) {
		this.recordTotal = recordTotal;
		//更新pageTotal
		this.pageTotal=(this.recordTotal-1)/this.howMany+1;
		//更新pageDataEnd
		if(this.pageNow*this.howMany<=this.recordTotal){
			this.pageDataEnd=this.pageNow*this.howMany;
		}
		if(this.pageNow*this.howMany>this.recordTotal){
			this.pageDataEnd=this.recordTotal;
		}
		//更新pageStart,pageEnd
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
	//设置pageProduct
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
