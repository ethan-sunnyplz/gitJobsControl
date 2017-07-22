package jobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.SqlConfig;

import com.ibatis.sqlmap.client.SqlMapClient;


public class JobsController {
	
	private static Log log = LogFactory.getLog(JobsController.class);
	private static SqlMapClient sqlMapClient = SqlConfig.getSqlMapClient();
	
	public static void main(String[] args){
		
		try {
			JobsControllerDAO dao = new JobsControllerDAO(sqlMapClient);
			List runningJobList = new ArrayList();
			runningJobList = dao.readRunningJobList(); //���� ���� �ִ� JOB LIST�� ��ȸ�Ѵ�.
	
			if(runningJobList.size() > 0){
				
				for(int i = 0 ; i < runningJobList.size() ; i++){
					Map runningJobMap = new HashMap();
					runningJobMap = (Map) runningJobList.get(i); 
				
					if(runningJobMap != null){
						Map jobInfoMap = dao.getJobPeriodInfo(runningJobMap); //JOB ������ ��ȸ�Ѵ�. PERIOD�� SYSDATE�� �̿��Ͽ� ���� ���� Ȯ��
						
						if(jobInfoMap != null){
							String closeYn = (String) jobInfoMap.get("CLOSEYN"); //���� ���� Ȯ��
							
							if("Y".equals(closeYn)){ // ������� JOB�� ���
								Map sessionInfo = dao.getSessionInfo(jobInfoMap);
								log.info("Session Info : "+sessionInfo);
								
								if(sessionInfo != null){
									String sid = sessionInfo.get("SID")+""; //SID
									String serial = sessionInfo.get("SERIAL#")+""; //SERIAL#
									String text = sid+","+serial;
									
									dao.closeSession(text);
									log.info("JOB ID : "+ jobInfoMap.get("JOB_ID")+" Close");
								}
							} else {
								log.info(jobInfoMap.get("JOB_ID")+"] is not regular time");
							}
						} else { // jobInfoMap != null
							log.info(runningJobMap.get("JOB")+"] is not JOB ID to be close");
						}
					} // runningJobMap != null
				} // for(int i = 0 ; i < runningJobList.size() ; i++)
			} else {
				log.info("There are no running jobs");
				log.info("");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.exit(0);
	}
}
