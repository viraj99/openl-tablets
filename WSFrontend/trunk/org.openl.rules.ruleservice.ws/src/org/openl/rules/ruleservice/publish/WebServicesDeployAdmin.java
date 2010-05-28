package org.openl.rules.ruleservice.publish;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.transport.DestinationFactory;
import org.openl.rules.ruleservice.instantiation.AClassInstantiationStrategy;
import org.openl.rules.ruleservice.instantiation.EngineFactoryInstantiationStrategy;
import org.openl.rules.ruleservice.instantiation.WebServiceEngineFactoryInstantiationStrategy;
import org.openl.rules.ruleservice.instantiation.WrapperAdjustingInstantiationStrategy;
import org.openl.rules.ruleservice.resolver.RuleServiceInfo;
import org.springframework.context.ApplicationContext;

public class WebServicesDeployAdmin implements DeploymentAdmin {
    private static final Log log = LogFactory.getLog(WebServicesDeployAdmin.class);
    private ApplicationContext context;

    private Map<String, Collection<Server>> runningServices = new HashMap<String, Collection<Server>>();
    
    private String baseAddress = "http://localhost:9000/";
    private DestinationFactory destinationFactory = null;
    
    public DestinationFactory getDestinationFactory() {
        return destinationFactory;
    }

    public void setDestinationFactory(DestinationFactory destinationFactory) {
        this.destinationFactory = destinationFactory;
    }

    public String getBaseAddress() {
        return baseAddress;
    }

    public void setBaseAddress(String address) {
        this.baseAddress = address;
    }

    public ApplicationContext getContext() {
        return context;
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    public synchronized void deploy(String serviceName, ClassLoader loader, List<RuleServiceInfo> infoList) {
        undeploy(serviceName);

        String address = getBaseAddress() + serviceName + "/";

        Collection<Server> servers = new ArrayList<Server>();
        for (RuleServiceInfo wsInfo : infoList) {
            try {
                servers.add(deploy(address, loader, wsInfo));
            } catch (Exception e) {
                log.error("failed to create service", e);
            }
        }

        runningServices.put(serviceName, servers);
    }

    private Server deploy(String baseAddress, ClassLoader loader, RuleServiceInfo wsInfo)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ServerFactoryBean svrFactory = getServerFactoryBean();
        svrFactory.setDestinationFactory(destinationFactory);

        instantiateServiceBean(loader, wsInfo, svrFactory);
        
        return exposeWebService(baseAddress, wsInfo, svrFactory);
    }

    private Server exposeWebService(String baseAddress, RuleServiceInfo wsInfo, ServerFactoryBean svrFactory) {
        svrFactory.setAddress(baseAddress + wsInfo.getName());
        return svrFactory.create();
    }

    private void instantiateServiceBean(ClassLoader loader, RuleServiceInfo wsInfo, ServerFactoryBean svrFactory)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        AClassInstantiationStrategy strategy = getStrategy(wsInfo, wsInfo.getClassName(), loader);
        
        svrFactory.setServiceClass(strategy.getServiceClass());
        svrFactory.setServiceBean(strategy.instantiate());
    }
    
    private ServerFactoryBean getServerFactoryBean(){
        if (context != null && context.containsBean("serverPrototype")) {
            return (ServerFactoryBean) context.getBean("serverPrototype");
        }
        return new ServerFactoryBean();
    }

    private AClassInstantiationStrategy getStrategy(RuleServiceInfo wsInfo, String className, ClassLoader classLoader) {

        switch (wsInfo.getServiceType()) {
            case DYNAMIC_WRAPPER:
                return new EngineFactoryInstantiationStrategy(wsInfo.getXlsFile(), className, classLoader);
            case STATIC_WRAPPER:
                String path = ".";
                try {
                    path = wsInfo.getProject().getCanonicalPath();
                } catch (IOException e) {
                    log.error("failed to get canonical path", e);
                }
                return new WrapperAdjustingInstantiationStrategy(path, className, classLoader);
            case AUTO_WRAPPER:
                return new WebServiceEngineFactoryInstantiationStrategy(wsInfo.getXlsFile(), className, classLoader);
        }
        
        throw new RuntimeException("Cannot resolve instantiation strategy");
    }

    public synchronized void undeploy(String serviceName) {
        Collection<Server> servers = runningServices.remove(serviceName);
        if (servers != null) {
            for (Server server : servers) {
                server.stop();
            }
        }
    }

}
