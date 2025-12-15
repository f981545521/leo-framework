package cn.acyou.leo.tool.mode;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author youfang
 * @version [1.0.0, 2025/12/15 11:06]
 **/
@Component
public class ServiceRouter {

    private final static Map<String, ModeParentService> serviceMap = new ConcurrentHashMap<>();

    @Autowired
    public ServiceRouter(List<ModeParentService> modeServices) {
        for (ModeParentService modeService : modeServices) {
            Class<? extends ModeParentService> aClass = modeService.getClass();
            Class<?>[] interfaces = aClass.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                serviceMap.put(aClass.getAnnotation(VersionedService.class).value() + "_" + anInterface.getName(), modeService);
            }
        }
    }

    public  static <T> T getService(String mode, Class<T> modeServiceClass) {
        return (T) serviceMap.get(mode + "_" + modeServiceClass.getName());
    }
}
