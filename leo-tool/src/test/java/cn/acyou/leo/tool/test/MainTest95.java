package cn.acyou.leo.tool.test;

import cn.acyou.leo.framework.media.encoder.MediaUtil;
import cn.acyou.leo.framework.util.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.info.VideoSize;

import java.util.ArrayList;
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

            //### 动态模版
            //[
            //  {
            //    "portrait_url": "https://anylang.obs.ap-southeast-3.myhuaweicloud.com/1d1m/resources/model/547954724757573/%E5%A4%B4%E5%83%8F.png",
            //    "name": "thanks",
            //    "time": "2024-05-20 10:19:58",
            //    "content": "Today is the Hmong Flower Mountain Festival, and we will be ascending the sacred Hmong mountain to admire the breathtaking floral landscapes.",
            //    "pictures": [
            //      "https://anylang.obs.ap-southeast-3.myhuaweicloud.com/1d1m/resources/model/546876536385605/%E5%A4%B4%E5%83%8F.png",
            //      "https://anylang.obs.ap-southeast-3.myhuaweicloud.com/1d1m/resources/model/546868386865221/%E5%A4%B4%E5%83%8F.png"
            //    ]
            //  }
            //]
            //JSONArray post = new JSONArray();
            //JSONObject postItem1 = new JSONObject();
            //postItem1.put("portrait_url", "https://anylang.obs.ap-southeast-3.myhuaweicloud.com/1d1m/resources/model/547954724757573/%E5%A4%B4%E5%83%8F.png");
            //postItem1.put("name", "thanks");
            //postItem1.put("time", "2024-05-20 10:19:58");
            //postItem1.put("content", "Today is the Hmong Flower Mountain Festival, and we will be ascending the sacred Hmong mountain to admire the breathtaking floral landscapes.");
            //postItem1.put("pictures", DesensitizedUtil.mobilePhone(RandomUtil.randomTelephone()));
            //postItem1.put("portrait_url", "https://anylang.obs.ap-southeast-3.myhuaweicloud.com/1d1m/resources/tx/"+num2.get(j)+".jpg");
            //post.add(postItem1);

            //### 感谢墙模版
            //[
            //  {
            //    "name": "thanks",
            //    "mobile": "188****1629",
            //    "content": "Thanks you, boss, for taking care of the business. \r\n I wirsh you great prosperlity!",
            //    "portrait_url": "https://anylang.obs.ap-southeast-3.myhuaweicloud.com/1d1m/resources/model/547954724757573/%E5%A4%B4%E5%83%8F.png"
            //  }
            //]
            JSONArray thanksWall = new JSONArray();
            List<String> thanksContent = getThanksContent();
            List<Integer> num1 = RandomUtil.randomNumbersInRange(0, thanksContent.size() - 1, 5);
            List<Integer> num2 = RandomUtil.randomNumbersInRange(1, 14, 5);
            for (int j = 0; j < 5; j++) {
                JSONObject wall = new JSONObject();
                wall.put("name", "thanks");
                wall.put("mobile", DesensitizedUtil.mobilePhone(RandomUtil.randomTelephone()));
                wall.put("content", thanksContent.get(num1.get(j)));
                wall.put("portrait_url", "https://anylang.obs.ap-southeast-3.myhuaweicloud.com/1d1m/resources/tx/"+num2.get(j)+".jpg");
                thanksWall.add(wall);
            }

            //### 分身：我的生活模版
            JSONObject myLive = new JSONObject();
            JSONArray myLiveJsonArray = new JSONArray();
            myLiveJsonArray.add(sh_path);
            myLive.put("myLive", myLiveJsonArray);

            String sql = "INSERT INTO `meta-1d1m`.`ai_nurture_user` (`id`, `user_id`, `home_title`, `home_lable`, `portrait_url`, `name`, `authentication_status`, `high_quality_support`, `hire_duration`," +
                    " `hire_count`, `story_title`, `story_content`, `vertical`, `split_personality`, `split_personality_time`, `life`, `trends`, `thank_wall`, `ext`, `remark`, `del_flag`," +
                    " `create_time`, `update_time`)" +
                    " VALUES (DEFAULT, "+userId+", NULL, NULL, '"+tx_path+"', '"+name+"', 1, 1, 345, 809, NULL, NULL, NULL, NULL," +
                    " '2024-05-08 17:31:51', NULL, NULL, '"+StringUtils.escapeSql(thanksWall.toJSONString())+"', NULL, NULL, 0, '2024-05-17 17:30:29', '2024-05-17 17:42:15');";
            String sql2 = "INSERT INTO `meta-1d1m`.`user_video_robot` (`id`, `user_id`, `robot_name`, `robot_code`, `scene_code`, `cover_url`, `video_url`, `duration`, `vertical`, `horizontal`, `train_status`, " +
                    "`tts_id`, `demo_video_make_status`, `demo_cover_url`, `demo_video_url`, `type`, `free_type`, `silent_video_url`, `ext`, `del_flag`, `create_time`, `update_time`, `remark`) " +
                    "VALUES (DEFAULT, "+userId+", '"+name+"', NULL, '"+sceneId+"', '"+sh_path+"', NULL, NULL, '"+height+"', '"+width+"', 20, NULL, '20', NULL, NULL, 100, 1, NULL, " +
                    //"'{\\'sex\\':\\'female\\',\\'age\\':29}', 0," +
                    "'"+myLive.toJSONString()+"', 0," +
                    " '2024-05-17 17:26:50', '2024-05-17 17:41:59', " +
                    "'"+StringUtils.escapeSql(story)+"');";
            System.out.println(sql);
            System.out.println(sql2);
        }

    }





    private static List<String> getThanksContent(){
        List<String> list = new ArrayList<>();
        list.add("Thank you for choosing my service. May your work and life be filled with happiness and joy.");
        list.add("Thank you for your trust and support. May you enjoy endless happiness and success in your work and life.");
        list.add("Thank you for choosing me. May every day be filled with energy and passion, as you achieve your goals and dreams.");
        list.add("Thank you for your trust. May your career prosper and your life be happy and fulfilled.");
        list.add("Thank you for your choice. May your life's journey be smooth, filled with hope and joy.");
        list.add("Thank you for your trust. May you overcome all challenges in your work and move towards a brighter future.");
        list.add("Thank you for your support. May your work and life be filled with warmth and wonderful moments.");
        list.add("Thank you for choosing me. May all your efforts be rewarded, as you realize your true worth in life.");
        list.add("Thank you for your trust and support. May your work and life be filled with laughter and satisfaction.");
        list.add("Thank you for choosing me. May every dream of yours sail smoothly towards fulfillment.");
        list.add("Thank you for your trust. May every day be filled with joy and satisfaction, as you achieve more success and happiness.");
        list.add("Thank you for your choice. May your work and life reach new heights, surpassing your own expectations.");
        list.add("Thank you for your trust. May you remain steadfast in your goals at work, moving forward with determination.");
        list.add("Thank you for your support. May every day be filled with sunshine and hope, with more beautiful memories to cherish.");
        list.add("Thank you for choosing me. May you have a wonderful life.");
        list.add("Thank you for your trust. May your work and life yield abundant fruits, and you revel in the joy of success.");
        list.add("Thank you for your support. May every effort of yours be recognized, and you achieve more accomplishments and glory.");
        list.add("Thank you for your choice. May your life be filled with happiness and joy, forever youthful, healthy, and happy.");
        list.add("Thank you for your trust. May your work and life be filled with charm and vitality, forever brimming with passion.");
        list.add("Thank you for choosing me. May every day be meaningful and fulfilling, as you pursue your dreams with the wind at your back.");
        list.add("Thank you for your trust. May you have unwavering faith in your work, and your life be filled with gratitude and joy.");
        list.add("Thank you for your support. May every day be filled with happiness and fulfillment, living a life without regrets.");
        list.add("Thank you for your choice. May your work and life be filled with endless possibilities, creating your own brilliance.");
        list.add("Thank you for your trust. May you always be enthusiastic at work, and your life be filled with hope.");
        list.add("Thank you for choosing me. May every dream of yours come true, and every day be fulfilling and beautiful.");
        list.add("Thank you for your trust. May your work and life be filled with sunshine and warmth, with happiness by your side.");
        list.add("Thank you for your support. May every effort of yours be rewarded, and every day be fulfilling and satisfying.");
        list.add("Thank you for your choice. May your life's journey be smooth sailing, with every goal achieved as desired.");
        list.add("Thank you for your trust. May you have unwavering faith in your work, and your life be filled with gratitude and joy.");
        list.add("Thank you for choosing me. May every day be filled with happiness and success, with both your career and life flourishing.");
        return list;
    }
}
