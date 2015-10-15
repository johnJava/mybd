/**
 * <p>文件名:	PointData.java</p>
 * <p>版权:  </p>
 * <p>公司:	company Co., Ltd.</p>
 * <p>项目名：gyee.jedna</p>
 * <p>作者：	刘厦(liusha.information@gmail.com)</p>
 */
package cn.gyee.appsoft.jrt.model;

/**
 * <p>
 * 		PointData
 * </p>
 * <p>
 * 		说明：获取测点值对象
 * </p>
 * 
 * @author 刘厦(liusha.information@gmail.com)
 * @version 0.0.0
 * <table style="border:1px solid gray;">
 *   <tr>
 *     <th width="100px">版本号</th>
 * 	   <th width="100px">动作</th>
 *     <th width="100px">修改人</th>
 *     <th width="100px">修改时间</th>
 *   </tr>
 *          <!-- 以 Table 方式书写修改历史 -->
 *   <tr>
 *      <td>0.0.0</td>
 *      <td>创建类</td>
 *      <td>刘厦</td>
 *      <td>2013-12-9下午4:34:03</td>
 *   </tr>
 *   <tr>
 *      <td></td>
 *      <td></td>
 *      <td></td>
 *      <td></td>
 *   </tr>
 * </table>
 */
public class PointData implements Comparable<PointData>{
	
	/**
	 * 测点ID
	 */
	private String pointId;
	
	/**
	 * 测点值
	 */
	private Double value;
	
	/**
	 * UTC时间
	 */
	private Integer utcTime;
	
	/**
	 * 毫秒秒数
	 */
	private Integer msTime;
	
	/**
	 * 状态
	 */
	private Short status;
	
	/**
	 * 
	 * 构造器
	 */
	public PointData() {
		
	}

	/**
	 * 
	 * 构造器
	 * @param pointId
	 * @param value
	 * @param utcTime
	 * @param msTime
	 * @param status
	 */
	public PointData(String pointId, Double value, Integer utcTime,
			Integer msTime, Short status) {
		super();
		this.pointId = pointId;
		this.value = value;
		this.utcTime = utcTime;
		this.msTime = msTime;
		this.status = status;
	}

	public Integer getUtcTime() {
		return utcTime;
	}

	public void setUtcTime(Integer utcTime) {
		this.utcTime = utcTime;
	}

	public Integer getMsTime() {
		return msTime;
	}

	public void setMsTime(Integer msTime) {
		this.msTime = msTime;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String getPointId() {
		return pointId;
	}

	public void setPointId(String pointId) {
		this.pointId = pointId;
	}

	@Override
	public int compareTo(PointData o) {
		if(this.utcTime < o.getUtcTime()){
			return -1;
		}else{
			return 1;
		}
	}
}
