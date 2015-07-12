package com.minhtdh.common.util;

import android.os.Environment;
import android.os.StatFs;

public class MemoryUtil {
	/**
	 * check if external Memory is have more than minimum memory
	 * @author MinhTDH
	 * @param minimum_mem_MB
	 * @return boolean
	 *********************************************************
	 */
	public static boolean isExternalStorageAvailable(double minimum_mem_MB) {
		boolean ret = false;
		String media_state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(media_state) == true) {
			StatFs memory_stat = new StatFs(
					Environment.getExternalStorageDirectory().getPath());
			double avaiable_memory = memory_stat.getAvailableBlocks()
					* (memory_stat.getBlockSize() / 1024d);
			double minimum_mem_B = minimum_mem_MB * 1024d;
			if (avaiable_memory >= minimum_mem_B) {
				ret = true;
			}
		}
		return ret;
	}
}
