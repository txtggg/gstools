package cst.gu.util.mpp;

import net.sf.mpxj.*;
import net.sf.mpxj.MPXJException;
import net.sf.mpxj.Resource;
import net.sf.mpxj.ResourceAssignment;
import net.sf.mpxj.Task;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.mpp.MPPReader;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cst.gu.util.datetime.LocalDateUtil;


public class SimpleMpxUtil{

   
    public static List<Map<String,Object>> psrseProjectFile(File mppfile){
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        MPPReader reader = new MPPReader();
        ProjectFile projectFile;
        try{
            projectFile = reader.read(mppfile);
            List<Task> taskList = projectFile.getAllTasks();
            for(Task task : taskList){
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id",task.getID());
                map.put("taskName",task.getName());
                map.put("startDate",LocalDateUtil.simpleFormatSecond(task.getStart()));
                map.put("endDate",LocalDateUtil.simpleFormatSecond(task.getFinish()));
                map.put("beforeTask",getBeforeTaskId(task));//获取前置任务的Id
                map.put("resource",getResources(task));//获得资源
                list.add(map);
            }
        }catch(MPXJException e ){
            e.printStackTrace();
        }
        return list;
    }

   
    private static String  getBeforeTaskId(Task task){
        StringBuffer beforeTaskId = new StringBuffer();
        if(task!=null){
            List<Relation> list = task.getPredecessors();
            if(list != null ){
                if(list.size()>0){
                    for(int i=0; i<list.size(); i++){
                        Relation relation = list.get(i);
                        beforeTaskId.append(relation.getTargetTask().getID()).append(';');
                    }
                    beforeTaskId.substring(beforeTaskId.length()-1);
                }
//                beforeTaskId.
            }
        }
        if(beforeTaskId.length() > 0){
        	return beforeTaskId.substring(0,beforeTaskId.length()-1);
        }
        return beforeTaskId.toString();
    }

   
    private static String getResources(Task task){
        if(task == null){
            return "";
        }
        StringBuffer sb = new StringBuffer();
        List<ResourceAssignment> assignments = task.getResourceAssignments();
        for(ResourceAssignment ra : assignments){
            Resource resource = ra.getResource();
            if(resource != null){
                sb = sb.append(resource.getName()).append('[').append(ra.getUnits()).append("%]").append(';');
            }
        }
        if(sb.length() > 0 ){
        	return sb.substring(0,sb.length() - 1);
        }
        return sb.toString();
    }
    
    
    public static void main(String[] args) {
		File f = new File("D:\\gu\\Downloads\\ppro615.mpp");
		System.out.println(psrseProjectFile(f));
		
	}
}