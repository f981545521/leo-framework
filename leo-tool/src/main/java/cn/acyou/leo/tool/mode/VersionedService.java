package cn.acyou.leo.tool.mode;

import org.springframework.stereotype.Service;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author youfang
 * @version [1.0.0, 2025/12/15 11:04]
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface VersionedService {

    VersionType value();

    enum VersionType {
        VERSION_A, VERSION_B
    }

}
