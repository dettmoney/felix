/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.felix.scr.impl.inject.methods;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.felix.scr.impl.inject.ActivatorParameter;
import org.apache.felix.scr.impl.inject.Annotations;
import org.apache.felix.scr.impl.inject.ClassUtils;
import org.apache.felix.scr.impl.inject.LifecycleMethod;
import org.apache.felix.scr.impl.inject.MethodResult;
import org.apache.felix.scr.impl.logger.ComponentLogger;
import org.apache.felix.scr.impl.manager.ComponentContextImpl;
import org.apache.felix.scr.impl.metadata.DSVersion;
import org.osgi.service.log.LogService;


public class ActivateMethod extends BaseMethod<ActivatorParameter, Object> implements LifecycleMethod
{

    protected final boolean m_supportsInterfaces;
    private static final HashMap<Class<?>, MethodInfo> classToMethodInfo = new HashMap<>();
    private static final HashSet<String> sortedNullClasses = new HashSet<>(73);
    private static final HashSet<String> nullClasses = new HashSet<>(130);
    static {
sortedNullClasses.add("com.ibm.ws.kernel.server.internal.ServerEndpointControlMBeanImpl");
sortedNullClasses.add("javax.management.StandardMBean");
sortedNullClasses.add("java.lang.Object");
sortedNullClasses.add("com.ibm.ws.kernel.server.internal.ServerInfoMBeanImpl");
sortedNullClasses.add("com.ibm.ws.kernel.filemonitor.internal.scan.ScanningCoreServiceImpl");
sortedNullClasses.add("com.ibm.ws.kernel.filemonitor.internal.FileNotificationImpl");
sortedNullClasses.add("com.ibm.ws.runtime.update.internal.RuntimeUpdateNotificationMBeanImpl");
sortedNullClasses.add("javax.management.StandardEmitterMBean");
sortedNullClasses.add("com.ibm.ws.event.internal.WorkStageExecutorServiceFactory");
sortedNullClasses.add("com.ibm.ws.threading.PolicyExecutorProvider");
sortedNullClasses.add("com.ibm.ws.threading.internal.ThreadingIntrospector");
sortedNullClasses.add("com.ibm.ws.artifact.zip.cache.internal.ZipCachingServiceImpl");
sortedNullClasses.add("com.ibm.ws.artifact.url.internal.WSJarURLStreamHandler");
sortedNullClasses.add("org.osgi.service.url.AbstractURLStreamHandlerService");
sortedNullClasses.add("java.net.URLStreamHandler");
sortedNullClasses.add("com.ibm.wsloading.internal.ApplicationClassloadingServiceFactory");
sortedNullClasses.add("com.ibm.wsloading.internal.ClassLoadingButlerAdapter");
sortedNullClasses.add("com.ibm.ws.container.service.naming.RemoteObjectInstanceFactoryImpl");
sortedNullClasses.add("com.ibm.wsloading.internal.NativeLibraryAdapter");
sortedNullClasses.add("com.ibm.ws.managedobject.internal.ManagedObjectServiceImpl");
sortedNullClasses.add("com.ibm.ws.event.internal.ScheduledEventServiceImpl");
sortedNullClasses.add("com.ibm.ws.app.manager.ear.internal.EARDeployedAppInfoFactoryImpl");
sortedNullClasses.add("com.ibm.ws.app.manager.module.AbstractDeployedAppInfoFactory");
sortedNullClasses.add("com.ibm.ws.webserver.plugin.runtime.collaborators.GenPluginConfigCollaborator");
sortedNullClasses.add("com.ibm.ws.webcontainer31.util.ServletInjectionClassListCollaborator");
sortedNullClasses.add("com.ibm.ws.webcontainer40.osgi.container.config.factory.WebAppConfiguratorFactory40Impl");
sortedNullClasses.add("com.ibm.ws.jaxrs20.client.component.JaxRsAppSecurity");
sortedNullClasses.add("com.ibm.ws.jaxrs20.server.component.JaxRsBeanValidation");
sortedNullClasses.add("com.ibm.ws.webcontainer40.async.factory.AsyncContextFactory40Impl");
sortedNullClasses.add("com.ibm.ws.webcontainer40.servlet.factory.CacheServletWrapperFactory40Impl");
sortedNullClasses.add("com.ibm.ws.webcontainer40.osgi.srt.factory.SRTConnectionContextPool40Impl");
sortedNullClasses.add("com.ibm.ws.webcontainer40.osgi.response.factory.IRequestResponseFactory40Impl");
sortedNullClasses.add("com.ibm.wsspi.webcontainer40.util.factory.URIMatcherFactory40Impl");
sortedNullClasses.add("com.ibm.ws.webcontainer40.osgi.webapp.factory.WebAppFactory40Impl");
sortedNullClasses.add("com.ibm.ws.app.manager.web.internal.WebModuleHandlerImpl");
sortedNullClasses.add("com.ibm.ws.app.manager.module.internal.ModuleHandlerBase");
sortedNullClasses.add("com.ibm.ws.app.manager.war.internal.WARDeployedAppInfoFactoryImpl");
sortedNullClasses.add("com.ibm.ws.app.manager.war.internal.WARApplicationHandlerImpl");
sortedNullClasses.add("com.ibm.ws.jaxrs20.web.JaxRsInjectionClassListCollaborator");
sortedNullClasses.add("com.ibm.ws.cxf.client.component.AsyncClientRunnableWrapperManager");
sortedNullClasses.add("org.eclipse.yasson.JsonBindingProvider");
sortedNullClasses.add("javax.json.bind.spi.JsonbProvider");
sortedNullClasses.add("com.ibm.ws.jndi.internal.WASInitialContextFactoryBuilder");
sortedNullClasses.add("org.glassfish.json.JsonProviderImpl");
sortedNullClasses.add("javax.json.spi.JsonProvider");
sortedNullClasses.add("com.ibm.ws.cdi.web.impl.el.WrappedELExpressionFactory");
sortedNullClasses.add("javax.el.ExpressionFactory");
sortedNullClasses.add("com.ibm.ws.jaxrs20.component.LibertySseFeature");
sortedNullClasses.add("com.ibm.ws.jaxrs20.providers.customexceptionmapper.CustomExceptionMapperRegister");
sortedNullClasses.add("com.ibm.ws.jaxrs20.providers.security.SecurityAnnoProviderRegister");
sortedNullClasses.add("com.ibm.ws.transaction.services.TransactionObjectFactoryInfo");
sortedNullClasses.add("com.ibm.wsspi.injectionengine.ObjectFactoryInfo");
sortedNullClasses.add("com.ibm.ws.transaction.services.TransactionSynchronizationRegistryObjectFactoryInfo");
sortedNullClasses.add("com.ibm.ws.adaptable.module.internal.NonPersistentCacheContainerAdapter");
sortedNullClasses.add("com.ibm.ws.javaee.ddmodel.permissions.PermissionsAdapter");
sortedNullClasses.add("com.ibm.ws.javaee.ddmodel.web.WebAppAdapter");
sortedNullClasses.add("com.ibm.ws.javaee.ddmodel.webbnd.WebBndAdapter");
sortedNullClasses.add("com.ibm.ws.javaee.ddmodel.webext.WebExtAdapter");
sortedNullClasses.add("com.ibm.ws.cdi.web.liberty.WeldConfiguratorHelperFactory");
sortedNullClasses.add("com.ibm.ws.container.service.config.internal.WebFragmentsInfoAdapter");
sortedNullClasses.add("com.ibm.ws.container.service.app.deploy.internal.WebModuleClassesInfoAdapter");
sortedNullClasses.add("com.ibm.ws.adaptable.module.internal.FastModeControlContainerAdapter");
sortedNullClasses.add("com.ibm.ws.adaptable.module.internal.NonPersistentCacheEntryAdapter");
sortedNullClasses.add("com.ibm.wsspi.webcontainer.webapp.WebAppConfigAdapter");
sortedNullClasses.add("com.ibm.ws.cdi.liberty.CDIDeferredMetaDataFactoryImpl");
sortedNullClasses.add("com.ibm.tx.jta.cdi.TransactionContextExtension");
sortedNullClasses.add("com.ibm.ws.javaee.ddmodel.managedbean.ManagedBeanBndAdapter");
sortedNullClasses.add("com.ibm.ws.webcontainer40.session.impl.factory.SessionContextRegistryImplFactory40Impl");
sortedNullClasses.add("com.ibm.ws.webcontainer.osgi.session.SessionHelper");
sortedNullClasses.add("com.ibm.ws.cdi.web.liberty.WeldServletInitializer");
sortedNullClasses.add("com.ibm.ws.classloading.internal.ApplicationClassloadingServiceFactory");
sortedNullClasses.add("com.ibm.ws.classloading.internal.ClassLoadingButlerAdapter");
sortedNullClasses.add("com.ibm.ws.classloading.internal.NativeLibraryAdapter");

nullClasses.add("com.ibm.tx.jta.UserTransactionFactory");
nullClasses.add("com.ibm.tx.jta.cdi.TransactionContextExtension");
nullClasses.add("com.ibm.tx.jta.embeddable.impl.EmbeddableTMHelper");
nullClasses.add("com.ibm.tx.jta.util.TxTMHelper");
nullClasses.add("com.ibm.webservices.handler.impl.GlobalHandlerServiceImpl");
nullClasses.add("com.ibm.websphere.channelfw.osgi.CHFWBundle");
nullClasses.add("com.ibm.ws.adaptable.module.internal.AdapterFactoryServiceImpl");
nullClasses.add("com.ibm.ws.adaptable.module.internal.FastModeControlContainerAdapter");
nullClasses.add("com.ibm.ws.adaptable.module.internal.NonPersistentCacheContainerAdapter");
nullClasses.add("com.ibm.ws.adaptable.module.internal.NonPersistentCacheEntryAdapter");
nullClasses.add("com.ibm.ws.anno.service.internal.AnnotationServiceImpl_Service");
nullClasses.add("com.ibm.ws.app.manager.ApplicationManager");
nullClasses.add("com.ibm.ws.app.manager.ear.internal.EARDeployedAppInfoFactoryImpl");
nullClasses.add("com.ibm.ws.app.manager.internal.monitor.AppMonitorConfigurator");
nullClasses.add("com.ibm.ws.app.manager.internal.monitor.ApplicationMonitor");
nullClasses.add("com.ibm.ws.app.manager.module.AbstractDeployedAppInfoFactory");
nullClasses.add("com.ibm.ws.app.manager.module.internal.ModuleHandlerBase");
nullClasses.add("com.ibm.ws.app.manager.war.internal.WARApplicationHandlerImpl");
nullClasses.add("com.ibm.ws.app.manager.war.internal.WARDeployedAppInfoFactoryImpl");
nullClasses.add("com.ibm.ws.app.manager.web.internal.WebModuleHandlerImpl");
nullClasses.add("com.ibm.ws.artifact.internal.ArtifactContainerFactoryService");
nullClasses.add("com.ibm.ws.artifact.url.internal.WSJarURLStreamHandler");
nullClasses.add("com.ibm.ws.artifact.zip.cache.internal.ZipCachingServiceImpl");
nullClasses.add("com.ibm.ws.bytebuffer.internal.ByteBufferConfiguration");
nullClasses.add("com.ibm.ws.cdi.config.liberty.CDI12ContainerConfig");
nullClasses.add("com.ibm.ws.cdi.liberty.CDIDeferredMetaDataFactoryImpl");
nullClasses.add("com.ibm.ws.cdi.web.impl.el.WrappedELExpressionFactory");
nullClasses.add("com.ibm.ws.cdi.web.liberty.WeldConfiguratorHelperFactory");
nullClasses.add("com.ibm.ws.cdi.web.liberty.WeldServletInitializer");
nullClasses.add("com.ibm.ws.classloading.configuration.GlobalClassloadingConfiguration");
nullClasses.add("com.ibm.ws.classloading.internal.ApplicationClassloadingServiceFactory");
nullClasses.add("com.ibm.ws.classloading.internal.ClassLoadingButlerAdapter");
nullClasses.add("com.ibm.ws.classloading.internal.ClassLoadingServiceImpl");
nullClasses.add("com.ibm.ws.classloading.internal.NativeLibraryAdapter");
nullClasses.add("com.ibm.ws.classloading.java2sec.PermissionManager");
nullClasses.add("com.ibm.ws.collector.manager.internal.CollectorManagerImpl");
nullClasses.add("com.ibm.ws.config.xml.internal.ConfigIntrospection");
nullClasses.add("com.ibm.ws.container.service.app.deploy.internal.WebModuleClassesInfoAdapter");
nullClasses.add("com.ibm.ws.container.service.config.internal.WebFragmentsInfoAdapter");
nullClasses.add("com.ibm.ws.container.service.metadata.internal.J2EENameFactoryImpl");
nullClasses.add("com.ibm.ws.container.service.naming.RemoteObjectInstanceFactoryImpl");
nullClasses.add("com.ibm.ws.crypto.util.VariableResolver");
nullClasses.add("com.ibm.ws.cxf.client.component.AsyncClientRunnableWrapperManager");
nullClasses.add("com.ibm.ws.event.internal.EventEngineImpl");
nullClasses.add("com.ibm.ws.event.internal.ScheduledEventServiceImpl");
nullClasses.add("com.ibm.ws.event.internal.WorkStageExecutorServiceFactory");
nullClasses.add("com.ibm.ws.http.dispatcher.internal.HttpDispatcher");
nullClasses.add("com.ibm.ws.http.internal.DefaultMimeTypesImpl");
nullClasses.add("com.ibm.ws.http.internal.DefaultWelcomePage");
nullClasses.add("com.ibm.ws.http.internal.EncodingUtilsImpl");
nullClasses.add("com.ibm.ws.http.internal.HttpEndpointImpl");
nullClasses.add("com.ibm.ws.http.internal.VirtualHostImpl");
nullClasses.add("com.ibm.ws.javaee.ddmodel.managedbean.ManagedBeanBndAdapter");
nullClasses.add("com.ibm.ws.javaee.ddmodel.permissions.PermissionsAdapter");
nullClasses.add("com.ibm.ws.javaee.ddmodel.web.WebAppAdapter");
nullClasses.add("com.ibm.ws.javaee.ddmodel.webbnd.WebBndAdapter");
nullClasses.add("com.ibm.ws.javaee.ddmodel.webext.WebExtAdapter");
nullClasses.add("com.ibm.ws.jaxrs20.cdi.component.JaxRsFactoryImplicitBeanCDICustomizer");
nullClasses.add("com.ibm.ws.jaxrs20.client.component.JaxRsAppSecurity");
nullClasses.add("com.ibm.ws.jaxrs20.component.Jaxrs20GlobalHandlerServiceImpl");
nullClasses.add("com.ibm.ws.jaxrs20.component.LibertySseFeature");
nullClasses.add("com.ibm.ws.jaxrs20.providers.customexceptionmapper.CustomExceptionMapperRegister");
nullClasses.add("com.ibm.ws.jaxrs20.providers.security.SecurityAnnoProviderRegister");
nullClasses.add("com.ibm.ws.jaxrs20.server.component.JaxRsBeanValidation");
nullClasses.add("com.ibm.ws.jaxrs20.web.JaxRsInjectionClassListCollaborator");
nullClasses.add("com.ibm.ws.jndi.internal.WASInitialContextFactoryBuilder");
nullClasses.add("com.ibm.ws.jndi.url.contexts.javacolon.internal.JavaColonNameService");
nullClasses.add("java.olon.internal.JavaColonNameService");
nullClasses.add("com.ibm.ws.kernel.feature.internal.FeatureManager");
nullClasses.add("com.ibm.ws.kernel.filemonitor.internal.CoreServiceImpl");
nullClasses.add("com.ibm.ws.kernel.filemonitor.internal.FileNotificationImpl");
nullClasses.add("com.ibm.ws.kernel.filemonitor.internal.scan.ScanningCoreServiceImpl");
nullClasses.add("com.ibm.ws.kernel.server.internal.ServerEndpointControlMBeanImpl");
nullClasses.add("com.ibm.ws.kernel.server.internal.ServerInfoMBeanImpl");
nullClasses.add("com.ibm.ws.library.internal.SharedLibraryFactory");
nullClasses.add("com.ibm.ws.logging.internal.osgi.FFDCJanitor");
nullClasses.add("com.ibm.ws.managedobject.internal.ManagedObjectServiceImpl");
nullClasses.add("com.ibm.ws.resource.internal.ResourceFactoryTracker");
nullClasses.add("com.ibm.ws.resource.internal.ResourceRefConfigFactoryImpl");
nullClasses.add("com.ibm.ws.runtime.update.internal.RuntimeUpdateManagerImpl");
nullClasses.add("com.ibm.ws.runtime.update.internal.RuntimeUpdateNotificationMBeanImpl");
nullClasses.add("com.ibm.ws.threading.PolicyExecutorProvider");
nullClasses.add("com.ibm.ws.threading.internal.DeferrableScheduledExecutorImpl");
nullClasses.add("com.ibm.ws.threading.internal.ExecutorServiceImpl");
nullClasses.add("com.ibm.ws.threading.internal.FutureMonitorImpl");
nullClasses.add("com.ibm.ws.threading.internal.ScheduledExecutorImpl");
nullClasses.add("com.ibm.ws.threading.internal.ThreadingIntrospector");
nullClasses.add("com.ibm.ws.timer.internal.QuickApproxTimeImpl");
nullClasses.add("com.ibm.ws.transaction.services.TMRecoveryService");
nullClasses.add("com.ibm.ws.transaction.services.TransactionManagerService");
nullClasses.add("com.ibm.ws.transaction.services.TransactionObjectFactoryInfo");
nullClasses.add("com.ibm.ws.transaction.services.TransactionSynchronizationRegistryObjectFactoryInfo");
nullClasses.add("com.ibm.ws.transaction.services.UOWManagerObjectFactoryInfo");
nullClasses.add("com.ibm.ws.transaction.services.UserTransactionService");
nullClasses.add("com.ibm.ws.transport.http.welcome.page.WebSphereWelcomePage");
nullClasses.add("com.ibm.ws.webcontainer.cors.CorsHelper");
nullClasses.add("com.ibm.ws.webcontainer.cors.CorsRequestInterceptor");
nullClasses.add("com.ibm.ws.webcontainer.osgi.WebContainer");
nullClasses.add("com.ibm.ws.webcontainer.osgi.mbeans.GeneratePluginConfigMBean");
nullClasses.add("com.ibm.ws.webcontainer.osgi.session.SessionHelper");
nullClasses.add("com.ibm.ws.webcontainer31.util.ServletInjectionClassListCollaborator");
nullClasses.add("com.ibm.ws.webcontainer40.async.factory.AsyncContextFactory40Impl");
nullClasses.add("com.ibm.ws.webcontainer40.osgi.container.config.factory.WebAppConfiguratorFactory40Impl");
nullClasses.add("com.ibm.ws.webcontainer40.osgi.response.factory.IRequestResponseFactory40Impl");
nullClasses.add("com.ibm.ws.webcontainer40.osgi.srt.factory.SRTConnectionContextPool40Impl");
nullClasses.add("com.ibm.ws.webcontainer40.osgi.webapp.factory.WebAppFactory40Impl");
nullClasses.add("com.ibm.ws.webcontainer40.servlet.factory.CacheServletWrapperFactory40Impl");
nullClasses.add("com.ibm.ws.webcontainer40.session.impl.factory.SessionContextRegistryImplFactory40Impl");
nullClasses.add("com.ibm.ws.webserver.plugin.runtime.collaborators.GenPluginConfigCollaborator");
nullClasses.add("com.ibm.ws.webserver.plugin.runtime.listeners.GeneratePluginConfigListener");
nullClasses.add("com.ibm.ws.webserver.plugin.runtime.requester.PluginConfigRequesterImpl");
nullClasses.add("com.ibm.wsspi.channelfw.ChannelConfiguration");
nullClasses.add("com.ibm.wsspi.classloading.ResourceProvider");
nullClasses.add("com.ibm.wsspi.config.internal.FilesetImpl");
nullClasses.add("com.ibm.wsspi.injectionengine.ObjectFactoryInfo");
nullClasses.add("com.ibm.wsspi.webcontainer.webapp.WebAppConfigAdapter");
nullClasses.add("com.ibm.wsspi.webcontainer40.util.factory.URIMatcherFactory40Impl");
nullClasses.add("java.lang.Object");
nullClasses.add("java.net.URLStreamHandler");
nullClasses.add("java.util.concurrent.AbstractExecutorService");
nullClasses.add("java.util.concurrent.ScheduledThreadPoolExecutor");
nullClasses.add("java.util.concurrent.ThreadPoolExecutor");
nullClasses.add("javax.el.ExpressionFactory");
nullClasses.add("javax.json.bind.spi.JsonbProvider");
nullClasses.add("javax.json.spi.JsonProvider");
nullClasses.add("javax.management.StandardEmitterMBean");
nullClasses.add("javax.management.StandardMBean");
nullClasses.add("org.eclipse.yasson.JsonBindingProvider");
nullClasses.add("org.glassfish.json.JsonProviderImpl");
nullClasses.add("org.osgi.service.url.AbstractURLStreamHandlerService");

    }

    public ActivateMethod( final String methodName,
            final boolean methodRequired,
            final Class<?> componentClass,
            final DSVersion dsVersion,
            final boolean configurableServiceProperties,
            final boolean supportsInterfaces)
    {
        super( methodName, methodRequired, componentClass, dsVersion, configurableServiceProperties );
        m_supportsInterfaces = supportsInterfaces;
    }


    @Override
    protected MethodInfo<Object> doFindMethod( final Class<?> targetClass,
            final boolean acceptPrivate,
            final boolean acceptPackage,
            final ComponentLogger logger )
        throws SuitableMethodNotAccessibleException, InvocationTargetException
    {

        boolean suitableMethodNotAccessible = false;

        String className = targetClass.getName();
        try
        {
            if (!nullClasses.contains(className)) {
                // find the declared method in this class
                final Method method = getMethod( targetClass, getMethodName(), new Class[]
                    { ClassUtils.COMPONENT_CONTEXT_CLASS }, acceptPrivate, acceptPackage, logger );
                if ( method != null )
                {
                    //System.out.println("***JTD: getMethod not null for " + targetClass + " returning " + method);
                    return new MethodInfo<>(method);
                } else {
                    //System.out.println("***JTD: getMethod for " + targetClass + " returning NULL");
                }
            }
        }
        catch ( SuitableMethodNotAccessibleException thrown )
        {
            logger.log( LogService.LOG_DEBUG, "SuitableMethodNotAccessible", thrown );
            suitableMethodNotAccessible = true;
        }
        if (getDSVersion().isDS11())
        {
            //System.out.println("***JTD: targetClass.toString " + className + ".");
            if (sortedNullClasses.contains(className)) {
                //System.out.println("***JTD: returning null for  " + targetClass);
                return null;
            }
            if (classToMethodInfo.containsKey(targetClass)) {
                //System.out.println("***JTD: returning cached version " + targetClass);
                return classToMethodInfo.get(targetClass);
            }
            //System.out.println("***JTD: calling getSortedMethods for " + targetClass);
            List<Method> methods = getSortedMethods( targetClass);
            for (Method m: methods)
            {
                final Class<?>[] parameterTypes = m.getParameterTypes();
                if (parameterTypes.length == 1)
                {
                    Class<?> type = parameterTypes[0];
                    //single parameter method with parameter ComponentContext will already have been found.
                    if (type == ClassUtils.BUNDLE_CONTEXT_CLASS)
                    {
                        if ( accept( m, acceptPrivate, acceptPackage, returnValue() ) )
                        {
                            MethodInfo mi = new MethodInfo<>(m);
                            //System.out.println("***JTD: putting1 " + targetClass + ", " + m);
                            classToMethodInfo.put(targetClass, mi);
                            return mi;
                        }
                        suitableMethodNotAccessible = true;
                    }
                    if (getDSVersion().isDS13() && isAnnotation(type))
                    {
                        if ( accept( m, acceptPrivate, acceptPackage, returnValue() ) )
                        {
                            MethodInfo mi = new MethodInfo<>(m);
                            //System.out.println("***JTD: putting2 " + targetClass + ", " + m);
                            classToMethodInfo.put(targetClass, mi);
                            return mi;
                        }
                        suitableMethodNotAccessible = true;
                    }
                    if (type == ClassUtils.MAP_CLASS)
                    {
                        if ( accept( m, acceptPrivate, acceptPackage, returnValue() ) )
                        {
                            MethodInfo mi = new MethodInfo<>(m);
                            //System.out.println("***JTD: putting3 " + targetClass + ", " + m);
                            classToMethodInfo.put(targetClass, mi);
                            return mi;
                        }
                        suitableMethodNotAccessible = true;
                    }
                    if (type == int.class)
                    {
                        if ( accept( m, acceptPrivate, acceptPackage, returnValue() ) )
                        {
                            MethodInfo mi = new MethodInfo<>(m);
                            //System.out.println("***JTD: putting4 " + targetClass + ", " + m);
                            classToMethodInfo.put(targetClass, mi);
                            return mi;
                        }
                        suitableMethodNotAccessible = true;
                    }
                    if (type == Integer.class)
                    {
                        if ( accept( m, acceptPrivate, acceptPackage, returnValue() ) )
                        {
                            MethodInfo mi = new MethodInfo<>(m);
                            //System.out.println("***JTD: putting5 " + targetClass + ", " + m);
                            classToMethodInfo.put(targetClass, mi);
                            return mi;
                        }
                        suitableMethodNotAccessible = true;
                    }

                }
                else if (parameterTypes.length > 1)
                {
                    boolean accept = true;
                    for (Class<?> type: parameterTypes)
                    {
                        accept = type == ClassUtils.COMPONENT_CONTEXT_CLASS
                            || type == ClassUtils.BUNDLE_CONTEXT_CLASS
                            || type == ClassUtils.MAP_CLASS
                            || ( isDeactivate() && ( type == int.class || type == Integer.class))
                            || ( getDSVersion().isDS13() && isAnnotation(type));
                        if ( !accept )
                        {
                            break;
                        }

                    }
                    if (accept)
                    {
                        if ( accept( m, acceptPrivate, acceptPackage, returnValue() ) )
                        {
                            MethodInfo mi = new MethodInfo<>(m);
                            //System.out.println("***JTD: putting6 " + targetClass + ", " + m);
                            classToMethodInfo.put(targetClass, mi);
                            return mi;
                        }
                        suitableMethodNotAccessible = true;
                    }

                }
                else //no parameters
                {
                    if ( accept( m, acceptPrivate, acceptPackage, returnValue() ) )
                    {
                            MethodInfo mi = new MethodInfo<>(m);
                            //System.out.println("***JTD: putting7 " + targetClass + ", " + m);
                            classToMethodInfo.put(targetClass, mi);
                            return mi;
                    }
                    suitableMethodNotAccessible = true;
                }

            }
            //System.out.println("***JTD: adding null for " + targetClass);
            classToMethodInfo.put(targetClass, null); 
        }

        if ( suitableMethodNotAccessible )
        {
            throw new SuitableMethodNotAccessibleException();
        }
        return null;
    }

    @Override
    protected void setTypes(Object types)
    {
        // Don't care about types
    }

    boolean isDeactivate()
    {
        return false;
    }


    /**
     * returns the declared methods of the target class, with the correct name, sorted by number of parameters ( no parameters last)
     * @param targetClass class to examine methods of
     * @return sorted methods of correct name;
     */
    List<Method> getSortedMethods(Class<?> targetClass)
    {
        List<Method> result = new ArrayList<>();
        Method[] methods = targetClass.getDeclaredMethods();
        for (Method m: methods)
        {
            if (m.getName().equals(getMethodName()))
            {
                result.add(m);
            }
        }
        Collections.sort(result, new Comparator<Method>(){

            @Override
            public int compare(Method m1, Method m2)
            {
                final int l1 = m1.getParameterTypes().length;
                final int l2 = m2.getParameterTypes().length;
                if ( l1 == 0)
                {
                    return l2;
                }
                if ( l2 == 0)
                {
                    return -l1;
                }
                if (l1 == 1 && l2 == 1)
                {
                    final Class<?> t1 = m1.getParameterTypes()[0];
                    final Class<?> t2 = m2.getParameterTypes()[0];
                    //t1, t2 can't be equal
                    if (t1 == ClassUtils.COMPONENT_CONTEXT_CLASS) return -1;
                    if (t2 == ClassUtils.COMPONENT_CONTEXT_CLASS) return 1;
                    if (t1 == ClassUtils.BUNDLE_CONTEXT_CLASS) return -1;
                    if (t2 == ClassUtils.BUNDLE_CONTEXT_CLASS) return 1;
                    if (isAnnotation(t1)) return isAnnotation(t2)? 0: -1;
                    if (isAnnotation(t2)) return 1;
                    if (t1 == ClassUtils.MAP_CLASS) return -1;
                    if (t2 == ClassUtils.MAP_CLASS) return 1;
                    if (t1 == int.class) return -1;
                    if (t2 == int.class) return 1;
                    if (t1 == Integer.class) return -1;
                    if (t2 == Integer.class) return 1;
                    return 0;
                }
                return l1 - l2;
            }

        });
        return result;
    }

    private boolean isAnnotation(final Class<?> t1)
    {
        return t1.isAnnotation() || (m_supportsInterfaces && t1.isInterface() && !(t1 == ClassUtils.MAP_CLASS));
    }


    @Override
    protected Object[] getParameters( Method method, ActivatorParameter rawParameter )
    {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final ActivatorParameter ap = rawParameter;
        final Object[] param = new Object[parameterTypes.length];
        for ( int i = 0; i < param.length; i++ )
        {
            if ( parameterTypes[i] == ClassUtils.COMPONENT_CONTEXT_CLASS )
            {
                param[i] = ap.getComponentContext();
            }
            else if ( parameterTypes[i] == ClassUtils.BUNDLE_CONTEXT_CLASS )
            {
                param[i] = ap.getComponentContext().getBundleContext();
            }
            else if ( parameterTypes[i] == ClassUtils.MAP_CLASS )
            {
                // note: getProperties() returns a ReadOnlyDictionary which is a Map
                param[i] = ap.getComponentContext().getProperties();
            }
            else if ( parameterTypes[i] == ClassUtils.INTEGER_CLASS || parameterTypes[i] == Integer.TYPE )
            {
                param[i] = ap.getReason();
            }
            else
            {
                param[i] = Annotations.toObject(parameterTypes[i],
                    (Map<String, Object>) ap.getComponentContext().getProperties(),
                    ap.getComponentContext().getBundleContext().getBundle(), m_supportsInterfaces);
            }
        }

        return param;
    }


    @Override
    protected String getMethodNamePrefix()
    {
        return "activate";
    }

    /**
     * @see org.apache.felix.scr.impl.inject.LifecycleMethod#invoke(java.lang.Object, org.apache.felix.scr.impl.manager.ComponentContextImpl, int, org.apache.felix.scr.impl.inject.MethodResult)
     */
    @Override
    public MethodResult invoke(final Object componentInstance,
    		final ComponentContextImpl<?> componentContext,
    		final int reason,
    		final MethodResult methodCallFailureResult) {
        return invoke(componentInstance, new ActivatorParameter(componentContext, reason), methodCallFailureResult);
    }

    @Override
    public MethodResult invoke(final  Object componentInstance,
    		final ActivatorParameter rawParameter,
    		final MethodResult methodCallFailureResult)
    {
        if (methodExists( rawParameter.getComponentContext().getLogger() ))
        {
            return super.invoke(componentInstance, rawParameter, methodCallFailureResult );
        }
        return null;
    }

}
