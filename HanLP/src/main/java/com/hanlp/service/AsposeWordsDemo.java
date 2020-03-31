//package com.hanlp.service;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//
//import com.aspose.words.Document;
//import com.aspose.words.FontSettings;
//import com.aspose.words.License;
//import com.aspose.words.SaveFormat;
//
///**
// * Title:
// * Description:
// * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
// * Company:北京拓尔思信息技术股份有限公司(TRS)
// * Project: SpringBootDemo
// * Author: 王杰
// * Create Time:2020/2/11 11:54
// */
//public class AsposeWordsDemo {
//
//	public static boolean getLicence() {
//		boolean result = false;
//		License license = new License();
//		try {
//			license.setLicense(new FileInputStream("/Users/wangjie/Downloads/license.xml"));
//			result = true;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
//
//	public static void doc2pdf(String address) {
//		if (!getLicence()) {
//			return;
//		}
//		try {
//			long old = System.currentTimeMillis();
//			File file = new File("/Users/wangjie/Downloads/2.pdf");
//			File file1 = new File("/Users/wangjie/Downloads/3.pdf");
//			Document document = new Document(address);
//			FontSettings.getDefaultInstance().setFontsFolder("/Users/wangjie/Development/项目/海关/字体/字体/", false);
//			document.save(new FileOutputStream(file), SaveFormat.PDF);
//			document.save(new FileOutputStream(file1), SaveFormat.PDF);
//			System.out.println((System.currentTimeMillis() - old) / 1000.00);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static void main(String[] args) {
//		doc2pdf("/Users/wangjie/Downloads/新增报告 (2).docx");
//	}
//
//}
