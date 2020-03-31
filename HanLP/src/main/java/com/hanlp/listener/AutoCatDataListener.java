package com.hanlp.listener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.hanlp.dto.AutoCatData;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.util.StringUtils;

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
	private static boolean checkAndCreatePath(String path) {
		File file = new File(path);
		return file.exists() ? true : file.mkdirs();
	}

	public static void main(String[] args) throws IOException {
		Map<String, Map<String, String>> totalMap = new HashMap<>();

		Map<String, String> catMap = new HashMap<>();
		catMap.put("涉枪爆风险", "weapon,arms,arm,armament,ordnance,ammunition,ammo,cartridge,fodder,munitions,gun,rifle,spear,firearm,生化武器,Biochemical Weapon,杀伤性武器,weapons of mass destruction,非法&持有&枪,unauthorized possession&gun,非法&携带&枪,Illegal carrying&gun,藏匿&武器,conceal&weapon,藏匿&枪,conceal&gun,伪装&武器,disguise&weapon,伪装&枪,disguise&gun,枪支散件,package,parts,components,spare parts,spares,fittings,手枪,pistol,突击步枪,Assault Rifle,逃避监管&武器,evade&take charge of&weapon,逃避监管&枪,evade&take charge of&gun,邮寄&武器,mail send by post&weapon,邮寄&枪,mail send by post&gun,贩卖&武器,sell&weapon,贩卖&枪,sell&gun,霰弹枪,Shotgun,AK47,武器&走私,弹药&走私,军火&走私,枪支&走私,枪,爆炸&走私,炸药&走私,explosive,硝化甘油炸药,Nitroglycerine,NG，甘油三硝酸酯类混合炸药,铵梯类炸药,Ammonite，含铵梯油炸药,多孔粒状铵油炸药,改性铵油炸药,膨化硝铵炸药,Expanded,AN explosive,粉状铵油,铵松蜡,铵沥蜡,水胶炸药,water gel explosive,乳化炸药,Emulsion,粉状乳化炸药,Powdery emulsive,乳化粒状铵油炸药，重铵油炸药,粘性炸药,退役&火药,震源药柱,Seismic charge,人工影响天气用,燃爆器材,矿岩破碎器材,中继起爆具,Primer,爆炸加工器材,油气井用起爆器,聚能射弹孔,Perforating charge,复合射孔器,Perforator,聚能切割弹,高能气体压裂弹,点火药盒,工业火雷管,Flash detonator,工业电雷管,Electric detonator,导爆管雷管,detonator with shock conducting tube,半导体桥电雷管,电子雷管,Electron delay detonator,油气井用电雷管,地震勘探电雷管,继爆管,其他工业雷管,工业导火索,industrial blasting fuse,工业导爆索,industrial detonating fuse,切割索,linear shaped charge,塑料导爆管,,shock conducting tube,引火线,安全气囊用点火具,特殊用途&点火具,海上救生&烟火信号,TNT、三硝基甲苯,trinitrotoluene,TNT,工业黑索今,RDX，环三亚甲基三硝胺,Hexogen,RDX,苦味酸、三硝基苯酚,picric acib,民用推进剂,太安,pentaerythritol tetranitrate,PETN,奥克托今,octogen,HMX,其他单质猛炸药,explosive compound,黑火药,black power,起爆药,initiating explosive,延期器材,非法枪支");
		catMap.put("管制器具风险", "管制刀具,管制器具,查获&刀具,查获&管制器具");
		catMap.put("两用物项风险", "military,nuclear,hydrogen bomb,missile,两用物项,两用&安全,(dual use)&safety,导弹相关,missile related,核两用品,dual purpose nuclear goods,监控化学品, controlled chemicals,MCC,易制毒化学品,precursor chemicals,化学武器,chemical weapons,麻黄碱,麻黄素,ephedrine,高锰酸钾&毒品,potassium permanganate&drug,放射性同位素&武器,radioisotope&weapon,铀&核武器,U235&nuclear weapon,人兽共患病原体,zoonosis,核&技术,nuclear&technology ,原子能,atomic energy,核&民用,nuclear&civil");
		catMap.put("涉政治类宣传品风险", "台独,藏独,达赖喇嘛,疆独,港独,中华民国,民进党,蔡英文,法轮功,东突,国民党,nationalist party,the Kuomintang,分裂&渗透,divided nation,infiltration,间谍,spy,secret agent,mole,极端&宗教思想,extremes&religion,宗教&印刷品,religion&prints,宗教&音像制品,religion&audiovisual product,邪教&书，heresy&book,89事件,六四事件,香港&暴乱,riot&hong kong,境外势力,Overseas forces,非法&集会,unlawful assembly,民族分裂,national splittism,颠覆&破坏,overthrow,subvert,destroy,ruin,两个中国,示威&集会,demonstrate&rally,问题地图,wrong&map,缴获&反动,capture&reactionary,海外&反动,overseas&reactionary,反华&杂志,(anti China)&magazine,反华&报纸,(anti China)&newspaper,非法出版,illegal publication");
		catMap.put("贸易制裁和进出口管制", "制裁&走私,禁运&走私,朝鲜,sanction,North Korea,Democratic People’s Republic of Korea,贸易制裁,中美贸易战,国际制裁,禁运&制裁,禁运&武器,禁运&半导体,ban&technology,ban&telocom,ban&sanction,embargo&sanctioin,embargo&smuggle,出口管控,出口管制,出口限制,export&control,export&restriction,朝鲜&核计划,导弹计划,导弹试验,伊朗&核,missile&test,nuclear weapon,nuclear&test,nuclear&program,Iran&nuclear,战略物品,strategic&item,restricted&item,sensitive&equipment,sensitive&item,贸易战,贸易摩擦,trade war,trade game,朝鲜&禁运,朝鲜&煤炭,朝鲜&铁,朝鲜&铁矿石,朝鲜&黄金矿,朝鲜&钛矿,朝鲜&钒矿,朝鲜&稀土,朝鲜&矿,朝鲜&航空,朝鲜&油,朝鲜&生铁,朝鲜&火箭,North Korea&embargo,north korea&coal,north korea&iron,north korea&steel,north korea&mine,north korea&titanium,north korea&rare earth,north korea&aviation,north korea&crude,north korea&fuel,单边制裁,极限施压,关税制裁,贸易制裁,反倾销,反补贴,tariff&sanction,trade&sanction,anti dumping,countervailing,伊朗&禁止,伊朗&制裁,伊朗&技术,重要战略资源,进出口许可&走私,进口许可&走私,出口许可&走私,license,稀土,rare earth,逃进口税,逃出口税,逃进出口税,出口骗退税,rare earth,terbium,thorium,toxic waste,cerium group,monazite sand,镧,铈,镨,钕,钷,钐,铕,钆,铽,镝,钬,铒,铥,镱,镥,钇,钪, 独居石,铈硅石,铈铝石,黑稀金矿,磷酸钇矿,氟碳铈镧矿,磷钇石,硅铍钇矿,褐帘石,The import license,opengeneral licence,specific licence,出口许可,氯氟烃&压缩机,CFCs,CFC,HCFC,HCFCs,chlorofluorocarbons&Compressor,脱胶虎骨,Tiger bone,犀牛角,Rhinoceros horn,鸦片汁液,鸦片浸膏,阿片,opium,thebaica,Mekonium,mecon,四氯化碳,carbon tetrachloride,三氯三氟乙烷,trichlorotrifluoroethane,Freon TF,TTE,牛黄,bezoar,calculus bovis,麝香,Musk,麻黄草,HerbaEphedrae,Ephedra Herb,Ephedra sinica Stapf,Nostoc,kонтрафактных,нефрит,янтарь,золото");
		catMap.put("密码产品和设备", "密码&走私,code,coding,加密&传真机,加密&电话机,加密&路由器,加密&交换机,密码机,密码卡,密码&设备,encryptioin,encrypt&fax mashine,encrypt&telephone,encrypt&router,encrypt&switch,passwordcard,password&machine,code&machine,password&device");
		totalMap.put("政治安全", catMap);

		Map<String, String> catMap2 = new HashMap<>();
		catMap2.put("规格品牌异常", "申报规格&海关,申报品牌&海关");
		catMap2.put("数重量异常", "申报数量&海关,申报重量&海关,申报数重量&海关");
		catMap2.put("归类异常", "HS编码&海关&归类异常,HS编码&进出口&归类异常,归类异常&海关,归类异常&海关");
		catMap2.put("价格异常", "价格异常&进出口,价格异常&海关,高价出口&低价进口,High Price&Export,(Low priced)&imports,收汇核销,Receipt of foreign exchange,专利费,patent fee,商标费,trademark fee,杂费&低报,incidental&low report,特许权使用费,royalties,royalty fee,royalty,运费&低报,freight& low report");
		catMap2.put("产终地异常", "伪报原产地,伪报原产国,伪报原产地&进口,伪报原产国&海关,false country of origin");
		catMap2.put("虚假贸易", "虚假贸易,fake trade,空壳公司,Shell company,shell companies,shelf company,Shell corporation,股份变更,Share change,虚假贸易,False trade,重复质押,Repeated pledge,虚假转口贸易,False entrepot trade,假单据 False documents,假报关单,False customs declaration,热钱流入,hot money inflows,汇差套利,Exchange Arbitrage,息差套利,Spreads arbitrage,港口货物吞吐量,TUN , Volume of Freight Handled at Ports,Port cargo throughput,出口交货值,Export Delivery Value,金融机构存贷款数据,Data on deposits and loans of financial institutions,骗取政府补贴,Defrauding government subsidies,诈骗,defraud, swindle,Fraud,出口&骗退税,海关&骗退税,虚开增值税专用发票,VAT invoices,循环进出口,Circulating&import&expor,道具商品,Prop goods,换装&拆装,dismounting");
		catMap2.put("金融管制", "金融管制,银行卡,financial regulation,黄金&走私,香烟&走私,人民币&出境,外汇流失");
		totalMap.put("经济安全", catMap2);

		Map<String, String> catMap3 = new HashMap<>();
		catMap3.put("文物", "文物&走私,古董&走私,antique,culture relic,heritage,trafficking&cultural goods,ancient&smuggle,文物&海关,古董&海关,文物&出境,古董&出境,kультурные ценности");
		catMap3.put("侵权风险", "知识产权&走私,知识产权&海关,property right,Intellectual Property Rights,Intellectual Property Rights&customs,侵权,Intellectual Property Rights/IPR infringement,Intellectual Property Rights/IPR violation,侵权商品,IPR infringing goods,假冒,counterfeit,fake,知识产权执法合作,Intellectual Property Rights/IPR enforcement,海关知识产权保护,知识产权&查获,Intellectual Property Rights/IPR seizure");
		catMap3.put("散发性宗教物品风险", "散发性宗教物品,宗教印刷品,宗教印刷品&进境,religious print");
		catMap3.put("淫秽物品风险", "黄色期刊,黄色刊物,暴力,porn,pornographic,eroticism,sexy,salacity,carnal desire violence,黄色,下流,淫秽,不良文化,精神污染");
		catMap3.put("禁限类印刷音像制品风险", "问题地图,problem map");
		totalMap.put("文化安全", catMap3);

		Map<String, String> catMap4 = new HashMap<>();
		catMap4.put("毒品风险", "毒品,drug,海洛因,可卡因,冰毒,heroin,cocaine,芬太尼,fentanyl,麻醉品,精神药品,非药用麻醉品,新精神活性物质,易制毒化学品,吸毒工具,大麻,氯胺酮,K他命,医用大麻油,曲马多,普瑞巴林,大麻&食品,大麻&按摩油,大麻油,hempseed oil,大麻酚,CBN,四氢大麻酚,THC,大麻二酚,CBD,cannabis,蓝精灵&氟硝西泮,地西泮,Diazepam,三唑仑&“迷魂药”,氟硝西泮,Flunitrazepam,三唑仑,新精神活性物质&催眠药, 新精神活性物质&抗焦虑药,新精神活性物质&麻醉药,新精神活性物质&酒类,新精神活性物质&烟草,新精神活性物质&阿片类,兴奋剂,致幻剂,镇静剂,合成大麻素,卡西酮,cathinone,苯乙胺,Phenethylamine,PEA,苯丙胺衍生物,二甲氧基苯乙胺衍生物,甲基苯丙胺,苯基哌嗪,苄基哌嗪,MDMA,氯胺酮,Ketamine,K粉,摇头丸，Ketamine,恰特草,Catha edulis,阿拉伯茶,埃塞俄比亚茶,色胺,氨基茚,苯环己基胺,小树枝&毒品,大麻叶,迷幻蘑菇,红五,一粒眠,跳跳糖,彩虹烟,神仙水,0号胶囊,红冰,开心果,开心冰,邮票&LSD,麻古,笑气，鸦片,吗啡,苯丙胺,Amphetamine,麻黄碱,ephedrine,卡西酮,Cathinone,甲卡西酮,2C B,聪明药,阿莫达非尼,сильнодействующее ,вещество,наркотик,кокаин,конопля,фентанил,трамадол,опийный мак");
		catMap4.put("毒药及毒性物质风险", "氰化物&进口,有毒化学品&进口,进口氰化物,进口有毒化学品,氰化物&海关,有毒化学品&海关,汞&进境,汞&进口,进口汞,四甲基铅&进境,四甲基铅&进口,四乙基铅&进境,四乙基铅&进口,三丁基锡化合物&进境,三丁基锡化合物&进口,poisonous chemicals");
		catMap4.put("药品", "medicine&illicit，pharmaceutical&illicit,印度药,兴奋剂,精神药物,麻醉药物");
		catMap4.put("进出口食品安全风险", "食品&安全&走私,食品&安全&入境,食品&安全&出境,食品&超标&进境,食品&超标&出境,食品&超标&入境,食品安全,冻品,food safety,frozen meat,冻肉&进口,冻肉&进境,食品&准入&进境,植物源性食品&准入,植物源性食品&进境,植物源性食品&入境,植物源性食品&进口,酒,婴幼儿配方&奶粉,大米,食用&植物油,冻&海鲜,鲜&海鲜,水产,seafood,大豆,Japan,cold chain,农产品&安全,produce&safety,agricultural products&safety,农药残留,pesticide residue,兽药残留,veterinary drug residue,添加剂&安全,additive&safety,粮食&安全,grain&safety,微生物&水果,microorganisms&fruit,乳品&不合格,dairy&safety,核辐射&食品,nuclear radiation&food,致癌性&食品,carcinogenicity&food,草甘膦&超标,glyphosate&excessive,肉&检疫,meat&quarantine,沙门氏菌&检出,salmonellae&detection,二噁英&食品&检出,dioxine&food&detection,浆果&残留,berry&residue,婴儿食品,infant food,养殖业&食品,aquaculture&food,细菌&肉制品,bacterium&meat,腐败&食品,perishable&food,rotted&food,啤酒&不合格,beer&unqualified,红酒&残留,wine&residue,牛肉&欧洲&安全,beef&Europe&safety");
		catMap4.put("危险化学及易燃易爆物品风险", "化学,化工,危险品,化学品,hazardous&chemical,货物事故通知系统,CINS,Cargo Incident Notification System,美国海关及边境保护局,US Customs and Border Protection,澳洲海关及边境保护局,Australian Customs and Border Protection,爆炸物,explosives,易燃&化学, flammable&chemical,气溶胶,aerosol,有机过氧化物,organic peroxides,自燃&化学,pyrophoric&chemical,致癌性&化学品,carcinogenicity&chemical,生殖毒性&化学品,reproductive toxicity&chemical,环境危害&化学品,environmental hazards&chemical,剧毒&化学品,hypertoxic&chemical,自反应物质,self reactive substances,自热&化学,(self heating)&chemical,腐蚀&化学,corrosive&chemical,氧化性&化学,oxidability&chemical,冷冻液化气体,freeze&liquefied gas,压缩气体,compressed gas,危害&环境&化学,harm&environment&chemical,助燃&化学,(combustion supporting)&chemical,爆炸物&海关,易燃物&海关,易燃物&进口,爆炸物&进口");
		catMap4.put("新旧异常", "以旧充新&海关,以旧充新&进口,旧机电&进口,旧机电&海关,装运前检验,进口旧机电,old kinescope,旧显像管,再生显像管,regenerative kinescope,旧监视器, old monitors,旧复印件, old copies,旧玻壳, old glass shell,旧印刷机,old printing press,旧通用显示,old Universal Display,旧发电机组,old generators,");
		catMap4.put("进出口商品安全风险", "商品&安全&出境,商品&安全&入境,商品&安全&进境,商品&安全&进口,商品&安全&出口,动植物检疫,疫病疫情,quarantine of animals and plants,epidemic disease,猪瘟,swine fever,hog cholera,recall,召回,植物产品&准入,植物产品&进境, 植物产品&入境,植物产品&进口,中药材&准入,中药材&进境,中药材&进口,中药材&入境,次氯酸钙&伪报,次氯酸钙&瞒报,次氯酸钙&伪瞒报,漂粉精&出口,漂白粉&出口,chloros,sodium hypochlorite,bleaching powder,进境货物木质包装,辣椒干&加工贸易,辣椒干&进口,辣椒干&出口,recall&Injury&hazard,recall&hazard,recall&injury,recall&safety,consumer&product&safety,召回&伤害,product&safety,玩具&伤害,toys&injury");
		totalMap.put("社会安全", catMap4);

		Map<String, String> catMap5 = new HashMap<>();
		catMap5.put("固体废物风险", "废塑料,废铜,废铝,废铁,废皮,废革,矿渣,废金属,废棉,废电池,废药,废碎料,废橡胶,回收纸,废机电,废电器,废物,矿渣,矿灰,残渣,下脚料,废纺织,waste,scrap,recycled,second hand,secondhand,second hand,洋垃圾,废动植物产品,animal&waste,plant&waste,recyling&plants,recyling&animal,plants&recyclables,硅废碎料,waste&silicon,silicon&scrap,化学品废物,waste&chemical,chemical&scrap,chemical&recycling,废皮革,waste&leather,废玻璃,waste&glass,废金属,废金属化合物,waste&metal,scrap&metal,scarp&compound,废电池,scrap&battery,废&设备,废&零部件,waste&equipment,scrap&parts,合金&废碎料,waste&material,scrap&material,solid waste,recycled&waste,recyclable&waste,garbage&recycle,(semi solid)&waste,waste&plastic,scrap&plastic,recyling&plastic,scrap&copper,scrap&aluminum,recycling&aluminum,recycling&steel,iron&scrap,leather&scrap,leather&remnant,slag&waste,slag&scrap,cotton&scrap,fabric&scrap,fabric&strip,recycling&fabric,heathcare&waste,medical&waste,hazardous waste,,wate&rubber,scrap&rubber,recycling&rubber,recycled&paper,waste&paper,wate&electricalmwaste&electronic equipment,WEEE,return&waste,refusal&waste,return&scrap,reject&waste,reject&scrap,reject&trash,ban&waste,ban&scrap,ban&waste disposal,waste&restriction,restrict&waste,remove&waste,退运&废物,走私&废物,禁止进口&废物,限制进口&废物");
		catMap5.put("消耗臭氧层", "消耗臭氧层,ODS,ozone,HCFCs");
		catMap5.put("濒危野生动植物及其制品风险", "濒危,象牙,鹦鹉螺,红木,紫檀,endangered,ivory,nautilus,mahogany,sandalwood,犀牛角,穿山甲,rhino horns,pangolin,totoaba,石首鱼,血檀,濒危珊瑚,濒危贝壳,海马干,虎骨,金钱鳘,沉香,precious coral,red coral,Corallium,象牙贸易,藏红花,金钱鳌,黄檀,saffron,corvina,drumfish,crocus,blood sandalwood,eaglewood,red sandalwood,砗磲,Tridacna,Tridacnidae,盔犀鸟,Rhinoplax vigil,Helmeted hornbill,犀牛角,rhinoceros horn,玳瑁,Hawksbill Turtle,Eretmochelys imbricata,濒危&鹦鹉蛋,parrot egg,CITES,Nautiloidea,bloodwood,padauk,Bixa orellana,Redwood,pterocarpus,Rhinoceros horn,Manis,Pterocarpus tinctorius,Endangered Corals,tiger&bone,Ivory trade,野生,野生动物,保育动物,野生动物走私,野生动物犯罪,野生动物非法贸易,野生动物鉴定,贩卖,走私,保护动物,濒危物种,象牙,狮子骨,犀牛角,穿山甲,穿山甲鳞片,赛加羚羊,盔犀鸟,陆龟,海龟,猛禽,港澳台&野生动物,虎&狮&豹制品,虎皮,虎尸,燕窝,大猫,砗磲,珊瑚,海马干,干鱼翅,唐冠螺,加州石首鱼,Wildlife,wildlife rescue,wildlife seizure,wildlife trade,wildlife trafficking,hunting trophies,smuggling,illegal trade of wildlife,China wildlife,China wildlife seizure,endangered species,CITES China,elephant,wild elephant,ivory,ivory seized,ivory trade,tiger,pangolin,pangolin seized,pangolin scale,pangolin trade,rhino,rhino horn,rhino trade,rhino poaching,anti rhino poaching,tortoise,tortoise trade,Turtle,turtle trade,wild view,trophy hunting,malaysia&ivory,Singapore&pangolin,Singapore&ivory,Vietnam&pangolin, Vietnam&ivory,Vietnam wildlife,Vietnam wildlife seizure,CITES,WCS,latf");
		catMap5.put("合法捕捞", "非法&捕捞,fish&illegal,fishing&illegal,非法捕捞,IUU,IUU fishing,禁渔区,forbidden fishing zone,禁渔期,休渔期&非法,Fishing closed season,禁用渔具,prohibited fishing gears,拖曳泵吸耙刺");
		catMap5.put("动物及其产品安全风险", "动物产品&进境,动物&进境,进境动植物产品检疫许可证,原皮&进境,原毛炭疽杆菌&进境,动物尸体&进境,动物产品&进出口,动物&进出口");
		catMap5.put("植物及其产品安全风险", "进境动植物产品检疫许可证,种子&进境,种苗&进境,苗木&进境");
		catMap5.put("核生化风险", "核生化武器&进境,核污染&进境,放射性物质&进境,核生化武器&海关,核污染&海关,放射性物质&海关");
		catMap5.put("血液、试剂、微生物等特殊物品", "微生物&进境,血液&进境,试剂&进境,人体组织&进境,生物制品&进境,土壤&进境,动植物病原体&进境,转基因&进境,出入境血液,出入境试剂,出入境微生物,出入境尸体,出入境棺柩,出入境骸骨,出入境特殊物品,出入境遗体,出入境骸骨,出入境骨灰");
		catMap5.put("公共卫生风险", "口岸&卫生,口岸&安全,health,hygiene,sanitation,hygienism,security,safety,登革热,dengue,埃博拉,Ebola,基孔肯雅热,Chikungunya,黄热病,YellowFever,拉沙热,Lassa fever,霍乱,cholera,中东呼吸综合征,Middle East Respiratory Syndrome,MERS,寨卡,Zika,鼠疫,plague,禽流感,influenza in birds,牛血清,BSA,Bovine Serum,胎牛血清,fetal bovine serum,FBS,fetal calf serum,FCS,生物安全,病媒,生物入侵,有害物种,biological&invasion,invasive&species,hazardous&species,猪瘟,hog cholera,swine fever,马瘟,horse sickness,炭疽,anthrax,口蹄疫,FMD,疯牛病,BSE,草地贪夜蛾,Spodoptera frugiperda");
		totalMap.put("生态安全", catMap5);

		Map<String, String> catMap6 = new HashMap<>();
		catMap6.put("海关", "海关,进境,出境,进口,出口,外贸,检验检疫,走私,海关查获,边境,进出口,进出境,customs,border,import,imported,importing,export,exporting,exported,smuggle,smuggling,smuggled,inspection and quarantine,import and export,商品*安全*出境,商品*安全*入境,商品*安全*进境,商品*安全*进口,商品*安全*出口,动植物检疫,疫病疫情,quarantine of animals and plants,epidemic disease,recall,植物产品*准入,植物产品*进境,植物产品*入境,植物产品*进口,中药材*准入,中药材*进境,中药材*进口,中药材*入境,次氯酸钙*伪报,次氯酸钙*瞒报,次氯酸钙*伪瞒报,漂粉精*伪瞒报,漂白粉*伪瞒报,进境货物木质包装,recall*Injury*hazard,recall*hazard,recall*injury,recall*safety,consumer*product*safety,召回*伤害*出口,召回*伤害*进口,食品*安全*走私,食品*安全*入境,食品*安全*出境,食品*超标*进境,食品*超标*出境,食品*超标*入境,冻品*走私,冻肉*走私,食品*准入*进境,植物源性食品*准入,植物源性食品*进境,植物源性食品*入境,植物源性食品*进口,二噁英*食品*检出,dioxine*food*detection,危险品*伪瞒报,进出口许可*走私,进口许可*走私,出口许可*走私,逃进口税,逃出口税,逃进出口税,出口骗退税,黄金*走私,香烟*走私,人民币*出境*海关,出口*骗退税,海关*骗退税,逃进出口税,边民互市*走私,侵权商品*查获,知识产权执法合作,Intellectual Property Rights/IPR enforcement,海关知识产权保护,知识产权*查获,Intellectual Property Rights/IPR seizure,知识产权*走私,知识产权*海关,文物*走私,古董*走私,trafficking*cultural goods,ancient*smuggle,文物*海关,古董*海关,文物*出境,古董*出境,猪瘟,hog cholera,swine fever,马瘟,horse sickness,炭疽,anthrax,口蹄疫,疯牛病,登革热,dengue,埃博拉,Ebola,基孔肯雅热,Chikungunya,黄热病,YellowFever,拉沙热,Lassa fever,霍乱,cholera,中东呼吸综合征,Middle East Respiratory Syndrome,MERS,寨卡,Zika,鼠疫,plague,濒危物种,象牙贸易,洋垃圾*进口,香港*冲突,香港*暴力,香港,Hong Kong,Hongkong,偷渡,smuggle,lorry,卡车,lorry death,煤炭*走私,煤炭*朝鲜,煤炭*走私*朝鲜,国际制裁,coal*smuggle,fuel*smuggle,fuel*North Korea,international sanctions,стратегический важный ресурс,МАПП,公路口岸,ЖАПП,铁路口岸,забайкальский край,后贝加尔边疆区,Федеральная таможенная служба,俄罗斯海关总署,Таможня,海关,задержали,查扣,Обнаружили,查获,Сибирское таможенное управление 西伯利亚海关局,Дальневосточное таможенное управление,远东海关局,Биробиджанская таможня,比罗比詹海关,Благовещенская таможня,布拉戈维申斯克海关,Бурятская таможня,布里亚特海关,Владивостокская таможня,海参崴海关,Камчатская таможня,楚科奇海关,Магаданская таможня,马加丹海关,Находкинская таможня,纳霍德卡海关,Сахалинская таможня,萨哈林海关,Уссурийская таможня,乌苏里斯克海关,Хабаровская таможня,哈巴罗夫斯克海关,Читинская таможня,赤塔海关,税関,密輸する,lén");
		totalMap.put("海关", catMap6);

		String filePath = "/Users/wangjie/Development/iso/share/ckm/customs";

		Map<String, String> keyWordMap = new HashMap<>();

		// 不需要提示有变化的
		Pattern pattern4 = Pattern.compile("&");
		List<String> className = new ArrayList<>();
		for (Map.Entry<String, Map<String, String>> bigClass : totalMap.entrySet()) {
			for (Map.Entry<String, String> secondClass : bigClass.getValue().entrySet()) {
				String[] keyWords = secondClass.getValue().split("[,，+]");
				checkAndCreatePath(String.format("%s%s%s", filePath, File.separator, bigClass.getKey()));
				className.add(String.format("%s\\%s", bigClass.getKey(), secondClass.getKey()));

				List<String> keyWordList = new ArrayList<>();
				for (String keyWord : keyWords) {
					if (StringUtils.isEmpty(keyWord)) {
						continue;
					}
					// 去除边界空格
					keyWord = keyWord.trim();

					// 不需要匹配到的提示
					Matcher matcher4 = pattern4.matcher(keyWord);
					keyWord = matcher4.replaceAll("*");

//					不考虑重复关键词问题
//					if (keyWordMap.containsKey(keyWord) && !"海关".equalsIgnoreCase(bigClass.getKey())) {
//						// 存在重复关键词
////						System.out.println(String.format("存在重复关键词：\n\t%s\n\t%s", keyWordMap.get(keyWord),
////								String.format("一级大类:[%s] 二级大类:[%s] 关键词:[%s] 处理之后的关键词:[%s]", bigClass.getKey(), secondClass.getKey(), before, keyWord)));
//						continue;
//					}
//					keyWordMap.put(keyWord, String.format("一级大类:[%s] 二级大类:[%s] 处理之后的关键词:[%s]", bigClass.getKey(), secondClass.getKey(), keyWord));

					keyWordList.add(keyWord);
				}
				File file = new File(String.format("%s%s%s%s%s.rul", filePath, File.separator, bigClass.getKey(), File.separator, secondClass.getKey()));
				IOUtils.writeLines(keyWordList, null, new FileOutputStream(file), Charset.forName("GBK"));
			}
		}
		File file = new File(String.format("%s%s%s", filePath, File.separator, "类别.lst"));
		IOUtils.writeLines(className, null, new FileOutputStream(file), Charset.forName("GBK"));
		File charNum = new File(String.format("%s%s%s", filePath, File.separator, "字数.txt"));
		IOUtils.write("-1 1", new FileOutputStream(charNum), StandardCharsets.UTF_8);
		File configrule = new File(String.format("%s%s%s", filePath, File.separator, "字数.txt"));
		List<String> configruleList = Arrays.asList("[cat_sen]", "yes",
				"[cat_sen_value]", "no",
				"[cat_sen_rulenums]", "no",
				"[cat_txt_rulenums]", "no",
				"[keep_rules_len]", "8192");
		IOUtils.writeLines(configruleList, null, new FileOutputStream(configrule), StandardCharsets.UTF_8);
	}
}
