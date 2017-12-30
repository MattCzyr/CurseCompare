package com.mattczyr.cursecompare;

public class OperatorThread extends Thread {

	private String url0;
	private String url1;

	public OperatorThread(String url0, String url1) {
		this.url0 = url0;
		this.url1 = url1;
		setName("Operator");
		setDaemon(true);
		start();
	}

	@Override
	public void run() {
		try {
			CurseCompare.compare(url0, url1);
		} catch (Exception e) {
			e.printStackTrace();
			Interface.error();
		}

		Interface.operatorThread = null;
	}

}