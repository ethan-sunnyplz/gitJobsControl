package jobs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.ErrorCode;
import util.SystemException;

import com.ibatis.sqlmap.client.SqlMapClient;

public class JobsControllerDAO {
	private SqlMapClient sqlMapClient;

	public JobsControllerDAO(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}
	
	public List readRunningJobList() {
		try {
			Map tempMap = new HashMap();
			return sqlMapClient.queryForList("jobsController.readRunningJobList",tempMap);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new SystemException(ErrorCode.DB_SELECT_ERROR, null);
		}
	}
	
	public Map getJobPeriodInfo(Map runningJobMap){
		try {
			return (Map) sqlMapClient.queryForObject("jobsController.getJobPeriodInfo", runningJobMap);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new SystemException(ErrorCode.DB_SELECT_ERROR, null);
		}
	}
	
	public Map getSessionInfo(Map jobInfoMap){
		try {
			return (Map) sqlMapClient.queryForObject("jobsController.getSessionInfo", jobInfoMap);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new SystemException(ErrorCode.DB_SELECT_ERROR, null);
		}
	}
	
	public void closeSession(String text){
		try {
			sqlMapClient.update("jobsController.closeSession", text);
		} catch (Throwable t) {
			t.printStackTrace();
			Object args[] = new Object[2];
			args[0] = "JobsControllerDAO.CloseSession";
			throw new SystemException(ErrorCode.DB_UPDATE_ERROR , args);
		}
	}
}
