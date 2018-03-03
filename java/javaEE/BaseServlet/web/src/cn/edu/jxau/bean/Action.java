package cn.edu.jxau.bean;

import java.util.Map;

public class Action {
	
	private String methodName;
	private Map<String,Result> resultMap;
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public Map<String, Result> getResultMap() {
		return resultMap;
	}
	public void setResultMap(Map<String, Result> resultMap) {
		this.resultMap = resultMap;
	}
	@Override
	public String toString() {
		return "Action [methodName=" + methodName + ", resultMap=" + resultMap + "]";
	}
}
