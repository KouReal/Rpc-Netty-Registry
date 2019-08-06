package springutils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware {
	private Logger LOGGER = LoggerFactory.getLogger(SpringContextUtil.class);
 
    // Spring应用上下文环境
    @Autowired
    private ApplicationContext applicationContext;
 
    /**
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境
     * 
     * @param applicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
    	
        this.applicationContext = applicationContext;
        LOGGER.debug("get appcontext:{}",applicationContext);
    }
 
    /**
     * @return ApplicationContext
     */
    public ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }
 
    /**
     * 获取对象 这里重写了bean方法，起主要作用
     * 
     * @param name
     * @return Object 一个以所给名字注册的bean的实例
     * @throws BeansException
     */
    public Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

 
}
