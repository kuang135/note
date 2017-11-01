package IntroSpectorTest;

import Bean.Person;

public class Test {

	
	public static void main(String[] args) throws Exception{
		
		Person p1=new Person();
		IntroSpectorTest.setPropertyValue(p1, "name", "詹姆斯");
		IntroSpectorTest.setPropertyValue(p1, "age", 30);
		Object name = IntroSpectorTest.getPropertyValue(p1, "name");
		Object age = IntroSpectorTest.getPropertyValue(p1, "age");
		System.out.println("name="+name+"----age="+age);
	}
	
}
