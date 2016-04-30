/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manufaktura.workers;

import manufaktura.system.Data;

/**
 *
 * @author daniel
 */
public class Worker {
	private String name_;
	
	public void doIt(Data data){
		System.out.println(name_ + ": doIt nedefinovano");
	}
	
	public void setName(String name){
		name_ = name;
	}
}
