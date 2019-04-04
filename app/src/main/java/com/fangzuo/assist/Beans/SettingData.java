package com.fangzuo.assist.Beans;

import java.util.List;

public class SettingData {


	/**
	 * size : 1
	 * setFiles : [{"name":"通用版","time":"20181225","use":"0"},{"name":"通用版","time":"20181225","use":"0"}]
	 */

	private int size;
	private List<SetFilesBean> setFiles;

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public List<SetFilesBean> getSetFiles() {
		return setFiles;
	}

	public void setSetFiles(List<SetFilesBean> setFiles) {
		this.setFiles = setFiles;
	}

	public static class SetFilesBean {
		/**
		 * name : 通用版
		 * time : 20181225
		 * use : 0
		 */

		private String name;
		private String time;
		private String use;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getUse() {
			return use;
		}

		public void setUse(String use) {
			this.use = use;
		}
	}
}
