package com.yuanbaopu.proxy;

import java.util.ArrayList;
import java.util.List;

public class Children {

	
	
	public static void main(String[] args) {
		ChuTi.printTis(20);
	}
	
}

enum Oper {	
	ADD("+"),MINUS("-"),PLUS("x"),DIV("/");
	private String flag;
	Oper(String flag) {
		this.flag = flag;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	static String getOperFlag(int i) {
		return Oper.values()[i].flag;
	}
	
	static Oper getOper(int i) {
		return Oper.values()[i];
	}
	
}

class ChuTi {
	
	static void printTis(int num) {
		Ti[] tis = getTis(num);
		for(Ti ti : tis) {
			System.out.println(ti);
		}
	}
	
	static Ti[] getTis(int num) {
		Ti[] tis = new Ti[num];
		for(int i = 0; i < num; i++) {
			tis[i] = getATi();
		}
		return tis;
	}
	
	static Ti getATi() {
		return new Ti();
	}
}

class Ti {
	
	Ti() {
		init();
	}
	
	void init() {
		int num = Math.random() > 0.7 ? 3 : 2;
		for(int i = 0; i < num; i++) {
			int num2 = Math.random() > 0.6 ? 30 : 80;
			int d = randomInt(num2);
			if(i > 0) { 
				if(ops.get(i - 1) == Oper.DIV) {
					ds.remove(i-1);
					ds.add(i-1,randomInt(10) * d);
				}
				if(ops.get(i - 1) == Oper.MINUS) {
					if(contains(Oper.DIV) || contains(Oper.PLUS) ) {
						
					} else if(ds.get(i -1) < d) {
						int p = ds.remove(i-1);
						ds.add(i-1,d);
						d = p;
					}
				}
			}
			ds.add(d);
			if(i < (num -1)) {
				int num3 = randomInt(10) % 4;
				if(Oper.getOper(num3) == Oper.PLUS || Oper.getOper(num3)== Oper.DIV)
					if(contains(Oper.getOper(num3))) {
						num3 = randomInt(10) % 2;
				}
				ops.add(Oper.values()[num3]);
			}
		}
	}
	
	List<Integer> ds = new ArrayList<Integer>();
	
	List<Oper> ops = new ArrayList<Oper>();
	
	boolean contains(Oper o) {
		for(Oper op : ops) {
			if(op.getFlag().equals(o.getFlag())) {
				return true;
			}
		}
		return false;
	}
	
	int randomInt(int global) {
		return new Long(Math.round(Math.random() * global)).intValue();
	}
	
	String express() {
		String ex = "";
		int i = 0;
		for(; i < ds.size() -1; i++) {
			ex += ds.get(i).toString() + " " + ops.get(i).getFlag() + " "; 
		}
		ex += ds.get(i) + " = ";
		return ex;
		
	}

	@Override
	public String toString() {
		return express();
	}
	
}
