package compiere.model.promociones.process.model;

public class XX_Store {
	
	private String id="";
	private String ip="";
	private String user="";
	private String pass="";
	private String root="";
	private String put="";
	
	public XX_Store (String id, String ip, String user, String pass, String root, String put){
		this.id=id;
		this.ip=ip;
		this.user=user;
		this.pass=pass;
		this.root=root;
		this.put=put;
	}

	public String getId() {
		return id;
	}

	public String getIp() {
		return ip;
	}

	public String getUser() {
		return user;
	}

	public String getPass() {
		return pass;
	}

	public String getRoot() {
		return root;
	}

	public String getPut() {
		return put;
	}
}
