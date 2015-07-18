package com.joedoe.bullshitbingo.common.config;

public class DataCacheConfig {
	
	public String catalogEndPoint;
	public String gridName;
	public String username;
	public String password;
	
	@Override
	public String toString() {
		return "DataCacheConfig [catalogEndPoint=" + catalogEndPoint
				+ ", gridName=" + gridName + ", username=" + username
				+ ", password=" + password + "]";
	}		

}
