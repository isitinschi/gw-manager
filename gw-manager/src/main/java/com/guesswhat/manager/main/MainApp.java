package com.guesswhat.manager.main;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainApp {
	
   public static void main(String[] args) {
	   ClassPathXmlApplicationContext ctx = 
             new ClassPathXmlApplicationContext("cfg-ApplicationContext.xml");
	   ctx.close();
   }
   
}
