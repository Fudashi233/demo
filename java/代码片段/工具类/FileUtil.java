package utils;

import java.io.File;

public class FileUtil {

	public static boolean deleteDir(String path) {
		
		File dir = new File(path);
		//$判断是否是存在的文件夹
		if(dir.exists() && dir.isDirectory()) {
			
			File[] fileArr = dir.listFiles();
			//$遍历文件夹，找出其中的文件和文件夹，如果是文件夹则递归调用deleteDir，否则直接删除
			for(int i=0;i<fileArr.length;i++) {
				
				File file = fileArr[i];
				if(file.isDirectory()) {
					
					deleteDir(file.getAbsolutePath());
				} else {
					
					if(!file.delete()) {
						
						throw new RuntimeException(file.getAbsolutePath()+"删除失败");
					}
				}
			}
		} else {
			
			return false;
		}
		//$删除空文件夹
		return dir.delete();
	}
}
