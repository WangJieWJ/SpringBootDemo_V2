package com.hanlp.listener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.hanlp.dto.ExportExcelDto;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020/3/6 09:45
 */
public class ExportExcelDtoListener extends AnalysisEventListener<ExportExcelDto> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExportExcelDtoListener.class);

	List<ExportExcelDto> exportExcelDtoList = new ArrayList<>();

	@Override
	public void invoke(ExportExcelDto exportExcelDto, AnalysisContext analysisContext) {
		if (Objects.equals("true", exportExcelDto.getSuccess())) {
			exportExcelDto.setTitle(exportExcelDto.getTitle().replaceAll("[\\t\\n\\r↵]", ""));
			exportExcelDto.setRiskContent(exportExcelDto.getRiskContent().replaceAll("[\\t\\n\\r↵]", ""));
			exportExcelDtoList.add(exportExcelDto);
		}
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext analysisContext) {
		String storeFileName = String.format("/Users/wangjie/Development/项目/海关/Word数据结构化/海关提供的语料/%s.txt", analysisContext.readSheetHolder().getSheetName());
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(new File(storeFileName));
			System.out.println(exportExcelDtoList.size());
			IOUtils.writeLines(exportExcelDtoList.stream().map(exportExcelDto -> String.format("%s。%s", exportExcelDto.getTitle(), exportExcelDto.getRiskContent())).collect(Collectors.toList()), "\n", fileOutputStream, "UTF-8");
			fileOutputStream.flush();
		} catch (IOException e) {
			LOGGER.error("报错:", e);
		}
	}

	public static void main(String[] args) {
		String exportExcelDto = "/Users/wangjie/Development/项目/海关/Word数据结构化/海关提供的语料/汇总终版.xlsx";
		ExcelReader excelReader = EasyExcel.read(exportExcelDto).build();
		ReadSheet readSheet1 =
				EasyExcel.readSheet(1).head(ExportExcelDto.class).registerReadListener(new ExportExcelDtoListener()).build();
		ReadSheet readSheet2 =
				EasyExcel.readSheet(2).head(ExportExcelDto.class).registerReadListener(new ExportExcelDtoListener()).build();
		excelReader.read(readSheet1, readSheet2);
		excelReader.finish();
	}
}
