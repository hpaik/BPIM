package org.bpim;

import static org.junit.Assert.*;
import static org.kie.api.runtime.EnvironmentName.ENTITY_MANAGER_FACTORY;
import static org.kie.api.runtime.EnvironmentName.OBJECT_MARSHALLING_STRATEGIES;
import static org.kie.api.runtime.EnvironmentName.TRANSACTION_MANAGER;
import static org.kie.api.runtime.EnvironmentName.USE_PESSIMISTIC_LOCKING;

import java.io.IOException;
import java.io.StringReader;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.bpim.engine.ExecutionContext;
import org.bpim.engine.ProcessInstanceContext;
import org.bpim.model.data.v1.DataPoolElement;
import org.bpim.objects.CustomerAccount;
import org.bpim.objects.ExtendedReceiveTaskHandler;
import org.bpim.objects.ExtendedServiceTaskHandler;
import org.bpim.objects.TestWorkItemHandler;
import org.bpim.objects.model.JourneyDetails;
import org.bpim.objects.model.JourneyMessage;
import org.drools.compiler.builder.impl.KnowledgeBuilderConfigurationImpl;
import org.drools.core.SessionConfiguration;
import org.drools.core.audit.WorkingMemoryInMemoryLogger;
import org.drools.core.impl.EnvironmentFactory;
import org.h2.tools.DeleteDbFiles;
import org.h2.tools.Server;
import org.jbpm.bpmn2.handler.ReceiveTaskHandler;
import org.bpim.objects.SendTaskHandler;
import org.jbpm.bpmn2.handler.ServiceTaskHandler;
import org.jbpm.bpmn2.xml.BPMNDISemanticModule;
import org.jbpm.bpmn2.xml.BPMNExtensionsSemanticModule;
import org.jbpm.bpmn2.xml.BPMNSemanticModule;
import org.jbpm.bpmn2.xml.XmlBPMNProcessDumper;
import org.jbpm.compiler.xml.XmlProcessReader;
import org.jbpm.marshalling.impl.ProcessInstanceResolverStrategy;
import org.jbpm.persistence.util.PersistenceUtil;
import org.jbpm.process.audit.AuditLogService;
import org.jbpm.process.audit.AuditLoggerFactory;
import org.jbpm.process.audit.JPAAuditLogService;
import org.jbpm.process.audit.AuditLoggerFactory.Type;
import org.jbpm.process.instance.event.DefaultSignalManagerFactory;
import org.jbpm.process.instance.impl.DefaultProcessInstanceManagerFactory;
import org.jbpm.process.instance.impl.demo.SystemOutWorkItemHandler;
import org.jbpm.ruleflow.core.RuleFlowProcess;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message.Level;
import org.kie.api.definition.process.Process;
import org.kie.api.io.Resource;
import org.kie.api.marshalling.ObjectMarshallingStrategy;
import org.kie.api.runtime.Environment;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkflowProcessInstance;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilderConfiguration;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.persistence.jpa.JPAKnowledgeService;
import org.kie.internal.runtime.StatefulKnowledgeSession;
import org.kie.internal.runtime.conf.ForceEagerActivationOption;
import org.xml.sax.SAXException;

import com.google.gson.Gson;

import bitronix.tm.TransactionManagerServices;
import bitronix.tm.resource.jdbc.PoolingDataSource;

public class StandaloneBPMNProcessTest {

	//private static final Logger logger = LoggerFactory.getLogger(StandaloneBPMNProcessTest.class);
	
	 public static String[] txStateName = { "ACTIVE", "MARKED_ROLLBACK",
         "PREPARED", "COMMITTED", "ROLLEDBACK", "UNKNOWN", "NO_TRANSACTION",
         "PREPARING", "COMMITTING", "ROLLING_BACK" };

	 public static final boolean PERSISTENCE = Boolean.valueOf(System.getProperty("org.jbpm.test.persistence", "true"));
	 public static final boolean LOCKING = Boolean.valueOf(System.getProperty("org.jbpm.test.locking", "false"));
	
	 private static boolean setupDataSource = false;
	 private boolean sessionPersistence = false;
	 private boolean pessimisticLocking = false;
	 private static H2Server server = new H2Server();
	 
	 private WorkingMemoryInMemoryLogger logger;
	 protected AuditLogService logService;
	
	 protected static EntityManagerFactory emf;
	 private static PoolingDataSource ds;
	
	// private RequireLocking testReqLocking;
	// private RequirePersistence testReqPersistence;
	
	@Parameters
    public static Collection<Object[]> persistence() {
        Object[][] data = new Object[][] { 
                { false, false }, 
                { true, false }, 
                { true, true } 
                };
        return Arrays.asList(data);
    };
    
   
    
    public StandaloneBPMNProcessTest() {
        //this(PERSISTENCE, LOCKING);
    	System.setProperty("jbpm.user.group.mapping", "classpath:/usergroups.properties");
        System.setProperty("jbpm.usergroup.callback", "org.jbpm.task.identity.DefaultUserGroupCallbackImpl");
    }

//    public StandaloneBPMNProcessTest(boolean sessionPersistence) {
//        this(sessionPersistence, LOCKING);
//    }
//
//    public StandaloneBPMNProcessTest(boolean sessionPersistance, boolean locking) {
//        System.setProperty("jbpm.user.group.mapping", "classpath:/usergroups.properties");
//        System.setProperty("jbpm.usergroup.callback", "org.jbpm.task.identity.DefaultUserGroupCallbackImpl");
//        this.sessionPersistence = sessionPersistance;
//        this.pessimisticLocking = locking;
//    }

    @BeforeClass
    public static void setup() throws Exception {
        setUpDataSource();
    }
    
    @After
    public void tearDown() {
        KnowledgeBaseFactory.setKnowledgeBaseServiceFactory(null);
    }

    /**
     * Tests
     */
    
    @Test
    public void testMinimalProcessWithGraphical() throws Exception {
        KieBase kbase = createKnowledgeBase("BPMN2-MinimalProcessWithGraphical.bpmn2");
        KieSession ksession = createKnowledgeSession(kbase);
        ProcessInstance processInstance = ksession.startProcess("Minimal");
        assertTrue(processInstance.getState() == ProcessInstance.STATE_COMPLETED);
    }
    
    @Test
    public void testDataObject() throws Exception {
        KieBase kbase = createKnowledgeBase("BPMN2-DataObject.bpmn2");
        KieSession ksession = createKnowledgeSession(kbase);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("employee", "UserId-12345");
        ProcessInstance processInstance = ksession.startProcess("Evaluation", params);
        assertTrue(processInstance.getState() == ProcessInstance.STATE_COMPLETED);
    }
    
    @Test
    public void testEvaluationProcess() throws Exception {
        KieBase kbase = createKnowledgeBase("BPMN2-EvaluationProcess.bpmn2");
        KieSession ksession = createKnowledgeSession(kbase);
        ksession.getWorkItemManager().registerWorkItemHandler("Human Task", new SystemOutWorkItemHandler());
        ksession.getWorkItemManager().registerWorkItemHandler("RegisterRequest", new SystemOutWorkItemHandler());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("employee", "UserId-12345");
        ProcessInstance processInstance = ksession.startProcess("Evaluation", params);
        assertTrue(processInstance.getState() == ProcessInstance.STATE_COMPLETED);
    }
    
    
    @Test
    public void testServiceTask() throws Exception {
        KieBase kbase = createKnowledgeBase("BPMN2-ServiceProcess.bpmn2");
        KieSession ksession = createKnowledgeSession(kbase);
        
        ksession.getWorkItemManager().registerWorkItemHandler("Service Task", new ServiceTaskHandler());
        TestWorkItemHandler workItemHandler = new TestWorkItemHandler();
        ksession.getWorkItemManager().registerWorkItemHandler("Human Task", workItemHandler);
        
        Map<String, Object> params = new HashMap<String, Object>();
        CustomerAccount customerAccount = new CustomerAccount();
        customerAccount.setCustomerId("111111");
        customerAccount.setAccountId("7050");
        customerAccount.setObjectId("123456");
        params.put("customerAccount", customerAccount);
        
        WorkflowProcessInstance processInstance = (WorkflowProcessInstance) ksession.startProcess("ServiceProcess", params);
        //assertProcessInstanceCompleted(processInstance.getId(), ksession);
        //assertEquals("Hello john!", processInstance.getVariable("s"));
    }
    
    @Test
    public void testCustomerJourney() throws Exception {
        KieBase kbase = createKnowledgeBase("BPMN2-GetCustomerAccount.bpmn2", "BPMN2-CustomerPayment.bpmn2");
        KieSession ksession = createKnowledgeSession(kbase);
        
        ksession.getWorkItemManager().registerWorkItemHandler("Service Task", new ExtendedServiceTaskHandler());
        
        TestWorkItemHandler workItemHandler = new TestWorkItemHandler();
        ksession.getWorkItemManager().registerWorkItemHandler("Human Task", workItemHandler);
        
        ExtendedReceiveTaskHandler receiveTaskHandler = new ExtendedReceiveTaskHandler(ksession);
        ksession.getWorkItemManager().registerWorkItemHandler("Receive Task", receiveTaskHandler);
        
        ksession.getWorkItemManager().registerWorkItemHandler("Send Task", new SendTaskHandler());
        
        Map<String, Object> params = new HashMap<String, Object>();
        JourneyMessage journeyMessage = new JourneyMessage();
        journeyMessage.setGateId("10045");
        journeyMessage.setCreationDTM(new Date());
        journeyMessage.setMessageType("IMAGE");
        params.put("journeyMessage", journeyMessage);
       
        WorkflowProcessInstance getCustomerAccountProcessInstance = (WorkflowProcessInstance) ksession.startProcess("GetCustomerAccountProcess", params);
        ExecutionContext executionContext = new ExecutionContext();
        ProcessInstanceContext processInstanceContext = executionContext.getProcessInstanceContext(String.valueOf(getCustomerAccountProcessInstance.getId())
        		, getCustomerAccountProcessInstance.getProcessName());
        
        Gson gson = new Gson();
        DataPoolElement journeyDetailsElement = processInstanceContext.getDataPoolElementByType(JourneyDetails.class.getName());
        JourneyDetails journeyDetails = gson.fromJson(journeyDetailsElement.getDataObject().toString(), JourneyDetails.class);                
        
        DataPoolElement customerAccountElement = processInstanceContext.getDataPoolElementByType(CustomerAccount.class.getName());
        CustomerAccount customerAccount = gson.fromJson(customerAccountElement.getDataObject().toString(), CustomerAccount.class);        
        
        
        WorkflowProcessInstance customerPaymentProcessInstance = (WorkflowProcessInstance) ksession.startProcess("CustomerPaymentProcess");
        receiveTaskHandler.messageReceived("CustomerJourneyDetails", new Object[]{journeyDetails, customerAccount});
        //assertProcessInstanceCompleted(processInstance.getId(), ksession);
        //assertEquals("Hello john!", processInstance.getVariable("s"));
    }
    
    @Test
    public void testUserTaskParametrizedInput() throws Exception {
        KieBase kbase = createKnowledgeBase("BPMN2-UserTaskWithParametrizedInput.bpmn2");
        KieSession ksession = createKnowledgeSession(kbase);
        TestWorkItemHandler workItemHandler = new TestWorkItemHandler();
        ksession.getWorkItemManager().registerWorkItemHandler("Human Task", workItemHandler);
        ProcessInstance processInstance = ksession.startProcess("UserTask");
        assertTrue(processInstance.getState() == ProcessInstance.STATE_ACTIVE);
//        ksession = restoreSession(ksession, true);
//        WorkItem workItem = workItemHandler.getWorkItem();
//        assertNotNull(workItem);
//        assertEquals("Executing task of process instance " + processInstance.getId() + " as work item with Hello", 
//                workItem.getParameter("Description").toString().trim());
//        ksession.getWorkItemManager().completeWorkItem(workItem.getId(), null);
//        assertProcessInstanceFinished(processInstance, ksession);
//        ksession.dispose();
    }
    
    
    
    public static void setUpDataSource() throws Exception {
        setupDataSource = true;
        server.start();
        ds = setupPoolingDataSource();
        emf = Persistence.createEntityManagerFactory("org.jbpm.persistence.jpa");
    }
    
    protected KieBase createKnowledgeBase(String... process) throws Exception {
        List<Resource> resources = new ArrayList<Resource>();
        for (int i = 0; i < process.length; ++i) {
            resources.addAll(buildAndDumpBPMN2Process(process[i]));
        }
        return createKnowledgeBaseFromResources(resources.toArray(new Resource[resources.size()]));
    }
    
    public static PoolingDataSource setupPoolingDataSource() {
        Properties dsProps = PersistenceUtil.getDatasourceProperties();
        String jdbcUrl = dsProps.getProperty("url");
        String driverClass = dsProps.getProperty("driverClassName");

        // Setup the datasource
        PoolingDataSource ds1 = PersistenceUtil.setupPoolingDataSource(dsProps, "jdbc/testDS1", false);
        if( driverClass.startsWith("org.h2") ) { 
            ds1.getDriverProperties().setProperty("url", jdbcUrl);
        }
        ds1.init();
        return ds1;
    }
    protected StatefulKnowledgeSession createKnowledgeSession(KieBase kbase)
            throws Exception {
        return createKnowledgeSession(kbase, null, null);
    }

    protected StatefulKnowledgeSession createKnowledgeSession(KieBase kbase,
            Environment env) throws Exception {
        return createKnowledgeSession(kbase, null, env);
    }
    
    protected Environment createEnvironment(EntityManagerFactory emf) {
        Environment env = EnvironmentFactory.newEnvironment();
        env.set(ENTITY_MANAGER_FACTORY, emf);
        env.set(TRANSACTION_MANAGER,
                TransactionManagerServices.getTransactionManager());
        if (sessionPersistence) {
            ObjectMarshallingStrategy[] strategies = (ObjectMarshallingStrategy[]) env.get(OBJECT_MARSHALLING_STRATEGIES);        
            
            List<ObjectMarshallingStrategy> listStrategies =new ArrayList<ObjectMarshallingStrategy>(Arrays.asList(strategies));
            listStrategies.add(0, new ProcessInstanceResolverStrategy());
            strategies = new ObjectMarshallingStrategy[listStrategies.size()];  
            env.set(OBJECT_MARSHALLING_STRATEGIES, listStrategies.toArray(strategies));
        }
        return env;
    }

    protected StatefulKnowledgeSession createKnowledgeSession(KieBase kbase,
            KieSessionConfiguration conf, Environment env) throws Exception {
        StatefulKnowledgeSession result;
        if (conf == null) {
            conf = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
        }
        
        // Do NOT use the Pseudo clock yet..
        // conf.setOption( ClockTypeOption.get( ClockType.PSEUDO_CLOCK.getId() )
        // );

        if (sessionPersistence) {
            if (env == null) {
                env = createEnvironment(emf);
            }
            if( pessimisticLocking ) { 
                env.set(USE_PESSIMISTIC_LOCKING, true);
            }
            conf.setOption(ForceEagerActivationOption.YES);
            result = JPAKnowledgeService.newStatefulKnowledgeSession(kbase,
                    conf, env);
            AuditLoggerFactory.newInstance(Type.JPA, result, null);
            logService = new JPAAuditLogService(env);
        } else {
            if (env == null) {
                env = EnvironmentFactory.newEnvironment();
            }

            Properties defaultProps = new Properties();
            defaultProps.setProperty("drools.processSignalManagerFactory",
                    DefaultSignalManagerFactory.class.getName());
            defaultProps.setProperty("drools.processInstanceManagerFactory",
                    DefaultProcessInstanceManagerFactory.class.getName());
            conf = SessionConfiguration.newInstance(defaultProps);
            conf.setOption(ForceEagerActivationOption.YES);
            result = (StatefulKnowledgeSession) kbase.newKieSession(conf, env);
            logger = new WorkingMemoryInMemoryLogger(result);
        }
        return result;
    }

    protected StatefulKnowledgeSession createKnowledgeSession(String... process)
            throws Exception {
        KieBase kbase = createKnowledgeBase(process);
        return createKnowledgeSession(kbase);
    }

    
 // Important to test this since persistence relies on this
    protected List<Resource> buildAndDumpBPMN2Process(String process) throws SAXException, IOException { 
        KnowledgeBuilderConfiguration conf = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration();
        ((KnowledgeBuilderConfigurationImpl) conf).initSemanticModules();
        ((KnowledgeBuilderConfigurationImpl) conf).addSemanticModule(new BPMNSemanticModule());
        ((KnowledgeBuilderConfigurationImpl) conf).addSemanticModule(new BPMNDISemanticModule());
        ((KnowledgeBuilderConfigurationImpl) conf).addSemanticModule(new BPMNExtensionsSemanticModule());
        
        Resource classpathResource = ResourceFactory.newClassPathResource(process);
        // Dump and reread
        XmlProcessReader processReader 
            = new XmlProcessReader(((KnowledgeBuilderConfigurationImpl) conf).getSemanticModules(), getClass().getClassLoader());
        List<Process> processes = processReader.read(this.getClass().getResourceAsStream("/" + process));
        List<Resource> resources = new ArrayList<Resource>();
        for (Process p : processes) {
            RuleFlowProcess ruleFlowProcess = (RuleFlowProcess) p;
            String dumpedString = XmlBPMNProcessDumper.INSTANCE.dump(ruleFlowProcess);
            Resource resource = ResourceFactory.newReaderResource(new StringReader(dumpedString));
            resource.setSourcePath(classpathResource.getSourcePath());
            resource.setTargetPath(classpathResource.getTargetPath());
            resources.add(resource);
        }
        return resources;
    }

    protected KieBase createKnowledgeBaseWithoutDumper(String... process) throws Exception {
        Resource[] resources = new Resource[process.length];
        for (int i = 0; i < process.length; ++i) {
            String p = process[i];
            resources[i] = (ResourceFactory.newClassPathResource(p));
        }
        return createKnowledgeBaseFromResources(resources);
    }
    
    protected KieBase createKnowledgeBaseFromResources(Resource... process)
            throws Exception {

        KieServices ks = KieServices.Factory.get();
        KieRepository kr = ks.getRepository();
        if (process.length > 0) {
            KieFileSystem kfs = ks.newKieFileSystem();

            for (Resource p : process) {
                kfs.write(p);
            }

            KieBuilder kb = ks.newKieBuilder(kfs);

            kb.buildAll(); // kieModule is automatically deployed to KieRepository
                           // if successfully built.

            if (kb.getResults().hasMessages(Level.ERROR)) {
                throw new RuntimeException("Build Errors:\n"
                        + kb.getResults().toString());
            }
        }

        KieContainer kContainer = ks.newKieContainer(kr.getDefaultReleaseId());
        return kContainer.getKieBase();
    }
	 
    private static class H2Server {
        private Server server;

        public synchronized void start() {
            if (server == null || !server.isRunning(false)) {
                try {
                    DeleteDbFiles.execute("~", "jbpm-db", true);
                    server = Server.createTcpServer(new String[0]);
                    server.start();
                } catch (SQLException e) {
                    throw new RuntimeException(
                            "Cannot start h2 server database", e);
                }
            }
        }

        public synchronized void finalize() throws Throwable {
            stop();
            super.finalize();
        }

        public void stop() {
            if (server != null) {
                server.stop();
                server.shutdown();
                DeleteDbFiles.execute("~", "jbpm-db", true);
                server = null;
            }
        }
    }
    
    @Target(value = ElementType.METHOD)
    @Retention(value = RetentionPolicy.RUNTIME)
    private @interface RequireLocking {
    	
        boolean value() default true;
        String comment() default "";

    }

}
