package layout;

import java.util.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class RequestData {
	
	private RequestType rt;
	private int computer_id;
	private String computer_name;
	private Timestamp introduced_date;
	private Timestamp discontinued_date;
	private int company_id;

	public RequestData(RequestType rt, String[] fields) {
		this.rt = rt;
		if(fields != null)
			setupFields(fields);
	}
	
	public void setupFields(String[] fields) {
		if(rt == RequestType.SHOW_COMPUTER || rt == RequestType.DELETE) {
			computer_id = Integer.parseInt(fields[0]);
		}
		else if(rt == RequestType.CREATE || rt == RequestType.UPDATE) {
			computer_name = fields[0];
			introduced_date = parseTimestamp(fields[1]);
			discontinued_date = parseTimestamp(fields[2]);
			company_id = Integer.parseInt(fields[3]);
		}
	}
	
	public Timestamp parseTimestamp(String s) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date parsedDate = dateFormat.parse(s);
			Timestamp timestamp = new Timestamp(parsedDate.getTime());
			return timestamp;
		}
		catch(ParseException e) {
			return null;
		}
	}

	public RequestType getRequestType() {
		return rt;
	}

	public void setRequestType(RequestType rt) {
		this.rt = rt;
	}

	public String getComputer_name() {
		return computer_name;
	}

	public void setComputer_name(String computer_name) {
		this.computer_name = computer_name;
	}

	public Timestamp getIntroducedDate() {
		return introduced_date;
	}

	public void setIntroducedDate(Timestamp introduced_date) {
		this.introduced_date = introduced_date;
	}

	public Timestamp getDiscontinuedDate() {
		return discontinued_date;
	}

	public void setDiscontinuedDate(Timestamp discontinued_date) {
		this.discontinued_date = discontinued_date;
	}

	public int getCompany_id() {
		return company_id;
	}

	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}

	public int getComputerId() {
		return computer_id;
	}

	public void setComputerId(int computer_id) {
		this.computer_id = computer_id;
	}
	
}
