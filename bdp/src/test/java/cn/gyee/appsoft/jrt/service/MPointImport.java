package cn.gyee.appsoft.jrt.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import appsoft.util.StrUtil;
import appsoft.wpcache.data.DbTool;

public class MPointImport {
	private List<MPoint> mps;//测点模板
	private int lastId;//表gyee_equipmentmeasuringpoint的主键最大值
	private List<HashMap<String, String>> equipmentidset;
	private DbTool dbtool ;
	public MPointImport() {
		dbtool= DbTool.getDbTool();
		
	}

	public static void main(String[] args) throws Exception{
		MPointImport mpointimport = new MPointImport();
		mpointimport.startInsert();
	}
	
	public void startInsert() throws Exception{
		String excelPath="F:/wangliang/风电项目/项目准备/基础数据配置/excel/gyee_equipmentmeasuringpoint.xlsx";
		initMpoints(excelPath,2);
		initRefMysqlInfo("02");
		for (HashMap<String, String> equipmentidmap:equipmentidset) {
			String equipmentId = equipmentidmap.get("id");
			String lineId= equipmentidmap.get("lineid");
			String projectId= equipmentidmap.get("projectid");
			String stationid =  equipmentidmap.get("powerstationid");
			String typeid =equipmentidmap.get("typeid");
			insertOneEquipmentPoint(equipmentId, lineId, projectId,stationid,typeid);
		}
	}
	public void initRefMysqlInfo(String powerstationid) throws Exception{
		String sql="select MAX(id) lastid from gyee_equipmentmeasuringpoint";
		ArrayList<HashMap<String, String>> rs = dbtool.listAll(sql);
		this.lastId=Integer.valueOf(rs.get(0).get("lastid"));
		sql="select id,lineId,projectId,powerstationid,typeid from gyee_equipment where powerstationid='"+powerstationid+"'";
		equipmentidset =  dbtool.listAll(sql);
	}
	public void insertOneEquipmentPoint(String equipmentId,String lineId,String projectId,String stationid,String typeId) throws Exception{
		for(MPoint mp:mps){
			Equipmentmeasuringpoint equipmentmeasuringpoint = new Equipmentmeasuringpoint();
			this.lastId++;
			equipmentmeasuringpoint.setId(this.lastId);
			equipmentmeasuringpoint.setIoType("AI");
			equipmentmeasuringpoint.setName(mp.name);
			equipmentmeasuringpoint.setPointDefinition(mp.pointDefinition);
			if(StrUtil.isNotEmpty(mp.pointDefinition)){
				equipmentmeasuringpoint.setIsCache(1);
				if(mp.pointDefinition.equalsIgnoreCase("jssta")
						||mp.pointDefinition.equalsIgnoreCase("avaitoday")
						||mp.pointDefinition.equalsIgnoreCase("grifry")
						||mp.pointDefinition.equalsIgnoreCase("jsnfdl")
						||mp.pointDefinition.equalsIgnoreCase("jsyfdl")){
					equipmentmeasuringpoint.setIsCalc(1);
				}else{
					equipmentmeasuringpoint.setIsCalc(0);
				}
			}
			String realtimeid = "P0000."+equipmentId+"."+this.lastId;
			equipmentmeasuringpoint.setRealTimeId(realtimeid);
			equipmentmeasuringpoint.setEquipmentId(equipmentId);
			equipmentmeasuringpoint.setEquipmentComponentId(mp.equipmentComponentId);
			equipmentmeasuringpoint.setLineId(lineId);
			equipmentmeasuringpoint.setPowerStationId(stationid);
			equipmentmeasuringpoint.setProjectId(projectId);
			equipmentmeasuringpoint.setTypeId(typeId);
			equipmentmeasuringpoint.setPointSort(mp.pointSort);
			equipmentmeasuringpoint.setEnName(mp.enName);
			insertBysql(equipmentmeasuringpoint.toSql());
		}
	}
	public void insertBysql(String sql) throws Exception{
		System.out.println("insert:"+sql);
		dbtool.updateDb(sql);
	}
	/**
	 * 初始化模板测点集合
	 * @param excelPath
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void initMpoints(String excelPath,int sheetIndex) throws FileNotFoundException, IOException{
		 Workbook wb = new XSSFWorkbook(new FileInputStream(new File(excelPath)));
		 Sheet sheet1 = wb.getSheetAt(sheetIndex);
		 mps = new ArrayList<MPoint>();
		    for (int i=1;i<= sheet1.getLastRowNum();i++) {
		    	Row row = sheet1.getRow(i);
		    	Cell c0 = row.getCell(0);
		    	Cell c1 = row.getCell(1);
		    	Cell c2 = row.getCell(2);
		    	Cell c3 = row.getCell(3);
		    	Cell c4 = row.getCell(4);
		    	MPoint mp = new MPoint();
		    	if(c0!=null)mp.name=c0.getStringCellValue();
		    	if(c1!=null)mp.pointDefinition=c1.getStringCellValue();
		    	if(c2!=null)mp.equipmentComponentId=c2.getStringCellValue();
		    	if(c3!=null)mp.pointSort=Double.valueOf(c3.getNumericCellValue()+"").intValue();
		    	if(c4!=null)mp.enName=c4.getStringCellValue();
		    	System.out.println(mp.toString());
		    	mps.add(mp);
		        }
		        System.out.println();
		    }
		
}



class Equipmentmeasuringpoint{
	private Integer id;private String ioType;private Integer isCalc;
	private String longId;private String name;private Integer orderNo;
	private String pointDefinition;private String realTimeId;
	private String remark;private String shortId;private String unit;
	private String equipmentId;
	private String equipmentComponentId;private String lineId;
	private String mptId;private String pointId;
	private String powerStationId;private String projectId;
	private String typeId;private Integer isCache;
	private Integer pointSort;private String yxErrcode;private String enName;
	private String baseInsertSQL;
	public Equipmentmeasuringpoint() {
		baseInsertSQL="INSERT INTO gyee_equipmentmeasuringpoint (id,ioType,isCalc,longId,name,orderNo,"
				+ "pointDefinition,realTimeId,remark,shortId,unit,equipmentId,equipmentComponentId,"
				+ "lineId,mptId,pointId,powerStationId,projectId,typeId,isCache,pointSort,yxErrcode,enName)"
				+ " VALUES (";
	}
	
	public String toSql(){
		String insertSql =baseInsertSQL;
		insertSql+=this.getId()+",";insertSql+=this.getIoType()+",";insertSql+=this.isCalc+",";
		insertSql+=this.getLongId()+",";insertSql+=this.getName()+",";insertSql+=this.getOrderNo()+",";
		insertSql+=this.getPointDefinition()+",";insertSql+=this.getRealTimeId()+",";
		insertSql+=this.getRemark()+",";insertSql+=this.getShortId()+",";
		insertSql+=this.getUnit()+",";insertSql+=this.getEquipmentId()+",";insertSql+=this.getEquipmentComponentId()+",";
		insertSql+=this.getLineId()+",";insertSql+=this.getMptId()+",";insertSql+=this.getPointId()+",";
		insertSql+=this.getPowerStationId()+",";insertSql+=this.getProjectId()+",";insertSql+=this.getTypeId()+",";
		insertSql+=this.getIsCache()+",";insertSql+=this.getPointSort()+",";insertSql+=this.getYxErrcode()+",";
		insertSql+=this.getEnName();
		insertSql+=" )";
		return insertSql;
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Equipmentmeasuringpoint [id=" + id + ", ioType=" 
	+ ioType + ", isCalc=" + isCalc + ", longId=" + longId + ", name="
				+ name + ", orderNo=" + orderNo + ", pointDefinition="
	+ pointDefinition + ", realTimeId=" + realTimeId + ", remark="
				+ remark + ", shortId=" + shortId + ", unit=" + unit 
				+ ", equipmentId=" + equipmentId + ", equipmentComponentId="
				+ equipmentComponentId + ", lineId=" + lineId + ", mptId="
				+ mptId + ", pointId=" + pointId + ", powerStationId=" 
				+ powerStationId + ", projectId=" + projectId + ", typeId=" 
				+ typeId + ", isCache=" + isCache + ", pointSort=" + pointSort 
				+ ", yxErrcode=" + yxErrcode + ", enName=" + enName + "]";
	}

	public String convert(String str){
		if(str!=null){
			return "\'"+str+"\'";
		}else{
			return str;
		}
	}
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the ioType
	 */
	public String getIoType() {
		return ioType;
	}

	/**
	 * @param ioType the ioType to set
	 */
	public void setIoType(String ioType) {
		this.ioType = convert(ioType);
	}

	/**
	 * @return the isCalc
	 */
	public Integer getIsCalc() {
		return isCalc;
	}

	/**
	 * @param isCalc the isCalc to set
	 */
	public void setIsCalc(Integer isCalc) {
		this.isCalc = isCalc;
	}

	/**
	 * @return the longId
	 */
	public String getLongId() {
		return this.getRealTimeId();
	}

	/**
	 * @param longId the longId to set
	 */
	public void setLongId(String longId) {
		this.longId = convert(longId);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = convert(name);
	}

	/**
	 * @return the orderNo
	 */
	public Integer getOrderNo() {
		return orderNo;
	}

	/**
	 * @param orderNo the orderNo to set
	 */
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * @return the pointDefinition
	 */
	public String getPointDefinition() {
		return pointDefinition;
	}

	/**
	 * @param pointDefinition the pointDefinition to set
	 */
	public void setPointDefinition(String pointDefinition) {
		this.pointDefinition = convert(pointDefinition);
	}

	/**
	 * @return the realTimeId
	 */
	public String getRealTimeId() {
		return realTimeId;
	}

	/**
	 * @param realTimeId the realTimeId to set
	 */
	public void setRealTimeId(String realTimeId) {
		this.realTimeId = convert(realTimeId);
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = convert(remark);
	}

	/**
	 * @return the shortId
	 */
	public String getShortId() {
		return this.getRealTimeId();
	}

	/**
	 * @param shortId the shortId to set
	 */
	public void setShortId(String shortId) {
		this.shortId = convert(shortId);
	}

	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * @return the equipmentId
	 */
	public String getEquipmentId() {
		return equipmentId;
	}

	/**
	 * @param equipmentId the equipmentId to set
	 */
	public void setEquipmentId(String equipmentId) {
		this.equipmentId = convert(equipmentId);
	}

	/**
	 * @return the equipmentComponentId
	 */
	public String getEquipmentComponentId() {
		return equipmentComponentId;
	}

	/**
	 * @param equipmentComponentId the equipmentComponentId to set
	 */
	public void setEquipmentComponentId(String equipmentComponentId) {
		this.equipmentComponentId = convert(equipmentComponentId);
	}

	/**
	 * @return the lineId
	 */
	public String getLineId() {
		return lineId;
	}

	/**
	 * @param lineId the lineId to set
	 */
	public void setLineId(String lineId) {
		this.lineId = convert(lineId);
	}

	/**
	 * @return the mptId
	 */
	public String getMptId() {
		return mptId;
	}

	/**
	 * @param mptId the mptId to set
	 */
	public void setMptId(String mptId) {
		this.mptId = convert(mptId);
	}

	/**
	 * @return the pointId
	 */
	public String getPointId() {
		return pointId;
	}

	/**
	 * @param pointId the pointId to set
	 */
	public void setPointId(String pointId) {
		this.pointId = convert(pointId);
	}

	/**
	 * @return the powerStationId
	 */
	public String getPowerStationId() {
		return powerStationId;
	}

	/**
	 * @param powerStationId the powerStationId to set
	 */
	public void setPowerStationId(String powerStationId) {
		this.powerStationId = convert(powerStationId);
	}

	/**
	 * @return the projectId
	 */
	public String getProjectId() {
		return projectId;
	}

	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(String projectId) {
		this.projectId = convert(projectId);
	}

	/**
	 * @return the typeId
	 */
	public String getTypeId() {
		return typeId;
	}

	/**
	 * @param typeId the typeId to set
	 */
	public void setTypeId(String typeId) {
		this.typeId = convert(typeId);
	}

	/**
	 * @return the isCache
	 */
	public Integer getIsCache() {
		return isCache;
	}

	/**
	 * @param isCache the isCache to set
	 */
	public void setIsCache(Integer isCache) {
		this.isCache = isCache;
	}

	/**
	 * @return the pointSort
	 */
	public Integer getPointSort() {
		return pointSort;
	}

	/**
	 * @param pointSort the pointSort to set
	 */
	public void setPointSort(Integer pointSort) {
		this.pointSort = pointSort;
	}

	/**
	 * @return the yxErrcode
	 */
	public String getYxErrcode() {
		return yxErrcode;
	}

	/**
	 * @param yxErrcode the yxErrcode to set
	 */
	public void setYxErrcode(String yxErrcode) {
		this.yxErrcode = convert(yxErrcode);
	}

	/**
	 * @return the enName
	 */
	public String getEnName() {
		return enName;
	}

	/**
	 * @param enName the enName to set
	 */
	public void setEnName(String enName) {
		this.enName = convert(enName);
	}
	
}

class MPoint{
	public String name;
	public String pointDefinition;
	public String equipmentComponentId;
	public String enName;
	public Integer pointSort;
	public MPoint() {
	}
	public MPoint( String name,String pointDefinition,String equipmentComponentId,int pointSort,String enName) {
		this.name=name;
		this.pointDefinition=pointDefinition;
		this.equipmentComponentId=equipmentComponentId;
		this.pointSort=pointSort;
		this.enName=enName;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MPoint [name=" + name + ", pointDefinition=" + pointDefinition + ", equipmentComponentId=" + equipmentComponentId + ", enName=" + enName + ", pointSort=" + pointSort + "]";
	}
	
}
