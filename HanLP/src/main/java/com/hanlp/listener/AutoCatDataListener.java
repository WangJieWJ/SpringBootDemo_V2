package com.hanlp.listener;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.hanlp.dto.AutoCatData;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title: 
 * Description: 
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2019-11-28 09:50
 */
public class AutoCatDataListener extends AnalysisEventListener<AutoCatData> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AutoCatDataListener.class);

	List<AutoCatData> autoCatDataList = new ArrayList<>();

	@Override
	public void invoke(AutoCatData autoCatData, AnalysisContext analysisContext) {
		LOGGER.info("解析到一条数据:{}", JSON.toJSONString(autoCatData));
		autoCatDataList.add(autoCatData);
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext analysisContext) {
		LOGGER.info("所有数据解析完毕");
		String filePath = "/Users/wangjie/Development/iso/share/ckm/autoCat";
		int fileNum = 1;
		for (AutoCatData autoCatData : autoCatDataList) {
			String fileStr = String.format("%s%s%s", filePath, File.separator, autoCatData.getPrimaryClass());
			if (checkAndCreatePath(fileStr)) {
				// 文件生成
				try {
					FileWriter writer = new FileWriter(String.format("%s%s%s.txt", fileStr, File.separator, fileNum));
					IOUtils.write(autoCatData.getDetail().getBytes("UTF-8"), writer, Charset.forName("GBK"));
					writer.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			fileNum++;
		}
	}

	/**
	 *
	 * @param path 文件路径
	 */
	public boolean checkAndCreatePath(String path) {
		File file = new File(path);
		return file.exists() ? true : file.mkdirs();
	}
}
