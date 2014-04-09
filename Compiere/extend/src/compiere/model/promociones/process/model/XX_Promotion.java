package compiere.model.promociones.process.model;

public class XX_Promotion {
	private int id=0;
	private String type="";
	private String startDate=null;
	private String startTime=null;
	private String endDate=null;
	private String endTime=null;
	private int priority=0;
	private String sync="";
	private String act="";
	
	public int getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getEndTime() {
		return endTime;
	}

	public int getPriority() {
		return priority;
	}

	public XX_Promotion (int id, String type, String startDate, String startTime, String endDate
			, String endTime, int priority, String sync, String act){
		this.id=id;
		this.type=type;
		this.startDate=startDate;
		this.startTime=startTime;
		this.endDate=endDate;
		this.endTime=endTime;
		this.priority=priority;
		this.sync=sync;
		this.act=act;
	}
}
