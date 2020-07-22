package cn.springcloud.nacos.config;

import cn.springcloud.nacos.biz.PropertiesAssemble;
import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.springcloud.nacos.NewZuulRouteLocator;

@Configuration
public class NewZuulConfig {

	static Logger log = LoggerFactory.getLogger(PropertiesAssemble.class);

	@Autowired
	private ZuulProperties zuulProperties;
	
	@Autowired
	private ServerProperties serverProperties;

	@NacosInjected
	private ConfigService configService;
	
	@Bean
	public NewZuulRouteLocator routeLocator() {
		NewZuulRouteLocator routeLocator = new NewZuulRouteLocator(
				this.serverProperties.getServlet().getServletPrefix(), this.zuulProperties);
		return routeLocator;
	}

}
