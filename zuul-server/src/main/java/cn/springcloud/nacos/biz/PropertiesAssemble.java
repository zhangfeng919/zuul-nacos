package cn.springcloud.nacos.biz;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import cn.springcloud.nacos.NewZuulRouteLocator;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;
import org.springframework.stereotype.Component;

import cn.springcloud.nacos.entity.ZuulRouteEntity;

@Component
public class PropertiesAssemble implements InitializingBean {

	static Logger log = LoggerFactory.getLogger(PropertiesAssemble.class);


	@Autowired
	private NewZuulRouteLocator simpleRouteLocator;
	@Autowired
	private NacosProperties nacosProperties;

	@Override
	public void afterPropertiesSet() throws Exception {
		getProperties();
	}

	public Map<String, ZuulRoute> getProperties() {
		Map<String, ZuulRoute> routes = new LinkedHashMap<>();
		List<ZuulRouteEntity> results = listenerNacos(nacosProperties.getDataId(),nacosProperties.getGroup());
		for (ZuulRouteEntity result : results) {
			if (StringUtils.isBlank(result.getPath())
					/*|| org.apache.commons.lang3.StringUtils.isBlank(result.getUrl())*/) {
				continue;
			}
			ZuulRoute zuulRoute = new ZuulRoute();
			try {
				BeanUtils.copyProperties(result, zuulRoute);
			} catch (Exception e) {
			}
			routes.put(zuulRoute.getPath(), zuulRoute);
		}
		return routes;
	}

	private List<ZuulRouteEntity> listenerNacos (String dataId, String group) {
		try {
			Properties properties = new Properties();
			properties.put(PropertyKeyConst.SERVER_ADDR, nacosProperties.getServerAddr());
			ConfigService configService = NacosFactory.createConfigService(properties);
			String content = configService.getConfig(dataId, group, 5000);
			System.out.println("从Nacos返回的配置：" + content);
			//注册Nacos配置更新监听器
            configService.addListener(dataId, group, new Listener()  {
                @Override
                public void receiveConfigInfo(String configInfo) {

                    log.info("nacos更新了", configInfo);

					simpleRouteLocator.refresh();

                }
                @Override
                public Executor getExecutor() {
                    return null;
                }
            });
			return JSONObject.parseArray(content, ZuulRouteEntity.class).stream().filter(zuulRouteEntity->zuulRouteEntity.getEnabled()).collect(Collectors.toList());
		} catch (NacosException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
}