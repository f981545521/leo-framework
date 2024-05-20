package cn.acyou.leo.tool.test;

import cn.acyou.leo.framework.media.encoder.MediaUtil;
import cn.acyou.leo.framework.util.ExcelUtil;
import cn.acyou.leo.framework.util.LoggerUtil;
import cn.acyou.leo.framework.util.StringUtils;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.info.VideoSize;

import java.util.List;
import java.util.Map;

/**
 * @author youfang
 * @version [1.0.0, 2024/5/17 17:46]
 **/
public class MainTest95 {
    public static void main(String[] args) throws Exception {
        LoggerUtil.disableMediaUtilLogger();
        String path = "C:\\Users\\1\\Downloads\\分身人设示例(2).xlsx";
        List<Map<String, Object>> maps = ExcelUtil.importData(path);

        for (int i = 0; i < maps.size(); i++) {
            Map<String, Object> map = maps.get(i);
            String name = map.get("名称").toString();
            String sceneId = map.get("场景ID").toString();
            String story = map.get("TA的故事—英文").toString();
            int userId = 2000 + i;
                            //https://anylang.obs.ap-southeast-3.myhuaweicloud.com/1d1m/resources/model/546650276134981/%E4%BB%96%E7%9A%84%E7%94%9F%E6%B4%BB.jpg
            String sh_path = "https://anylang.obs.ap-southeast-3.myhuaweicloud.com/1d1m/resources/model/"+sceneId+"/%E4%BB%96%E7%9A%84%E7%94%9F%E6%B4%BB.jpg";
            String tx_path = "https://anylang.obs.ap-southeast-3.myhuaweicloud.com/1d1m/resources/model/"+sceneId+"/%E5%A4%B4%E5%83%8F.png";
            Integer height = 0;
            Integer width = 0;
            try {
                MultimediaInfo mediaInfo = MediaUtil.instance().getMediaInfo(sh_path);
                VideoSize size = mediaInfo.getVideo().getSize();
                height = size.getHeight();
                width = size.getWidth();
            }catch (Exception e) {
                //ignore
            }

            String sql = "INSERT INTO `meta-1d1m`.`ai_nurture_user` (`id`, `user_id`, `home_title`, `home_lable`, `portrait_url`, `name`, `authentication_status`, `high_quality_support`, `hire_duration`," +
                    " `hire_count`, `story_title`, `story_content`, `vertical`, `split_personality`, `split_personality_time`, `life`, `trends`, `thank_wall`, `ext`, `remark`, `del_flag`," +
                    " `create_time`, `update_time`)" +
                    " VALUES (DEFAULT, "+userId+", NULL, NULL, '"+tx_path+"', '"+name+"', 1, 1, 345, 809, NULL, NULL, NULL, NULL," +
                    " '2024-05-08 17:31:51', NULL, NULL, NULL, NULL, NULL, 0, '2024-05-17 17:30:29', '2024-05-17 17:42:15');";
            String sql2 = "INSERT INTO `meta-1d1m`.`user_video_robot` (`id`, `user_id`, `robot_name`, `robot_code`, `scene_code`, `cover_url`, `video_url`, `duration`, `vertical`, `horizontal`, `train_status`, " +
                    "`tts_id`, `demo_video_make_status`, `demo_cover_url`, `demo_video_url`, `type`, `free_type`, `silent_video_url`, `ext`, `del_flag`, `create_time`, `update_time`, `remark`) " +
                    "VALUES (DEFAULT, "+userId+", '"+name+"', NULL, '"+sceneId+"', '"+sh_path+"', NULL, NULL, '"+height+"', '"+width+"', 20, NULL, '20', NULL, NULL, 100, 1, NULL, " +
                    //"'{\\'sex\\':\\'female\\',\\'age\\':29}', 0," +
                    "NULL, 0," +
                    " '2024-05-17 17:26:50', '2024-05-17 17:41:59', " +
                    "'"+story.replace("'","\\'").replace("\r","\\r").replace("\n","\\n")+"');";
            System.out.println(sql);
            System.out.println(sql2);
        }
    }
}
