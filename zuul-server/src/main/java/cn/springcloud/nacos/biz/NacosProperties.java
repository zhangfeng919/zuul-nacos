package cn.springcloud.nacos.biz;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName NacosProperties
 * @Description //TODO
 * @Author zhangfeng
 * @Date 2020/7/22 15:02
 * @Version 1.0
 **/
@Component
public class NacosProperties {

    @Value("${spring.cloud.nacos.discovery.server-addr:localhost:8848}")
    private String serverAddr;

    @Value("${project.nacos.dataId:zuul-routes}")
    private String dataId;

    @Value("${project.nacos.group:ZUUL_GATEWAY}")
    private String group;

    @Value("${project.nacos.nameSpace:public}")
    private String nameSpace;

    public String getServerAddr() {
        return serverAddr;
    }

    public String getDataId() {
        return dataId;
    }

    public String getGroup() {
        return group;
    }

    public String getNameSpace() {
        return nameSpace;
    }
}
